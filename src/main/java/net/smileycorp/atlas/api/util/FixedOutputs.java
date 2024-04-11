package net.smileycorp.atlas.api.util;

import net.minecraft.util.RandomSource;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class FixedOutputs<T> extends WeightedOutputs<T> {

	public FixedOutputs(Collection<T> entries) {
		this(1, entries);
	}

	public FixedOutputs(int defaultTries, Collection<T> entries) {
		super(defaultTries, mapCollection(entries));
	}

	@Override
	public void addEntry(T t, int weight) {
		addEntry(t);
	}

	public void addEntry(T t) {
		entries.add(new SimpleEntry<T, Integer>(t, 1));
	}

	@Override
	public void addEntries(Collection<Entry<T, Integer>> entries) {
		this.entries.addAll(mapEntries(entries));
	}

	public void addCollection(Collection<T> entries) {
		this.entries.addAll(mapCollection(entries));
	}

	@Override
	public void addEntries(Map<T, Integer> entries) {
		this.entries.addAll(mapCollection(entries.keySet()));
	}

	@Override
	public List<T> getResults(RandomSource rand, int tries) {
		List<T> list = new ArrayList<T>();
		for (int i = 0; i < tries*defaultTries; i++) {
			getResult(list, i);
		}
		return list;
	}

	private void getResult(List<T> list, int i) {
		if (i < entries.size()) {
			list.add(entries.get(i).getKey());
		} else {
			getResult(list, i-entries.size());
		}
	}

	private static <T> List<Entry<T, Integer>> mapCollection(Collection<T> entries) {
		List<Entry<T, Integer>> result = new ArrayList<Entry<T, Integer>>();
		for (T entry : entries) {
			result.add(new SimpleEntry<T, Integer>(entry, 1));
		}
		return result;
	}

	private static <T> List<Entry<T, Integer>> mapEntries(Collection<Entry<T, Integer>> entries) {
		List<Entry<T, Integer>> result = new ArrayList<Entry<T, Integer>>();
		for (Entry<T, Integer> entry : entries) {
			result.add(new SimpleEntry<T, Integer>(entry.getKey(), 1));
		}
		return result;
	}
}
