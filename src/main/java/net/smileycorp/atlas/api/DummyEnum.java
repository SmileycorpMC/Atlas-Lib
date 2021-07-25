package net.smileycorp.atlas.api;

import net.minecraft.util.StringRepresentable;

public enum DummyEnum implements StringRepresentable {
	
	DUMMY;

	@Override
	public String getSerializedName() {
		return "dummy";
	}
	
}
