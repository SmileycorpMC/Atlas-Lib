package net.smileycorp.atlas.api.block.wood;

import com.google.common.collect.Sets;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.event.terraingen.TerrainGen;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Random;

public class BlockBaseSapling<T extends Enum<T> & WoodEnum> extends BlockBush implements WoodVariant<T>, IGrowable {
    
    protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);
    
    //fake static property to bypass blockstate validation
    private static PropertyEnum staticProp;
    private final Class<T> types;
    private final int ordinal;
    private PropertyEnum<T> type;
    
    private BlockBaseSapling(String name, String modid, CreativeTabs tab, Class<T> types, int ordinal) {
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
        return new BlockStateContainer(this, type, BlockSapling.STAGE);
    }
    
    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(type).ordinal() % 4 + state.getValue(BlockSapling.STAGE) * 4;
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(type, types.getEnumConstants()[ordinal * 4 + meta % 4])
                .withProperty(BlockSapling.STAGE, meta / 4);
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
        return state.getValue(type).getName() + "_sapling";
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(this, 1, getMetaFromState(state) % 4);
    }
    
    @Override
    public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
        return state.getValue(type).getLeavesSoundType();
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
        for (int i = 0; i < type.getAllowedValues().size(); i++) if (types.getEnumConstants()[ordinal * 4 + i].hasSapling())
            items.add(new ItemStack(this, 1, i));
    }
    
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
    {
        return SAPLING_AABB;
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.isRemote) return;
        super.updateTick(world, pos, state, rand);
        if (!world.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (world.getLightFromNeighbors(pos.up()) < 9 || rand.nextInt(7) > 0) return;
        grow(world, pos, state, rand);
    }
    
    public void grow(World world, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(BlockSapling.STAGE).intValue() == 0) world.setBlockState(pos, state.cycleProperty(BlockSapling.STAGE), 4);
        else generateTree(world, pos, state, rand);
    }
    
    public void generateTree(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!TerrainGen.saplingGrowTree(world, rand, pos)) return;
        T type = state.getValue(this.type);
        if (type.hasLargeTree()) if (generateLargeTree(world, pos, state, rand, type)) return;
        else if (!type.hasTree()) return;
        WorldGenerator worldgenerator = type.getTree();
        IBlockState air = Blocks.AIR.getDefaultState();
        world.setBlockState(pos, air, 4);
        world.setBlockToAir(pos);
        if (!worldgenerator.generate(world, rand, pos)) world.setBlockState(pos, state, 4);
    }
    
    private boolean generateLargeTree(World world, BlockPos pos, IBlockState state, Random rand, T type) {
        for (int i = 0; i >= -1; --i) {
            for (int j = 0; j >= -1; --j) {
                if (!isTwoByTwoOfType(world, pos, i, j, type)) continue;
                WorldGenerator worldgenerator = type.getLargeTree();
                IBlockState air = Blocks.AIR.getDefaultState();
                world.setBlockState(pos.add(i, 0, j), air, 4);
                world.setBlockState(pos.add(i + 1, 0, j), air, 4);
                world.setBlockState(pos.add(i, 0, j + 1), air, 4);
                world.setBlockState(pos.add(i + 1, 0, j + 1), air, 4);
                if (!worldgenerator.generate(world, rand, pos)) {
                    world.setBlockState(pos.add(i, 0, j), state, 4);
                    world.setBlockState(pos.add(i + 1, 0, j), state, 4);
                    world.setBlockState(pos.add(i, 0, j + 1), state, 4);
                    world.setBlockState(pos.add(i + 1, 0, j + 1), state, 4);
                    return false;
                }
                return true;
            }
        }
        return false;
    }
    
    private boolean isTwoByTwoOfType(World world, BlockPos pos, int i, int j, T type) {
        return this.isTypeAt(world, pos.add(i, 0, j), type) &&
                isTypeAt(world, pos.add(i + 1, 0, j), type) &&
                isTypeAt(world, pos.add(i, 0, j + 1), type) &&
                isTypeAt(world, pos.add(i + 1, 0, j + 1), type);
    }
    
    public boolean isTypeAt(World world, BlockPos pos, T type) {
        IBlockState state = world.getBlockState(pos);
        return state.getBlock() == this && state.getValue(this.type) == type;
    }
    
    @Override
    public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean isClient)
    {
        return true;
    }
    
    @Override
    public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
        return world.rand.nextFloat() < 0.45D;
    }
    
    @Override
    public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
        grow(world, pos, state, rand);
    }
    
    @Override
    public PropertyEnum<T> typeProperty() {
        return type;
    }
    
    @Nullable
    public static <T extends Enum<T> & WoodEnum> BlockBaseSapling<T> create(String name, String modid, CreativeTabs tab, Class<T> clazz, int ordinal) {
        Collection<T> types = Sets.newHashSet();
        boolean hasTree = false;
        for (int i = ordinal * 4; i < (ordinal + 1) * 4; i++) {
            if (i >= clazz.getEnumConstants().length) break;
            T type = clazz.getEnumConstants()[i];
            types.add(type);
            if (type.hasSapling()) hasTree = true;
        }
        staticProp = PropertyEnum.create("type", clazz, types);
        return hasTree ? new BlockBaseSapling<>(name, modid, tab, clazz, ordinal) : null;
    }
    
}
