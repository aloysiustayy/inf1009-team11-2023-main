package com.mygdx.game.ObjectPool;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.mygdx.game.GameWorld;
import com.mygdx.game.Entity.Entity;

public abstract class ObjectPool<T> {
    private final Set<T> available = new HashSet<>();
    private final Set<T> inUse = new HashSet<>();

    GameWorld world;
    public List<Entity> pooledObjects = new ArrayList<Entity>();
    public Entity objToPool;

    // This will be override in child class
    protected abstract T create();

    public synchronized T checkOut() {
        if (available.isEmpty())
            available.add(create());
        T instance = available.iterator().next();
        available.remove(instance);
        inUse.add(instance);
        return instance;
    }

    public synchronized void checkIn(T instance) {
        inUse.remove(instance);
        available.add(instance);
    }
}
