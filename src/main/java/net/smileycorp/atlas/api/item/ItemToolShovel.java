package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemSpade;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class ItemToolShovel extends ItemSpade {

	public ItemToolShovel(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		super(material);
		name = name.toLowerCase(Locale.US) + "_shovel";
		setRegistryName(new ResourceLocation(modid, name));
		setUnlocalizedName(modid + "." + name);
		setCreativeTab(tab);
	}

}
