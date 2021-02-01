package net.smileycorp.atlas.api.interfaces;

public interface IBlockProperties {
	
	public default int getMaxMeta(){
		return 0;
	}
	
	public default boolean usesCustomItemHandler(){
		return false;
	}
	
	public default boolean useInventoryVariant(){
		return false;
	}
}
