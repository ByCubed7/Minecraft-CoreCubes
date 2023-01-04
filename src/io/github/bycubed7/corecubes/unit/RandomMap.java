package io.github.bycubed7.corecubes.unit;

import java.util.NavigableMap;
import java.util.Random;
import java.util.TreeMap;

public class RandomMap<E> {
    private final NavigableMap<Double, E> map = new TreeMap<Double, E>();
    private Random random;
    private double total = 0;

    public RandomMap() {
        this(new Random());
    }

    public RandomMap(Random random) {
        this.random = random;
    }

    
    // Sets
    
    public RandomMap<E> add(double weight, E result) {
        if (weight <= 0) return this;
        total += weight;
        map.put(total, result);
        return this;
    }
    
    public void setSeed(long seed) {
        random.setSeed(seed);
    }


    // Gets

    public E next() {
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }

    public E next(long seed) {
    	random = new Random(seed);
        double value = random.nextDouble() * total;
        return map.higherEntry(value).getValue();
    }
    
}