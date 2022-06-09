package net.smileycorp.atlas.api.item;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ArmourSet {

	final String name;
	final Tiers material;

	protected final Map<ArmourType, RegistryObject<ArmorItem>> tools = new HashMap<ArmourType, RegistryObject<ArmorItem>>();

	public ArmourSet(String name, Tiers material, CreativeModeTab tab, DeferredRegister<Item> registry) {
		this(name, material, tab, ArmorItem.class, registry);
	}

	public ArmourSet(String name, Tiers material, CreativeModeTab tab, Class<? extends ArmorItem> clazz, DeferredRegister<Item> registry) {
		this.name=name;
		this.material=material;
		for (ArmourType type : ArmourType.values()) {
			RegistryObject<ArmorItem> item = registry.register(name + "_" + type.name, () -> type.createItem(material, tab, null));
			tools.put(type, item);
		}
	}

	public String getName() {
		return name;
	}

	public Tier getMaterial() {
		return material;
	}

	public ArmorItem getItem(ArmourType type) {
		return tools.get(type).get();
	}

	public Collection<ArmorItem> getItems() {
		Set<ArmorItem> set = new HashSet<ArmorItem>();
		for (RegistryObject<ArmorItem> registry : tools.values()) set.add(registry.get());
		return set;
	}

	public static class ArmourType {

		static Set<ArmourType> REGISTERED_TYPES = new HashSet<ArmourType>();

		public static ArmourType HELMET = register("helmet", EquipmentSlot.HEAD);
		public static ArmourType CHESTPLATE = register("chestplate", EquipmentSlot.CHEST);
		public static ArmourType LEGGINGS = register("leggings", EquipmentSlot.LEGS);
		public static ArmourType BOOTS = register("boots", EquipmentSlot.FEET);

		final protected String name;
		final protected EquipmentSlot slot;

		ArmourType(String name, EquipmentSlot slot) {
			this.name=name;
			this.slot=slot;
		}

		public static Set<ArmourType> values() {
			return REGISTERED_TYPES;
		}

		public static ArmourType register(String name, EquipmentSlot slot) {
			ArmourType type = new ArmourType(name, slot);
			REGISTERED_TYPES.add(type);
			return type;
		}

		public String getName() {
			return name;
		}

		public ArmorItem createItem(Tier material, CreativeModeTab tab, Class<? extends ArmorItem> clazz) {
			try {
				ArmorItem item = ObfuscationReflectionHelper.findConstructor(clazz, Tier.class, EquipmentSlot.class,  Properties.class).newInstance(material, slot, new Properties().tab(tab));
				return item;
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

}
