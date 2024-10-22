package net.smileycorp.atlas.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

public interface BlockProperties {
	
	default int getMaxMeta(){
		return 0;
	}
	
	default boolean usesCustomItemHandler(){
		return false;
	}
	
	default String byMeta(int meta) {
		return "normal";
	}
	
	default String byState(IBlockState state) {
		return byMeta(((Block)this).getMetaFromState(state));
	}
	
}
