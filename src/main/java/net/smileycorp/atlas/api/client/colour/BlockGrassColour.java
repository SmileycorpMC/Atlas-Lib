package net.smileycorp.atlas.api.client.colour;

import net.minecraft.block.BlockGrass;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockGrassColour implements IBlockColor {

	@Override
	public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex) {
		if (state.getBlock() instanceof BlockGrass) if (state.getValue(BlockGrass.SNOWY)) return 0xFFFFFF;
		return world.getBiome(pos).getGrassColorAtPos(pos);
	}

}
