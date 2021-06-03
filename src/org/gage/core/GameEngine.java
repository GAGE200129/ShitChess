package org.gage.core;

public class GameEngine {
	private final IGameLogic gameLogic;
	private final Input input;
	private final Window window;
	private final Timer timer;
	private boolean isRunning;

	public GameEngine(String windowTitle, int width, int height, boolean vsSync, IGameLogic gameLogic)
			throws Exception {
		this.input = new Input();
		this.window = new Window(width, height, windowTitle, vsSync, input);
		this.timer = new Timer();
		this.gameLogic = gameLogic;
		this.isRunning = true;
		this.init();
		this.gameLoop();
	}

	private void gameLoop() {
		this.timer.init();
		double delta;

		while (isRunning) {
			delta = timer.getElapsedTime();

			input();
			update(delta);
			render();

		}

		shutdown();
	}

	private void shutdown() {
		MemoryManager.shutdown();
		gameLogic.shutdown();
		window.shutdown();
	}

	private void init() throws Exception {
		gameLogic.init();
		window.showWindow();
	}

	private void input() {
		gameLogic.input(input);
		if (input.isKeyPress(KeyCodes.KEY_ESCAPE) || window.isWindowShouldClose()) {
			this.isRunning = false;
		}
	}

	private void update(double delta) {
		gameLogic.update(window, delta);
		input.update(window.getWidth(), window.getHeight());
	}

	private void render() {
		window.clear();
		gameLogic.render(window);
		window.update();
	}

}
