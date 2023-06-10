package net.smileycorp.atlas.api.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;


public class DataType<T extends Comparable<T>> {

	private static Map<String, DataType<?>> registry = new HashMap<String, DataType<?>>();

	public static DataType<Byte> BYTE = new DataType<Byte>("byte", Byte.class, (json)->json.getAsByte(), (nbt, key) -> nbt.getByte(key));
	public static DataType<Short> SHORT = new DataType<Short>("short", Short.class, (json)->json.getAsShort(), (nbt, key) -> nbt.getShort(key));
	public static DataType<Integer> INT = new DataType<Integer>("int", Integer.class, (json)->json.getAsInt(), (nbt, key) -> nbt.getInt(key));
	public static DataType<Long> LONG = new DataType<Long>("long", Long.class, (json)->json.getAsLong(), (nbt, key) -> nbt.getLong(key));
	public static DataType<Float> FLOAT = new DataType<Float>("float", Float.class, (json)->json.getAsFloat(), (nbt, key) -> nbt.getFloat(key));
	public static DataType<Double> DOUBLE = new DataType<Double>("double", Double.class, (json)->json.getAsDouble(), (nbt, key) -> nbt.getDouble(key));
	public static DataType<String> STRING = new DataType<String>("string", String.class, (json)->json.getAsString(), (nbt, key) -> nbt.getString(key));
	public static DataType<Boolean> BOOLEAN = new DataType<Boolean>("boolean", Boolean.class, (json)->json.getAsBoolean(), (nbt, key) -> nbt.getBoolean(key));

	private final String name;
	private final Class<T> clazz;
	private final Function<JsonElement, T> jsonReader;
	private final BiFunction<CompoundTag, String, T> nbtReader;

	private DataType(String name, Class<T> clazz, Function<JsonElement, T> jsonReader, BiFunction<CompoundTag, String, T> nbtReader) {
		this.name=name;
		this.clazz=clazz;
		this.jsonReader=jsonReader;
		this.nbtReader=nbtReader;
		registry.put(name, this);
	}

	public Class<T> getType() {
		return clazz;
	}

	public String getName() {
		return name;
	}

	public Boolean isNumber() {
		return this!=BOOLEAN && this!=STRING ;
	}

	public T cast(Comparable<?> value) {
		return clazz.cast(value);
	}

	public T readFromJson(JsonElement element) {
		return jsonReader.apply(element);
	}

	public T readFromNBT(CompoundTag nbt, String key) {
		return nbtReader.apply(nbt, key);
	}

	public static DataType<?> of(String name) {
		if (registry.containsKey(name)) return registry.get(name);
		return null;
	}

	public static DataType<?> of(Class<?> clazz) {
		for (DataType<?> type : registry.values()) {
			if (type.getType()==clazz) return type;
		}
		return null;
	}

}
