package org.gage.chess;

import org.gage.data.GameItem;
import org.gage.data.Mesh;

public class BoardPiece extends GameItem {

	private static Mesh sMesh;

	private Mesh mesh;
	private boolean markForMove;

	public BoardPiece() {
		if (sMesh == null) {
			sMesh = generateMesh();
		}

		this.mesh = sMesh;
		this.markForMove = false;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public boolean isMarkForMove() {
		return markForMove;
	}

	public void setMarkForMove(boolean markForMove) {
		this.markForMove = markForMove;
	}

	private static Mesh generateMesh() {
		float[] positions = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, };
		float[] normals = new float[] { 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, };
		float[] uvs = new float[] { 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, };
		int[] indices = new int[] { 0, 1, 2, 2, 1, 3 };

		return new Mesh(positions, uvs, normals, indices);
	}
}
