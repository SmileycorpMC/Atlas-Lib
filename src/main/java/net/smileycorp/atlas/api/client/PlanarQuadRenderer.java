package net.smileycorp.atlas.api.client;

import com.mojang.math.Vector3f;

import net.minecraft.core.Direction;

class PlanarQuadRenderer {

	static Vector3f getOffsetFor(Direction facing, double x, double y, double z, int layer) {
		return getOffsetFor(facing, (float)x, (float)y, (float)z, layer);
	}

	private static Vector3f getOffsetFor(Direction facing, float x, float y, float z, int layer) {
		return getOffsetFor(facing, new Vector3f(x, y, z), layer);
	}

	public static Vector3f getOffsetFor(Direction facing, Vector3f vector, int layer) {
		if (layer == 0) return vector;
		switch(facing) {
		case UP:
			vector.add(0, offsetLayer(layer), 0);
			break;
		case DOWN:
			vector.add(0, offsetLayer(-layer), 0);
			break;
		case NORTH:
			vector.add(0, 0, offsetLayer(-layer));
			break;
		case SOUTH:
			vector.add(0, 0, offsetLayer(layer));
			break;
		case EAST:
			vector.add(offsetLayer(layer), 0, 0);
			break;
		case WEST:
			vector.add(offsetLayer(-layer), 0, 0);
			break;
		}
		return vector;
	}

	private static float offsetLayer(int layer) {
		return 0.001f*layer;
	}

	static Vector3f[] getQuadsFor(Direction facing) {
		if (facing!=null) {
			switch(facing) {
			case DOWN:
				return new Vector3f[] {
						new Vector3f(0, 0, 1),
						new Vector3f(0, 0, 0),
						new Vector3f(1, 0, 0),
						new Vector3f(1, 0, 1)};
			case NORTH:
				return new Vector3f[] {
						new Vector3f(1, 1, 0),
						new Vector3f(1, 0, 0),
						new Vector3f(0, 0, 0),
						new Vector3f(0, 1, 0)};
			case SOUTH:
				return new Vector3f[] {
						new Vector3f(0, 1, 1),
						new Vector3f(0, 0, 1),
						new Vector3f(1, 0, 1),
						new Vector3f(1, 1, 1)};
			case EAST:
				return new Vector3f[] {
						new Vector3f(1, 1, 1),
						new Vector3f(1, 0, 1),
						new Vector3f(1, 0, 0),
						new Vector3f(1, 1, 0)};
			case WEST:
				return new Vector3f[] {
						new Vector3f(0, 1, 0),
						new Vector3f(0, 0, 0),
						new Vector3f(0, 0, 1),
						new Vector3f(0, 1, 1)};
			default:
				return new Vector3f[] {
						new Vector3f(1, 1, 1),
						new Vector3f(1, 1, 0),
						new Vector3f(0, 1, 0),
						new Vector3f(0, 1, 1)};
			}
		}
		return new Vector3f[] {
				new Vector3f(1, 1, 1),
				new Vector3f(1, 1, 0),
				new Vector3f(0, 1, 0),
				new Vector3f(0, 1, 1)};
	}
}