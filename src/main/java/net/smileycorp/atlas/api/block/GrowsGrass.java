package net.smileycorp.atlas.api.block;

import net.minecraft.block.state.IBlockState;

import java.util.Random;

public interface GrowsGrass {
    
    IBlockState getGrass(IBlockState state, Random rand);
    
}
