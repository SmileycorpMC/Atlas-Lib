package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSpade;
import net.minecraft.util.ResourceLocation;

public class ToolItemSpade extends ItemSpade {

	public ToolItemSpade(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		super(material);
		name = name + "_Spade";
		setRegistryName(new ResourceLocation(modid, name.toLowerCase()));
		setUnlocalizedName(modid+"."+name.replace("_", ""));
		setCreativeTab(tab);
	}

}
