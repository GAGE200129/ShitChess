package org.gage.core;

import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.opengl.KHRDebug;
import org.lwjgl.system.Callback;

public class Window {

	private int width;
	private int height;
	private String title;
	private long window;

	private Callback debugProc;

	public Window(int width, int height, String title, boolean vsync, Input input) {
		this.width = width;
		this.height = height;
		this.title = title;

		if (!GLFW.glfwInit())
			throw new IllegalStateException("Unable to initialize GLFW");

		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);

		this.window = GLFW.glfwCreateWindow(width, height, title, 0, 0);

		if (window == 0) {
			throw new RuntimeException("GLFW window creation failed !");
		}

		GLFW.glfwHideWindow(window);
		GLFWErrorCallback.createPrint(System.err).set();
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();

		GLCapabilities caps = GL.createCapabilities();
		debugProc = GLUtil.setupDebugMessageCallback();

		if (caps.OpenGL43) {
			GL43.glDebugMessageControl(GL43.GL_DEBUG_SOURCE_API, GL43.GL_DEBUG_TYPE_OTHER,
					GL43.GL_DEBUG_SEVERITY_NOTIFICATION, (IntBuffer) null, false);
		} else if (caps.GL_KHR_debug) {
			KHRDebug.glDebugMessageControl(KHRDebug.GL_DEBUG_SOURCE_API, KHRDebug.GL_DEBUG_TYPE_OTHER,
					KHRDebug.GL_DEBUG_SEVERITY_NOTIFICATION, (IntBuffer) null, false);
		}

		GLFW.glfwSwapInterval(vsync ? 1 : 0);

		GLFW.glfwSetFramebufferSizeCallback(window, (window, width1, height1) -> {
			this.width = width1;
			this.height = height1;
			GL11.glViewport(0, 0, width1, height1);
		});

		GLFW.glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
			input.onKeyPress(action, key);

		});
		GLFW.glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
			input.onButtonPress(action, button);
		});

		GLFW.glfwSetCursorPosCallback(window, (window, x, y) -> {
			input.onMouseMove((float) x, (float) y);
		});

		GLFW.glfwSetScrollCallback(window, (window, x, y) -> {
			input.onScrollOffset((int) y);
		});

		// Setup cursor
		//GLFW.glfwSetCursor(window, GLFW.glfwCreateStandardCursor(GLFW.GLFW_HRESIZE_CURSOR));

	}

	public void showWindow() {
		GLFW.glfwShowWindow(window);
	}

	public boolean isWindowShouldClose() {
		return GLFW.glfwWindowShouldClose(window);
	}

	public void update() {

		GLFW.glfwSwapBuffers(window);
		GLFW.glfwPollEvents();
	}

	public void shutdown() {
		if (debugProc != null) {
			debugProc.free();
		}
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public String getTitle() {
		return title;
	}

	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

	}

}
