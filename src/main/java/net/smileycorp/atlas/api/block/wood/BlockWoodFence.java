package net.smileycorp.atlas.api.block.wood;

import net.minecraft.block.BlockFence;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.ResourceLocation;

public class BlockWoodFence<T extends Enum<T> & WoodEnum> extends BlockFence {
    
    public BlockWoodFence(String modid, T variant, CreativeTabs tab) {
        super(Material.WOOD, variant.plankColour());
        setHardness(variant.getHardness());
        setResistance(variant.getResistance());
        setCreativeTab(tab);
        setSoundType(variant.getSoundType());
        String name = variant.getName() + "_fence";
        setRegistryName(new ResourceLocation(modid, name));
        setUnlocalizedName(modid + "." + name);
        useNeighborBrightness = true;
    }
    
}
