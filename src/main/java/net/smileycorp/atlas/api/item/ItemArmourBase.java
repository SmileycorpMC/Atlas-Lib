package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Locale;

public class ItemArmourBase extends ItemArmor implements IMetaItem {

    public ItemArmourBase(String modid, String name, ArmorMaterial material, CreativeTabs tab, EntityEquipmentSlot slot) {
        super(material, 0, slot);
        setRegistryName(new ResourceLocation(modid, name));
        setUnlocalizedName(modid + "." + name);
        setCreativeTab(tab);
    }

    public static Item helmet(String modid, String name, ArmorMaterial material, CreativeTabs tab) {
        return new ItemArmourBase(modid, name.toLowerCase(Locale.US) + "_helmet", material, tab, EntityEquipmentSlot.HEAD);
    }

    public static Item chestplate(String modid, String name, ArmorMaterial material, CreativeTabs tab) {
        return new ItemArmourBase(modid, name.toLowerCase(Locale.US) + "_chestplate", material, tab, EntityEquipmentSlot.CHEST);
    }

    public static Item leggings(String modid, String name, ArmorMaterial material, CreativeTabs tab) {
        return new ItemArmourBase(modid, name.toLowerCase(Locale.US) + "_leggings", material, tab, EntityEquipmentSlot.LEGS);
    }

    public static Item boots(String modid, String name, ArmorMaterial material, CreativeTabs tab) {
        return new ItemArmourBase(modid, name.toLowerCase(Locale.US) + "_boots", material, tab, EntityEquipmentSlot.FEET);
    }

}
