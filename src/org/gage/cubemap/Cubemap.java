package org.gage.cubemap;

import java.io.IOException;
import java.io.InputStream;

import org.gage.data.Mesh;
import org.gage.data.TextureCube;

public class Cubemap {
	
	private final TextureCube texture;
	private final Mesh mesh;

	public Cubemap(String cubemap) throws IOException {

		InputStream[] is = new InputStream[] { 
				Cubemap.class.getResourceAsStream(cubemap + "/right.jpg"),
				Cubemap.class.getResourceAsStream(cubemap + "/left.jpg"),
				Cubemap.class.getResourceAsStream(cubemap + "/top.jpg"),
				Cubemap.class.getResourceAsStream(cubemap + "/bottom.jpg"),
				Cubemap.class.getResourceAsStream(cubemap + "/front.jpg"),
				Cubemap.class.getResourceAsStream(cubemap + "/back.jpg")
		};
		
		float[] skyboxVertices = {
			    // positions          
			    -1.0f,  1.0f, -1.0f,
			    -1.0f, -1.0f, -1.0f,
			     1.0f, -1.0f, -1.0f,
			     1.0f, -1.0f, -1.0f,
			     1.0f,  1.0f, -1.0f,
			    -1.0f,  1.0f, -1.0f,

			    -1.0f, -1.0f,  1.0f,
			    -1.0f, -1.0f, -1.0f,
			    -1.0f,  1.0f, -1.0f,
			    -1.0f,  1.0f, -1.0f,
			    -1.0f,  1.0f,  1.0f,
			    -1.0f, -1.0f,  1.0f,

			     1.0f, -1.0f, -1.0f,
			     1.0f, -1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			     1.0f,  1.0f, -1.0f,
			     1.0f, -1.0f, -1.0f,

			    -1.0f, -1.0f,  1.0f,
			    -1.0f,  1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			     1.0f, -1.0f,  1.0f,
			    -1.0f, -1.0f,  1.0f,

			    -1.0f,  1.0f, -1.0f,
			     1.0f,  1.0f, -1.0f,
			     1.0f,  1.0f,  1.0f,
			     1.0f,  1.0f,  1.0f,
			    -1.0f,  1.0f,  1.0f,
			    -1.0f,  1.0f, -1.0f,

			    -1.0f, -1.0f, -1.0f,
			    -1.0f, -1.0f,  1.0f,
			     1.0f, -1.0f, -1.0f,
			     1.0f, -1.0f, -1.0f,
			    -1.0f, -1.0f,  1.0f,
			     1.0f, -1.0f,  1.0f
			};
		
		this.texture = new TextureCube(is);
		this.mesh = new Mesh(skyboxVertices);
		
		
	}
	
	public void render() {
		texture.bind();
		mesh.renderNoIndices();
	}
}
