#version 330 core

out vec4 out_Color;

in vec3 FS_TexCoord;

uniform samplerCube uCubemap;

void main() {
	out_Color = texture(uCubemap, FS_TexCoord);
}