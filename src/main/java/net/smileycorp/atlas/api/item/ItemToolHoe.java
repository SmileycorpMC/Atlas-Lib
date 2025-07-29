package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class ItemToolHoe extends ItemHoe {

	public ItemToolHoe(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		super(material);
		name = name.toLowerCase(Locale.US) + "_hoe";
		setRegistryName(new ResourceLocation(modid, name));
		setUnlocalizedName(modid + "." + name);
		setCreativeTab(tab);
	}

}
