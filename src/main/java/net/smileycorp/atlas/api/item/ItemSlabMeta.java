package net.smileycorp.atlas.api.item;

import net.minecraft.block.BlockSlab;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.block.BlockProperties;

public class ItemSlabMeta extends ItemSlab implements IMetaItem {

	public ItemSlabMeta(BlockSlab singleSlab, BlockSlab doubleSlab) {
		super(singleSlab, singleSlab, doubleSlab);
		setRegistryName(block.getRegistryName());
		setUnlocalizedName(block.getUnlocalizedName());
		setHasSubtypes(true);
	}
	
	@Override
	public int getMaxMeta() {
		return ((BlockProperties)block).getMaxMeta();
	}
	
	@Override
	public String byMeta(int meta) {
		return ((BlockProperties)block).byMeta(meta);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return "tile." + block.getRegistryName().getResourceDomain() + "." + byMeta(stack.getMetadata());
	}

}
