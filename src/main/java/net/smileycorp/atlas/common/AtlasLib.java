package net.smileycorp.atlas.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLConstructModEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.smileycorp.atlas.api.block.FuelHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = Constants.MODID)
@Mod.EventBusSubscriber(modid = Constants.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtlasLib {

	private static Logger logger = LogManager.getLogger(Constants.MODID);

	public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Constants.MODID);

	@SubscribeEvent
	public static void modConstruction(FMLConstructModEvent event) {
		NeoForge.EVENT_BUS.register(FuelHandler.INSTANCE);
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

	public static void logInfo(Object message) {
		logger.info(message);
	}

	public static void logError(Object message, Exception e) {
		logger.error(message, e);
		e.printStackTrace();
	}

}
