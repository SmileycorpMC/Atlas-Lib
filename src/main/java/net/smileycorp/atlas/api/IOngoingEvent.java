package net.smileycorp.atlas.api;

import net.minecraft.nbt.CompoundNBT;

public interface IOngoingEvent<T> {

	public void readFromNBT(CompoundNBT nbt);

	public CompoundNBT writeToNBT(CompoundNBT nbt);

	public void update(T world);

	public boolean isActive(T world);
}
