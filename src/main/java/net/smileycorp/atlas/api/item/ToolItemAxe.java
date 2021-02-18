package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.ResourceLocation;

public class ToolItemAxe extends ItemAxe {

	public ToolItemAxe(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		this(modid, name, material, tab, 8, -3.3F+(material.getAttackDamage()/10F));
	}

	public ToolItemAxe(String modid, String name, ToolMaterial material, CreativeTabs tab, float axedamage, float axespeed) {
		super(material, axedamage, axespeed);
		name = name + "_Axe";
		setRegistryName(new ResourceLocation(modid, name.toLowerCase()));
		setUnlocalizedName(modid+"."+name.replace("_", ""));
		setCreativeTab(tab);
	}

}
