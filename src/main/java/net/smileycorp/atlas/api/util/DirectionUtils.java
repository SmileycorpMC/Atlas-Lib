package net.smileycorp.atlas.api.util;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class DirectionUtils {
	
	public static RayTraceResult getPlayerRayTrace(World world, EntityPlayer player, float blockReach) {
		Vec3d eyepos = player.getPositionEyes(1f);
	    Vec3d lookangle = player.getLook(1f);
	    Vec3d lastVec = eyepos.addVector(lookangle.x, lookangle.y, lookangle.z);
	    RayTraceResult blockRay = world.rayTraceBlocks(eyepos, eyepos.addVector(lookangle.x * blockReach, lookangle.y * blockReach, lookangle.z * blockReach), false, false, true);
		for (int x = 0; x <16*blockReach; x++) {
			float reach = x/16f;
		    Vec3d vec = eyepos.addVector(lookangle.x*reach, lookangle.y*reach, lookangle.z*reach);
		    if (blockRay == null || blockRay.hitVec == null) return new RayTraceResult(lookangle, null);
		    if (blockRay.hitVec.distanceTo(eyepos) < vec.distanceTo(eyepos)) break;
		    AxisAlignedBB AABB = new AxisAlignedBB(lastVec.x, lastVec.y, lastVec.z, vec.x, vec.y, vec.z);
		    List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(player, AABB);
		    if (!entities.isEmpty()) {
		    	return new RayTraceResult(entities.get(0), lookangle);
		    }
		    lastVec = vec;
		}
		return blockRay;
	}
	
	public static EnumFacing getFacing(Vec3d vec) {
		return EnumFacing.getFacingFromVector((float)vec.x, (float)vec.y, (float)vec.z);
	}

	public static EnumFacing getRandomDirectionXZ(Random rand) {
		return getXZDirection(rand.nextInt(4));
	}

	public static EnumFacing getXZDirection(int direction) {
		switch (direction) {
			case 1:
				return EnumFacing.SOUTH;
			case 2:
				return EnumFacing.EAST;
			case 3:
				return EnumFacing.WEST;
			default:
				return EnumFacing.NORTH;
		}
	}
	
	public static int getXZMeta(EnumFacing facing) {
		if (facing==EnumFacing.UP||facing==EnumFacing.DOWN) {
			return facing.getIndex()+4;
		}
		return facing.getIndex()-2;
	}
	
	public static Vec3d getDirectionVecXZ(BlockPos startpos, BlockPos endpos) {
		int dx = endpos.getX()-startpos.getX();
		int dz = endpos.getZ()-startpos.getZ();
		double angle = Math.atan(dz/dx);
		return getDirectionVecXZ(angle);
	}
	
	public static Vec3d getDirectionVecXZDegrees(double angle) {
		double rad = Math.toRadians(angle);
		return getDirectionVecXZ(rad);
	}
	
	public static Vec3d getDirectionVecXZ(double angle) {
		double x = Math.cos(angle);
		double z = Math.sin(angle);
		return new Vec3d(x, 0, z);
	}
	
	public static Vec3d getRandomDirectionVecXZ(Random rand) {
		int angle = rand.nextInt(360);
		return getDirectionVecXZDegrees(angle);
	}

	public static BlockPos getClosestLoadedPos(World world, BlockPos basepos, Vec3d direction, int radius) {
		BlockPos pos = world.getTopSolidOrLiquidBlock(basepos.add(direction.x*radius, 0, direction.z*radius));
		while (!world.getChunkFromBlockCoords(pos).isLoaded()) {
			if (radius==0) return basepos;
			radius--;
			pos = world.getTopSolidOrLiquidBlock(basepos.add(direction.x*radius, 0, direction.z*radius));
		}
		return pos;
	}
	
	public static BlockPos getClosestLoadedPos(World world, BlockPos basepos, Vec3d direction, int radius, int maxlight, int minlight) {
		BlockPos pos = world.getTopSolidOrLiquidBlock(basepos.add(direction.x*radius, 0, direction.z*radius));
		while (!world.getChunkFromBlockCoords(pos).isLoaded() || !isBrightnessAllowed(world, basepos, maxlight, minlight)) {
			if (radius==0) return basepos;
			radius--;
			pos = world.getTopSolidOrLiquidBlock(basepos.add(direction.x*radius, 0, direction.z*radius));
		}
		return pos;
	}
	
	public static boolean isBrightnessAllowed(World world, BlockPos pos, int maxlight, int minlight) {
		int blocklight = world.getLightFromNeighbors(pos);
		if (blocklight > maxlight) return false;
		if (blocklight < minlight) return false;
		return true;
	}

}
