package org.gage.utils;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.assimp.AIVector3D;

public class MathUtils {

	public static Vector3f aiVector3DToVector3f(AIVector3D vec) {
		return new Vector3f(vec.x(), vec.y(), vec.z());
	}

	public static Vector3f rayPicking(Matrix4f projection, Matrix4f view, float cursorX, float cursorY,
			float screenWidth, float screenHeight) {

		Vector4f ray = new Vector4f();
		ray.x = (2.0f * cursorX) / screenWidth - 1.0f;
		ray.y = 1.0f - (2.0f * cursorY) / screenHeight;
		ray.z = -1.0f;
		ray.w = 1.0f;
		ray.mul(new Matrix4f(projection).invert());

		ray.z = -1;
		ray.w = 0;
		ray.mul(new Matrix4f(view).invert());
		ray.normalize();

		return new Vector3f(ray.x, ray.y, ray.z).normalize();
	}

	public static void createViewMatrix(float pitch, float yaw, Vector3f position, Matrix4f to) {
		Quaternionf qPitch = new Quaternionf();
		Quaternionf qYaw = new Quaternionf();
		qPitch.rotateX((float) Math.toRadians(pitch));
		qPitch.rotateY((float) Math.toRadians(yaw));
		qPitch.mul(qYaw).normalize();

		to.identity();
		to.rotate(qPitch);
		to.scale(1.0f);
		to.translate(new Vector3f(position).mul(-1f));
	}

	public static void createProjectionMatrix(float fov, float near, float far, float width, float height,
			Matrix4f to) {

		to.identity();
		to.perspective((float) Math.toRadians(fov), (float) width / (float) height, near, far);
	}

}
