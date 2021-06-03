package org.gage.core;

import java.util.ArrayList;

import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class MemoryManager {
	private static ArrayList<Integer> shaders = new ArrayList<Integer>();
	private static ArrayList<Integer> textures = new ArrayList<Integer>();
	private static ArrayList<Integer> vaos = new ArrayList<Integer>();
	private static ArrayList<Integer> vbos = new ArrayList<Integer>();

	public static void addShader(Integer i) {
		shaders.add(i);
	}

	public static void shutdown() {
		for (Integer i : shaders) {
			GL20.glDeleteProgram(i);
		}

		for (Integer i : vaos) {
			GL30.glDeleteVertexArrays(i);
		}
		
		for(Integer i : vbos) {
			GL15.glDeleteBuffers(i);
		}
		
		for(Integer i : textures) {
			GL13.glDeleteTextures(i);
		}
		
	}

	public static void addVAO(int vao) {
		vaos.add(vao);

	}

	public static void addVBO(int vbo) {
		vbos.add(vbo);

	}

	public static void addTexture(int id) {
		textures.add(id);
		
	}
}
