package net.smileycorp.atlas.api.block.wood;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.atlas.api.block.PropertyString;

import java.util.List;

public class BlockBaseLog extends BlockLog implements IBlockProperties {
	
	//fake static property to bypass blockstate validation
	protected static PropertyString staticProp;
	
	protected PropertyString prop;
	
	public boolean isFlamable;

	protected BlockBaseLog(String name, String modid, CreativeTabs tab, List<String> variants, boolean isFlamable) {
		super();
		name = name.isEmpty() ? "Log":"Log_"+name;
		setCreativeTab(tab);
		setRegistryName(new ResourceLocation(modid, name.toLowerCase()));
		setUnlocalizedName(modid+"."+name.replace("_", ""));
		this.isFlamable = isFlamable;
		this.setDefaultState(this.blockState.getBaseState().withProperty(LOG_AXIS, BlockLog.EnumAxis.Y).withProperty(prop, variants.get(0)));
	}
	
	public static BlockBaseLog create(String name, String modid, CreativeTabs tab, List<String> variants, boolean isFlamable) {
		staticProp = new PropertyString("property", variants);
		return new BlockBaseLog(name, modid, tab, variants, isFlamable);
	}

	public PropertyString getProperty() {
		return prop;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		prop=staticProp;
		return new BlockStateContainer(this, new IProperty[]{prop, LOG_AXIS});
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		List<String> variants = prop.getAllowedValues();
		IBlockState state = this.getDefaultState();
		int varMeta = meta%4;
		int dirMeta = meta - meta%4;
    	if (varMeta>variants.size()-1) varMeta=0;
    	BlockLog.EnumAxis axis;
        switch (dirMeta) {
        case 0:
            	axis = BlockLog.EnumAxis.Y;
		case 4:
            	axis = BlockLog.EnumAxis.X;
		case 8:
            	axis = BlockLog.EnumAxis.Z;
		default:
            	axis = BlockLog.EnumAxis.NONE;
        }
		return state.withProperty(prop, variants.get(varMeta)).withProperty(LOG_AXIS, axis);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		String variant = (String) state.getProperties().get(prop);
		List<String> variants = prop.getAllowedValues();
		int varMeta = variants.contains(variant) ? variants.indexOf(variant) : 0;
		switch (state.getValue(LOG_AXIS)) {
	    	case Y:
	    			return varMeta;
			case X:
	                 return varMeta + 4;
			case Z:
	            	return varMeta + 8;
			default:
	            	return varMeta + 12;
	        }
    }
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, this.getMetaFromState(state));
	}
	
	@Override
	public int getMaxMeta(){
		return prop.getAllowedValues().size();
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
