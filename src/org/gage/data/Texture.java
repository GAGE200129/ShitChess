package org.gage.data;

import static org.lwjgl.opengl.GL11.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.gage.core.MemoryManager;
import org.gage.utils.FileUtils;
import org.lwjgl.stb.STBImage;

public class Texture {

	private int id = 0;
	private final int width;
	private final int height;

	public Texture(InputStream is) throws IOException {

		int[] outWidth = new int[1];
		int[] outHeight = new int[1];
		this.id = loadTexture(is, outWidth, outHeight);
		this.width = outWidth[0];
		this.height = outHeight[0];
		this.nearestFiltering();

	}

	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}

	public void nearestFiltering() {
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		unbind();
	}

	public void linearFiltering() {
		bind();
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		unbind();
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	private static int loadTexture(InputStream is, int[] outWidth, int[] outHeight) throws IOException {
		int id = glGenTextures();
		MemoryManager.addTexture(id);
		ByteBuffer rawFileData = FileUtils.resourceToByteBuffer(is);
		int[] width = new int[1];
		int[] height = new int[1];
		int[] bpp = new int[1];
		STBImage.stbi_set_flip_vertically_on_load(true);
		ByteBuffer processedImage = STBImage.stbi_load_from_memory(rawFileData, width, height, bpp, 0);
		if (processedImage == null)
			throw new IOException("Failed to load stb-image.");

		int format = 0;
		if (bpp[0] == 3) {
			format = GL_RGB;
		} else if (bpp[0] == 4) {
			format = GL_RGBA;
		}
		glBindTexture(GL_TEXTURE_2D, id);
		glTexImage2D(GL_TEXTURE_2D, 0, format, width[0], height[0], 0, format, GL_UNSIGNED_BYTE, processedImage);
		glBindTexture(GL_TEXTURE_2D, 0);
		STBImage.stbi_image_free(processedImage);

		outWidth[0] = width[0];
		outHeight[0] = height[0];

		return id;
	}
}
