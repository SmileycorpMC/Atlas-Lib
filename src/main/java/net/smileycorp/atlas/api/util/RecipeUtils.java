package net.smileycorp.atlas.api.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

public class RecipeUtils {

    public static boolean compareItemStacks(ItemStack stack, ItemStack check, boolean useNBT) {
    	if (stack==null||check==null) return false;
    	if (check.getItem() == stack.getItem() && (check.getMetadata() == OreDictionary.WILDCARD_VALUE || check.getMetadata() == stack.getMetadata()))  {
    		if (!useNBT) return true;
    		NBTTagCompound nbt1 = stack.getTagCompound();
    		NBTTagCompound nbt2 = check.getTagCompound();
    		if (nbt1==null||nbt2==null) return (nbt1==nbt2);
    		return nbt1.equals(nbt2);
    	}
    	return false;
    }
    
    public static boolean compareItemStacksCanFit(ItemStack stack, ItemStack check) {
    	if (stack.isEmpty()||check.isEmpty()) return true;
		if (compareItemStacks(stack, check, true)) {
			if (stack.getCount()+check.getCount()<=stack.getMaxStackSize()) {
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
