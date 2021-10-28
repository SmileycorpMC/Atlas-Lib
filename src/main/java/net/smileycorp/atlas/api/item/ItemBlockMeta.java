package net.smileycorp.atlas.api.item;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.util.TextUtils;

public class ItemBlockMeta<T extends Comparable<T>> extends ItemBlock {

	final IProperty<T> prop;
	final String name;

	public ItemBlockMeta(Block block, IProperty<T> prop) {
		this(block, prop, "");
	}

	public ItemBlockMeta(Block block, IProperty<T> prop, String name) {
		super(block);
		setRegistryName(block.getRegistryName());
		setUnlocalizedName(block.getUnlocalizedName());
		setHasSubtypes(true);
		this.prop = prop;
		this.name = name;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getNameForState(block.getStateFromMeta(stack.getMetadata()));
	}

	protected String getNameForState(IBlockState state) {
		String variant = TextUtils.toProperCase(prop.getName((T)state.getValue(prop)));
		return "tile." + block.getRegistryName().getResourceDomain() + "." + name + variant.replace(" ", "");
	}

}
