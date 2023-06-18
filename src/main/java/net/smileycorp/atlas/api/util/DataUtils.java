package net.smileycorp.atlas.api.util;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.smileycorp.atlas.common.AtlasLib;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

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

	public static void copyFileFromMod(String path, String modid) {
		Logger logger = LogManager.getLogger(modid);
		try {
			ModFile mod = FMLLoader.getLoadingModList().getModFileById(modid).getFile();
			File directory = FMLPaths.CONFIGDIR.get().resolve(modid).toFile();
			File output = new File(directory, path);
			File dir = output.getParentFile();
			if (dir != null) dir.mkdirs();
			FileUtils.copyInputStreamToFile(Files.newInputStream(mod.findResource("config_defaults/" + path), StandardOpenOption.READ), new File(directory, path));
			logger.info("Copied file " + path);
		} catch (Exception e) {
			logger.error("Failed to copy file " + path, e);
		}
	}

}
