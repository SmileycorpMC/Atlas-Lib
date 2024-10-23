package net.smileycorp.atlas.api.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.smileycorp.atlas.api.block.BlockProperties;

public class MetaStateMapper extends StateMapperBase {
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        return new ModelResourceLocation(state.getBlock().getRegistryName().getResourceDomain() +
                ":" + (((BlockProperties) state.getBlock()).byState(state)));
    }

}
