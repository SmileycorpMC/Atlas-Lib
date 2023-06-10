package net.smileycorp.atlas.api;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.smileycorp.atlas.api.entity.AtlasBoat;
import net.smileycorp.atlas.api.entity.AtlasChestBoat;
import net.smileycorp.atlas.api.item.AtlasBoatItem;
import net.smileycorp.atlas.common.AtlasLib;

import java.util.HashMap;
import java.util.Map;

public class BoatRegistry  {

	private Map<ResourceLocation, Type> TYPES = new HashMap<ResourceLocation, Type>();

	public static BoatRegistry INSTANCE = new BoatRegistry();

	public static RegistryObject<EntityType<AtlasBoat>> BOAT_ENTITY;
	public static RegistryObject<EntityType<AtlasChestBoat>> CHEST_BOAT_ENTITY;

	@SubscribeEvent
	public void addCreative(BuildCreativeModeTabContentsEvent event) {
		for (Type type : TYPES.values()) if (event.getTab() == type.creativeTab) event.accept(type.getBoat());
	}

	public Type register(String name, String modid, DeferredRegister<Item> register, CreativeModeTab creativeTab) {
		if (TYPES.isEmpty()) {
			BOAT_ENTITY = AtlasLib.ENTITIES.register("atlas_boat", ()-> EntityType.Builder.<AtlasBoat>of(AtlasBoat::new, MobCategory.MISC)
					.sized(1.375F, 0.5625F).clientTrackingRange(10).build("atlas_boat"));
			CHEST_BOAT_ENTITY = AtlasLib.ENTITIES.register("atlas_chest_boat", ()-> EntityType.Builder.<AtlasChestBoat>of(AtlasChestBoat::new, MobCategory.MISC)
					.sized(1.375F, 0.5625F).clientTrackingRange(10).build("atlas_chest_boat"));
			MinecraftForge.EVENT_BUS.register(this);
		}
		Type type = new Type(name, modid, register, creativeTab);
		TYPES.put(type.getRegistryName(), type);
		return type;
	}

	public Type get(ResourceLocation loc) {
		return TYPES.get(loc);
	}

	public Type get(String name, String modid) {
		return get(new ResourceLocation(modid, name));
	}


	public static class Type {

		private final String name, modid;
		private final RegistryObject<Item> boat, chest_boat;
		private final CreativeModeTab creativeTab;

		protected Type(String name, String modid, DeferredRegister<Item> register, CreativeModeTab creativeTab) {
			this.name = name;
			this.modid = modid;
			boat = register.register(name+"_boat", () -> new AtlasBoatItem(this, new Item.Properties()));
			chest_boat = register.register(name+"_chest_boat", () -> new AtlasBoatItem(this, new Item.Properties()));
			this.creativeTab = creativeTab;
		}

		public String getName() {
			return name;
		}

		public String getModid() {
			return modid;
		}

		public Item getBoat() {
			return boat.get();
		}

		public Item getChestBoat() {
			return chest_boat.get();
		}

		public ResourceLocation getRegistryName() {
			return new ResourceLocation(modid, name);
		}

	}
}
