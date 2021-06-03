#version 330 core

layout(location = 0) in vec3 inPos;
layout(location = 1) in vec2 inUV;

out vec2 FS_UV;

uniform mat4 uTransformationMatrix;
uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

void main() {
	FS_UV = inUV;
	gl_Position = uProjectionMatrix * uViewMatrix * uTransformationMatrix * vec4(inPos, 1);
}