package net.smileycorp.atlas.api.block;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class BlockMetaBase extends BlockBase {
	//fake static property to bypass blockstate validation
	protected static PropertyString staticProp;
	
	protected PropertyString prop;

	protected BlockMetaBase(String name, String modid, Material material, SoundType sound, float h, float r, String tool, int level, CreativeTabs tab, List<String> variants) {
		super(name, modid, material, sound, h, r, tool, level, tab);
		setDefaultState(this.blockState.getBaseState().withProperty(prop, variants.get(0)));
	}
	
	public static BlockMetaBase create(String name, String modid, Material material, SoundType sound, float h, float r, String tool, int level, CreativeTabs tab, List<String> variants) {
		staticProp = new PropertyString("property", variants);
		return new BlockMetaBase(name, modid, material, sound, h, r, tool, level, tab, variants);
	}
	
	public static BlockMetaBase create(String name, String modid, Material material, SoundType sound, float h, float r, int level, CreativeTabs tab, List<String> variants) {
		return create(name, modid, material, sound, h, r, "pickaxe", level, tab, variants); 
	}
	
	public PropertyString getProperty() {
		return prop;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		prop=staticProp;
		return new BlockStateContainer(this, new IProperty[]{prop});
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(placer.getHeldItem(hand).getMetadata());
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		List<String> variants = prop.getAllowedValues();
		if (meta > variants.size()-1) meta = 0;
		return this.getDefaultState().withProperty(prop, variants.get(meta));
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		String variant = (String) state.getProperties().get(prop);
		List<String> variants = prop.getAllowedValues();
		if (variants.contains(variant)) {
			return variants.indexOf(variant);
		}
		return 0;
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, this.getMetaFromState(state));
	}
	
	@Override
	public int getMaxMeta(){
		return prop.getAllowedValues().size();
	}

}
