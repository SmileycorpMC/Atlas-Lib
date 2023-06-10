package net.smileycorp.atlas.common;

import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.smileycorp.atlas.api.block.FuelHandler;

@Mod(value = Constants.MODID)
@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtlasLib {

	public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Constants.MODID);

	@SubscribeEvent
	public static void modConstruction(FMLConstructModEvent event) {
		MinecraftForge.EVENT_BUS.register(FuelHandler.INSTANCE);
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
