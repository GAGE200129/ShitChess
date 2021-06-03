package org.gage.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class FileUtils {

	public static String resourceToString(InputStream is) throws IOException {
		StringBuilder sb = new StringBuilder();
		BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		String line;
		while ((line = rd.readLine()) != null) {
			sb.append(line).append("\n");
		}

		return sb.toString();

	}
	
	

	public static ByteBuffer resourceToByteBuffer(InputStream is) throws IOException {
		if (is == null) {
			throw new IOException("Inputstream is null");
		}
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		int nRead;
		byte[] data = new byte[is.available()];
		while ((nRead = is.read(data, 0, data.length)) != -1) {
			buffer.write(data, 0, nRead);
		}
		byte[] bytes = buffer.toByteArray();
		ByteBuffer result = BufferUtils.createByteBuffer(bytes.length);
		result.put(bytes);
		result.flip();

		return result;
	}
}
