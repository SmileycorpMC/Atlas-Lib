package net.smileycorp.atlas.api.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.NumberNBT;
import net.minecraft.nbt.StringNBT;

public class NBTExplorer<T extends Comparable<T>> {

	private final String target;
	private final Class<T> returnType;

	public NBTExplorer (String target, Class<T> returnType) {
		this.target = target;
		this.returnType = returnType;
	}

	@SuppressWarnings("unchecked")
	public T findValue(CompoundNBT compound) throws Exception {
		String[] directory = target.split("\\.");
		INBT nbt = compound;
		for (int i = 0; i < directory.length; i++) {
			if (i == directory.length-1) {
				Object value;
				if (nbt instanceof NumberNBT) {
					value = ((NumberNBT) nbt).getAsNumber();
				} else if (nbt instanceof StringNBT) {
					value =((StringNBT)nbt).getString();
				} else {
					throw new Exception("Cannot find value");
				}
				if (returnType.isAssignableFrom(value.getClass())) {
					return (T) value;
				} else {
					throw new Exception("Value is not of type " + returnType);
				}
			} else if (nbt instanceof CompoundNBT) {
				nbt = ((CompoundNBT) nbt).get(directory[i]);
			} else {
				throw new Exception("Cannot find value");
			}
		}
		return null;
	}

}
