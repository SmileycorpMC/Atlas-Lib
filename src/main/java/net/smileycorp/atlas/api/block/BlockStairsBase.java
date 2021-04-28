package net.smileycorp.atlas.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import org.apache.commons.lang3.text.WordUtils;

public class BlockStairsBase extends BlockStairs {

	public BlockStairsBase(Block base) {
		super(base.getDefaultState());
		useNeighborBrightness=true;
		setCreativeTab(base.getCreativeTabToDisplayOn());
		String modid = base.getRegistryName().getResourceDomain();
		String name = WordUtils.capitalize(base.getRegistryName().getResourcePath(), '_')+"_Stairs";
		setRegistryName(new ResourceLocation(modid, name.toLowerCase()));
		setUnlocalizedName(modid+"."+name.replace("_", ""));
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return world.getBlockState(pos).getMaterial() == Material.WOOD ? 20 : super.getFlammability(world, pos, facing);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return world.getBlockState(pos).getMaterial() == Material.WOOD ? 5 : super.getFireSpreadSpeed(world, pos, facing);
	}

}
