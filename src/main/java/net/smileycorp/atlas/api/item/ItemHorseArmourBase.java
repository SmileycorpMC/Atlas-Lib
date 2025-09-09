package net.smileycorp.atlas.api.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;

import java.util.Locale;

public class ItemHorseArmourBase extends Item implements IMetaItem {

    private HorseArmorType type;

    public ItemHorseArmourBase(String modid, String name, int strength, CreativeTabs tab) {
        name = name.toLowerCase(Locale.US);
        type = EnumHelper.addHorseArmor(name, modid + ":textures/entities/horse/armor/horse_armor_" + name + ".png", strength);
        name += "_horse_armour";
        setRegistryName(new ResourceLocation(modid, name));
        setUnlocalizedName(modid + "." + name);
        setCreativeTab(tab);
        setMaxStackSize(1);
    }

    @Override
    public HorseArmorType getHorseArmorType(ItemStack stack) {
        return type;
    }

}
