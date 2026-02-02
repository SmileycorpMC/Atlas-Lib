package net.smileycorp.atlas.api.block;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

public class BlockGrassBase extends BlockGrass implements BlockProperties {

    private static boolean registered;
    private final Function<IBlockState, IBlockState> base, farmland;

    public BlockGrassBase(String name, String modid, CreativeTabs tab, Function<IBlockState, IBlockState> base) {
       this(name, modid, tab, base, null);
    }

    public BlockGrassBase(String name, String modid, CreativeTabs tab, Function<IBlockState, IBlockState> base, @Nullable Function<IBlockState, IBlockState> farmland) {
        super();
        name = name.toLowerCase(Locale.US);
        setRegistryName(new ResourceLocation(modid, name.toLowerCase(Locale.US)));
        setUnlocalizedName(modid + "." + name);
        setCreativeTab(tab);
        setSoundType(SoundType.PLANT);
        this.base = base;
        this.farmland = farmland;
        if (!registered && farmland != null) {
            registered = true;
            MinecraftForge.EVENT_BUS.register(new EventHandler());
        }
    }

    public BlockGrassBase(String name, String modid, SoundType sound, float h, float r, String tool, int level, CreativeTabs tab, Function<IBlockState, IBlockState> base) {
        this(name, modid, sound, h, r, tool, level, tab, base, null);
    }

    public BlockGrassBase(String name, String modid, SoundType sound, float h, float r, String tool, int level, CreativeTabs tab,
                          Function<IBlockState, IBlockState> base, @Nullable Function<IBlockState, IBlockState> farmland) {
        this(name, modid, tab, base, farmland);
        setResistance(r);
        setHardness(h);
        setHarvestLevel(tool, level);
        setSoundType(sound);
    }
    
    @Override
    public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
        if (world.isRemote) return;
        if (!world.isAreaLoaded(pos, 3)) return;
        if (world.getLightFromNeighbors(pos.up()) < 4 && world.getBlockState(pos.up()).getLightOpacity(world, pos.up()) > 2) {
            world.setBlockState(pos, base.apply(state));
            return;
        }
        if (world.getLightFromNeighbors(pos.up()) < 9) return;
        for (int i = 0; i < 4; ++i) {
            BlockPos blockpos = pos.add(rand.nextInt(3) - 1, rand.nextInt(5) - 3, rand.nextInt(3) - 1);
            if (blockpos.getY() >= 0 && blockpos.getY() < 256 && !world.isBlockLoaded(blockpos)) return;
            IBlockState iblockstate = world.getBlockState(blockpos.up());
            IBlockState iblockstate1 = world.getBlockState(blockpos);
            if (iblockstate1.getBlock() == Blocks.DIRT && iblockstate1.getValue(BlockDirt.VARIANT) == BlockDirt.DirtType.DIRT) {
                if (world.getLightFromNeighbors(blockpos.up()) >= 4 && iblockstate.getLightOpacity(world, pos.up()) <= 2)
                    world.setBlockState(blockpos, Blocks.GRASS.getDefaultState());
            } else if (iblockstate1.getBlock() instanceof GrowsGrass)
                if (world.getLightFromNeighbors(blockpos.up()) >= 4 && iblockstate.getLightOpacity(world, pos.up()) <= 2)
                    world.setBlockState(blockpos, ((GrowsGrass) iblockstate1.getBlock()).getGrass(state, rand));
        }
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        IBlockState state1 = base.apply(state);
        return state1.getBlock().getItemDropped(state1, rand, fortune);
    }

    @Override
    public int damageDropped(IBlockState state) {
        IBlockState state1 = base.apply(state);
        return state1.getBlock().damageDropped(state1);
    }

    @Override
    public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plant) {
        EnumPlantType type = plant.getPlantType(world, pos.offset(direction));
        if (type == EnumPlantType.Plains) return true;
        if (type == EnumPlantType.Cave) return state.isSideSolid(world, pos, EnumFacing.UP);
        if (type == EnumPlantType.Beach) for (EnumFacing facing : EnumFacing.HORIZONTALS)
            if (world.getBlockState(pos.offset(facing)).getMaterial() == Material.WATER) return true;
        return false;
    }

    @Override
    public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
        return base.apply(state).getBlockHardness(world, pos);
    }

    @Override
    public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity entity, Explosion explosion) {
        IBlockState state = base.apply(world.getBlockState(pos));
        return state.getBlock().getExplosionResistance(world, pos, entity, explosion);
    }

    private static class EventHandler {

        @SubscribeEvent
        public void useHoe(UseHoeEvent event) {
            World world = event.getWorld();
            BlockPos pos = event.getPos();
            IBlockState state = world.getBlockState(pos);
            if (!(state.getBlock() instanceof BlockGrassBase)) return;
            BlockGrassBase block = (BlockGrassBase) state.getBlock();
            if (block.farmland == null) return;
            if (!world.isAirBlock(pos.up())) return;
            event.setResult(Event.Result.ALLOW);
            EntityPlayer player = event.getEntityPlayer();
            world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1, 1);
            if (world.isRemote) return;
            world.setBlockState(pos, block.farmland.apply(state), 11);
            event.getCurrent().damageItem(1, player);
        }

    }

}
