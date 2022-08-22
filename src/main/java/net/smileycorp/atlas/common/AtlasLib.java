package net.smileycorp.atlas.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.smileycorp.atlas.api.block.FuelHandler;

@Mod(value = ModDefinitions.MODID)
@Mod.EventBusSubscriber(modid = ModDefinitions.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AtlasLib {

	public static DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, ModDefinitions.MODID);

	public static SimpleChannel NETWORK_INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(ModDefinitions.MODID, "main"), ()-> "1", "1"::equals, "1"::equals);

	@SubscribeEvent
	public static void modConstruction(FMLConstructModEvent event) {
		MinecraftForge.EVENT_BUS.register(FuelHandler.INSTANCE);
		ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
	}

}
