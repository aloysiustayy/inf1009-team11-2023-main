package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.Entity.*;
import static com.mygdx.game.Utils.Constants.*;

import java.util.ArrayList;
import java.util.List;

public class GameEngine extends ApplicationAdapter {

	SpriteBatch batch;
	// Texture img;
	private OrthographicCamera camera;
	private GameWorld world;
	private Box2DDebugRenderer b2dr;
	private List<Entity> myEntities = new ArrayList<Entity>();
	private Entity player, platform;

	@Override
	public void create() {
		// Best is to have only 1 spritebatch in game
		batch = new SpriteBatch();

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w / SCALE, h / SCALE);

		// The gravity that will act on entity
		float gravityForce = -10.0f;

		// The world object that entity will spawn in
		world = new GameWorld(gravityForce, false);

		// To draw the squarethingy like a hitbox but just for debugging
		b2dr = new Box2DDebugRenderer();

		// Create entities, set texture and batch
		player = new Entity(32, 32, 32, 32, false);
		platform = new Entity(0, 0, 200, 100, true);

		myEntities.add(player);
		myEntities.add(platform);

		for (Entity entity : myEntities) {
			entity.setBatch(batch);
		}
		player.setTexture("soybean.png");
		platform.setTexture("badlogic.jpg");

	}

	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0, 1);

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		for (Entity entity : myEntities) {
			entity.renderEntity(camera);
		}

		update(Gdx.graphics.getDeltaTime());
		b2dr.render(world.getWorld(), camera.combined.scl(PPM));
	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width / SCALE, height / SCALE);
	}

	@Override
	public void dispose() {
		// Dispose of world
		world.dispose();

		// Dispose of entity's spritebatch and texture
		for (Entity entity : myEntities)
			entity.dispose();

	}

	// Just another method for render so render doesnt becomes too cluttered
	public void update(float delta) {
		world.updateWorld();
		cameraUpdate(delta);

		// Update that handles player input
		player.inputUpdate(delta);
		batch.setProjectionMatrix(camera.combined);
	}

	// Makes it so that camera follows player
	public void cameraUpdate(float delta) {
		Vector3 position = camera.position;
		position.x = player.getPosition().x * PPM;
		position.y = player.getPosition().y * PPM;
		camera.position.set(position);
		camera.update();
	}

}
