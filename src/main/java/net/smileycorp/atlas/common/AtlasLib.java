package net.smileycorp.atlas.common;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import net.smileycorp.atlas.api.block.FuelHandler;

@Mod(modid = ModDefinitions.modid, name=ModDefinitions.name, version=ModDefinitions.version)
public class AtlasLib {
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event){
		GameRegistry.registerFuelHandler(FuelHandler.INSTANCE);
	}
}
