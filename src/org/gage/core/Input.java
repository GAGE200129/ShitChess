package org.gage.core;

public class Input {

	private static final int MAX_KEYS = 1024;
	private static final int MAX_BUTTONS = 5;
	private static final int PRESS = 1;
	private static final int RELEASE = 0;

	private boolean[] keys, buttons, buttons1;
	private int mouseX, mouseY, prevMouseX, prevMouseY;
	private float mouseXNDC, mouseYNDC;
	private int scroll;

	public Input() {
		this.keys = new boolean[MAX_KEYS];
		this.buttons = new boolean[MAX_BUTTONS];
		this.buttons1 = new boolean[MAX_BUTTONS];
		this.mouseX = 0;
		this.mouseY = 0;
		this.prevMouseX = 0;
		this.prevMouseY = 0;
		this.mouseXNDC = 0;
		this.mouseYNDC = 0;
	}

	public void update(int displayWidth, int displayHeight) {
		prevMouseX = mouseX;
		prevMouseY = mouseY;

		mouseXNDC = (float) mouseX / (float) displayWidth * 2.0f - 1.0f;
		mouseYNDC = (float) (displayHeight - mouseY) / (float) displayHeight * 2.0f - 1.0f;

		scroll = 0;
		for (int i = 0; i < MAX_BUTTONS; i++) {
			buttons1[i] = false;
		}
	}

	public int getDeltaMouseX() {
		return mouseX - prevMouseX;
	}

	public int getDeltaMouseY() {
		return mouseY - prevMouseY;
	}

	public void onScrollOffset(int y) {
		this.scroll = y;
	}

	public void onKeyPress(int action, int key) {
		if (action == PRESS) {
			keys[key] = true;
		} else if (action == RELEASE) {
			keys[key] = false;
		}
	}

	public void onButtonPress(int action, int button) {
		if (action == PRESS) {
			buttons[button] = true;
			buttons1[button] = true;
		} else if (action == RELEASE) {
			buttons[button] = false;
		}
	}

	public boolean isKeyPress(int key) {
		return keys[key];
	}

	public void onMouseMove(float x, float y) {
		this.mouseX = (int) x;
		this.mouseY = (int) y;
	}

	public boolean isButtonPress(int button) {
		return buttons[button];
	}

	public boolean isButtonPressOnce(int button) {
		return buttons1[button];
	}

	public int getMouseX() {
		return mouseX;
	}

	public int getMouseY() {
		return mouseY;
	}

	public int getScroll() {
		return scroll;
	}

	public float getMouseXNDC() {
		return mouseXNDC;
	}

	public float getMouseYNDC() {
		return mouseYNDC;
	}

}
