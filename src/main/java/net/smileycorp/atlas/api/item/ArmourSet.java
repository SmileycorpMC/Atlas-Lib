package net.smileycorp.atlas.api.item;

import com.google.common.collect.Maps;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.util.Func;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;

@SuppressWarnings("deprecation")
public class ArmourSet {

	final String modid;
	final String name;
	final ItemArmor.ArmorMaterial material;

	Map<ArmorType, Item> tools = Maps.newEnumMap(ArmorType.class);

	public ArmourSet(String modid, String name, ItemArmor.ArmorMaterial material, CreativeTabs tab) {
		this(modid, name, material, tab, -1);
	}

	public ArmourSet(String modid, String name, ItemArmor.ArmorMaterial material, CreativeTabs tab, int horseArmourStrength) {
		this.name = name;
		this.modid = modid;
		this.material = material;
		for (ArmorType type : ArmorType.values()) {
			if (type == ArmorType.HORSE) {
				if (horseArmourStrength >= 0) tools.put(type, new ItemHorseArmourBase(modid, name, horseArmourStrength, tab));
				continue;
			}
			Item item = type.createItem(modid, name, material, tab);
			if (item != null) tools.put(type, item);
		}
	}

	public String getModID() {
		return modid;
	}

	public String getName() {
		return name;
	}

	public ItemArmor.ArmorMaterial getMaterial() {
		return material;
	}

	public Item getItem(ArmorType type) {
		return tools.get(type);
	}

	public Item getItem(EntityEquipmentSlot slot) {
		return tools.get(ArmorType.get(slot));
	}

	public Item getHelmet() {
		return getItem(ArmorType.HELMET);
	}

	public Item getChestplate() {
		return getItem(ArmorType.CHESTPLATE);
	}

	public Item getLeggings() {
		return getItem(ArmorType.LEGGINGS);
	}

	public Item getBoots() {
		return getItem(ArmorType.BOOTS);
	}

	public boolean hasHorseArmour() {
		return tools.containsKey(ArmorType.HORSE);
	}

	public Item getHorseArmour() {
		return getItem(ArmorType.HORSE);
	}

	public Collection<Item> getItems() {
		return tools.values();
	}

	public void registerItems(IForgeRegistry<Item> registry) {
		tools.values().forEach(registry::register);
	}

	public void registerModels() {
		tools.entrySet().forEach(entry ->
			ModelLoader.setCustomModelResourceLocation(entry.getValue(), 0,
					new ModelResourceLocation(modid + ":items/"
					+ name.toLowerCase() + "_armour", entry.getKey().name().toLowerCase(Locale.US))));
	}

	public void registerRecipes() {
		registerRecipes(true);
	}

	public void registerRecipes(boolean oredict) {
		ItemStack stack = material.getRepairItemStack();
		int[] ores = oredict ? OreDictionary.getOreIDs(stack) : new int[0];
		Ingredient ingredient = ores.length == 0 ? Ingredient.fromStacks(stack) : new OreIngredient(OreDictionary.getOreName(ores[0]));
		tools.entrySet().forEach(entry -> entry.getKey().registerRecipe(modid, name, entry.getValue(), ingredient));
	}

	public enum ArmorType {
		HELMET("helmet", ItemArmourBase::helmet, EntityEquipmentSlot.HEAD, "MMM", "M M"),
		CHESTPLATE("chestplate", ItemArmourBase::chestplate, EntityEquipmentSlot.CHEST, "M M", "MMM", "MMM"),
		LEGGINGS("leggings",ItemArmourBase::leggings, EntityEquipmentSlot.LEGS, "MMM", "M M", "M M"),
		BOOTS("boots", ItemArmourBase::boots, EntityEquipmentSlot.FEET, "M M", "M M"),
		HORSE("horse_armour", Func::Null, null);

		final String name;
		final ArmourConstructor constructor;
		final EntityEquipmentSlot slot;
		final Object[] pattern;

		ArmorType(String name, ArmourConstructor constructor, EntityEquipmentSlot slot, Object... pattern) {
			this.name = name;
			this.constructor = constructor;
			this.slot = slot;
			this.pattern = pattern;
		}

		public Item createItem(String modid, String name, ItemArmor.ArmorMaterial material, CreativeTabs tab) {
			return constructor.create(modid, name, material, tab);
		}

		public void registerRecipe(String modid, String material, Item item, Ingredient ingredient) {
			if (pattern.length < 1) return;
			Object[] recipe = {'M', ingredient, 'S'};
			GameRegistry.addShapedRecipe(new ResourceLocation(modid, material.toLowerCase(Locale.US) + "_" + name),
					new ResourceLocation(modid, material.toLowerCase(Locale.US) + "_" + name),
					new ItemStack(item), ArrayUtils.addAll(pattern, recipe));
		}

		public static ArmorType get(EntityEquipmentSlot slot) {
			for (ArmorType type : values()) if (type.slot == slot) return type;
			return null;
		}

	}

	private interface ArmourConstructor {

		Item create(String modid, String name, ItemArmor.ArmorMaterial material, CreativeTabs tab);

	}
	
}
