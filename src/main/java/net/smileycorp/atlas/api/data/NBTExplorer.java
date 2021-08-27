package net.smileycorp.atlas.api.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;

public class NBTExplorer<T extends Comparable<T>> {

	private final String target;
	private final DataType<T> returnType;

	public NBTExplorer (String target, DataType<T> returnType) {
		this.target = target;
		this.returnType = returnType;
	}

	public T findValue(CompoundTag compound) throws Exception {
		String[] directory = target.split("\\.");
		Tag nbt = compound;
		for (int i = 0; i < directory.length; i++) {
			try {
				if (nbt instanceof CompoundTag) {
					if (i == directory.length-1) {
						T value = returnType.readFromNBT((CompoundTag) nbt, directory[i]);
						if (value == null) {
							throw new Exception("Value " + directory[i] + "is not of type " + returnType.getType());
						}
						return value;
					} else {
						nbt = ((CompoundTag) nbt).get(directory[i]);
					}
				} else if (nbt instanceof ListTag) {
					Tag nextNBT = null;
					for (Tag tag : ((ListTag)nbt)) {
						if (tag instanceof CompoundTag) {
							if (((CompoundTag) tag).getString("Name").equals(directory[i])) {
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
