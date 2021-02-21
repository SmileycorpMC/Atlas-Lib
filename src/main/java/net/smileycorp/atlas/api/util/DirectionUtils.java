package net.smileycorp.atlas.api.util;

import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;

public class DirectionUtils {

	public static EnumFacing getRandomDirection(Random rand) {
		return getDirection(rand.nextInt(4));
	}

	public static EnumFacing getDirection(int direction) {
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
	
	public static int getMeta(EnumFacing facing) {
		if (facing==EnumFacing.UP||facing==EnumFacing.DOWN) {
			return facing.getIndex()+4;
		}
		return facing.getIndex()-2;
	}
	
	public static BlockPos getPos(BlockPos pos, EnumFacing facing) {
		return new BlockPos(pos).add(facing.getDirectionVec());
	}
	
	public static BlockPos getPos(BlockPos pos, int facing) {
		return new BlockPos(pos).add(getDirection(facing).getDirectionVec());
	}

}
