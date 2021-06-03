package org.gage.data;

public class GameItem {
	private Mesh mesh;
	private Material material;
	private Transform transform;
	
	public GameItem(GameItem other) {
		this.mesh = other.mesh;
		this.material = other.material;
		this.transform = new Transform(other.transform);
	}
	
	public GameItem() {
		this.mesh = null;
		this.material = new Material();
		this.transform = new Transform();
	}

	public GameItem(Mesh mesh, Material material, Transform transform) {
		this.mesh = mesh;
		this.material = material;
		this.transform = transform;
	}

	public GameItem setMesh(Mesh mesh) {
		this.mesh = mesh;
		return this;
	}

	public Mesh getMesh() {
		return mesh;
	}

	public Material getMaterial() {
		return material;
	}

	public Transform getTransform() {
		return transform;
	}

	public GameItem setTransform(Transform transform) {
		this.transform = transform;
		return this;
	}

	public GameItem setMaterial(Material material) {
		this.material = material;
		return this;
	}

}
