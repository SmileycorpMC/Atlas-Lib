package net.smileycorp.atlas.api.client;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.smileycorp.atlas.api.block.wood.*;

import java.util.Map;

public class WoodStateMapper<T extends Enum<T> & WoodEnum> extends StateMapperBase {
    
    private final WoodVariant<T> variant;
    
    public WoodStateMapper(WoodVariant<T> variant) {
        this.variant = variant;
    }
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map<IProperty<?>, Comparable<?>> properties = Maps.newHashMap(state.getProperties());
        properties.remove(variant.getVariantProperty());
        if (variant instanceof BlockBaseSapling) properties.remove(BlockSapling.STAGE);
        if (variant instanceof BlockBaseLeaves) {
            properties.remove(BlockLeaves.DECAYABLE);
            properties.remove(BlockLeaves.CHECK_DECAY);
        }
        if (variant instanceof BlockBaseLog && state.getValue(BlockLog.LOG_AXIS) == BlockLog.EnumAxis.NONE) properties.remove(BlockLog.LOG_AXIS);
        return new ModelResourceLocation(state.getBlock().getRegistryName().getResourceDomain()
                + ":" + variant.byState(state), getPropertyString(properties));
    }
    
}
