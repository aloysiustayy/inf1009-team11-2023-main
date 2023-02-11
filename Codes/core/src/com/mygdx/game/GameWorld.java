package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class GameWorld {

    public static World world;
    public static GameWorld instance;

    public GameWorld() {
        if (instance == null) {
            instance = this;
        } else {
            this.dispose();
        }
        System.out.println("This is my world haha: " + GameWorld.world);
    }

    public GameWorld(float gravityForce, boolean doSleep) {
        GameWorld.world = new World(new Vector2(0, gravityForce), false);
        instance = this;
    }

    public World getWorld() {

        return GameWorld.world;
    }

    public void updateWorld() {
        GameWorld.world.step(1 / 60f, 6, 2);
    }

    public void dispose() {
        GameWorld.world.dispose();
    }
}
