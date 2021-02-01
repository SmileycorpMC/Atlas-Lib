package net.smileycorp.atlas.api.block;

import java.util.Collection;

import com.google.common.base.Optional;

import net.minecraft.block.properties.IProperty;

public class PropertyString implements IProperty<String> {
	
	private final String name;
	
	public PropertyString(String name) {
		this.name=name;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public Collection<String> getAllowedValues() {
		return null;
	}

	@Override
	public Class<String> getValueClass() {
		return String.class;
	}

	@Override
	public Optional<String> parseValue(String value) {
		return null;
	}

	@Override
	public String getName(String string) {
		return name;
	}

}
