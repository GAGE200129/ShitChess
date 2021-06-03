package org.gage.data;

import org.joml.Vector4f;

public class Material {
	private Texture texture;
	private Vector4f ambient;
	private Vector4f diffuse;
	private Vector4f specular;
	private float reflectance;

	public Material() {
		this.texture = null;
		this.diffuse = new Vector4f(1, 1, 1, 1);
		this.ambient = new Vector4f(1, 1, 1, 1);
		this.specular = new Vector4f(1, 1, 1, 1);
		this.reflectance = 1.0f;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Vector4f getAmbient() {
		return ambient;
	}

	public void setAmbient(Vector4f ambient) {
		this.ambient = ambient;
	}

	public Vector4f getDiffuse() {
		return diffuse;
	}

	public void setDiffuse(Vector4f diffuse) {
		this.diffuse = diffuse;
	}

	public Vector4f getSpecular() {
		return specular;
	}

	public void setSpecular(Vector4f specular) {
		this.specular = specular;
	}

	public float getReflectance() {
		return reflectance;
	}

	public void setReflectance(float reflectance) {
		this.reflectance = reflectance;
	}

	public void setDiffuse(float r, float g, float b, float a) {
		this.diffuse.x = r;
		this.diffuse.y = g;
		this.diffuse.z = b;
		this.diffuse.w = a;
		
	}

}
