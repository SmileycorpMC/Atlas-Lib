package net.smileycorp.atlas.api.block;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import com.google.common.base.Optional;

import net.minecraft.block.properties.IProperty;
import net.minecraftforge.common.property.IUnlistedProperty;

public class PropertyString implements IUnlistedProperty<String> {
	
	private final String name;
	private Predicate<String> allowedValues;
	
	public PropertyString(String name, Predicate<String> predicate) {
		this.name=name;
		this.allowedValues=predicate;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isValid(String value) {
		return allowedValues.test(value);
	}

	@Override
	public Class<String> getType() {
		return String.class;
	}

	@Override
	public String valueToString(String value) {
		return value;
	}

}
