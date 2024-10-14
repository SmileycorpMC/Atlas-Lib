package net.smileycorp.atlas.api.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.*;
import net.minecraft.world.World;

import java.util.List;
import java.util.Random;

public class DirectionUtils {

	public static RayTraceResult rayTrace(World world, EntityLivingBase entity, float distance) {
		Vec3d pos = entity.getPositionVector().addVector(0, entity.posY, 0);
		Vec3d dir = entity.getLookVec();
		Vec3d lastVec = pos.addVector(dir.x, dir.y, dir.z);
		RayTraceResult blockRay = world.rayTraceBlocks(pos, pos.addVector(dir.x * distance, dir.y * distance, dir.z * distance), false, false, true);
		for (int x = 0; x < 16 * distance; x++) {
			float reach = x * 0.0625f;
			Vec3d vec = pos.addVector(dir.x * reach, dir.y * reach, dir.z * reach);
			if (blockRay == null || blockRay.hitVec == null) return new RayTraceResult(dir, null);
			if (blockRay.hitVec.distanceTo(pos) < vec.distanceTo(pos)) break;
			AxisAlignedBB AABB = new AxisAlignedBB(lastVec.x, lastVec.y, lastVec.z, vec.x, vec.y, vec.z);
			List<Entity> entities = world.getEntitiesWithinAABBExcludingEntity(entity, AABB);
			if (!entities.isEmpty()) return new RayTraceResult(entities.get(0), dir);
			lastVec = vec;
		}
		return blockRay;
	}

	//use rayTrace instead
	@Deprecated
	public static RayTraceResult getPlayerRayTrace(World world, EntityPlayer player, float distance) {
		return rayTrace(world, player, distance);
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
		return facing.getIndex() + (facing == EnumFacing.UP || facing == EnumFacing.DOWN ? 4 : -2);
	}

	public static Vec3d getDirectionVecXZ(Vec3i start, Vec3i end) {
		return getDirectionVecXZ(centerOf(start), centerOf(end));
	}

	public static Vec3d getDirectionVecXZ(Vec3d start, Vec3d end) {
		double dx = end.x-start.x;
		double dz = end.z-start.z;
		double angle = Math.atan2(dz, dx);
		return getDirectionVecXZ(angle);
	}

	public static Vec3d getDirectionVecXZ(Entity start, Entity end) {
		return getDirectionVecXZ(start.getPositionVector(), end.getPositionVector());
	}

	public static Vec3d getDirectionVecXZDegrees(double angle) {
		return getDirectionVecXZ(Math.toRadians(angle));
	}

	public static Vec3d getDirectionVecXZ(double angle) {
		return new Vec3d(Math.cos(angle), 0, Math.sin(angle));
	}

	public static Vec3d getRandomDirectionVecXZ(Random rand) {
		return getDirectionVecXZDegrees(rand.nextInt(360));
	}

	public static Vec3d getDirectionVec(Entity start, Entity end) {
		return getDirectionVec(start.getPositionVector(), end.getPositionVector());
	}

	public static Vec3d getDirectionVec(Vec3i start, Vec3i end) {
		return getDirectionVec(centerOf(start), centerOf(end));
	}

	public static Vec3d getDirectionVec(Vec3d start, Vec3d end) {
		if (start.equals(end)) return new Vec3d(0,0,0);
		double dx = end.x - start.x;
		double dy = end.y - start.y;
		double dz = end.z - start.z;
		double magnitude = Math.sqrt(dx * dx + dy * dy + dz * dz);
		return new Vec3d(dx / magnitude, dy / magnitude, dz / magnitude);
	}

	public static BlockPos getClosestLoadedPos(World world, BlockPos basepos, Vec3d direction, double radius) {
		BlockPos pos = world.getTopSolidOrLiquidBlock(basepos.add(direction.x * radius, 0, direction.z * radius));
		while (!world.getChunkFromBlockCoords(pos).isLoaded()) {
			if (radius == 0) return basepos;
			radius--;
			pos = world.getTopSolidOrLiquidBlock(basepos.add(direction.x * radius, 0, direction.z * radius));
		}
		return pos;
	}

	public static BlockPos getClosestLoadedPos(World world, BlockPos basepos, Vec3d direction, double radius, int maxlight, int minlight) {
		BlockPos pos = world.getTopSolidOrLiquidBlock(basepos.add(direction.x * radius, 0, direction.z * radius));
		while (!world.getChunkFromBlockCoords(pos).isLoaded() || !isBrightnessAllowed(world, basepos, maxlight, minlight)) {
			if (radius == 0) return basepos;
			radius--;
			pos = world.getTopSolidOrLiquidBlock(basepos.add(direction.x * radius, 0, direction.z * radius));
		}
		return pos;
	}

	public static boolean isBrightnessAllowed(World world, BlockPos pos, int maxlight, int minlight) {
		int blocklight = world.getLightFromNeighbors(pos);
		if (blocklight > maxlight) return false;
		if (blocklight < minlight) return false;
		return true;
	}
	
	public static Vec3d centerOf(Vec3i pos) {
		return new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
	}
	public static void throwItem(EntityLivingBase entity, ItemStack stack, Vec3d target) {
		EntityItem item = new EntityItem(entity.world, entity.posX, entity.posY + entity.getEyeHeight() - 0.3, entity.posZ, stack);
		item.setThrower(entity.getUniqueID().toString());
		Vec3d vel = target.subtract(item.posX, item.posY, item.posZ)
				.normalize().scale(0.3);
		item.motionX = vel.x;
		item.motionY = vel.y;
		item.motionZ = vel.z;
		item.setDefaultPickupDelay();
		entity.world.spawnEntity(item);
	}
	

}
