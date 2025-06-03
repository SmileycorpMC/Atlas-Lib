package net.smileycorp.atlas.api.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.FarmlandWaterManager;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;

import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

public class BlockFarmlandBase extends BlockFarmland implements BlockProperties {

    private final Function<IBlockState, IBlockState> base;

    public BlockFarmlandBase(String name, String modid, CreativeTabs tab, Function<IBlockState, IBlockState> base) {
        super();
        name = name.toLowerCase(Locale.US);
        setRegistryName(new ResourceLocation(modid, name.toLowerCase(Locale.US)));
        setUnlocalizedName(modid + "." + name);
        setCreativeTab(tab);
        setSoundType(SoundType.PLANT);
        this.base = base;
    }

    public BlockFarmlandBase(String name, String modid, SoundType sound, float h, float r, String tool, int level, CreativeTabs tab, Function<IBlockState, IBlockState> base) {
        this(name, modid, tab, base);
        setResistance(r);
        setHardness(h);
        setHarvestLevel(tool, level);
        setSoundType(sound);
    }

    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        int i = state.getValue(MOISTURE);
        if (!hasWater(world, pos) && !world.isRainingAt(pos.up())) {
            if (i > 0) world.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(i - 1)), 2);
            else if (!hasCrops(world, pos)) turnToDirt(world, pos, state);
            return;
        }
        if (i < 7) world.setBlockState(pos, state.withProperty(MOISTURE, Integer.valueOf(7)), 2);
    }

    private boolean hasCrops(World world, BlockPos pos) {
        Block block = world.getBlockState(pos.up()).getBlock();
        return block instanceof IPlantable && canSustainPlant(world.getBlockState(pos), world, pos, EnumFacing.UP, (IPlantable)block);
    }

    private boolean hasWater(World worldIn, BlockPos pos) {
        for (BlockPos.MutableBlockPos blockpos$mutableblockpos : BlockPos.getAllInBoxMutable(pos.add(-4, 0, -4), pos.add(4, 1, 4)))
            if (worldIn.getBlockState(blockpos$mutableblockpos).getMaterial() == Material.WATER) return true;
        return FarmlandWaterManager.hasBlockWaterTicket(worldIn, pos);
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
        entity.fall(fallDistance, 1);
        if (!ForgeHooks.onFarmlandTrample(world, pos, Blocks.DIRT.getDefaultState(), fallDistance, entity)) return;
        turnToDirt(world, pos, world.getBlockState(pos));
    }

    @Override
    public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
        if (!world.getBlockState(pos.up()).getMaterial().isSolid()) return;
        turnToDirt(world, pos, state);
    }

    @Override
    public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
        if (!world.getBlockState(pos.up()).getMaterial().isSolid()) return;
        turnToDirt(world, pos, state);
    }

    protected void turnToDirt(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, base.apply(state));
        AxisAlignedBB axisalignedbb = field_194405_c.offset(pos);
        for (Entity entity : world.getEntitiesWithinAABBExcludingEntity(null, axisalignedbb))
            entity.setPositionAndUpdate(entity.posX, entity.posY + Math.min(axisalignedbb.maxY - axisalignedbb.minY,
                    axisalignedbb.maxY - entity.getEntityBoundingBox().minY) + 0.001, entity.posZ);
    }

}
