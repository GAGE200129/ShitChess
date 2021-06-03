package org.gage.chess;

import java.util.Random;

public class Checkerboard {
	private BoardPiece[][] board;

	public Checkerboard() {

		Random random = new Random();
		this.board = new BoardPiece[8][8];
		int k = 0;
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				BoardPiece item = new BoardPiece();
				item.getTransform().position.set(j, 0, i);
				float r = random.nextFloat() / 2;
				float g = random.nextFloat() / 2;
				float b = random.nextFloat() / 2;
				if (j % 8 == 0)
					k++;

				if (k % 2 == 0) {
					item.getMaterial().setDiffuse(0, 0, 0, 1f);
				} else {
					item.getMaterial().setDiffuse(r, g, b, 1f);
				}

				k++;

				board[i][j] = item;
			}
		}
	}

	public BoardPiece[][] getBoard() {
		return board;
	}

}
