package net.smileycorp.atlas.api.util;

import java.util.UUID;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

public class DataUtils {

	public static boolean isValidUUID(String uuid) {
		try  {
			UUID.fromString(uuid);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static void saveItemsToNBT(CompoundTag nbt, NonNullList<ItemStack> items) {
		ListTag nbtItems = new ListTag();
		for (ItemStack stack : items) nbtItems.add(stack.save(new CompoundTag()));
		nbt.put("Inventory", nbtItems);
	}

	public static NonNullList<ItemStack> readItemsFromNBT(CompoundTag nbt) {
		NonNullList<ItemStack> items = NonNullList.create();
		for (Tag tag : nbt.getList("Inventory", 10)) if (tag instanceof CompoundTag) items.add(ItemStack.of((CompoundTag) tag));
		return items;
	}

}
