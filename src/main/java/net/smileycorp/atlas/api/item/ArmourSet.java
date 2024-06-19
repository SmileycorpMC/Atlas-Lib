package net.smileycorp.atlas.api.item;

import com.google.common.collect.Maps;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Tier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.util.ObfuscationReflectionHelper;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public class ArmourSet {

	final String name;
	final Tier material;
	protected final Supplier<CreativeModeTab> tab;

	protected final Map<ArmourType, DeferredHolder<Item, ArmorItem>> armor = Maps.newHashMap();

	public ArmourSet(String name, Tier material, Supplier<CreativeModeTab> tab, DeferredRegister<Item> registry, IEventBus modBus) {
		this(name, material, tab, ArmorItem.class, registry, modBus);
	}

	public ArmourSet(String name, Tier material, Supplier<CreativeModeTab> tab, Class<? extends ArmorItem> clazz, DeferredRegister<Item> registry, IEventBus modBus) {
		this.name = name;
		this.material = material;
		this.tab = tab;
		for (ArmourType type : ArmourType.values()) {
			DeferredHolder<Item, ArmorItem> item = registry.register(name + "_" + type.name, () -> type.createItem(material, null));
			armor.put(type, item);
		}
		modBus.register(this);
	}

	@SubscribeEvent
	public void addCreative(BuildCreativeModeTabContentsEvent event) {
		if (event.getTab() == tab.get()) for (ArmorItem item : getItems()) event.accept(item);
	}

	public String getName() {
		return name;
	}

	public Tier getMaterial() {
		return material;
	}

	public ArmorItem getItem(ArmourType type) {
		return armor.get(type).get();
	}

	public Collection<ArmorItem> getItems() {
		Set<ArmorItem> set = new HashSet<ArmorItem>();
		for (DeferredHolder<Item, ArmorItem> registry : armor.values()) set.add(registry.get());
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

		public ArmorItem createItem(Tier material, Class<? extends ArmorItem> clazz) {
			try {
				ArmorItem item = ObfuscationReflectionHelper.findConstructor(clazz, Tier.class, EquipmentSlot.class,  Properties.class).newInstance(material, slot, new Properties());
				return item;
			} catch (InstantiationException | IllegalAccessException
					 | IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}
	}

}
