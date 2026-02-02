package net.smileycorp.atlas.api.block;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.List;

public class PropertyBoolean implements IUnlistedProperty<Boolean> {

    private final String name;

    public PropertyBoolean(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isValid(Boolean value) {
        return value != null;
    }

    @Override
    public Class<Boolean> getType() {
        return Boolean.class;
    }

    @Override
    public String valueToString(Boolean value) {
        return value.toString();
    }

}
