package net.smileycorp.atlas.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.DummyEnum;
import org.apache.commons.lang3.text.WordUtils;

import java.util.Random;

public class BlockSlabBase extends BlockSlab {
	
	final boolean isDouble;
	BlockSlab half;
	
	static PropertyEnum<DummyEnum> DUMMY = PropertyEnum.<DummyEnum>create("dummy", DummyEnum.class);
	
	@SuppressWarnings("deprecation")
	public BlockSlabBase(Block base, float h, float r, String tool, int harvestLevel, boolean isDouble) {
		super(base.getDefaultState().getMaterial());
		this.isDouble=isDouble;
		if(!isDouble) {
			useNeighborBrightness=true;
			setCreativeTab(base.getCreativeTabToDisplayOn());
		}
		String name = WordUtils.capitalize(base.getRegistryName().getResourcePath(), '_')+"_Slab";
		String modid = base.getRegistryName().getResourceDomain();
		setRegistryName(new ResourceLocation(modid, name.toLowerCase()+(isDouble ? "_double":"")));
		setUnlocalizedName(modid+"."+name.replace("_", ""));
		setSoundType(base.getSoundType());
		setHardness(h);
		setResistance(r);
		setHarvestLevel(tool, harvestLevel);
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return world.getBlockState(pos).getMaterial() == Material.WOOD ? 20 : super.getFlammability(world, pos, facing);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return world.getBlockState(pos).getMaterial() == Material.WOOD ? 5 : super.getFireSpreadSpeed(world, pos, facing);
	}
	
	@Override
	public BlockStateContainer createBlockState() {
		return isDouble() ? new BlockStateContainer(this, DUMMY):new BlockStateContainer(this, HALF, DUMMY) ;
	}
	
	@Override
	public String getUnlocalizedName(int meta) {
		return getUnlocalizedName();
	}


	@Override
	public boolean isDouble() {
		return isDouble;
	}
	
	@Override
	public boolean isFullBlock(IBlockState state) {
		return isDouble();
	}

	@Override
	public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		return isDouble() || (state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.TOP && side == EnumFacing.UP) || (state.getValue(BlockSlab.HALF) == BlockSlab.EnumBlockHalf.BOTTOM && side == EnumFacing.DOWN);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return isDouble ? getDefaultState() : getDefaultState().withProperty(HALF, meta == 8 ? EnumBlockHalf.TOP : EnumBlockHalf.BOTTOM);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return isDouble ? 0 : (state.getValue(HALF) == EnumBlockHalf.TOP ? 8 : 0);
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(half);
	}
	
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(half);
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return DUMMY;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return DummyEnum.DUMMY;
	}
}
