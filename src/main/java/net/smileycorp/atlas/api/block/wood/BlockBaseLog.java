package net.smileycorp.atlas.api.block.wood;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockLog;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Collection;

public class BlockBaseLog<T extends Enum<T> & WoodEnum> extends BlockLog implements WoodVariant<T> {
	
	//fake static property to bypass blockstate validation
	private static PropertyEnum staticProp;
	private final Class<T> types;
	private final int ordinal;
	private PropertyEnum<T> type;
	
	private BlockBaseLog(String name, String modid, CreativeTabs tab, Class<T> types, int ordinal) {
		super();
		this.types = types;
		this.ordinal = ordinal;
		setRegistryName(new ResourceLocation(modid, name));
		setUnlocalizedName(modid + "." + name);
		setCreativeTab(tab);
		setDefaultState(blockState.getBaseState().withProperty(type, types.getEnumConstants()[ordinal * 4]));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		type = staticProp;
		return new BlockStateContainer(this, type, LOG_AXIS);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = state.getValue(type).ordinal() % 4;
		switch (state.getValue(LOG_AXIS)) {
			case X:
				return 4 + meta;
			case Y:
				return meta;
			case Z:
				return 8 + meta;
			default:
				return 12 + meta;
		}
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumAxis axis = EnumAxis.NONE;
		if (meta < 4) axis = EnumAxis.Y;
		else if (meta < 8) axis = EnumAxis.X;
		else if (meta < 12) axis = EnumAxis.Z;
		return getDefaultState().withProperty(type, types.getEnumConstants()[ordinal * 4 + meta % 4])
				.withProperty(LOG_AXIS, axis);
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
		String name = state.getValue(type).getName();
		return name + (state.getValue(LOG_AXIS) == EnumAxis.NONE ? (name.contains("wood") ? "" : "_wood") : "_log");
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		ItemStack stack = placer.getHeldItem(hand);
		return (stack.getMetadata() < 12 ? super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
				: getDefaultState().withProperty(LOG_AXIS, EnumAxis.NONE))
				.withProperty(type, types.getEnumConstants()[ordinal * 4 + stack.getMetadata() % 4]);
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return state.getValue(type).ordinal() % 4;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(this, 1, getMetaFromState(state) % 4);
	}
	
	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(type).logColour();
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
		return state.getValue(type).getSoundType();
	}
	
	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		return state.getValue(type).getHardness();
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
		return world.getBlockState(pos).getValue(type).getResistance() / 5f;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < type.getAllowedValues().size(); i++) {
			items.add(new ItemStack(this, 1, i));
			items.add(new ItemStack(this, 1, i + 12));
		}
	}
	
	@Override
	public PropertyEnum<T> getVariantProperty() {
		return type;
	}
	
	public static <T extends Enum<T> & WoodEnum> BlockBaseLog<T> create(String name, String modid, CreativeTabs tab, Class<T> clazz, int ordinal) {
		Collection<T> types = Sets.newHashSet();
		for (int i = ordinal * 4; i < (ordinal + 1) * 4; i++) {
			if (i >= clazz.getEnumConstants().length) break;
			types.add(clazz.getEnumConstants()[i]);
		}
		staticProp = PropertyEnum.create("type", clazz, types);
		return new BlockBaseLog<>(name, modid, tab, clazz, ordinal);
	}
	
}
