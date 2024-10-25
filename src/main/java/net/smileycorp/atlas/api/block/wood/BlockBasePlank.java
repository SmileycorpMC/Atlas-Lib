package net.smileycorp.atlas.api.block.wood;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
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

public class BlockBasePlank<T extends Enum<T> & WoodEnum> extends Block implements WoodVariant<T> {
    
    //fake static property to bypass blockstate validation
    private static PropertyEnum staticProp;
    private final Class<T> types;
    private final int ordinal;
    private PropertyEnum<T> type;
    
    private BlockBasePlank(String name, String modid, CreativeTabs tab, Class<T> types, int ordinal) {
        super(Material.WOOD);
        this.types = types;
        this.ordinal = ordinal;
        setRegistryName(new ResourceLocation(modid, name));
        setUnlocalizedName(modid + "." + name);
        setCreativeTab(tab);
        setDefaultState(blockState.getBaseState().withProperty(type, types.getEnumConstants()[ordinal * 16]));
    }
    
    @Override
    protected BlockStateContainer createBlockState() {
        type = staticProp;
        return new BlockStateContainer(this, type);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(type).ordinal() % 16;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(type, types.getEnumConstants()[ordinal * 16 + meta]);
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
        return state.getValue(type).getName() + "_planks";
    }
    
    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return getDefaultState().withProperty(type, types.getEnumConstants()[ordinal * 16 + placer.getHeldItem(hand).getMetadata() % 16]);
    }
    
    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(type).ordinal() % 16;
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, state.getValue(type).ordinal() % 16);
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
        for (int i = 0; i < type.getAllowedValues().size(); i++) items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public PropertyEnum<T> getVariantProperty() {
        return type;
    }
    
    public static <T extends Enum<T> & WoodEnum> BlockBasePlank<T> create(String name, String modid, CreativeTabs tab, Class<T> clazz, int ordinal) {
        Collection<T> types = Sets.newHashSet();
        for (int i = ordinal * 16; i < (ordinal + 1) * 16; i++) {
            if (i >= clazz.getEnumConstants().length) break;
            types.add(clazz.getEnumConstants()[i]);
        }
        staticProp = PropertyEnum.create("type", clazz, types);
        return new BlockBasePlank<>(name, modid, tab, clazz, ordinal);
    }
    
}
