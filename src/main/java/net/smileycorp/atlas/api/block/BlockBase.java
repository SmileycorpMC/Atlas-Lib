package net.smileycorp.atlas.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.atlas.api.interfaces.IBlockProperties;

public class BlockBase extends Block implements IBlockProperties {
	
	public boolean isFlamable;
	
	public BlockBase(String name, String modid, Material material, SoundType sound, float h, float r, String tool, int level, CreativeTabs tab) {
		super(material);
		setResistance(r);
		setHardness(h);
		setHarvestLevel(tool, level);
		setRegistryName(new ResourceLocation(modid, name.toLowerCase()));
		setUnlocalizedName(modid+"."+name.replace("_", ""));
		setCreativeTab(tab);
		setSoundType(sound);
		if (material == Material.WOOD) this.isFlamable = true;
	}

	public BlockBase(String name, String modid, Material material, SoundType sound, float h, float r, int level, CreativeTabs tab) {
		this(name, modid, material, sound, h, r, "pickaxe", level, tab); 
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return isFlamable ? 20 : super.getFlammability(world, pos, facing);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return isFlamable ? 5 : super.getFireSpreadSpeed(world, pos, facing);
	}

}
