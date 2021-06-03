package org.gage.chess;

public class MapData {
	private ChessPiece piece;
	private boolean mark;
	private BoardPiece boardPiece;

	public MapData(ChessPiece piece, boolean mark, BoardPiece boardPiece) {
		this.piece = piece;
		this.mark = mark;
		this.boardPiece = boardPiece;
	}

	public BoardPiece getBoardPiece() {
		return boardPiece;
	}

	public void setBoardPiece(BoardPiece boardPiece) {
		this.boardPiece = boardPiece;
	}

	public ChessPiece getPiece() {
		return piece;
	}

	public void setPiece(ChessPiece piece) {
		this.piece = piece;
	}

	public boolean isMark() {
		return mark;
	}

	public void setMark(boolean mark) {
		this.mark = mark;
	}

}
