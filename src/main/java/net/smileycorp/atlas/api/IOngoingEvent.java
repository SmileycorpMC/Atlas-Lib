package net.smileycorp.atlas.api;

import net.minecraft.nbt.CompoundTag;

public interface IOngoingEvent<T> {

	void readFromNBT(CompoundTag nbt);
	CompoundTag writeToNBT(CompoundTag nbt);
	void update(T world);
	boolean isActive(T world);
	
}
