package net.smileycorp.atlas.api.item;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.IForgeRegistry;

public class ArmourSet {

	final String modid;
	final String name;
	final Tiers material;
	
	protected final Map<ArmourType, Item> tools = new HashMap<ArmourType, Item>();
	
	public ArmourSet(String modid, String name, Tiers material, CreativeModeTab tab) {
		this(modid, name, material, tab, ArmorItem.class);
	}
	
	public ArmourSet(String modid, String name, Tiers material, CreativeModeTab tab, Class<? extends ArmorItem> clazz) {
		this.name=name;
		this.modid=modid;
		this.material=material;
		for (ArmourType type : ArmourType.values()) {
			Item item = type.createItem(modid, name, material, tab, null);
			if (item!=null) tools.put(type, item);
		}
	}
	
	public String getModID() {
		return modid;
	}
	
	public String getName() {
		return name;
	}
	
	public Tier getMaterial() {
		return material;
	}
	
	public Item getItem(ArmourType type) {
		return tools.get(type);
	}
	
	public Collection<Item> getItems() {
		return tools.values();
	}

	public void registerItems(IForgeRegistry<Item> registry) {
		for (Item tool:tools.values()) {
			registry.register(tool);
		}
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
		
		public ArmorItem createItem(String modid, String name, Tier material, CreativeModeTab tab, Class<? extends ArmorItem> clazz) {
			try {
				ArmorItem item = ObfuscationReflectionHelper.findConstructor(clazz, Tier.class, EquipmentSlot.class,  Properties.class).newInstance(material, slot, new Properties().tab(tab));
				item.setRegistryName(new ResourceLocation(modid, name + "_" + this.name));
				return item;
			} catch (InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
}
