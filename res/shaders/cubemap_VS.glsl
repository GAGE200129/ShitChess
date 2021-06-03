#version 330 core

layout(location = 0) in vec3 inPos;

out vec3 FS_TexCoord;

uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

void main() {
	FS_TexCoord = inPos;
	gl_Position = (uProjectionMatrix * mat4(mat3(uViewMatrix)) * vec4(inPos, 1)).xyww;
}