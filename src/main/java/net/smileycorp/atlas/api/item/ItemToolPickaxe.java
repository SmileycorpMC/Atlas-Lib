package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class ItemToolPickaxe extends ItemPickaxe {

	public ItemToolPickaxe(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		super(material);
		name = name.toLowerCase(Locale.US) + "_pickaxe";
		setRegistryName(new ResourceLocation(modid, name));
		setUnlocalizedName(modid + "." + name);
		setCreativeTab(tab);
	}

}
