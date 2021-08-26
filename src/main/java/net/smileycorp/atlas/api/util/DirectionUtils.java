package net.smileycorp.atlas.api.util;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap.Type;

public class DirectionUtils {

	public static RayTraceResult getPlayerRayTrace(World world, PlayerEntity player, float blockReach) {
		Vector3d eyepos = player.getEyePosition(1);
	    Vector3d  lookangle = player.getLookAngle();
	    Vector3d  lastVec = eyepos.add(lookangle);
	    Vector3d rayend = eyepos.add(lookangle.x * blockReach, lookangle.y * blockReach, lookangle.z * blockReach);
	    RayTraceContext context = new RayTraceContext(eyepos, rayend, BlockMode.COLLIDER, FluidMode.NONE, null);
	    RayTraceResult blockRay = world.clip(context);
		for (int x = 0; x <16*blockReach; x++) {
			float reach = x/16f;
		    Vector3d  vec = eyepos.add(lookangle.x*reach, lookangle.y*reach, lookangle.z*reach);
		    if (blockRay == null || blockRay.getLocation() == null) return new BlockRayTraceResult(lookangle, getFacing(lookangle), null, false);
		    if (blockRay.getLocation().distanceTo(eyepos) < vec.distanceTo(eyepos)) break;
		    AxisAlignedBB AABB = new AxisAlignedBB(lastVec.x, lastVec.y, lastVec.z, vec.x, vec.y, vec.z);
		    List<Entity> entities = world.getEntities(player, AABB);
		    if (!entities.isEmpty()) {
		    	return new EntityRayTraceResult(entities.get(0), lookangle);
		    }
		    lastVec = vec;
		}
		return blockRay;
	}

	private static Vector3d  getPosVecForEntity(Entity entity) {
		return new Vector3d (entity.getX(), entity.getY(), entity.getZ());
	}

	public static Direction getFacing(Vector3d  vec) {
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

	public static Vector3d  getDirectionVecXZ(Vector3i startpos, Vector3i endpos) {
		return getDirectionVecXZ(new Vector3d (startpos.getX(), startpos.getY(), startpos.getZ()),
				new Vector3d (endpos.getX(), endpos.getY(), endpos.getZ()));
	}

	public static Vector3d  getDirectionVecXZ(Vector3d  startpos, Vector3d  endpos) {
		double dx = endpos.x-startpos.x;
		double dz = endpos.z-startpos.z;
		double angle = Math.atan2(dz, dx);
		return getDirectionVecXZ(angle);
	}

	public static Vector3d  getDirectionVecXZ(Entity entity1, Entity entity2) {
		Vector3d  startpos = getPosVecForEntity(entity1);
		Vector3d  endpos = getPosVecForEntity(entity2);
		return getDirectionVecXZ(startpos, endpos);
	}

	public static Vector3d  getDirectionVecXZDegrees(double angle) {
		double rad = Math.toRadians(angle);
		return getDirectionVecXZ(rad);
	}

	public static Vector3d  getDirectionVecXZ(double angle) {
		double x = Math.cos(angle);
		double z = Math.sin(angle);
		return new Vector3d (x, 0, z);
	}

	public static Vector3d  getRandomDirectionVecXZ(Random rand) {
		int angle = rand.nextInt(360);
		return getDirectionVecXZDegrees(angle);
	}

	public static Vector3d  getDirectionVec(Entity entity1, Entity entity2) {
		Vector3d  startpos = getPosVecForEntity(entity1);
		Vector3d  endpos = getPosVecForEntity(entity2);
		return getDirectionVec(startpos, endpos);
	}

	public static Vector3d  getDirectionVec(Vector3i startpos, Vector3i endpos) {
		return getDirectionVec(new Vector3d (startpos.getX(), startpos.getY(), startpos.getZ()),
				new Vector3d (endpos.getX(), endpos.getY(), endpos.getZ()));
	}

	public static Vector3d  getDirectionVec(Vector3d  startpos, Vector3d  endpos) {
		if (startpos.equals(endpos)) return new Vector3d (0,0,0);
		double dx = endpos.x-startpos.x;
		double dy = endpos.y-startpos.y;
		double dz = endpos.z-startpos.z;
		double magnitude = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
		double mx = (endpos.x+startpos.x)/magnitude;
		double my = (endpos.y+startpos.y)/magnitude;
		double mz = (endpos.z+startpos.z)/magnitude;
		return new Vector3d (mx, my, mz);
	}

	public static BlockPos getClosestLoadedPos(World world, BlockPos basepos, Vector3d  direction, double radius) {
		BlockPos pos = world.getHeightmapPos(Type.WORLD_SURFACE, basepos.offset(direction.x*radius, 0, direction.z*radius));
		while (!world.hasChunk(pos.getX(), pos.getZ())) {
			if (radius==0) return basepos;
			radius--;
			pos = world.getHeightmapPos(Type.WORLD_SURFACE, basepos.offset(direction.x*radius, 0, direction.z*radius));
		}
		return pos;
	}

	public static BlockPos getClosestLoadedPos(World world, BlockPos basepos, Vector3d  direction, double radius, int maxlight, int minlight) {
		BlockPos pos = world.getHeightmapPos(Type.WORLD_SURFACE, basepos.offset(direction.x*radius, 0, direction.z*radius));
		while (!world.hasChunk(pos.getX(), pos.getZ()) || !isBrightnessAllowed(world, basepos, maxlight, minlight)) {
			if (radius==0) return basepos;
			radius--;
			pos = world.getHeightmapPos(Type.WORLD_SURFACE, basepos.offset(direction.x*radius, 0, direction.z*radius));
		}
		return pos;
	}

	public static boolean isBrightnessAllowed(World world, BlockPos pos, int maxlight, int minlight) {
		int blocklight = world.getLightEmission(pos);
		if (blocklight > maxlight) return false;
		if (blocklight < minlight) return false;
		return true;
	}

}
