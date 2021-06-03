package org.gage.graph.light;

import org.joml.Vector3f;

public class PointLight {
	private Vector3f color;
	private Vector3f position;
	private float intensity;
	private Attenuation att;

	public PointLight() {
		this(new Vector3f(1, 1, 1), new Vector3f(0, 0, 0), 1, new Attenuation());
	}

	public PointLight(Vector3f color, Vector3f position, float intensity, Attenuation att) {
		this.color = color;
		this.position = position;
		this.intensity = intensity;
		this.att = att;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getIntensity() {
		return intensity;
	}

	public void setIntensity(float intensity) {
		this.intensity = intensity;
	}

	public Attenuation getAtt() {
		return att;
	}

	public void setAtt(Attenuation att) {
		this.att = att;
	}

}
