package org.gage.chess;

import java.io.IOException;

import org.gage.data.Mesh;
import org.joml.Vector2i;

public class Rook extends ChessPiece {
	private static Mesh mesh = null;

	public Rook(int locX, int locY, Side side) {
		super(locX, locY, side);

		if (mesh == null) {
			try {
				mesh = new Mesh(Pawn.class.getResourceAsStream("/models/rook.obj"), "OBJ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		super.setMesh(mesh);
	}

	@Override
	public void summonMoves(MapData[][] map) {

		for (int i = 1; i < 8; i++) {
			Vector2i v = new Vector2i(this.getLocX(), this.getLocY() + i);
			if (isInBound(v)) {
				if (map[v.y][v.x].getPiece() != null) {
					if (map[v.y][v.x].getPiece().getSide() != this.getSide()) {
						map[v.y][v.x].getPiece().setVulnerable(true);
					}
					break;
				}
				map[v.y][v.x].setMark(true);
				map[v.y][v.x].getBoardPiece().setMarkForMove(true);
			}
		}

		for (int i = 1; i < 8; i++) {
			Vector2i v = new Vector2i(this.getLocX(), this.getLocY() - i);
			if (isInBound(v)) {
				if (map[v.y][v.x].getPiece() != null) {
					if (map[v.y][v.x].getPiece().getSide() != this.getSide()) {
						map[v.y][v.x].getPiece().setVulnerable(true);
					}
					break;
				}
				map[v.y][v.x].setMark(true);
				map[v.y][v.x].getBoardPiece().setMarkForMove(true);
			}
		}

		for (int i = 1; i < 8; i++) {
			Vector2i h = new Vector2i(this.getLocX() + i, this.getLocY());
			if (isInBound(h)) {
				if (map[h.y][h.x].getPiece() != null) {
					if (map[h.y][h.x].getPiece().getSide() != this.getSide()) {
						map[h.y][h.x].getPiece().setVulnerable(true);
					}
					break;
				}
				map[h.y][h.x].setMark(true);
				map[h.y][h.x].getBoardPiece().setMarkForMove(true);
			}
		}

		for (int i = 1; i < 8; i++) {
			Vector2i h = new Vector2i(this.getLocX() - i, this.getLocY());
			if (isInBound(h)) {
				if (map[h.y][h.x].getPiece() != null) {
					if (map[h.y][h.x].getPiece().getSide() != this.getSide()) {
						map[h.y][h.x].getPiece().setVulnerable(true);
					}
					break;
				}
				map[h.y][h.x].setMark(true);
				map[h.y][h.x].getBoardPiece().setMarkForMove(true);
			}
		}

	}

	private boolean isInBound(Vector2i v) {
		return v.x >= 0 && v.x < 8 && v.y >= 0 && v.y < 8;
	}
}
