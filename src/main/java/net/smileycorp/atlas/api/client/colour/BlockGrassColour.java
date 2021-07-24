package net.smileycorp.atlas.api.client.colour;

import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockDisplayReader;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.level.ColorResolver;

public class BlockGrassColour implements IBlockColor, ColorResolver {

	@Override
	public int getColor(BlockState state, IBlockDisplayReader reader, BlockPos pos, int tintIndex) {
		return reader.getBlockColor(pos, this);
	}

	@Override
	public int getColor(Biome biome, double p_getColor_2_, double p_getColor_4_) {
		return biome.getGrassColor(p_getColor_2_, p_getColor_4_);
	}

}
