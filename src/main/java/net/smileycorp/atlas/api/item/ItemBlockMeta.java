package net.smileycorp.atlas.api.item;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.block.BlockProperties;

public class ItemBlockMeta extends ItemBlock implements IMetaItem {

	public ItemBlockMeta(Block block) {
		super(block);
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
