package net.smileycorp.atlas.api.util;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class RecipeUtils {

    public static boolean compareItemStacks(ItemStack stack1, ItemStack stack2, boolean useNBT) {
    	if (stack1==null||stack2==null) return false;
    	if (stack2.getItem() == stack1.getItem() && (stack2.getMetadata() == 32767 || stack2.getMetadata() == stack1.getMetadata()))  {
    		NBTTagCompound nbt1 = stack1.getTagCompound();
    		NBTTagCompound nbt2 = stack2.getTagCompound();
    		if (nbt1==null||nbt2==null) return (nbt1==nbt2);
    		return nbt1.equals(nbt2)||!useNBT;
    	}
    	return false;
    }
    
    public static boolean compareItemStacksCanFit(ItemStack stack1, ItemStack stack2) {
    	if (stack1.isEmpty()||stack2.isEmpty()) return true;
		if (compareItemStacks(stack1, stack2, true)) {
			if (stack1.getCount()+stack2.getCount()<=stack1.getMaxStackSize()) {
				return true;
			}
		}
    	return false;
	}
    
    /*checks to see if stack2 is bigger than stack 1, if matchExactly is true, only checks that they are the same size*/
    public static boolean compareItemStacksWithSize(ItemStack stack1, ItemStack stack2, boolean matchExactly) {
		if (compareItemStacks(stack1, stack2, true)) {
			if (stack1.getCount()==stack2.getCount()) {
				return true;
			} else if (!matchExactly && stack1.getCount()<stack2.getCount()) {
				return true;
			}
		}
    	return false;
	}

	public static boolean canResultsFitInSlots(List<ItemStack> outputList, List<ItemStack> slots, int amount) {
		for (ItemStack out : outputList) {
			amount = compareSlots(out, slots, amount);
			if (amount<=0) {
				return true;
			}
		}
		return false;
	}
	
	private static int compareSlots(ItemStack out, List<ItemStack> slots, int amount) {
		for (ItemStack slot : slots) {
			if (compareItemStacksCanFit(out, slot)) {
				return amount--;
			}
		}
		for (ItemStack slot : slots) {
			if (slot.isEmpty()) {
				return amount--;
			}
		}
		return amount;
	}
}
