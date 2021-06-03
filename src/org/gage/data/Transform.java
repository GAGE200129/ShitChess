package org.gage.data;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class Transform {
	public Vector3f position;
	public Quaternionf rotation;
	public float scale;

	public Matrix4f transformationMatrix;

	public Transform() {
		this(new Vector3f(), new Quaternionf().identity(), 1f);
	}

	public Transform(Vector3f position, Quaternionf rotation, float scale) {
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.transformationMatrix = new Matrix4f();
	}

	public Transform(Transform other) {
		this.position = new Vector3f(other.position);
		this.rotation = new Quaternionf(other.rotation);
		this.scale = other.scale;
	}

	public Matrix4f buildTransformationMatrix() {
		transformationMatrix.identity();
		transformationMatrix.translationRotateScale(position, rotation, scale);
		return transformationMatrix;
	}
	

}
