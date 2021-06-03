package org.gage.chess;

import java.util.Stack;

import org.gage.core.Input;
import org.gage.core.Window;
import org.gage.graph.Camera;
import org.gage.graph.RenderConfig;
import org.gage.graph.light.DirectionalLight;
import org.gage.graph.light.PointLight;
import org.gage.utils.MathUtils;
import org.joml.Vector3f;

public class MasterChess {

	private static final int MAX_PIECES = 32;

	private static final Vector3f WHITE_POS = new Vector3f(4, 4.5f, -3.5f);
	private static final Vector3f BLACK_POS = new Vector3f(4, 4.5f, 11.5f);

	private RenderConfig config;
	private Camera camera;
	private Vector3f desiredCameraPos;
	private float desiredCameraYaw;
	private float desiredCameraPitch;
	private float deltaMouseX, deltaMouseY;
	
	private ChessPieceRenderer pieceRenderer;
	private CheckerboardRenderer boardRenderer;
	private Checkerboard board;
	private ChessPiece[] pieces;
	private ChessPiece[] promotions;
	private ChessPiece selection;
	private MapData map[][];
	private boolean clicked, dragCamera;
	private int cursorX, cursorY;
	private int screenWidth, screenHeight;
	private ChessPiece.Side currentSide;
	private Stack<ChessPiece> whiteGulag, blackGulag;

	private int pawnLocation;
	private Boolean pawnPromotion;
	private Pawn promotedPawn;

	private float time;

	public MasterChess(RenderConfig config, Camera camera) throws Exception {
		this.camera = camera;
		this.desiredCameraPos = new Vector3f();
		this.config = config;
		this.pieceRenderer = new ChessPieceRenderer();
		this.boardRenderer = new CheckerboardRenderer();
		this.board = new Checkerboard();
		this.selection = null;

		this.desiredCameraPitch = (float) Math.toRadians(30f);

		this.initAllPieces();
	}

	public void input(Input input) {
		clicked = input.isButtonPressOnce(0);
		dragCamera = input.isButtonPress(1);
		cursorX = input.getMouseX();
		cursorY = input.getMouseY();
		deltaMouseX = input.getDeltaMouseX();
		deltaMouseY = input.getDeltaMouseY();
	}

	public void update(float delta) {
		clearAndUpdateMap(delta);

		if (selection != null) {
			selection.summonMoves(map);
		}
		Vector3f ray = MathUtils.rayPicking(camera.projectionMatrix, camera.transform.transformationMatrix, cursorX,
				cursorY, screenWidth, screenHeight);

		float t = -camera.transform.position.y / ray.y;

		int x = (int) (camera.transform.position.x + ray.x * t);
		int z = (int) Math.floor(camera.transform.position.z + ray.z * t);

		if (clicked) {
			handleNextEvent(x, z);
		}
		handlePawnPromotion(delta, x, z, clicked);

		// Update gulag
		for (int i = 0; i < whiteGulag.size(); i++) {
			whiteGulag.get(i).setLocX(-1);
			whiteGulag.get(i).setLocY(i);
		}
		for (int i = 0; i < blackGulag.size(); i++) {
			blackGulag.get(i).setLocX(8);
			blackGulag.get(i).setLocY(i);
		}

		// Update timer
		time += delta;

		boardRenderer.uploadTime(time);
		pieceRenderer.uploadTime(time);

		// Update camera

		camera.transform.position.lerp(desiredCameraPos, delta * 5);
		if (!dragCamera) {
			camera.yaw = Math.fma(desiredCameraYaw - camera.yaw, delta * 5, camera.yaw);
			camera.pitch = Math.fma(desiredCameraPitch - camera.pitch, delta * 5, camera.pitch);
		} else {
			camera.yaw += deltaMouseX * delta;
			camera.pitch += deltaMouseY * delta;
		}
		if (!pawnPromotion) {
			if (currentSide == ChessPiece.Side.WHITE) {
				if (!desiredCameraPos.equals(WHITE_POS)) {
					desiredCameraPos.set(WHITE_POS);
					desiredCameraYaw = (float) Math.PI;
				}
			} else if (currentSide == ChessPiece.Side.BLACK) {
				if (!desiredCameraPos.equals(BLACK_POS)) {
					desiredCameraPos.set(BLACK_POS);
					desiredCameraYaw = (float) 0;
				}
			}
		}
	}

