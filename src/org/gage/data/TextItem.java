package org.gage.data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.gage.utils.JavaUtils;

public class TextItem extends GameItem {
	private static final float ZPOS = 0.0f;
	private static final int VERTICES_PER_QUAD = 4;

	private String text;
	private final int numCols;
	private final int numRows;

	public TextItem(String text, InputStream is, int numCols, int numRows) throws IOException {
		this.text = text;
		this.numCols = numCols;
		this.numRows = numRows;
		
		Texture texture = new Texture(is);
		float[] positions = null;
		float[] uvs = null;
		float[] normals = null;
		int[] indices = null;
		buildMesh(texture, numCols, numRows, positions, uvs, normals, indices);
		this.getMaterial().setTexture(texture);
		this.setMesh(new Mesh(positions, uvs, normals, indices));
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		Texture texture = this.getMaterial().getTexture();
		float[] positions = null;
		float[] uvs = null;
		float[] normals = null;
		int[] indices = null;
		buildMesh(texture, numCols, numRows, positions, uvs, normals, indices);
		this.getMesh().updateBuffers(positions, uvs, normals, indices);
	}

	private void buildMesh(Texture texture, int numCols, int numRows, float[] outPositions, float[] outUvs,
			float[] outNormals, int[] outIndices) {
		byte[] chars = text.getBytes(Charset.forName("ISO-8859-1"));
		int numChars = chars.length;

		List<Float> positions = new ArrayList<Float>();
		List<Float> textCoords = new ArrayList<Float>();
		float[] normals = new float[0];
		List<Integer> indices = new ArrayList<Integer>();

		float tileWidth = (float) texture.getWidth() / (float) numCols;
		float tileHeight = (float) texture.getHeight() / (float) numRows;

		for (int i = 0; i < numChars; i++) {
			byte currChar = chars[i];
			int col = currChar % numCols;
			int row = currChar / numCols;

			// Build a character tile composed by two triangles

			// Left Top vertex
			positions.add((float) i * tileWidth); // x
			positions.add(0.0f); // y
			positions.add(ZPOS); // z
			textCoords.add((float) col / (float) numCols);
			textCoords.add((float) row / (float) numRows);
			indices.add(i * VERTICES_PER_QUAD);

			// Left Bottom vertex
			positions.add((float) i * tileWidth); // x
			positions.add(tileHeight); // y
			positions.add(ZPOS); // z
			textCoords.add((float) col / (float) numCols);
			textCoords.add((float) (row + 1) / (float) numRows);
			indices.add(i * VERTICES_PER_QUAD + 1);

			// Right Bottom vertex
			positions.add((float) i * tileWidth + tileWidth); // x
			positions.add(tileHeight); // y
			positions.add(ZPOS); // z
			textCoords.add((float) (col + 1) / (float) numCols);
			textCoords.add((float) (row + 1) / (float) numRows);
			indices.add(i * VERTICES_PER_QUAD + 2);

			// Right Top vertex
			positions.add((float) i * tileWidth + tileWidth); // x
			positions.add(0.0f); // y
			positions.add(ZPOS); // z
			textCoords.add((float) (col + 1) / (float) numCols);
			textCoords.add((float) row / (float) numRows);
			indices.add(i * VERTICES_PER_QUAD + 3);

			// Add indices por left top and bottom right vertices
			indices.add(i * VERTICES_PER_QUAD);
			indices.add(i * VERTICES_PER_QUAD + 2);
		}
		outPositions = JavaUtils.FloatArrayListToFloatArray(positions);
		outUvs = JavaUtils.FloatArrayListToFloatArray(textCoords);
		outNormals = normals;
		outIndices = JavaUtils.IntArrayListToIntArray(indices);
	}
}
