package net.smileycorp.atlas.api.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public class BlockUtils {
	
	public static int getFortune(int fortune, RandomSource rand) {
		return getFortune(fortune, 1, rand);
	}
	
	public static int getFortune(int fortune, int base, RandomSource rand) {
		int drops = (Math.max(0, rand.nextInt(fortune + 2) - 1) + 1 + fortune)*base;
		return drops;
	}
	
	public static boolean always(BlockState state, BlockGetter world, BlockPos pos) {
		return true;
	}
	
	public static boolean never(BlockState state, BlockGetter world, BlockPos pos) {
		return false;
	}
	
	public static Boolean always(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> entity) {
		return (boolean)true;
	}
	
	public static Boolean never(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> entity) {
		return (boolean)false;
	}
	
	public static Boolean jungleMob(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> entity) {
		return entity == EntityType.OCELOT || entity == EntityType.PARROT;
	}
	
}
