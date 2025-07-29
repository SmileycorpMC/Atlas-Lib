package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.ResourceLocation;

import java.util.Locale;

public class ItemToolAxe extends ItemAxe {

	public ItemToolAxe(String modid, String name, ToolMaterial material, CreativeTabs tab) {
		this(modid, name, material, tab, 8, (material.getAttackDamage() / 10f) -3.3f);
	}

	public ItemToolAxe(String modid, String name, ToolMaterial material, CreativeTabs tab, float damage, float attackSpeed) {
		super(material, damage, attackSpeed);
		name = name.toLowerCase(Locale.US) + "_axe";
		setRegistryName(new ResourceLocation(modid, name));
		setUnlocalizedName(modid + "." + name);
		setCreativeTab(tab);
	}

}
