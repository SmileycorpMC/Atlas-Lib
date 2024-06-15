package net.smileycorp.atlas.common;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.smileycorp.atlas.api.block.FuelHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(value = Constants.MODID)
public class AtlasLib {

	private static Logger logger = LogManager.getLogger(Constants.MODID);

	public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, Constants.MODID);
	
	public AtlasLib(ModContainer container, IEventBus bus) {
		NeoForge.EVENT_BUS.register(FuelHandler.INSTANCE);
		ENTITIES.register(bus);
		Constants.MOD_BUS = bus;
	}

	public static void logInfo(Object message) {
		logger.info(message);
	}

	public static void logError(Object message, Exception e) {
		logger.error(message, e);
		e.printStackTrace();
	}

}
