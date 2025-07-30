package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class ItemToolSword extends ItemSword implements IMetaItem {

	public ItemToolSword(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		super(material);
		name = name.toLowerCase(Locale.US) + "_sword";
		setRegistryName(new ResourceLocation(modid, name));
		setUnlocalizedName(modid + "." + name);
		setCreativeTab(tab);
	}

}
