package net.smileycorp.atlas.api.block.wood;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.gen.feature.WorldGenerator;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Supplier;

public interface WoodEnum extends IStringSerializable {
    
    @Nullable
    Supplier<WorldGenerator> getTree();
    
    @Nullable
    Supplier<WorldGenerator> getLargeTree();
    
    default boolean hasTree() {
        return getTree() != null;
    }
    
    default boolean hasLargeTree() {
        return getLargeTree() != null;
    }
    
    default boolean hasSapling() {
        return hasTree() || hasLargeTree();
    }
    
    MapColor plankColour();
    
    MapColor logColour();
    
    MapColor leavesColour();
    
    default boolean isFlammable() {
        return true;
    }
    
    default SoundType getSoundType() {
        return SoundType.WOOD;
    }
    
    default SoundType getLeavesSoundType() {
        return SoundType.PLANT;
    }
    
    default float getHardness() {
        return 2f;
    }
    
    default float getResistance() {
        return 5f;
    }
    
    float saplingDropChance();
    
    Map<ItemStack, Float> getLeafDrops();
    
}
