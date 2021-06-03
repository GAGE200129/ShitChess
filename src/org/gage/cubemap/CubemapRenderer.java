package org.gage.cubemap;

import org.gage.graph.Camera;
import org.gage.graph.RenderConfig;
import org.gage.graph.ShaderProgram;

public class CubemapRenderer {
	private final ShaderProgram shader;
	private final RenderConfig config;

	public CubemapRenderer(RenderConfig config) throws Exception {
		this.shader = new ShaderProgram();
		this.config = config;

		shader.loadVertexShader(CubemapRenderer.class.getResourceAsStream("/shaders/cubemap_VS.glsl"));
		shader.loadFragmentShader(CubemapRenderer.class.getResourceAsStream("/shaders/cubemap_FS.glsl"));
		shader.compileAndLink();
		shader.loadUniform("uProjectionMatrix");
		shader.loadUniform("uViewMatrix");
	}

	public void render(Cubemap cubemap, Camera camera) {
		config.enable();
		shader.bind();
		shader.loadMat4("uProjectionMatrix", camera.projectionMatrix);
		shader.loadMat4("uViewMatrix", camera.transform.transformationMatrix);
		cubemap.render();
		config.disable();
	}
}
