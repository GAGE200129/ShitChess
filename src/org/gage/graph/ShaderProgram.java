package org.gage.graph;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import org.gage.core.MemoryManager;
import org.gage.data.Material;
import org.gage.graph.light.Attenuation;
import org.gage.graph.light.DirectionalLight;
import org.gage.graph.light.PointLight;
import org.gage.utils.FileUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL33;

public class ShaderProgram {

	private int programID;
	private ArrayList<Integer> shaders;
	private HashMap<String, Integer> uniforms;
	private float[] matrix4Buffer;

	public ShaderProgram() {
		this.programID = GL20.glCreateProgram();
		this.shaders = new ArrayList<Integer>();
		this.uniforms = new HashMap<String, Integer>();
		this.matrix4Buffer = new float[16];

		MemoryManager.addShader(programID);
	}

	public void bind() {
		GL20.glUseProgram(programID);
	}

	public void loadUniform(String name) {
		int location = GL20.glGetUniformLocation(programID, name);
		uniforms.put(name, location);
	}

	public void loadUniformAttenuation(String name) {
		this.loadUniform(name + ".constant");
		this.loadUniform(name + ".linear");
		this.loadUniform(name + ".exponent");
	}

	public void loadUniformMaterial(String name) {
		this.loadUniform(name + ".ambient");
		this.loadUniform(name + ".diffuse");
		this.loadUniform(name + ".specular");
		this.loadUniform(name + ".reflectance");
	}

	public void loadUniformPointLight(String name) {
		this.loadUniform(name + ".color");
		this.loadUniform(name + ".position");
		this.loadUniform(name + ".intensity");
		this.loadUniformAttenuation(name + ".att");
	}

	public void loadUniformDirectionalLight(String name) {
		this.loadUniform(name + ".color");
		this.loadUniform(name + ".direction");
		this.loadUniform(name + ".intensity");
	}

	public void loadMaterial(String name, Material material) {
		this.loadVec4(name + ".ambient", material.getAmbient());
		this.loadVec4(name + ".diffuse", material.getDiffuse());
		this.loadVec4(name + ".specular", material.getSpecular());
		this.loadFloat(name + ".reflectance", material.getReflectance());
	}

	public void loadAttenuation(String name, Attenuation att) {
		this.loadFloat(name + ".constant", att.getConstant());
		this.loadFloat(name + ".linear", att.getLinear());
		this.loadFloat(name + ".exponent", att.getExponent());
	}

	public void loadPointLight(String name, PointLight l) {
		this.loadVec3(name + ".color", l.getColor());
		this.loadVec3(name + ".position", l.getPosition());
		this.loadFloat(name + ".intensity", l.getIntensity());
		this.loadAttenuation(name + ".att", l.getAtt());
	}
	
	public void loadDirectionalLight(String name, DirectionalLight l) {
		this.loadVec3(name + ".color", l.getColor());
		this.loadVec3(name + ".direction", l.getDirection());
		this.loadFloat(name + ".intensity", l.getIntensity());
	}
	
	public void loadBoolean(String name, boolean b) {

		GL33.glUniform1f(uniforms.get(name), b ? 1 : 0);
	}

	public void loadFloat(String name, float v) {
		GL20.glUniform1f(uniforms.get(name), v);
	}
	public void loadInt(String name, int v) {
		GL20.glUniform1i(uniforms.get(name), v);	
	}

	public void loadMat4(String name, Matrix4f mat) {
		if (!uniforms.containsKey(name))
			throw new RuntimeException("Uniform not added: " + name);

		mat.get(matrix4Buffer);
		GL20.glUniformMatrix4fv(uniforms.get(name), false, matrix4Buffer);

	}

	public void loadVec4(String name, Vector4f v) {
		GL20.glUniform4f(uniforms.get(name), v.x, v.y, v.z, v.w);

	}

	public void loadVec3(String name, Vector3f v) {
		GL20.glUniform3f(uniforms.get(name), v.x, v.y, v.z);

	}

	public void loadVec3(String name, float x, float y, float z) {
		GL20.glUniform3f(uniforms.get(name), x, y, z);
	}

	public void compileAndLink() {
		for (Integer i : shaders) {
			GL20.glAttachShader(programID, i);
		}

		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);

		for (Integer i : shaders) {
			GL20.glDeleteShader(i);
		}
	}

	public void loadGeometryShader(InputStream is) throws Exception {
		shaders.add(compileShader(is, GL33.GL_GEOMETRY_SHADER));
	}

	public void loadVertexShader(InputStream is) throws Exception {
		shaders.add(compileShader(is, GL20.GL_VERTEX_SHADER));
	}

	public void loadFragmentShader(InputStream is) throws Exception {
		shaders.add(compileShader(is, GL20.GL_FRAGMENT_SHADER));
	}

	private int compileShader(InputStream is, int type) throws Exception {
		int shader = GL20.glCreateShader(type);

		String source = FileUtils.resourceToString(is);
		GL20.glShaderSource(shader, source);
		GL20.glCompileShader(shader);

		int status = GL20.glGetShaderi(shader, GL20.GL_COMPILE_STATUS);
		if (status == GL11.GL_FALSE) {
			String errorMessage = GL20.glGetShaderInfoLog(shader);
			throw new Exception("Shader compilation failed: \n" + errorMessage);
		}

		return shader;
	}

	

}
