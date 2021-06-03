package org.gage.core;

public interface IGameLogic {
	void init() throws Exception;

	void input(Input input);

	void update(Window window, double delta);

	void shutdown();

	void render(Window window);
}
