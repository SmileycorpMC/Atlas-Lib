package net.smileycorp.atlas.api.block.wood;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.atlas.api.block.PropertyString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class BlockBaseLeaves<T extends Enum<T> & WoodEnum>  extends BlockLeaves implements BlockProperties {

	//fake static property to bypass blockstate validation
	private static PropertyEnum staticProp;
	private final Class<T> types;
	private final int ordinal;
	private PropertyEnum<T> type;
	protected BlockBaseSapling sapling;
	
	private BlockBaseLeaves(String name, String modid, CreativeTabs tab, BlockBaseSapling sapling, Class<T> types, int ordinal) {
		this.types = types;
		this.ordinal = ordinal;
		setRegistryName(new ResourceLocation(modid, name));
		setUnlocalizedName(modid + "." + name);
		setCreativeTab(tab);
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		type = staticProp;
		return new BlockStateContainer(this, type);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(type).ordinal() % 4;
		if (!state.getValue(DECAYABLE)) meta += 4;
		if (state.getValue(CHECK_DECAY)) meta += 8;
		return meta;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(type, types.getEnumConstants()[ordinal * 4 + meta % 4])
				.withProperty(DECAYABLE, meta % 8 < 4).withProperty(CHECK_DECAY, meta >= 8);
	}
	
	@Override
	public int getMaxMeta() {
		return type.getAllowedValues().size();
	}
	
	
	@Override
	public String byMeta(int meta) {
		return byState(getStateFromMeta(meta));
	}
	
	@Override
	public String byState(IBlockState state) {
		return state.getValue(type).name();
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(type).plankColour();
	}
	
	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return world.getBlockState(pos).getValue(type).isFlammable();
	}
	
	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return isFlammable(world, pos, facing) ? 20 : super.getFlammability(world, pos, facing);
	}
	
	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing facing) {
		return isFlammable(world, pos, facing) ? 5 : super.getFireSpreadSpeed(world, pos, facing);
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
		return state.getValue(type).getLeavesSoundType();
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < type.getAllowedValues().size(); i++) items.add(new ItemStack(this, 1, i));
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return Lists.newArrayList(new ItemStack(this, 1, world.getBlockState(pos).getValue(type).ordinal() % 4));
	}
	
	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		Random rand = world instanceof World ? ((World)world).rand : RANDOM;
		T type = state.getValue(this.type);
		if (type.hasSapling() && rand.nextFloat() < type.saplingDropChance()) drops.add(new ItemStack(sapling, 1, type.ordinal() % 4));
		for (Map.Entry<Float, ItemStack> drop : type.getLeafDrops().entrySet()) if (rand.nextFloat() < drop.getKey()) drops.add(drop.getValue());
	}
	
	@Override
	public EnumType getWoodType(int meta) {
		return null;
	}
	
	@Override
	public boolean shouldSideBeRendered(@Nonnull IBlockState blockState, @Nonnull IBlockAccess blockAccess, @Nonnull BlockPos pos, @Nonnull EnumFacing side) {
	    leavesFancy = !Blocks.LEAVES.isOpaqueCube(blockState);
	    return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
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
	
	public PropertyEnum<T> typeProperty() {
		return type;
	}
	
	public static <T extends Enum<T> & WoodEnum> BlockBaseLeaves<T> create(String name, String modid, CreativeTabs tab, BlockBaseSapling sapling, Class<T> clazz, int ordinal) {
		Collection<T> types = Sets.newHashSet();
		for (int i = ordinal * 4; i < (ordinal + 1) * 4; i++) {
			if (i >= clazz.getEnumConstants().length) break;
			types.add(clazz.getEnumConstants()[i]);
		}
		staticProp = PropertyEnum.create("type", clazz, types);
		return new BlockBaseLeaves<>(name, modid, tab, sapling, clazz, ordinal);
	}

}
