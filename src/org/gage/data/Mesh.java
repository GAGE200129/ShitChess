package org.gage.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.gage.core.MemoryManager;
import org.gage.utils.FileUtils;
import org.lwjgl.assimp.AIFace;
import org.lwjgl.assimp.AIMesh;
import org.lwjgl.assimp.AIScene;
import org.lwjgl.assimp.AIVector3D;
import org.lwjgl.assimp.Assimp;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
	private int vao;
	private int positionsVBO;
	private int uvVBO;
	private int normalVBO;
	private int ebo;
	private int vertexCount;

	public Mesh(float[] positions, float[] uvs, float[] normals, int[] indices) {
		this.loadMeshData(positions, uvs, normals, indices);
	}

	public Mesh(float[] positions) {
		this.loadMeshData(positions);
	}

	public Mesh(InputStream modelPath, String fileFormat) throws IOException {
		ByteBuffer fileData = FileUtils.resourceToByteBuffer(modelPath);
		AIScene scene = Assimp.aiImportFileFromMemory(fileData,
				Assimp.aiProcess_Triangulate | Assimp.aiProcess_GenNormals, fileFormat);

		AIMesh mesh = AIMesh.create(scene.mMeshes().get(0));
		float[] positions = new float[mesh.mNumVertices() * 3];
		float[] normals = new float[mesh.mNumVertices() * 3];
		float[] uvs = new float[mesh.mNumVertices() * 2];
		int[] indices = new int[mesh.mNumFaces() * 3];

		int positionIndex = 0;
		int normalIndex = 0;
		int uvIndex = 0;
		int index = 0;

		for (int i = 0; i < mesh.mNumVertices(); i++) {
			AIVector3D position = mesh.mVertices().get(i);
			AIVector3D normal = mesh.mNormals().get(i);
			AIVector3D uv = mesh.mTextureCoords(0).get(i);

			positions[positionIndex++] = position.x();
			positions[positionIndex++] = position.y();
			positions[positionIndex++] = position.z();

			normals[normalIndex++] = normal.x();
			normals[normalIndex++] = normal.y();
			normals[normalIndex++] = normal.z();

			uvs[uvIndex++] = uv.x();
			uvs[uvIndex++] = uv.y();
		}

		for (int i = 0; i < mesh.mNumFaces(); i++) {
			AIFace face = mesh.mFaces().get(i);
			for (int j = 0; j < face.mNumIndices(); j++) {
				indices[index++] = face.mIndices().get(j);
			}
		}
		this.loadMeshData(positions, uvs, normals, indices);

	}

	public void render() {
		GL30.glBindVertexArray(getVao());
		GL11.glDrawElements(GL11.GL_TRIANGLES, getVertexCount(), GL11.GL_UNSIGNED_INT, 0);

		GL30.glBindVertexArray(0);
	}

	public void renderNoIndices() {
		GL30.glBindVertexArray(getVao());
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, getVertexCount());

		GL30.glBindVertexArray(0);
	}

	public int getVao() {
		return vao;
	}

	public int getVertexCount() {
		return vertexCount;
	}

	private void loadMeshData(float[] positions) {
		this.vao = generateVAO();
		this.vertexCount = positions.length / 3;

		generateLayout(generateBuffer(positions), 0, 3);

		unbindVAO();
	}

	private void loadMeshData(float[] positions, float[] uvs, float[] normals, int[] indices) {
		this.vao = generateVAO();
		this.vertexCount = indices.length;
		this.positionsVBO = generateBuffer(positions);
		this.uvVBO = generateBuffer(uvs);
		this.normalVBO = generateBuffer(normals);
		this.ebo = generateIndexBuffer(indices);

		generateLayout(positionsVBO, 0, 3);
		generateLayout(uvVBO, 1, 2);
		generateLayout(normalVBO, 2, 3);
		unbindVAO();
	}
	
	private static void updateBuffer(int vbo, float[] data) {
		FloatBuffer floatBuffer = (FloatBuffer) MemoryUtil.memAllocFloat(data.length).put(data).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		MemoryUtil.memFree(floatBuffer);
	}
	
	private static int generateBuffer(float[] data) {
		int vbo = GL15.glGenBuffers();
		MemoryManager.addVBO(vbo);

		FloatBuffer floatBuffer = (FloatBuffer) MemoryUtil.memAllocFloat(data.length).put(data).flip();
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatBuffer, GL15.GL_STATIC_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		MemoryUtil.memFree(floatBuffer);
		return vbo;
	}
	
	private static void updateIndexBuffer(int ebo, int[] data) {
		IntBuffer intBuffer = (IntBuffer) MemoryUtil.memAllocInt(data.length).put(data).flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ebo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL15.GL_STATIC_DRAW);
		MemoryUtil.memFree(intBuffer);
	}

	private static int generateIndexBuffer(int[] data) {
		int ibo = GL15.glGenBuffers();

		MemoryManager.addVBO(ibo);

		IntBuffer intBuffer = (IntBuffer) MemoryUtil.memAllocInt(data.length).put(data).flip();
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, ibo);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, intBuffer, GL15.GL_STATIC_DRAW);
		MemoryUtil.memFree(intBuffer);

		return ibo;
	}

	private static void generateLayout(int vbo, int slot, int size) {
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo);
		GL20.glEnableVertexAttribArray(slot);
		GL20.glVertexAttribPointer(slot, size, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private static int generateVAO() {
		int vao = GL30.glGenVertexArrays();
		MemoryManager.addVAO(vao);
		GL30.glBindVertexArray(vao);
		return vao;
	}

	private static void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	public void updateBuffers(float[] positions, float[] uvs, float[] normals, int[] indices) {
		this.vertexCount = indices.length;
		updateBuffer(positionsVBO, positions);
		updateBuffer(uvVBO, uvs);
		updateBuffer(normalVBO, normals);
		updateIndexBuffer(ebo, indices);
	}
}
