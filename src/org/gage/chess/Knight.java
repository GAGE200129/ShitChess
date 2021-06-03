package org.gage.chess;

import java.io.IOException;

import org.gage.data.Mesh;
import org.joml.Vector2i;

public class Knight extends ChessPiece {
	private static Mesh mesh = null;

	public Knight(int locX, int locY, Side side) {
		super(locX, locY, side);

		if (mesh == null) {
			try {
				mesh = new Mesh(Pawn.class.getResourceAsStream("/models/knight.obj"), "OBJ");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		super.setMesh(mesh);

		if (side == Side.BLACK) {
			this.getTransform().rotation.rotateY((float) Math.PI);
		}
	}

	@Override
	public void summonMoves(MapData[][] map) {

		Vector2i loc1 = new Vector2i(getLocX() + 1, getLocY() + 2);
		Vector2i loc2 = new Vector2i(getLocX() - 1, getLocY() + 2);

		Vector2i loc3 = new Vector2i(getLocX() + 1, getLocY() - 2);
		Vector2i loc4 = new Vector2i(getLocX() - 1, getLocY() - 2);

		Vector2i loc5 = new Vector2i(getLocX() + 2, getLocY() + 1);
		Vector2i loc6 = new Vector2i(getLocX() + 2, getLocY() - 1);

		Vector2i loc7 = new Vector2i(getLocX() - 2, getLocY() + 1);
		Vector2i loc8 = new Vector2i(getLocX() - 2, getLocY() - 1);

		summonMove(loc1, map);
		summonMove(loc2, map);
		summonMove(loc3, map);
		summonMove(loc4, map);
		summonMove(loc5, map);
		summonMove(loc6, map);
		summonMove(loc7, map);
		summonMove(loc8, map);
	}

	private void summonMove(Vector2i v, MapData[][] map) {
		if (v.x >= 0 && v.x < 8 && v.y >= 0 && v.y < 8) {
			MapData data = map[v.y][v.x];
			
			if (data.getPiece() == null) {
				data.setMark(true);
				data.getBoardPiece().setMarkForMove(true);
			} else if (this.getSide() != data.getPiece().getSide()) {
				data.getPiece().setVulnerable(true);
			}

		}
	}
}
