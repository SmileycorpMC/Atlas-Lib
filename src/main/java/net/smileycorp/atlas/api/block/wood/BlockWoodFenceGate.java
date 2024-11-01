package net.smileycorp.atlas.api.block.wood;

import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockWoodFenceGate<T extends Enum<T> & WoodEnum> extends BlockFenceGate {
    
    private final T variant;
    
    public BlockWoodFenceGate(String modid, T variant, CreativeTabs tab) {
        super(BlockPlanks.EnumType.OAK);
        this.variant = variant;
        setHardness(variant.getHardness());
        setResistance(variant.getResistance());
        setCreativeTab(tab);
        setSoundType(variant.getSoundType());
        String name = variant.getName() + "_fence_gate";
        setRegistryName(new ResourceLocation(modid, name));
        setUnlocalizedName(modid + "." + name);
        useNeighborBrightness = true;
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
        return variant.plankColour();
    }
    
}
