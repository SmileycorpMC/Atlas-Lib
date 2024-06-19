package net.smileycorp.atlas.api.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.CollisionContext;

import java.util.List;

public class VecMath {

	public static HitResult entityRayTrace(Level level, Entity entity, float reach) {
		Vec3 eye = entity.getEyePosition();
		Vec3 angle = entity.getLookAngle();
		Vec3 last = eye.add(angle);
		Vec3 end = eye.add(angle.x * reach, angle.y * reach, angle.z * reach);
		//level.
		ClipContext context = new ClipContext(eye, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.of(entity));
		BlockHitResult blockRay = level.clip(context);
		for (int x = 0; x <16 * reach; x++) {
			float segment = x/16f;
			Vec3 vec = eye.add(angle.x * segment, angle.y * segment, angle.z * segment);
			if (blockRay == null || blockRay.getLocation() == null)
				return new BlockHitResult(angle, Direction.getNearest(angle), null, false);
			if (blockRay.getLocation().distanceTo(eye) < vec.distanceTo(eye)) break;
			AABB aabb = new AABB(last.x, last.y, last.z, vec.x, vec.y, vec.z);
			List<Entity> entities = level.getEntities(entity, aabb);
			if (!entities.isEmpty()) return new EntityHitResult(entities.get(0), angle);
			last = vec;
		}
		return blockRay;
	}

	public static Direction randomDirXZ(RandomSource rand) {
		return xzFromMeta(rand.nextInt(4));
	}

	public static Direction xzFromMeta(int direction) {
		switch (direction) {
		case 1:
			return Direction.SOUTH;
		case 2:
			return Direction.EAST;
		case 3:
			return Direction.WEST;
		default:
			return Direction.NORTH;
		}
	}

	public static int metaFromXZ(Direction facing) {
		return facing == Direction.UP || facing == Direction.DOWN ? facing.ordinal() + 4 : facing.ordinal() - 2;
	}

	public static Vec3 directionXZ(Vec3i start, Vec3i end) {
		return directionXZ(new Vec3 (start.getX(), start.getY(), start.getZ()),
				new Vec3 (end.getX(), end.getY(), end.getZ()));
	}

	public static Vec3 directionXZ(Vec3 start, Vec3 end) {
		return directionXZ(Math.atan2(end.x - start.x, end.z - start.z));
	}

	public static Vec3 directionXZ(Entity start, Entity end) {
		return directionXZ(start.position(), end.position());
	}

	public static Vec3 directionXZDegrees(double angle) {
		return directionXZ(Math.toRadians(angle));
	}

	public static Vec3 directionXZ(double angle) {
		return new Vec3(Math.cos(angle), 0, Math.sin(angle));
	}

	public static Vec3 randomXZVec(RandomSource rand) {
		return directionXZDegrees(rand.nextInt(360));
	}

	public static Vec3 direction(Entity start, Entity end) {
		return direction(start.position(),end.position());
	}

	public static Vec3 direction(Vec3i startpos, Vec3i endpos) {
		return direction(new Vec3 (startpos.getX(), startpos.getY(), startpos.getZ()),
				new Vec3 (endpos.getX(), endpos.getY(), endpos.getZ()));
	}

	public static Vec3 direction(Vec3 startpos, Vec3 endpos) {
		if (startpos.equals(endpos)) return new Vec3 (0,0,0);
		double dx = endpos.x - startpos.x;
		double dy = endpos.y - startpos.y;
		double dz = endpos.z - startpos.z;
		double magnitude = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
		return new Vec3 (dx / magnitude, dy / magnitude, dz / magnitude);
	}

	public static BlockPos closestLoadedPos(Level level, BlockPos basepos, Vec3 direction, double radius) {
		return closestLoadedPos(level, basepos, direction, radius, Heightmap.Types.WORLD_SURFACE_WG);
	}

	public static BlockPos closestLoadedPos(Level level, BlockPos basepos, Vec3 direction, double radius, Heightmap.Types type) {
		BlockPos pos = level.getHeightmapPos(type, basepos.offset((int)(direction.x*radius), 0, (int) (direction.z*radius)));
		while (!level.hasChunk(pos.getX()/16, pos.getZ()/16)) {
			if (radius == 0) return basepos;
			radius--;
			pos = level.getHeightmapPos(type, basepos.offset((int) (direction.x*radius), 0, (int) (direction.z*radius)));
		}
		return pos;
	}

	public static BlockPos closestLoadedPos(Level level, BlockPos basepos, Vec3 direction, double radius, int maxlight, int minlight) {
		return closestLoadedPos(level, basepos, direction, radius, maxlight, minlight, Heightmap.Types.WORLD_SURFACE_WG);
	}

	public static BlockPos closestLoadedPos(Level level, BlockPos basepos, Vec3 direction, double radius, int maxlight, int minlight, Heightmap.Types type) {
		BlockPos pos = level.getHeightmapPos(type, basepos.offset((int) (direction.x*radius), 0, (int) (direction.z*radius)));
		while (!level.hasChunk(pos.getX()/16, pos.getZ()/16) || !isBrightnessAllowed(level, pos, maxlight, minlight)) {
			if (radius == 0) return basepos;
			radius--;
			pos = level.getHeightmapPos(type, basepos.offset((int) (direction.x*radius), 0, (int) (direction.z*radius)));
		}
		return pos;
	}

	public static Vec3 closestLoadedPos(Level level, Vec3 basepos, Vec3 direction, double radius) {
		return closestLoadedPos(level, basepos, direction, radius, Heightmap.Types.WORLD_SURFACE_WG);
	}

	public static Vec3 closestLoadedPos(Level level, Vec3 basepos, Vec3 direction, double radius, Heightmap.Types type) {
		Vec3 pos = basepos.add(direction.x * radius, 0,direction.z * radius);
		pos = pos.add(0, level.getHeight(type, (int)pos.x, (int)pos.z) - pos.y, 0);
		while (!level.hasChunk(((int)pos.x)/16, ((int)pos.z)/16)) {
			if (radius==0) return basepos;
			radius--;
			pos = basepos.add(direction.x * radius, 0,direction.z * radius)
					.add(0, level.getHeight(type, (int)pos.x, (int)pos.z)-pos.y, 0);
		}
		return pos;
	}

	public static Vec3 closestLoadedPos(Level level, Vec3 basepos, Vec3 direction, double radius, int maxlight, int minlight) {
		return closestLoadedPos(level, basepos, direction, radius, maxlight, minlight, Heightmap.Types.WORLD_SURFACE_WG);
	}

	public static Vec3 closestLoadedPos(Level level, Vec3 basepos, Vec3 direction, double radius, int maxlight, int minlight, Heightmap.Types type) {
		Vec3 pos = basepos.add(direction.x * radius, 0,direction.z * radius);
		pos = pos.add(0, level.getHeight(type, (int)pos.x, (int)pos.z) - pos.y, 0);
		while (!level.hasChunk(((int)pos.x)/16, ((int)pos.z)/16) || !isBrightnessAllowed(level, BlockPos.containing(pos), maxlight, minlight)) {
			if (radius == 0) return basepos;
			radius--;
			pos = basepos.add(direction.x*radius, 0,direction.z * radius);
			pos = pos.add(0, level.getHeight(type, (int)pos.x, (int)pos.z) - pos.y, 0);
		}
		return pos;
	}

	public static boolean isBrightnessAllowed(Level level, BlockPos pos, int maxlight, int minlight) {
		int blocklight = level.getBrightness(LightLayer.BLOCK, pos);
		if (blocklight > maxlight) return false;
		if (blocklight < minlight) return false;
		return true;
	}

}
