package net.smileycorp.atlas.api.config;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.config.Configuration;
import net.smileycorp.atlas.api.util.ModLogger;

import java.util.List;

public class BiomeGenEntry {

    private final String name;

    private static String[] generationBiomesStr;
    private static List<Biome> generationBiomes;
    private static String[] generationBiomesBlacklistStr;

    public BiomeGenEntry(Configuration config, String name, String[] biomes, String[] biomeBlacklist) {
        this.name = name;
        generationBiomesStr = config.get("generation", "generationBiomes", biomes, "Which biomes can " + name + " generate in (Can specify either biomes names or Biome Dictionaries)?").getStringList();
        generationBiomesBlacklistStr = config.get("generation", "generationBiomesBlacklist", biomeBlacklist, "Which biomes can " + name + " can never generate in (Overrides generationBiomes, Can specify either biomes names or Biome Dictionaries)?").getStringList();
    }

    public List<Biome> getGenerationBiomes(ModLogger logger) {
        if (generationBiomes == null) {
            logger.logInfo("reading biome generation config for " + name);
            generationBiomes = Lists.newArrayList();
            for (String str : generationBiomesStr) {
                if (str.contains(":")) {
                    try {
                        Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(str));
                        if (biome != null) generationBiomes.add(biome);
                        else logger.logError("Biome " + str + " is not registered", new NullPointerException());
                    } catch (Exception e) {
                        logger.logError(str + " is not a valid registry name", e);
                    }
                }
                else {
                    try {
                        BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);
                        for (Biome biome : BiomeDictionary.getBiomes(type)) generationBiomes.add(biome);
                    } catch (Exception e) {
                        logger.logError(str + " is not a valid registry name", e);
                    }
                }
            }
            for (String str : generationBiomesBlacklistStr) {
                if (str.contains(":")) {
                    try {
                        Biome biome = Biome.REGISTRY.getObject(new ResourceLocation(str));
                        if (biome != null) generationBiomes.remove(biome);
                        else logger.logError("Biome " + str + " is not registered", new NullPointerException());
                    } catch (Exception e) {
                        logger.logError(str + " is not a valid registry name", e);
                    }
                }
                else {
                    try {
                        BiomeDictionary.Type type = BiomeDictionary.Type.getType(str);
                        for (Biome biome : BiomeDictionary.getBiomes(type)) generationBiomes.remove(biome);
                    } catch (Exception e) {
                        logger.logError(str + " is not a valid registry name", e);
                    }
                }
            }
            logger.logInfo("Registered generation biomes " + generationBiomes + " for " + name);
        }
        return generationBiomes;
    }

}
