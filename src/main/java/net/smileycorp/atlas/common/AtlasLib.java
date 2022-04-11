package net.smileycorp.atlas.common;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.BoatRegistry;
import net.smileycorp.atlas.api.block.FuelHandler;
import net.smileycorp.atlas.api.entity.AtlasBoat;

@Mod(value = ModDefinitions.MODID)
@Mod.EventBusSubscriber(modid = ModDefinitions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtlasLib {

	public static final EntityType<AtlasBoat> BOAT_ENTITY = EntityType.Builder.<AtlasBoat>of(AtlasBoat::new, MobCategory.MISC)
			.sized(1.375F, 0.5625F).clientTrackingRange(10).build("atlas_boat");

	@SubscribeEvent
	public static void modConstruction(FMLConstructModEvent event) {
		MinecraftForge.EVENT_BUS.register(FuelHandler.INSTANCE);
	}

	@SubscribeEvent
	public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {
		IForgeRegistry<EntityType<?>> registry = event.getRegistry();
		if (BoatRegistry.INSTANCE.hasValues()) registry.register(BOAT_ENTITY);
	}

}
