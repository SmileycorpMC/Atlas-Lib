package net.smileycorp.atlas.api.recipe;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

public class WeightedOutputs<T> {

	private final List<Entry<T, Integer>> entries;
	private final int deafaultTries;
	
	public WeightedOutputs(Map<T, Integer> entries) {
		this(1, new ArrayList<Entry<T, Integer>>(entries.entrySet()));
	}
	
	public WeightedOutputs(int deafultTries, Map<T, Integer> entries) {
		this(deafultTries, new ArrayList<Entry<T, Integer>>(entries.entrySet()));
	}

	public WeightedOutputs(int deafaultTries, List<Entry<T, Integer>> entries) {
		this.deafaultTries = deafaultTries;
		this.entries = entries;
	}

	public int getAmount() {
		return deafaultTries;
	}

	public List<Entry<T, Integer>> getTable() {
		return new ArrayList<Entry<T, Integer>>(entries);
	}
	
	public T getResult(Random rand) {
		return getResults(rand, 1).get(0);
	}

	public List<T> getResults(Random rand) {
		return getResults(rand, 1);
	}

	private List<T> getResults(Random rand, int tries) {
		List<T> list = new ArrayList<T>();
		List<Entry<T, Integer>> mappedEntries = new ArrayList<Entry<T, Integer>>();
		int max = 0;
		for(Entry<T, Integer> entry : entries) {
			mappedEntries.add(new SimpleEntry<T, Integer>(entry.getKey(), max));
			max+=entry.getValue();
		}
		if (max>0) {
			Collections.reverse(mappedEntries);
			for(int i=0; i<(tries*deafaultTries);i++){
				int result = rand.nextInt(max);
				for(Entry<T, Integer> entry : mappedEntries) {
					if (result>=entry.getValue()) {
						list.add(entry.getKey());
						break;
					}
				}
			}
		}
		return list;
	}
}
