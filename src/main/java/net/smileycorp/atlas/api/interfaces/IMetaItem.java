package net.smileycorp.atlas.api.interfaces;

public interface IMetaItem {
	
	public default String byMeta(int meta) {
		return null;
	};
	
	public default int getMaxMeta() {
		return 0;
	}
}
