package org.gage.chess;

import org.gage.data.Material;
import org.gage.graph.Camera;
import org.gage.graph.ShaderProgram;
import org.gage.graph.light.DirectionalLight;
import org.gage.graph.light.PointLight;
import org.joml.Vector3f;

public class ChessPieceRenderer {

	private ShaderProgram shader;
	private float time;

	public ChessPieceRenderer() throws Exception {
		this.initShader();
	}

	public void uploadTime(float time) {
		this.time = time;
	}

	public void render(Camera camera, ChessPiece piece) {

		shader.bind();
		shader.loadMat4("uProjectionMatrix", camera.projectionMatrix);
		shader.loadMat4("uViewMatrix", camera.transform.transformationMatrix);
		shader.loadVec3("uCameraPos", camera.transform.position);
		shader.loadMat4("uTransformationMatrix", piece.getTransform().buildTransformationMatrix());
		shader.loadMaterial("uMaterial", piece.getMaterial());
		piece.getMesh().render();

	}

	public void render(Camera camera, PointLight light, DirectionalLight directional, int specularPower,
			Vector3f ambient, ChessPiece[] pieces) {

		shader.bind();
		shader.loadMat4("uProjectionMatrix", camera.projectionMatrix);
		shader.loadMat4("uViewMatrix", camera.transform.transformationMatrix);
		shader.loadVec3("uCameraPos", camera.transform.position);
		shader.loadPointLight("uPointLight", light);
		shader.loadDirectionalLight("uDirectionalLight", directional);
		shader.loadVec3("uAmbientLight", ambient);
		shader.loadInt("uSpecularPower", specularPower);
		for (int i = 0; i < pieces.length; i++) {
			ChessPiece piece = pieces[i];
			if (piece == null)
				continue;
			shader.loadMaterial("uMaterial", piece.getMaterial());
			shader.loadMat4("uTransformationMatrix", piece.getTransform().buildTransformationMatrix());
			piece.getMesh().render();
		}

	}

	private void initShader() throws Exception {
		this.shader = new ShaderProgram();
		this.shader.loadVertexShader(ChessPieceRenderer.class.getResourceAsStream("/shaders/piece_VS.glsl"));
		this.shader.loadFragmentShader(ChessPieceRenderer.class.getResourceAsStream("/shaders/piece_FS.glsl"));
		this.shader.compileAndLink();

		shader.loadUniform("uTransformationMatrix");
		shader.loadUniform("uProjectionMatrix");
		shader.loadUniform("uViewMatrix");
		shader.loadUniform("uCameraPos");
		shader.loadUniform("uAmbientLight");
		shader.loadUniform("uSpecularPower");
		shader.loadUniformMaterial("uMaterial");
		shader.loadUniformPointLight("uPointLight");
		shader.loadUniformDirectionalLight("uDirectionalLight");
	}

}
