package org.gage;

import org.gage.chess.MasterChess;
import org.gage.core.GameEngine;
import org.gage.core.IGameLogic;
import org.gage.core.Input;
import org.gage.core.Window;
import org.gage.cubemap.Cubemap;
import org.gage.cubemap.CubemapRenderer;
import org.gage.graph.Camera;
import org.gage.graph.DefaultConfig;
import org.gage.graph.SkyboxConfig;
import org.gage.graph.light.DirectionalLight;
import org.gage.graph.light.PointLight;
import org.joml.Vector3f;

public class Main implements IGameLogic {

	private MasterChess chess;
	private Camera camera;

	private PointLight light;
	private DirectionalLight directional;
	private float lightAngle;
	private Vector3f ambient;
	private int specularPower;
	private Cubemap cubemap;
	private CubemapRenderer cubemapRenderer;

	@Override
	public void init() throws Exception {
		camera = new Camera();
		chess = new MasterChess(new DefaultConfig(), camera);
		light = new PointLight();
		directional = new DirectionalLight(new Vector3f(1, 1, 1), new Vector3f(0, 1, 0.3f), 0.5f);
		ambient = new Vector3f(0.1f, 0.1f, 0.1f);
		specularPower = 32;
		cubemap = new Cubemap("/cubemaps/sky");
		cubemapRenderer = new CubemapRenderer(new SkyboxConfig());
		light.getAtt().setConstant(0.3f);
		light.setPosition(new Vector3f(0, 0, 0));
		light.setIntensity(0.5f);

	}

	@Override
	public void input(Input input) {
		chess.input(input);
	}

	@Override
	public void update(Window window, double delta) {

		camera.update(window, delta);
		chess.update((float) delta);

		lightAngle += delta * 50.0f;
		if (lightAngle > 90) {
			directional.setIntensity(0);
			if (lightAngle >= 360) {
				lightAngle = -90;
			}
		} else if (lightAngle <= -80 || lightAngle >= 80) {
			float factor = 1 - (float) (Math.abs(lightAngle) - 80) / 10.0f;
			directional.setIntensity(factor);
			directional.getColor().y = Math.max(factor, 0.9f);
			directional.getColor().z = Math.max(factor, 0.5f);
		} else {
			directional.setIntensity(1);
			directional.getColor().x = 1;
			directional.getColor().y = 1;
			directional.getColor().z = 1;
		}
		double angRad = Math.toRadians(lightAngle);
		directional.getDirection().x = (float) Math.sin(angRad);
		directional.getDirection().y = (float) Math.cos(angRad);
	}

	@Override
	public void render(Window window) {
		cubemapRenderer.render(cubemap, camera);
		chess.render(camera, light, directional, specularPower, ambient, window);
	}

	@Override
	public void shutdown() {

	}

	public static void main(String[] args) {

		try {
			new GameEngine("Cumgame", 800, 600, true, new Main());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
