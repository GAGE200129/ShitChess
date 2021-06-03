package org.gage.graph;

import static org.lwjgl.opengl.GL11.*;

public class DefaultConfig implements RenderConfig {

	@Override
	public void enable() {
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_STENCIL_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void disable() {

	}

}
