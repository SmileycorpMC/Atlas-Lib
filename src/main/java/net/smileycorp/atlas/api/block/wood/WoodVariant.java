package net.smileycorp.atlas.api.block.wood;

import net.minecraft.block.properties.PropertyEnum;
import net.smileycorp.atlas.api.block.BlockProperties;

public interface WoodVariant<T extends Enum<T> & WoodEnum> extends BlockProperties {
    
    PropertyEnum<T> typeProperty();
    
}
