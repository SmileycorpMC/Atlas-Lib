package net.smileycorp.atlas.api.block;

import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Locale;
import java.util.Random;
import java.util.function.Function;

public class BlockGrassBase extends BlockGrass {
    
    private final Function<IBlockState, IBlockState> base;
    
    public BlockGrassBase(String name, String modid, CreativeTabs tab, Function<IBlockState, IBlockState> base) {
        super();
        name = name.toLowerCase(Locale.US);
        setRegistryName(new ResourceLocation(modid, name.toLowerCase(Locale.US)));
        setUnlocalizedName(modid + "." + name);
        setCreativeTab(tab);
        this.base = base;
    }
    
    public BlockGrassBase(String name, String modid, SoundType sound, float h, float r, String tool, int level, CreativeTabs tab, Function<IBlockState, IBlockState> base) {
        this(name, modid, tab, base);
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
    
}
