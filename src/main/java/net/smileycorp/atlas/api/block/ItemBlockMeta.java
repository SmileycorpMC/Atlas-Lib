package net.smileycorp.atlas.api.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockMeta extends ItemBlock {
	public ItemBlockMeta(Block block) {
		super(block);
		setHasSubtypes(true);
		setRegistryName(block.getRegistryName());
		setUnlocalizedName(block.getUnlocalizedName());
	}
	
	@Override
	public int getMetadata(int i) {
		return i;
	}
}