	public void render(Camera camera, PointLight light, DirectionalLight directional, int specularPower,
			Vector3f ambient, Window window) {
		config.enable();
		pieceRenderer.render(camera, light, directional, specularPower, ambient, pieces);
		boardRenderer.render(camera, light, directional, specularPower, ambient, board);

		if (pawnPromotion) {
			pieceRenderer.render(camera, light, directional, specularPower, ambient, promotions);
		}

		screenWidth = window.getWidth();
		screenHeight = window.getHeight();
		config.disable();

	}

	private void handlePawnPromotion(float delta, int x, int z, boolean clicked) {
		if (pawnPromotion) {
			int checkZ = promotedPawn.getSide() == ChessPiece.Side.WHITE ? 8 : -1;
			for (int i = 0; i < 4; i++) {

				promotions[i].setSide(promotedPawn.getSide());
				promotions[i].setLocX(i);
				promotions[i].setLocY(checkZ);
				promotions[i].update(delta);

				if (clicked && x == i && z == checkZ) {
					instanceNewChessPiece(promotions[i]);
					break;
				}
			}

		}
	}

	private void instanceNewChessPiece(ChessPiece pieceToInstance) {
		ChessPiece instance = null;
		ChessPiece.Side side = promotedPawn.getSide();
		if (pieceToInstance instanceof Knight) {
			instance = new Knight(promotedPawn.getLocX(), promotedPawn.getLocY(), side);
		} else if (pieceToInstance instanceof Rook) {
			instance = new Rook(promotedPawn.getLocX(), promotedPawn.getLocY(), side);
		} else if (pieceToInstance instanceof Bishop) {
			instance = new Bishop(promotedPawn.getLocX(), promotedPawn.getLocY(), side);
		} else if (pieceToInstance instanceof Queen) {
			instance = new Queen(promotedPawn.getLocX(), promotedPawn.getLocY(), side);
		}

		pieces[pawnLocation] = instance;
		promotedPawn = null;
		pawnPromotion = false;
	}

	private void handleNextEvent(int x, int z) {
		if (!(x < 0 || x > 7 || z < 0 || z > 7) && !pawnPromotion) {
			if (selection != null && map[z][x].getPiece() != null && map[z][x].getPiece().isVulnerable()) {
				ChessPiece needToRemove = map[z][x].getPiece();
				selection.setLocX(needToRemove.getLocX());
				selection.setLocY(needToRemove.getLocY());

				// Move to gulag
				switch (needToRemove.getSide()) {
				case WHITE:
					whiteGulag.add(needToRemove);
					break;
				case BLACK:
					blackGulag.add(needToRemove);
					break;
				}

				selection = null;
				switchSide();
				if (needToRemove instanceof King) {
					this.initAllPieces();
				}
			} else if (map[z][x].getPiece() == selection) {
				selection = null;
			} else if (selection == null || map[z][x].getPiece() != null) {
				if (map[z][x].getPiece().getSide() == currentSide)
					selection = map[z][x].getPiece();

			} else {
				if (map[z][x].isMark()) {
					selection.setLocX(x);
					selection.setLocY(z);
					selection = null;
					switchSide();
				}
			}
		}
	}

	private void switchSide() {
		if (currentSide == ChessPiece.Side.WHITE) {
			currentSide = ChessPiece.Side.BLACK;
		} else if (currentSide == ChessPiece.Side.BLACK) {
			currentSide = ChessPiece.Side.WHITE;
		}
	}

