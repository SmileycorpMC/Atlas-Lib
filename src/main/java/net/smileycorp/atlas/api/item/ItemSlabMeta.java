package net.smileycorp.atlas.api.item;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockProperties;
import net.minecraft.item.ItemSlab;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.block.BlockProperties;

public class ItemSlabMeta<T extends BlockSlab & BlockProperties> extends ItemSlab implements IMetaItem {

	public ItemSlabMeta(T half, T full) {
		super(half, half, full);
		setRegistryName(block.getRegistryName());
		setUnlocalizedName(block.getUnlocalizedName());
		if (half.getMaxMeta() > 0) setHasSubtypes(true);
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
		return ((BlockSlab) block).getUnlocalizedName(stack.getMetadata());
	}

}
