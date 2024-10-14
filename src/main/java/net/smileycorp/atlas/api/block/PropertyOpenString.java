package net.smileycorp.atlas.api.block;

import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.function.Predicate;

public class PropertyOpenString implements IUnlistedProperty<String> {
	
	private final String name;
	private Predicate<String> allowedValues;
	
	public PropertyOpenString(String name, Predicate<String> predicate) {
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
