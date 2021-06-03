package org.gage.chess;

import java.io.IOException;

import org.gage.data.Mesh;

public class Pawn extends ChessPiece {

	private static Mesh pawnMesh = null;

	public Pawn(int locX, int locY, Side side) {
		super(locX, locY, side);

		if (pawnMesh == null) {
			try {
				pawnMesh = new Mesh(Pawn.class.getResourceAsStream("/models/pawn.obj"), "OBJ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		super.setMesh(pawnMesh);
	}

	@Override
	public void summonMoves(MapData[][] map) {

		int locY, locY1, locX;

		if (getSide() == Side.WHITE) {
			locY = getLocY() + 1;
			locY1 = getLocY() + 2;
			locX = getLocX();
			if (locY < 8 && map[locY][locX].getPiece() == null) {
				map[locY][locX].setMark(true);
				map[locY][locX].getBoardPiece().setMarkForMove(true);
			}
			if (locY1 < 8 && map[locY1][getLocX()].getPiece() == null && getLocY() < 2
					&& map[locY][locX].getPiece() == null) {
				map[locY1][locX].setMark(true);
				map[locY1][locX].getBoardPiece().setMarkForMove(true);
			}
		} else if (getSide() == Side.BLACK) {
			locY = getLocY() - 1;
			locY1 = getLocY() - 2;
			locX = getLocX();
			if (locY >= 0 && map[locY][locX].getPiece() == null) {
				map[locY][locX].setMark(true);
				map[locY][locX].getBoardPiece().setMarkForMove(true);
			}
			if (locY1 >= 0 && map[locY1][getLocX()].getPiece() == null && getLocY() > 5
					&& map[locY][locX].getPiece() == null) {
				map[locY1][locX].setMark(true);
				map[locY1][locX].getBoardPiece().setMarkForMove(true);
			}
		}

		// Check for vulnerability
		if (getSide() == Side.WHITE) {

			if (getLocY() + 1 < 8) {
				if (getLocX() + 1 < 8 && map[getLocY() + 1][getLocX() + 1].getPiece() != null) {
					if (map[getLocY() + 1][getLocX() + 1].getPiece().getSide() != this.getSide())
						map[getLocY() + 1][getLocX() + 1].getPiece().setVulnerable(true);
				}
				if (getLocX() - 1 >= 0 && map[getLocY() + 1][getLocX() - 1].getPiece() != null) {
					if (map[getLocY() + 1][getLocX() - 1].getPiece().getSide() != this.getSide())
						map[getLocY() + 1][getLocX() - 1].getPiece().setVulnerable(true);
				}
			}
		} else if (getSide() == Side.BLACK) {
			if (getLocY() - 1 >= 0) {
				if (getLocX() + 1 < 8 && map[getLocY() - 1][getLocX() + 1].getPiece() != null) {
					if (map[getLocY() - 1][getLocX() + 1].getPiece().getSide() != this.getSide())
						map[getLocY() - 1][getLocX() + 1].getPiece().setVulnerable(true);
				}
				if (getLocX() - 1 >= 0 && map[getLocY() - 1][getLocX() - 1].getPiece() != null) {
					if (map[getLocY() - 1][getLocX() - 1].getPiece().getSide() != this.getSide())
						map[getLocY() - 1][getLocX() - 1].getPiece().setVulnerable(true);
				}
			}
		}

	}

}
