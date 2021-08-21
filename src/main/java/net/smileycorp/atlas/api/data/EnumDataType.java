package net.smileycorp.atlas.api.data;

import java.util.function.Function;

import com.google.gson.JsonElement;


public enum EnumDataType {

	BYTE("byte", Byte.class, (json)->json.getAsByte()),
	SHORT("short", Short.class, (json)->json.getAsShort()),
	INT("int", Integer.class, (json)->json.getAsInt()),
	LONG("long", Long.class, (json)->json.getAsLong()),
	FLOAT("float", Float.class, (json)->json.getAsFloat()),
	DOUBLE("double", Double.class, (json)->json.getAsDouble()),
	STRING("string", String.class, (json)->json.getAsString()),
	BOOLEAN("boolean", Boolean.class, (json)->json.getAsBoolean());

	private final String name;
	private final Class<?> clazz;
	private final Function<JsonElement, Comparable<?>> jsonReader;

	private EnumDataType(String name, Class<?> clazz, Function<JsonElement, Comparable<?>> jsonReader) {
		this.name=name;
		this.clazz=clazz;
		this.jsonReader=jsonReader;
	}

	public Class<?> getType() {
		return clazz;
	}

	public String getName() {
		return name;
	}

	public Comparable<?> readFromJson(JsonElement element) {
		return jsonReader.apply(element);
	}

	public static EnumDataType of(String name) {
		for (EnumDataType type : values()) {
			if (type.getName().equals(name)) return type;
		}
		return null;
	}

	public static EnumDataType of(Class<?> clazz) {
		for (EnumDataType type : values()) {
			if (type.getType()==clazz) return (EnumDataType) type;
		}
		return null;
	}

}
