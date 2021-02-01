package net.smileycorp.atlas.api.util;

import java.util.Random;

import net.minecraft.util.EnumFacing;

public class PositionUtils {

	public static EnumFacing getRandomDirection(Random rand) {
		switch (rand.nextInt(4)) {
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

}
