package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemHoe;
import net.minecraft.util.ResourceLocation;

public class ToolItemHoe extends ItemHoe {

	public ToolItemHoe(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		super(material);
		name = name + "_Hoe";
		setRegistryName(new ResourceLocation(modid, name.toLowerCase()));
		setUnlocalizedName(modid+"."+name.replace("_", ""));
		setCreativeTab(tab);
	}

}
