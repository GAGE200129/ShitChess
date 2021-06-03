package org.gage.graph;

import org.lwjgl.opengl.GL11;

public class SkyboxConfig implements RenderConfig {

	@Override
	public void enable() {
		GL11.glDepthMask(false);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
	}

	@Override
	public void disable() {
		GL11.glDepthMask(true);
		GL11.glDepthFunc(GL11.GL_LESS);
	}

}
