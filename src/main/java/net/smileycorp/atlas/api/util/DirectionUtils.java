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

public class DirectionUtils {

	public static HitResult getEntityRayTrace(Level level, Entity entity, float reach) {
		Vec3 eyepos = entity.getEyePosition();
		Vec3 lookangle = entity.getLookAngle();
		Vec3 lastVec = eyepos.add(lookangle);
		Vec3 rayend = eyepos.add(lookangle.x * reach, lookangle.y * reach, lookangle.z * reach);
		//level.
		ClipContext context = new ClipContext(eyepos, rayend, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, CollisionContext.of(entity));
		BlockHitResult blockRay = level.clip(context);
		for (int x = 0; x <16*reach; x++) {
			float reachpart = x/16f;
			Vec3 vec = eyepos.add(lookangle.x*reachpart, lookangle.y*reachpart, lookangle.z*reachpart);
			if (blockRay == null || blockRay.getLocation() == null) return new BlockHitResult(lookangle, getFacing(lookangle), null, false);
			if (blockRay.getLocation().distanceTo(eyepos) < vec.distanceTo(eyepos)) break;
			AABB aabb = new AABB(lastVec.x, lastVec.y, lastVec.z, vec.x, vec.y, vec.z);
			List<Entity> entities = level.getEntities(entity, aabb);
			if (!entities.isEmpty()) {
				return new EntityHitResult(entities.get(0), lookangle);
			}
			lastVec = vec;
		}
		return blockRay;
	}

	public static Direction getFacing(Vec3 vec) {
		return Direction.getNearest(vec.x, vec.y, vec.z);
	}

	public static Direction getRandomDirectionXZ(RandomSource rand) {
		return getXZDirection(rand.nextInt(4));
	}

	public static Direction getXZDirection(int direction) {
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

	public static int getXZMeta(Direction facing) {
		return facing == Direction.UP || facing == Direction.DOWN ? facing.ordinal() + 4 : facing.ordinal() - 2;
	}

	public static Vec3 getDirectionVecXZ(Vec3i startpos, Vec3i endpos) {
		return getDirectionVecXZ(new Vec3 (startpos.getX(), startpos.getY(), startpos.getZ()),
				new Vec3 (endpos.getX(), endpos.getY(), endpos.getZ()));
	}

	public static Vec3 getDirectionVecXZ(Vec3 startpos, Vec3 endpos) {
		double dx = endpos.x-startpos.x;
		double dz = endpos.z-startpos.z;
		double angle = Math.atan2(dz, dx);
		return getDirectionVecXZ(angle);
	}

	public static Vec3 getDirectionVecXZ(Entity entity1, Entity entity2) {
		return getDirectionVecXZ(entity1.position(), entity2.position());
	}

	public static Vec3 getDirectionVecXZDegrees(double angle) {
		return getDirectionVecXZ(Math.toRadians(angle));
	}

	public static Vec3 getDirectionVecXZ(double angle) {
		return new Vec3(Math.cos(angle), 0, Math.sin(angle));
	}

	public static Vec3 getRandomDirectionVecXZ(RandomSource rand) {
		return getDirectionVecXZDegrees(rand.nextInt(360));
	}

	public static Vec3 getDirectionVec(Entity entity1, Entity entity2) {
		return getDirectionVec(entity1.position(),entity2.position());
	}

	public static Vec3 getDirectionVec(Vec3i startpos, Vec3i endpos) {
		return getDirectionVec(new Vec3 (startpos.getX(), startpos.getY(), startpos.getZ()),
				new Vec3 (endpos.getX(), endpos.getY(), endpos.getZ()));
	}

	public static Vec3 getDirectionVec(Vec3 startpos, Vec3 endpos) {
		if (startpos.equals(endpos)) return new Vec3 (0,0,0);
		double dx = endpos.x - startpos.x;
		double dy = endpos.y - startpos.y;
		double dz = endpos.z - startpos.z;
		double magnitude = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
		return new Vec3 (dx / magnitude, dy / magnitude, dz / magnitude);
	}

	public static BlockPos getClosestLoadedPos(Level level, BlockPos basepos, Vec3 direction, double radius) {
		return getClosestLoadedPos(level, basepos, direction, radius, Heightmap.Types.WORLD_SURFACE_WG);
	}

	public static BlockPos getClosestLoadedPos(Level level, BlockPos basepos, Vec3 direction, double radius, Heightmap.Types type) {
		BlockPos pos = level.getHeightmapPos(type, basepos.offset((int)(direction.x*radius), 0, (int) (direction.z*radius)));
		while (!level.hasChunk(pos.getX()/16, pos.getZ()/16)) {
			if (radius == 0) return basepos;
			radius--;
			pos = level.getHeightmapPos(type, basepos.offset((int) (direction.x*radius), 0, (int) (direction.z*radius)));
		}
		return pos;
	}

	public static BlockPos getClosestLoadedPos(Level level, BlockPos basepos, Vec3 direction, double radius, int maxlight, int minlight) {
		return getClosestLoadedPos(level, basepos, direction, radius, maxlight, minlight, Heightmap.Types.WORLD_SURFACE_WG);
	}

	public static BlockPos getClosestLoadedPos(Level level, BlockPos basepos, Vec3 direction, double radius, int maxlight, int minlight, Heightmap.Types type) {
		BlockPos pos = level.getHeightmapPos(type, basepos.offset((int) (direction.x*radius), 0, (int) (direction.z*radius)));
		while (!level.hasChunk(pos.getX()/16, pos.getZ()/16) || !isBrightnessAllowed(level, pos, maxlight, minlight)) {
			if (radius == 0) return basepos;
			radius--;
			pos = level.getHeightmapPos(type, basepos.offset((int) (direction.x*radius), 0, (int) (direction.z*radius)));
		}
		return pos;
	}

	public static Vec3 getClosestLoadedPos(Level level, Vec3 basepos, Vec3 direction, double radius) {
		return getClosestLoadedPos(level, basepos, direction, radius, Heightmap.Types.WORLD_SURFACE_WG);
	}

	public static Vec3 getClosestLoadedPos(Level level, Vec3 basepos, Vec3 direction, double radius, Heightmap.Types type) {
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

	public static Vec3 getClosestLoadedPos(Level level, Vec3 basepos, Vec3 direction, double radius, int maxlight, int minlight) {
		return getClosestLoadedPos(level, basepos, direction, radius, maxlight, minlight, Heightmap.Types.WORLD_SURFACE_WG);
	}

	public static Vec3 getClosestLoadedPos(Level level, Vec3 basepos, Vec3 direction, double radius, int maxlight, int minlight, Heightmap.Types type) {
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
