package net.smileycorp.atlas.api.util;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
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
	
	public static Vec3d getPosVecForEntity(Entity entity) {
		return new Vec3d(entity.posX, entity.posY, entity.posZ);
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
	
	public static Vec3d getDirectionVecXZ(Vec3i startpos, Vec3i endpos) {
		return getDirectionVecXZ(new Vec3d(startpos), new Vec3d(endpos));
	}
	
	public static Vec3d getDirectionVecXZ(Vec3d startpos, Vec3d endpos) {
		double dx = endpos.x-startpos.x;
		double dz = endpos.z-startpos.z;
		double angle = Math.atan2(dz, dx);
		return getDirectionVecXZ(angle);
	}
	
	public static Vec3d getDirectionVecXZ(Entity entity1, EntityZombie entity2) {
		Vec3d startpos = getPosVecForEntity(entity1);
		Vec3d endpos = getPosVecForEntity(entity2);
		return getDirectionVecXZ(startpos, endpos);
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
	
	public static Vec3d getDirectionVec(Entity entity1, Entity entity2) {
		Vec3d startpos = getPosVecForEntity(entity1);
		Vec3d endpos = getPosVecForEntity(entity2);
		return getDirectionVec(startpos, endpos);
	}
	
	public static Vec3d getDirectionVec(Vec3i startpos, Vec3i endpos) {
		return getDirectionVec(new Vec3d(startpos), new Vec3d(endpos));
	}
	
	public static Vec3d getDirectionVec(Vec3d startpos, Vec3d endpos) {
		if (startpos.equals(endpos)) return new Vec3d(0,0,0);
		double dx = endpos.x-startpos.x;
		double dy = endpos.y-startpos.y;
		double dz = endpos.z-startpos.z;
		double magnitude = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
		double mx = (endpos.x+startpos.x)/magnitude;
		double my = (endpos.y+startpos.y)/magnitude;
		double mz = (endpos.z+startpos.z)/magnitude;
		return new Vec3d(mx, my, mz);
	}
	
	public static double dotProduct(Vec3i vec1, Vec3i vec2) {
		return dotProduct(new Vec3d(vec1), new Vec3d(vec2));
	}

	public static double dotProduct(Vec3d vec1, Vec3d vec2) {
		double x = vec1.x * vec2.x;
		double y = vec1.y * vec2.y;
		double z = vec1.z * vec2.z;
		return x + y+ z;
	}

	public static BlockPos getClosestLoadedPos(World world, BlockPos basepos, Vec3d direction, double radius) {
		BlockPos pos = world.getTopSolidOrLiquidBlock(basepos.add(direction.x*radius, 0, direction.z*radius));
		while (!world.getChunkFromBlockCoords(pos).isLoaded()) {
			if (radius==0) return basepos;
			radius--;
			pos = world.getTopSolidOrLiquidBlock(basepos.add(direction.x*radius, 0, direction.z*radius));
		}
		return pos;
	}
	
	public static BlockPos getClosestLoadedPos(World world, BlockPos basepos, Vec3d direction, double radius, int maxlight, int minlight) {
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
