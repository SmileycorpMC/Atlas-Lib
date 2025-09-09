package net.smileycorp.atlas.api.config;

import net.minecraftforge.common.config.Configuration;

public class WorldGenEntry {
    
    private int size;
    private int spawnChances;
    private int minHeight;
    private int maxHeight;
    private int[] dimensions;

    public WorldGenEntry(Configuration config, String name, int spawnChances, int minHeight, int maxHeight, int... dimensions) {
        this(config, name, 0, spawnChances, minHeight, maxHeight, dimensions);
    }
    
    public WorldGenEntry(Configuration config, String name, int size, int spawnChances, int minHeight, int maxHeight, int... dimensions) {
        if (size > 0) this.size = config.get(name, "size", size, "Size of block generation to generate").getInt();
        this.spawnChances = config.get(name, "spawnChances", spawnChances, "Number of chances for " + name + " to generate (Set to 0 to disable generation)").getInt();
        this.minHeight = config.get(name, "minHeight", minHeight, "Minimum Y level " + name + " can generate. (Setting below 0 or above 255 may cause issues if a world extension mod is not installed)").getInt();
        this.maxHeight = config.get(name, "maxHeight", maxHeight, "Maximum Y level " + name + " can generate. (Setting below 0 or above 255 may cause issues if a world extension mod is not installed)").getInt();
        this.dimensions = config.get(name, "dimensions", dimensions, "Which dimensions can " + name + " generate in?").getIntList();
    }

    public WorldGenEntry(Configuration config, String name, int spawnChances, int yHeight, int... dimensions) {
        if (size > 0) this.size = config.get(name, "size", size, "Size of block generation to generate").getInt();
        this.spawnChances = config.get(name, "spawnChances", spawnChances, "Number of chances for " + name + " to generate (Set to 0 to disable generation)").getInt();
        this.minHeight = config.get(name, "minHeight", minHeight, "Y level " + name + " generates at. (Setting below 0 or above 255 may cause issues if a world extension mod is not installed)").getInt();
        this.maxHeight = minHeight;
        this.dimensions = config.get(name, "dimensions", dimensions, "Which dimensions can " + name + " generate in?").getIntList();
    }

    public int getMaxHeight() {
        return maxHeight;
    }
    
    public int getMinHeight() {
        return minHeight;
    }
    
    public int getSpawnChances() {
        return spawnChances;
    }
    
    public int getSize() {
        return size;
    }

    public int[] getDimensions() {return dimensions;}
    
}
