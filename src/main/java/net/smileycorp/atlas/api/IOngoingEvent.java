package net.smileycorp.atlas.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public interface IOngoingEvent {
	
	public void readFromNBT(CompoundTag nbt);
	
	public CompoundTag writeToNBT(CompoundTag nbt);
	
	public void update(Level level);
	
	public boolean isActive(Level level);
}
