package net.smileycorp.atlas.api.item;

public interface IMetaItem {
	
	public default String byMeta(int meta) {
		return null;
	};
	
	public default int getMaxMeta() {
		return 0;
	}
}
