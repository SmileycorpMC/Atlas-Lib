package net.smileycorp.atlas.api.client.colour;

import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;

public class GrassBlockColour implements BlockColor {

	@Override
	public int getColor(BlockState state, BlockAndTintGetter reader, BlockPos pos, int tintIndex) {
		return reader.getBlockTint(pos, BiomeColors.GRASS_COLOR_RESOLVER);
	}

}
