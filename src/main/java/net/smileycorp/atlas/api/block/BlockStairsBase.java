package net.smileycorp.atlas.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Locale;

public class BlockStairsBase extends BlockStairs {

	public BlockStairsBase(Block base) {
		this(base.getRegistryName().getResourcePath(), base.getDefaultState());
	}
	
	public BlockStairsBase(String name, IBlockState state) {
		super(state);
		useNeighborBrightness = true;
		Block base = state.getBlock();
		setSoundType(base.getSoundType(state, null, null, null));
		setCreativeTab(base.getCreativeTabToDisplayOn());
		String modid = base.getRegistryName().getResourceDomain();
		name = name + "_stairs";
		setRegistryName(new ResourceLocation(modid, name));
		setUnlocalizedName(modid + "." + name);
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
