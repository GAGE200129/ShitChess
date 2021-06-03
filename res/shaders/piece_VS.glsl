#version 330 core

layout(location = 0) in vec3 inPos;
layout(location = 2) in vec3 inNormal;

out vec3 FS_Normal;
out vec3 FS_VertexPos;

uniform mat4 uTransformationMatrix;
uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

void main() {
	
	mat4 modelViewMat = uViewMatrix * uTransformationMatrix;
	
	vec4 vertModelView = modelViewMat * vec4(inPos, 1);
	gl_Position = uProjectionMatrix * vertModelView;
	
	
	FS_Normal = (modelViewMat * vec4(inNormal, 0)).xyz;
	FS_VertexPos = vertModelView.xyz;
}