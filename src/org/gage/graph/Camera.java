package org.gage.graph;

import org.gage.core.Window;
import org.gage.data.Transform;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Camera {

	private static final Vector3f LOCAL_UP = new Vector3f(0, 1, 0);

	public Transform transform;
	public Matrix4f projectionMatrix;
	public float fov, near, far;
	public float pitch, yaw;

	public Camera() {
		this.transform = new Transform();
		this.projectionMatrix = new Matrix4f().identity();
		this.fov = 60.0f;
		this.near = 0.1f;
		this.far = 100.0f;
	}

	public void update(Window window, double d) {

		// Update projection
		float aspectRatio = (float) window.getWidth() / (float) window.getHeight();
		float fovRadians = (float) Math.toRadians(fov);
		projectionMatrix.identity();
		projectionMatrix.perspective(fovRadians, aspectRatio, near, far);

	
		transform.transformationMatrix.identity();
		transform.transformationMatrix.scale(1);
		transform.transformationMatrix.rotateX(pitch);
		transform.transformationMatrix.rotateY(yaw);
		transform.transformationMatrix.rotateZ(0);
		transform.transformationMatrix.translate(new Vector3f(transform.position).mul(-1));
		

	}


}
