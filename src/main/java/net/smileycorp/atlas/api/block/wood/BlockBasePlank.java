package net.smileycorp.atlas.api.block.wood;

import java.util.List;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.smileycorp.atlas.api.block.BlockMetaBase;

public class BlockBasePlank extends BlockMetaBase {
	
	String name;
	
	public BlockBasePlank(String name, String modid, CreativeTabs tab, List<String> variants, boolean isFlamable) {
		super(name, modid, Material.WOOD, SoundType.WOOD, 2f, 5f, "axe", 0, tab, variants);
		this.isFlamable = isFlamable;
	}
}
