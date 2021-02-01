package net.smileycorp.atlas.api.interfaces;

public interface IMetaBlock {
	
	public default String byMeta(int meta) {
		return null;
	};
	
	public default int getMaxMeta() {
		return 0;
	}
}
