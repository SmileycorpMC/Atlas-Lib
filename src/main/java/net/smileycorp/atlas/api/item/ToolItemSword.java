package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;

public class ToolItemSword extends ItemSword {

	public ToolItemSword(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		super(material);
		name = name + "_Sword";
		setRegistryName(new ResourceLocation(modid, name.toLowerCase()));
		setUnlocalizedName(modid+"."+name.replace("_", ""));
		setCreativeTab(tab);
	}

}
