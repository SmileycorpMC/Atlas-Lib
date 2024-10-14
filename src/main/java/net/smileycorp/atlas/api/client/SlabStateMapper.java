package net.smileycorp.atlas.api.client;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.atlas.api.block.BlockSlabBase;

public class SlabStateMapper extends StateMapperBase {

	final ResourceLocation location;
	
	public SlabStateMapper(BlockSlabBase slab) {
		location = slab.getRegistryName();
	}

	@Override
	protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
		String statename = "half="+state.getValue(BlockSlab.HALF).toString();
		return new ModelResourceLocation(location, statename);
	}

}
