package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.ResourceLocation;

public class ToolItemPickaxe extends ItemPickaxe {

	public ToolItemPickaxe(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		super(material);
		name = name + "_Pickaxe";
		setRegistryName(new ResourceLocation(modid, name.toLowerCase()));
		setUnlocalizedName(modid+"."+name.replace("_", ""));
		setCreativeTab(tab);
	}

}
