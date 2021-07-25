package net.smileycorp.atlas.api.client.colour;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public class BlockGrassColour implements BlockColor, ColorResolver {

	@Override
	public int getColor(BlockState state, BlockAndTintGetter reader, BlockPos pos, int tintIndex) {
		return reader.getBlockTint(pos, this);
	}

	@Override
	public int getColor(Biome biome, double p_getColor_2_, double p_getColor_4_) {
		return biome.getFoliageColor();
	}


}
