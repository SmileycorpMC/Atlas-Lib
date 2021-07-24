package net.smileycorp.atlas.api;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;

public interface IOngoingEvent {
	
	public void readFromNBT(CompoundNBT nbt);
	
	public CompoundNBT writeToNBT(CompoundNBT nbt);
	
	public void update(World world);
	
	public boolean isActive(World world);
}
