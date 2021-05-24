package net.smileycorp.atlas.api.block;

import java.util.List;

import net.minecraft.block.properties.IProperty;

import com.google.common.base.Optional;

public class PropertyString implements IProperty<String> {
	
	private final String name;
	private List<String> allowedValues;
	
	public PropertyString(String name, List<String> allowedValues) {
		this.name=name;
		this.allowedValues=allowedValues;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getAllowedValues() {
		return allowedValues;
	}

	@Override
	public Class<String> getValueClass() {
		return String.class;
	}

	@Override
	public Optional<String> parseValue(String value) {
		return Optional.of(value);
	}

	@Override
	public String getName(String value) {
		return value;
	}

}
