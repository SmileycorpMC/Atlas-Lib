package net.smileycorp.atlas.api.client;

import com.google.common.collect.Maps;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.smileycorp.atlas.api.block.wood.BlockBaseLeaves;
import net.smileycorp.atlas.api.block.wood.BlockBaseSapling;
import net.smileycorp.atlas.api.block.wood.WoodEnum;
import net.smileycorp.atlas.api.block.wood.WoodVariant;

import java.util.Map;

public class WoodStateMapper<T extends Enum<T> & WoodEnum> extends StateMapperBase {
    
    private final WoodVariant<T> variant;
    
    public WoodStateMapper(WoodVariant<T> variant) {
        this.variant = variant;
    }
    
    @Override
    protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
        Map<IProperty<?>, Comparable<?>> properties = Maps.newHashMap(state.getProperties());
        properties.remove(variant.typeProperty());
        if (variant instanceof BlockBaseSapling) properties.remove(BlockSapling.STAGE);
        if (variant instanceof BlockBaseLeaves) {
            properties.remove(BlockLeaves.DECAYABLE);
            properties.remove(BlockLeaves.CHECK_DECAY);
        }
        return new ModelResourceLocation(state.getBlock().getRegistryName().getResourceDomain()
                + ":" + variant.byState(state), getPropertyString(properties));
    }
    
}
