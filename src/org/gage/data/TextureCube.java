package org.gage.data;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_RGB;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_WRAP_R;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP;
import static org.lwjgl.opengl.GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.gage.core.MemoryManager;
import org.gage.utils.FileUtils;
import org.lwjgl.stb.STBImage;

public class TextureCube {

	private int id;

	public TextureCube(InputStream[] is) throws IOException {
		this.id = loadTextureCube(is);
		this.nearestFiltering();
	}

	public void bind() {
		glBindTexture(GL_TEXTURE_CUBE_MAP, id);
	}

	public void unbind() {
		glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
	}

	public void nearestFiltering() {
		bind();
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		unbind();
	}

	public void linearFiltering() {
		bind();
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		unbind();
	}

	private static int loadTextureCube(InputStream[] is) throws IOException {
		int id = glGenTextures();
		MemoryManager.addTexture(id);
		int[] width = new int[1];
		int[] height = new int[1];
		int[] bpp = new int[1];
		STBImage.stbi_set_flip_vertically_on_load(false);

		glBindTexture(GL_TEXTURE_CUBE_MAP, id);

		for (int i = 0; i < is.length; i++) {
			ByteBuffer rawFileData = FileUtils.resourceToByteBuffer(is[i]);
			ByteBuffer processedImage = STBImage.stbi_load_from_memory(rawFileData, width, height, bpp,
					STBImage.STBI_rgb);
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGB, width[0], height[0], 0, GL_RGB,
					GL_UNSIGNED_BYTE, processedImage);
			STBImage.stbi_image_free(processedImage);
		}

		
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_WRAP_R, GL_CLAMP_TO_EDGE);

		glBindTexture(GL_TEXTURE_CUBE_MAP, 0);

		return id;
	}
}
