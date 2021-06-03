package org.gage.chess;

import java.io.IOException;

import org.gage.data.Mesh;
import org.joml.Vector2i;

public class Bishop extends ChessPiece {
	private static Mesh mesh = null;

	public Bishop(int locX, int locY, Side side){
		super(locX, locY, side);

		if (mesh == null) {
			try {
				mesh = new Mesh(Pawn.class.getResourceAsStream("/models/bishop.obj"), "OBJ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		super.setMesh(mesh);
	}

	@Override
	public void summonMoves(MapData[][] map) {
		for (int i = 1; i < 8; i++) {
			Vector2i v = new Vector2i(this.getLocX() + i, this.getLocY() + i);
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
			Vector2i v = new Vector2i(this.getLocX() - i, this.getLocY() + i);
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
			Vector2i v = new Vector2i(this.getLocX() + i, this.getLocY() - i);
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
			Vector2i v = new Vector2i(this.getLocX() - i, this.getLocY() - i);
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
		
		
	}
	
	private boolean isInBound(Vector2i v) {
		return v.x >= 0 && v.x < 8 && v.y >= 0 && v.y < 8;
	}
}
