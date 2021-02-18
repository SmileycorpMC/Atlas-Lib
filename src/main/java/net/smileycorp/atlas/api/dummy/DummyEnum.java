package net.smileycorp.atlas.api.dummy;

import net.minecraft.util.IStringSerializable;

public enum DummyEnum implements IStringSerializable {
	
	DUMMY;

	@Override
	public String getName() {
		return "dummy";
	}
	
}
