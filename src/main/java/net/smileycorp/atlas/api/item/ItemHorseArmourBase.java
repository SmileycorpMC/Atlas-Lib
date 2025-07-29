package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

import java.util.Locale;

public class ItemHorseArmourBase extends Item {

    private HorseArmorType type;

    public ItemHorseArmourBase(String modid, String name, int strength, CreativeTabs tab) {
        name = name.toLowerCase(Locale.US) + "_horse_armour";
        setRegistryName(new ResourceLocation(modid, name));
        setUnlocalizedName(modid + "." + name);
        setCreativeTab(tab);
        setMaxStackSize(1);
        type = EnumHelper.addHorseArmor(name, name, strength);
    }

    @Override
    public HorseArmorType getHorseArmorType(ItemStack stack) {
        return type;
    }

}
