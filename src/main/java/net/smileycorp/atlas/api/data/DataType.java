package net.smileycorp.atlas.api.data;

import com.google.gson.JsonElement;
import net.minecraft.nbt.CompoundTag;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;


public class DataType<T extends Comparable<T>> {

	private static Map<String, DataType<?>> registry = new HashMap<String, DataType<?>>();

	public static DataType<Byte> BYTE = new DataType<>("byte", Byte.class, JsonElement::getAsByte, CompoundTag::getByte);
	public static DataType<Short> SHORT = new DataType<>("short", Short.class, JsonElement::getAsShort, CompoundTag::getShort);
	public static DataType<Integer> INT = new DataType<>("int", Integer.class, JsonElement::getAsInt, CompoundTag::getInt);
	public static DataType<Long> LONG = new DataType<>("long", Long.class, JsonElement::getAsLong, CompoundTag::getLong);
	public static DataType<Float> FLOAT = new DataType<>("float", Float.class, JsonElement::getAsFloat, CompoundTag::getFloat);
	public static DataType<Double> DOUBLE = new DataType<>("double", Double.class, JsonElement::getAsDouble, CompoundTag::getDouble);
	public static DataType<String> STRING = new DataType<>("string", String.class, JsonElement::getAsString, CompoundTag::getString);
	public static DataType<Boolean> BOOLEAN = new DataType<>("boolean", Boolean.class, JsonElement::getAsBoolean, CompoundTag::getBoolean);

	private final String name;
	private final Class<T> clazz;
	private final Function<JsonElement, T> jsonReader;
	private final BiFunction<CompoundTag, String, T> nbtReader;

	private DataType(String name, Class<T> clazz, Function<JsonElement, T> jsonReader, BiFunction<CompoundTag, String, T> nbtReader) {
		this.name = name;
		this.clazz = clazz;
		this.jsonReader = jsonReader;
		this.nbtReader = nbtReader;
		registry.put(name, this);
	}

	public Class<T> getType() {
		return clazz;
	}

	public String getName() {
		return name;
	}

	public boolean isNumber() {
		return Number.class.isAssignableFrom(clazz);
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
		return registry.containsKey(name) ? registry.get(name) : null;
	}

	public static DataType<?> of(Class<?> clazz) {
		for (DataType<?> type : registry.values()) if (type.getType() == clazz) return type;
		return null;
	}

}
