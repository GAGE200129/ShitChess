package org.gage.chess;

import org.gage.data.Material;
import org.gage.graph.Camera;
import org.gage.graph.ShaderProgram;
import org.gage.graph.light.DirectionalLight;
import org.gage.graph.light.PointLight;
import org.joml.Vector3f;

public class CheckerboardRenderer {

	private ShaderProgram shader;
	private float time;

	public CheckerboardRenderer() throws Exception {
		this.initShader();
		
	}
	
	public void uploadTime(float time) {
		this.time = time;
	}

	public void render(Camera camera, PointLight light, DirectionalLight directional, int specularPower, Vector3f ambient, Checkerboard board) {
	
		shader.bind();
		shader.loadMat4("uProjectionMatrix", camera.projectionMatrix);
		shader.loadMat4("uViewMatrix", camera.transform.transformationMatrix);
		
		shader.loadPointLight("uPointLight", light);
		shader.loadVec3("uAmbientLight", ambient);
		shader.loadInt("uSpecularPower", specularPower);
		shader.loadDirectionalLight("uDirectionalLight", directional);
		
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				BoardPiece piece = board.getBoard()[i][j];
				Material material = piece.getMaterial();
				shader.loadMaterial("uMaterial", material);
				shader.loadMat4("uTransformationMatrix", piece.getTransform().buildTransformationMatrix());
				
				piece.getMesh().render();
			}
		}
	}

	private void initShader() throws Exception {
		this.shader = new ShaderProgram();
		this.shader.loadVertexShader(CheckerboardRenderer.class.getResourceAsStream("/shaders/board_VS.glsl"));
		this.shader.loadFragmentShader(CheckerboardRenderer.class.getResourceAsStream("/shaders/board_FS.glsl"));
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
