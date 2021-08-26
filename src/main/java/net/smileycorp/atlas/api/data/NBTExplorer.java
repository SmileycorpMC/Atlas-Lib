package net.smileycorp.atlas.api.data;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;

public class NBTExplorer<T extends Comparable<T>> {

	private final String target;
	private final DataType<T> returnType;

	public NBTExplorer (String target, DataType<T> returnType) {
		this.target = target;
		this.returnType = returnType;
	}

	public T findValue(CompoundNBT compound) throws Exception {
		String[] directory = target.split("\\.");
		INBT nbt = compound;
		for (int i = 0; i < directory.length; i++) {
			try {
				if (nbt instanceof CompoundNBT) {
					if (i == directory.length-1) {
						T value = returnType.readFromNBT((CompoundNBT) nbt, directory[i]);
						if (value == null) {
							throw new Exception("Value " + directory[i] + "is not of type " + returnType.getType());
						}
						return value;
					} else {
						nbt = ((CompoundNBT) nbt).get(directory[i]);
					}
				} else if (nbt instanceof ListNBT) {
					INBT nextNBT = null;
					for (INBT tag : ((ListNBT)nbt)) {
						if (tag instanceof CompoundNBT) {
							if (((CompoundNBT) tag).getString("Name").equals(directory[i])) {
								nextNBT = tag;
								break;
							}
						}
					}
					if  (nextNBT==null) {
						throw new Exception("NBTList " + nbt + " does not contain \"Name\":\"" + directory[i] + "\"");
					} else {
						nbt = nextNBT;
					}
				} else {
					throw new Exception("Value " + directory[i] + " is not an applicable type or cannot be found as nbt is " + nbt);
				}
			} catch (Exception e) {
				StringBuilder builder = new StringBuilder();
				for (int j = 0; j < directory.length; j++) {
					if (i == j) {
						builder.append(">"+directory[j]+"<");
					} else {
						builder.append(directory[j]);
					}
					if (j < directory.length-1) {
						builder.append("\\.");
					}
				}
				throw new Exception(builder.toString() + " " + e.getMessage(), e.getCause());
			}
		}
		throw new Exception("Could not find value " + directory);
	}

	@Override
	public String toString() {
		return target;
	}

}
