package org.gage.chess;

import org.gage.data.GameItem;
import org.joml.Vector3f;

public abstract class ChessPiece extends GameItem {

	public enum Side {
		WHITE, BLACK
	};

	private int locX;
	private int locY;
	private Side side;
	private boolean vulnerable;

	public ChessPiece(int locX, int locY, Side side) {
		this.locX = locX;
		this.locY = locY;
		this.side = side;
		this.vulnerable = false;

	}

	public abstract void summonMoves(MapData[][] map);

	public void update(float delta) {
		getTransform().position.lerp(new Vector3f(getLocX() + 0.5f, 0, getLocY() + 0.5f), 20f * delta);
		
		if (side == Side.WHITE) {
			super.getMaterial().setDiffuse(0.9f, 0.5f, 0, 1f);
		} else if (side == Side.BLACK) {
			super.getMaterial().setDiffuse(0, 0.5f, 0.2f, 1f);
		}
	}

	public void setSide(Side side) {
		this.side = side;
	}

	public int getLocX() {
		return locX;
	}

	public void setLocX(int locX) {
		this.locX = locX;
	}

	public int getLocY() {
		return locY;
	}

	public void setLocY(int locY) {
		this.locY = locY;
	}

	public Side getSide() {
		return side;
	}

	public boolean isVulnerable() {
		return vulnerable;
	}

	public void setVulnerable(boolean vulnerable) {
		this.vulnerable = vulnerable;
	}

}
