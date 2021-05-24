package net.smileycorp.atlas.api.block.wood;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.block.IBlockProperties;
import net.smileycorp.atlas.api.block.PropertyString;

public class BlockBaseLeaves extends BlockLeaves implements IBlockProperties {

	protected boolean isFlamable;
	protected PropertyString prop;
	protected List<ItemStack> saplings;

	public BlockBaseLeaves(String name, String modid, CreativeTabs tab,
			List<String> variants, List<ItemStack> saplings, boolean isFlamable) {
		this.isFlamable=isFlamable;
		this.prop = new PropertyString("variant", variants);
		this.saplings = saplings;
	}
	
	public PropertyString getProperty() {
		return prop;
	}

	@Override
	protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] {CHECK_DECAY, DECAYABLE, prop});
    }
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world,
			BlockPos pos, int fortune) {
		List<ItemStack> drops = new ArrayList<ItemStack>();
		drops.add(new ItemStack(this));
		return drops;
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
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		int varMeta = getMetaFromState(state) % 4;
		if (varMeta > saplings.size()) varMeta = 0;
	    return saplings.get(varMeta).getItem();
	 }
	
	@Override
	public int damageDropped(IBlockState state) {
		int varMeta = getMetaFromState(state) % 4;
		if (varMeta > saplings.size()) varMeta = 0;
    	return saplings.get(varMeta).getMetadata();
	}
	
	@Override
	public EnumType getWoodType(int meta) {
		return null;
	}
	
	@Override
	public boolean shouldSideBeRendered(@Nonnull IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
	    this.leavesFancy = !Blocks.LEAVES.isOpaqueCube(blockState);
	    return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
	
	 @Override
	  public IBlockState getStateFromMeta(int meta) {
	    return this.getDefaultState().withProperty(DECAYABLE, meta!=4).withProperty(CHECK_DECAY, meta==8);
	  }
	
	  @Override
	  public int getMetaFromState(IBlockState state) {
		 int varMeta = 0;
		  
	    if(!state.getValue(DECAYABLE)) {
	      return 4;
	    }
	
	    if(state.getValue(CHECK_DECAY)) {
	      return 8;
	    }
	
	    return 0;
	  }
	  
	@Override
	public boolean isLeaves(IBlockState state, IBlockAccess world, BlockPos pos) {
	    return true;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state) {
	    return Blocks.LEAVES.isOpaqueCube(state);
	  }
	
	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
	    return Blocks.LEAVES.getBlockLayer();
	}

}