	private void clearAndUpdateMap(float delta) {

		// Clear the map
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				if (map[i][j].getPiece() != null)
					map[i][j].getPiece().setVulnerable(false);
				map[i][j].setPiece(null);
				map[i][j].setMark(false);
				map[i][j].getBoardPiece().setMarkForMove(false);
			}
		}

		// Update every single pieces and update the map
		for (int i = 0; i < MAX_PIECES; i++) {
			ChessPiece piece = pieces[i];
			if (piece == null)
				continue;

			int x = piece.getLocX();
			int z = piece.getLocY();
			if (!(x < 0 || x > 7 || z < 0 || z > 7)) { // Only add the pieces in the board not outside
				map[z][x].setPiece(piece);
				map[z][x].setBoardPiece(board.getBoard()[z][x]);

				// Check for pawn promotions
				if (piece instanceof Pawn) {
					if (piece.getLocY() == 7 || piece.getLocY() == 0) {
						pawnPromotion = true;
						promotedPawn = (Pawn) piece;
						pawnLocation = i;
					}
				}
			}
			piece.update(delta);

		}
	}

	private void initAllPieces() {
		this.map = new MapData[8][8];
		this.pieces = new ChessPiece[MAX_PIECES];
		this.promotions = new ChessPiece[4];
		this.currentSide = ChessPiece.Side.WHITE;
		this.whiteGulag = new Stack<ChessPiece>();
		this.blackGulag = new Stack<ChessPiece>();
		this.pawnLocation = 0;
		this.pawnPromotion = false;
		this.promotedPawn = null;
		// Pawns
		int num = 0;
		for (int i = 0; i < 8; i++) {
			pieces[num++] = new Pawn(i, 1, ChessPiece.Side.WHITE);
		}
		for (int i = 0; i < 8; i++) {
			pieces[num++] = new Pawn(i, 6, ChessPiece.Side.BLACK);
		}

		// Rooks
		pieces[num++] = new Rook(0, 0, ChessPiece.Side.WHITE);
		pieces[num++] = new Rook(7, 0, ChessPiece.Side.WHITE);
		pieces[num++] = new Rook(0, 7, ChessPiece.Side.BLACK);
		pieces[num++] = new Rook(7, 7, ChessPiece.Side.BLACK);

		// Bishops

		pieces[num++] = new Bishop(2, 0, ChessPiece.Side.WHITE);
		pieces[num++] = new Bishop(5, 0, ChessPiece.Side.WHITE);
		pieces[num++] = new Bishop(2, 7, ChessPiece.Side.BLACK);
		pieces[num++] = new Bishop(5, 7, ChessPiece.Side.BLACK);

		// Knights
		pieces[num++] = new Knight(1, 0, ChessPiece.Side.WHITE);
		pieces[num++] = new Knight(6, 0, ChessPiece.Side.WHITE);
		pieces[num++] = new Knight(1, 7, ChessPiece.Side.BLACK);
		pieces[num++] = new Knight(6, 7, ChessPiece.Side.BLACK);

		// Kings
		pieces[num++] = new King(3, 0, ChessPiece.Side.WHITE);
		pieces[num++] = new King(3, 7, ChessPiece.Side.BLACK);

		// Queens
		pieces[num++] = new Queen(4, 0, ChessPiece.Side.WHITE);
		pieces[num++] = new Queen(4, 7, ChessPiece.Side.BLACK);

		num = 0;

		promotions[num++] = new Rook(0, 0, ChessPiece.Side.WHITE);
		promotions[num++] = new Bishop(0, 0, ChessPiece.Side.WHITE);
		promotions[num++] = new Knight(0, 0, ChessPiece.Side.WHITE);
		promotions[num++] = new Queen(0, 0, ChessPiece.Side.WHITE);

		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				map[i][j] = new MapData(null, false, board.getBoard()[i][j]);
			}
		}
	}
}
