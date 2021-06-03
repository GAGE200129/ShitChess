#version 330 core

struct Attenuation
{
    float constant;
    float linear;
    float exponent;
};

struct PointLight
{
    vec3 color;
    // Light position is assumed to be in view coordinates
    vec3 position;
    float intensity;
    Attenuation att;
};

struct DirectionalLight
{
	vec3 color;
	vec3 direction;
	float intensity;
};

struct Material
{
    vec4 ambient;
    vec4 diffuse;
    vec4 specular;
    float reflectance;
};

in vec3 FS_Normal;
in vec3 FS_VertexPos;

out vec4 out_Color;

uniform vec3 uColor;
uniform bool uVulnerable;
uniform float uTime;
uniform vec3 uCameraPos;
uniform Material uMaterial;
uniform int uSpecularPower;

//Light
uniform PointLight uPointLight;
uniform DirectionalLight uDirectionalLight;
uniform vec3 uAmbientLight;

//Globals
vec4 ambientC;
vec4 diffuseC;
vec4 specularC;

void setupColours(Material mat);

vec4 calLightColor(vec3 lightColor, float lightIntensity, vec3 position, vec3 toLightDir, vec3 normal);
vec4 calPointLight(PointLight light, vec3 position, vec3 normal);
vec4 calDirectionalLight(DirectionalLight light, vec3 position, vec3 normal);

void main() 
{
	setupColours(uMaterial);
	
	vec3 unitNormal = normalize(FS_Normal);
	
	vec4 diffuseSpecColor =  calPointLight(uPointLight, FS_VertexPos, unitNormal);
	diffuseSpecColor += calDirectionalLight(uDirectionalLight, FS_VertexPos, unitNormal);
	
	out_Color = ambientC * vec4(uAmbientLight, 1.0) + diffuseSpecColor;
	
}


void setupColours(Material mat)
{
	ambientC = mat.ambient;
	diffuseC = mat.diffuse;
	specularC = mat.specular;
}

vec4 calLightColor(vec3 lightColor, float lightIntensity, vec3 position, vec3 toLightDir, vec3 normal)
{
	vec4 diffuseColor = vec4(0, 0, 0, 0);
    vec4 specColor = vec4(0, 0, 0, 0);
    
    //Diffuse
	float diffuseFactor = max(dot(normal, toLightDir), 0.0);
	diffuseColor = diffuseC * vec4(lightColor, 1.0) * lightIntensity * diffuseFactor;
	
	
	if(diffuseFactor > 0.0) {
		//Specular
		vec3 cameraDirection = normalize(uCameraPos - position);
		vec3 fromLightSource = -toLightDir;
		vec3 reflectedVec = normalize(reflect(fromLightSource, normal));
		float specularFactor = max(dot(cameraDirection, reflectedVec), 0.0);
		specularFactor = pow(specularFactor, uSpecularPower);
		specColor = specularC * specularFactor * uMaterial.reflectance * vec4(lightColor, 1.0) * lightIntensity;
	}
	
	
	return (diffuseColor + specColor);
}

vec4 calPointLight(PointLight light, vec3 position, vec3 normal)
{
	vec3 lightDirection = light.position - position;
	vec3 toLightDir = normalize(lightDirection);
	vec4 lightColor = calLightColor(light.color, light.intensity, position, toLightDir, normal);
	
	//Attenuation
	
	float distance = length(lightDirection);
	float attenuation = light.att.constant + light.att.linear * distance + 
			light.att.exponent * distance * distance;
			
	return lightColor / attenuation;
}

vec4 calDirectionalLight(DirectionalLight light, vec3 position, vec3 normal)
{
	return calLightColor(light.color, light.intensity, position, normalize(light.direction), normal);
}

















