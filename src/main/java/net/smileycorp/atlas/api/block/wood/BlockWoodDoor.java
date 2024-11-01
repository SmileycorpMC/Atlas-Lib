package net.smileycorp.atlas.api.block.wood;

import net.minecraft.block.BlockDoor;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.util.ResourceLocation;

public class BlockWoodDoor <T extends Enum<T> & WoodEnum> extends BlockDoor {
    
    private final ItemDoor item;
    
    public BlockWoodDoor(String modid, T variant, CreativeTabs tab) {
        super(Material.WOOD);
        setHardness(variant.getHardness());
        setResistance(variant.getResistance());
        setCreativeTab(tab);
        setSoundType(variant.getSoundType());
        String name = variant.getName() + "_door";
        setRegistryName(new ResourceLocation(modid, name));
        setUnlocalizedName(modid + "." + name);
        useNeighborBrightness = true;
        item = new ItemDoor(this);
        item.setRegistryName(new ResourceLocation(modid, name));
        item.setUnlocalizedName(modid + "." + name);
        item.setCreativeTab(tab);
    }
    
    public Item getItem() {
        return item;
    }
    
}
