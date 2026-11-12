package net.smileycorp.atlas.api.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
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
		Vec3d pos = entity.getPositionVector().addVector(0, entity.getEyeHeight(), 0);
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
	
	public static Vec2f getProjectedPos(Vec3d pos, Vec3d cameraPos, float yaw, float pitch, int screenWidth, int screenHeight, float fov) {
		double scaledFov = Math.tan(Math.toRadians(fov) / 2f);
		double aspectRatio = (double) screenWidth / (double) screenHeight;
		pos = pos.subtract(cameraPos);
		pos.rotateYaw(-yaw);
		pos.rotatePitch(-pitch);
		return new Vec2f((float) (pos.x / (-pos.z * scaledFov)) * screenWidth, (float) ((pos.y * aspectRatio) / (-pos.z * scaledFov)) * screenHeight);
	}

	public static boolean hasCollision(World world, Vec3d start, Vec3d end, CollisionContext ctx) {
		if (start.equals(end)) return false;
		int i = MathHelper.floor(end.x);
		int j = MathHelper.floor(end.y);
		int k = MathHelper.floor(end.z);
		int l = MathHelper.floor(start.x);
		int i1 = MathHelper.floor(start.y);
		int j1 = MathHelper.floor(start.z);
		BlockPos pos = new BlockPos(l, i1, j1);
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();

		if (ctx.apply(world, pos, state, block) && state.getCollisionBoundingBox(world, pos) != Block.NULL_AABB
				&& block.canCollideCheck(state, false) && state.collisionRayTrace(world, pos, start, end) != null) return true;
		int k1 = 200;
		while (k1-- >= 0) {
			boolean flag2 = true;
			boolean flag = true;
			boolean flag1 = true;
			double d0 = 999.0D;
			double d1 = 999.0D;
			double d2 = 999.0D;
			if (i > l) d0 = (double)l + 1.0D;
			else if (i < l) d0 = (double)l + 0.0D;
			else flag2 = false;

			if (j > i1) d1 = (double)i1 + 1.0D;
			else if (j < i1) d1 = (double)i1 + 0.0D;
			else flag = false;
			if (k > j1) d2 = (double)j1 + 1.0D;
			else if (k < j1) d2 = (double)j1 + 0.0D;
			else flag1 = false;

			double d3 = 999.0D;
			double d4 = 999.0D;
			double d5 = 999.0D;
			double dx = end.x - start.x;
			double dy = end.y - start.y;
			double dz = end.z - start.z;
			if (flag2) d3 = (d0 - start.x) / dx;
			if (flag) d4 = (d1 - start.y) / dy;
			if (flag1) d5 = (d2 - start.z) / dz;
			if (d3 == -0.0D) d3 = -1.0E-4D;
			if (d4 == -0.0D) d4 = -1.0E-4D;
			if (d5 == -0.0D) d5 = -1.0E-4D;
			EnumFacing enumfacing;
			if (d3 < d4 && d3 < d5) {
				enumfacing = i > l ? EnumFacing.WEST : EnumFacing.EAST;
				start = new Vec3d(d0, start.y + dy * d3, start.z + dz * d3);
			}
			else if (d4 < d5) {
				enumfacing = j > i1 ? EnumFacing.DOWN : EnumFacing.UP;
				start = new Vec3d(start.x + dx * d4, d1, start.z + dz * d4);
			}
			else {
				enumfacing = k > j1 ? EnumFacing.NORTH : EnumFacing.SOUTH;
				start = new Vec3d(start.x + dx * d5, start.y + dy * d5, d2);
			}
			l = MathHelper.floor(start.x) - (enumfacing == EnumFacing.EAST ? 1 : 0);
			i1 = MathHelper.floor(start.y) - (enumfacing == EnumFacing.UP ? 1 : 0);
			j1 = MathHelper.floor(start.z) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
			pos = new BlockPos(l, i1, j1);
			state = world.getBlockState(pos);
			block = state.getBlock();
			if (ctx.apply(world, pos, state, block) && state.getCollisionBoundingBox(world, pos) != Block.NULL_AABB
					&& block.canCollideCheck(state, false) && state.collisionRayTrace(world, pos, start, end) != null) return true;
		}
		return false;
	}

	public interface CollisionContext {

		boolean apply (World world, BlockPos pos, IBlockState state, Block block);

	}
}
