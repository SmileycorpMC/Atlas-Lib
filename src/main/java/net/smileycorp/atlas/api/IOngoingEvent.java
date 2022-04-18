package net.smileycorp.atlas.api;

import net.minecraft.nbt.CompoundTag;

public interface IOngoingEvent<T> {

	public void readFromNBT(CompoundTag nbt);

	public CompoundTag writeToNBT(CompoundTag nbt);

	public void update(T world);

	public boolean isActive(T world);
}
