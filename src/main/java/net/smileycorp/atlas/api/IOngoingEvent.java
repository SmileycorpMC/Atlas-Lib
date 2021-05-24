package net.smileycorp.atlas.api;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public interface IOngoingEvent {
	
	public void readFromNBT(NBTTagCompound nbt);
	
	public NBTTagCompound writeToNBT(NBTTagCompound nbt);
	
	public void update(World world);
	
	public boolean isActive(World world);
}
