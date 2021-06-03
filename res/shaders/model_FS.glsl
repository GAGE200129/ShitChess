#version 330 core

in vec2 FS_UV;

out vec4 out_Color;

uniform sampler2D uDiffuse;
uniform bool uHasDiffuse;
uniform vec3 uColor;

void main() {
	vec4 textureColor = vec4(1, 1, 1, 1);
	
	if(uHasDiffuse) {
		textureColor = texture(uDiffuse, FS_UV);
	}
	
	out_Color = vec4(uColor, 1) * textureColor;
}