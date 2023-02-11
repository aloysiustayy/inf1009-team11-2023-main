package com.mygdx.game.ObjectPool;

import com.mygdx.game.Entity.*;

public class PlatformPool extends ObjectPool<Entity> {

    protected int platformWidth = 100;
    protected int platformHeight = 100;

    @Override
    protected Entity create() {
        Entity newEntity = new Entity(0, 0, platformWidth, platformHeight, true);
        newEntity.setTexture("badlogic.jpg");
        return newEntity;
    }

}
