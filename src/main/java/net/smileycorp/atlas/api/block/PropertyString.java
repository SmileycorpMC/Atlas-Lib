package net.smileycorp.atlas.api.block;

import net.minecraft.world.level.block.state.properties.Property;

import java.util.List;
import java.util.Optional;

public class PropertyString extends Property<String> {

	private final String name;
	private List<String> allowedValues;

	public PropertyString(String name, List<String> allowedValues) {
		super(name, String.class);
		this.name=name;
		this.allowedValues=allowedValues;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public List<String> getPossibleValues() {
		return allowedValues;
	}

	@Override
	public Class<String> getValueClass() {
		return String.class;
	}

	@Override
	public Optional<String> getValue(String value) {
		return Optional.of(value);
	}

	@Override
	public String getName(String value) {
		return value;
	}

}
