package net.smileycorp.atlas.api.block;

import com.google.common.base.Optional;
import net.minecraft.block.properties.IProperty;

import java.util.ArrayList;
import java.util.List;

public class PropertyString implements IProperty<String> {
	
	private final String name;
	private List<String> allowedValues;
	
	public PropertyString(String name, List<String> allowedValues) {
		this.name=name;
		this.allowedValues=allowedValues;
	}
	
	public PropertyString(String name) {
		this(name, new ArrayList<String>());
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
	
	public int ordinal(String value) {
		return allowedValues.indexOf(value);
	}

	public String getValue(int index) {
		return allowedValues.get(index);
	}

}
