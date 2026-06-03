package net.smileycorp.atlas.api.data;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class NBTExplorer<T extends Comparable<T>> {

    private final String target;
    private final DataType<T> returnType;

    public NBTExplorer(String target, DataType<T> returnType) {
        this.target = target;
        this.returnType = returnType;
    }

    public T findValue(NBTTagCompound compound) throws Exception {
        String[] directory = target.split("\\.");
        NBTBase nbt = compound;
        for (int i = 0; i < directory.length; i++) {
            try {
                if (nbt instanceof NBTTagCompound) {
                    if (i == directory.length - 1) {
                        T value = returnType.readFromNBT((NBTTagCompound) nbt, directory[i]);
                        if (value == null) throw new Exception("Value " + directory[i] + "is not of type " + returnType.getType());
                        return value;
                    } else nbt = ((NBTTagCompound) nbt).getTag(directory[i]);
                } else if (nbt instanceof NBTTagList) {
                    NBTBase nextNBT = null;
                    for (NBTBase tag : ((NBTTagList)nbt)) if (tag instanceof NBTTagCompound && ((NBTTagCompound) tag).getString("Name").equals(directory[i])) {
                        nextNBT = tag;
                        break;
                    }
                    if  (nextNBT == null) throw new Exception("NBTTagList " + nbt + " does not contain \"Name\":\"" + directory[i] + "\"");
                    else nbt = nextNBT;
                } else throw new Exception("Value " + directory[i] + " is not an applicable type or cannot be found as nbt is " + nbt);
            } catch (Exception e) {
                StringBuilder builder = new StringBuilder();
                for (int j = 0; j < directory.length; j++) {
                    if (i == j) builder.append(">").append(directory[j]).append("<");
                    else builder.append(directory[j]);
                    if (j < directory.length - 1) builder.append("\\.");
                }
                throw new Exception(builder + " " + e.getMessage(), e.getCause());
            }
        }
        throw new Exception("Could not find value " + directory);
    }

    public String toString() {
        return this.target;
    }
    
}
