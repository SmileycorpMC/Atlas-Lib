package net.smileycorp.atlas.api.util;

import java.util.List;
import java.util.Random;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class DirectionUtils {

	public static HitResult getEntityRayTrace(Level level, Entity entity, float reach) {
		Vec3 eyepos = entity.getEyePosition();
		Vec3  lookangle = entity.getLookAngle();
		Vec3  lastVec = eyepos.add(lookangle);
		Vec3 rayend = eyepos.add(lookangle.x * reach, lookangle.y * reach, lookangle.z * reach);
		//level.
		ClipContext context = new ClipContext(eyepos, rayend, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, null);
		BlockHitResult blockRay = level.clip(context);
		for (int x = 0; x <16*reach; x++) {
			float reachpart = x/16f;
			Vec3  vec = eyepos.add(lookangle.x*reachpart, lookangle.y*reachpart, lookangle.z*reachpart);
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

	private static Vec3  getPosVecForEntity(Entity entity) {
		return new Vec3 (entity.getX(), entity.getY(), entity.getZ());
	}

	public static Direction getFacing(Vec3 vec) {
		return Direction.getNearest(vec.x, vec.y, vec.z);
	}

	public static Direction getRandomDirectionXZ(Random rand) {
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
		if (facing==Direction.UP||facing==Direction.DOWN) {
			return facing.ordinal()+4;
		}
		return facing.ordinal()-2;
	}

	public static Vec3  getDirectionVecXZ(Vec3i startpos, Vec3i endpos) {
		return getDirectionVecXZ(new Vec3 (startpos.getX(), startpos.getY(), startpos.getZ()),
				new Vec3 (endpos.getX(), endpos.getY(), endpos.getZ()));
	}

	public static Vec3 getDirectionVecXZ(Vec3 startpos, Vec3 endpos) {
		double dx = endpos.x-startpos.x;
		double dz = endpos.z-startpos.z;
		double angle = Math.atan2(dz, dx);
		return getDirectionVecXZ(angle);
	}

	public static Vec3  getDirectionVecXZ(Entity entity1, Entity entity2) {
		Vec3  startpos = getPosVecForEntity(entity1);
		Vec3  endpos = getPosVecForEntity(entity2);
		return getDirectionVecXZ(startpos, endpos);
	}

	public static Vec3  getDirectionVecXZDegrees(double angle) {
		double rad = Math.toRadians(angle);
		return getDirectionVecXZ(rad);
	}

	public static Vec3  getDirectionVecXZ(double angle) {
		double x = Math.cos(angle);
		double z = Math.sin(angle);
		return new Vec3 (x, 0, z);
	}

	public static Vec3  getRandomDirectionVecXZ(Random rand) {
		int angle = rand.nextInt(360);
		return getDirectionVecXZDegrees(angle);
	}

	public static Vec3  getDirectionVec(Entity entity1, Entity entity2) {
		Vec3  startpos = getPosVecForEntity(entity1);
		Vec3  endpos = getPosVecForEntity(entity2);
		return getDirectionVec(startpos, endpos);
	}

	public static Vec3  getDirectionVec(Vec3i startpos, Vec3i endpos) {
		return getDirectionVec(new Vec3 (startpos.getX(), startpos.getY(), startpos.getZ()),
				new Vec3 (endpos.getX(), endpos.getY(), endpos.getZ()));
	}

	public static Vec3 getDirectionVec(Vec3  startpos, Vec3  endpos) {
		if (startpos.equals(endpos)) return new Vec3 (0,0,0);
		double dx = endpos.x-startpos.x;
		double dy = endpos.y-startpos.y;
		double dz = endpos.z-startpos.z;
		double magnitude = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
		double mx = (endpos.x-startpos.x)/magnitude;
		double my = (endpos.y-startpos.y)/magnitude;
		double mz = (endpos.z-startpos.z)/magnitude;
		return new Vec3 (mx, my, mz);
	}

	public static BlockPos getClosestLoadedPos(Level level, BlockPos basepos, Vec3  direction, double radius) {
		return getClosestLoadedPos(level, basepos, direction, radius, Heightmap.Types.WORLD_SURFACE_WG);
	}

	public static BlockPos getClosestLoadedPos(Level level, BlockPos basepos, Vec3  direction, double radius, Heightmap.Types type) {
		BlockPos pos = level.getHeightmapPos(type, basepos.offset(direction.x*radius, 0, direction.z*radius));
		while (!level.hasChunk(pos.getX()/16, pos.getZ()/16)) {
			if (radius==0) return basepos;
			radius--;
			pos = level.getHeightmapPos(type, basepos.offset(direction.x*radius, 0, direction.z*radius));
		}
		return pos;
	}

	public static BlockPos getClosestLoadedPos(Level level, BlockPos basepos, Vec3  direction, double radius, int maxlight, int minlight) {
		return getClosestLoadedPos(level, basepos, direction, radius, maxlight, minlight, Heightmap.Types.WORLD_SURFACE_WG);
	}

	public static BlockPos getClosestLoadedPos(Level level, BlockPos basepos, Vec3  direction, double radius, int maxlight, int minlight, Heightmap.Types type) {
		BlockPos pos = level.getHeightmapPos(type, basepos.offset(direction.x*radius, 0, direction.z*radius));
		while (!level.hasChunk(pos.getX()/16, pos.getZ()/16) || !isBrightnessAllowed(level, basepos, maxlight, minlight)) {
			if (radius==0) return basepos;
			radius--;
			pos = level.getHeightmapPos(type, basepos.offset(direction.x*radius, 0, direction.z*radius));
		}
		return pos;
	}

	public static boolean isBrightnessAllowed(Level level, BlockPos pos, int maxlight, int minlight) {
		int blocklight = level.getBrightness(LightLayer.BLOCK, pos);
		if (blocklight > maxlight) return false;
		if (blocklight < minlight) return false;
		return true;
	}

	public void findNearestVilage(ServerLevel level, Entity entity) {

	}

}
