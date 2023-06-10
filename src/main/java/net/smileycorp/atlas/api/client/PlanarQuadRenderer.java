package net.smileycorp.atlas.api.client;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

class PlanarQuadRenderer {

	static Vec3 getOffsetFor(Direction facing, double x, double y, double z, int layer) {
		return getOffsetFor(facing, (float)x, (float)y, (float)z, layer);
	}

	private static Vec3 getOffsetFor(Direction facing, float x, float y, float z, int layer) {
		return getOffsetFor(facing, new Vec3(x, y, z), layer);
	}

	public static Vec3 getOffsetFor(Direction facing, Vec3 vector, int layer) {
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

	static Vec3[] getQuadsFor(Direction facing) {
		if (facing!=null) {
			switch(facing) {
			case DOWN:
				return new Vec3[] {
						new Vec3(0, 0, 1),
						new Vec3(0, 0, 0),
						new Vec3(1, 0, 0),
						new Vec3(1, 0, 1)};
			case NORTH:
				return new Vec3[] {
						new Vec3(1, 1, 0),
						new Vec3(1, 0, 0),
						new Vec3(0, 0, 0),
						new Vec3(0, 1, 0)};
			case SOUTH:
				return new Vec3[] {
						new Vec3(0, 1, 1),
						new Vec3(0, 0, 1),
						new Vec3(1, 0, 1),
						new Vec3(1, 1, 1)};
			case EAST:
				return new Vec3[] {
						new Vec3(1, 1, 1),
						new Vec3(1, 0, 1),
						new Vec3(1, 0, 0),
						new Vec3(1, 1, 0)};
			case WEST:
				return new Vec3[] {
						new Vec3(0, 1, 0),
						new Vec3(0, 0, 0),
						new Vec3(0, 0, 1),
						new Vec3(0, 1, 1)};
			default:
				return new Vec3[] {
						new Vec3(1, 1, 1),
						new Vec3(1, 1, 0),
						new Vec3(0, 1, 0),
						new Vec3(0, 1, 1)};
			}
		}
		return new Vec3[] {
				new Vec3(1, 1, 1),
				new Vec3(1, 1, 0),
				new Vec3(0, 1, 0),
				new Vec3(0, 1, 1)};
	}
}