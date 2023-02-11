package com.mygdx.game.Entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.mygdx.game.GameWorld;
import static com.mygdx.game.Utils.Constants.*;

public class Entity {

    private Body myBody;

    private SpriteBatch batch;
    private Texture mySprite;
    String name;
    private boolean isActive = true;

    // Global shape so dont have to create each time hide and unhide in object
    // pooling
    // PolygonShape shape;
    PolygonShape shape;
    // Shapes myShape;
    // Keep track of Fixture to destroy it when hidden, and assign back when
    // createFixture
    // Fixture is basically the collision body, without fixture, cannot detect
    // collision
    private Fixture myFixture;

    public Entity(int xPos, int yPos, int width, int height, boolean isStatic) {

        myBody = createBox(xPos, yPos, width, height, isStatic);
    }

    // Needed to draw
    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    // The image that the entity will have
    public void setTexture(String path) {
        mySprite = new Texture(path);
    }

    public Body getBody() {
        return this.myBody;
    }

    public Vector2 getPosition() {
        return this.myBody.getPosition();
    }

    int width, height;

    private Body createBox(int xPos, int yPos, int width, int height, boolean isStatic) {
        Body pbody;
        BodyDef def = new BodyDef();
        if (isStatic)
            def.type = BodyType.StaticBody;
        else
            def.type = BodyType.DynamicBody;

        if (def.type == BodyType.DynamicBody) {
            name = "Player";
        }

        // Set initial position of box
        def.position.set(xPos / PPM, yPos / PPM);

        // Set box fixedRotation to true so wont rotate
        def.fixedRotation = true;

        // Create a body
        pbody = GameWorld.instance.getWorld().createBody(def);

        this.width = width;
        this.height = height;

        shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        myFixture = pbody.createFixture(shape, 1.0f);

        pbody.setActive(true);
        shape.dispose();
        return pbody;
    }

    public void SetPosition(float xPos, float yPos) {
        getBody().setTransform(new Vector2(xPos / PPM, yPos / PPM), 0);
    }

    // Set entity active with isActive
    public void SetActive(boolean isActive) {
        this.isActive = isActive;

        // Destroy / Create Fixture, Fixture is to give it shape
        if (!isActive) {
            getBody().destroyFixture(myFixture);
        } else {
            shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
            myFixture = getBody().createFixture(shape, 1.0f);
        }
    }

    public boolean GetActive() {
        return isActive;
    }

    public void renderEntity(OrthographicCamera camera) {

        // If any of this condition not met, return from method
        if (batch == null || mySprite == null || !GetActive())
            return;

        batch.begin();

        float textureWidthScale = mySprite.getWidth() / 4;
        float textureHeightScale = mySprite.getHeight() / 4;

        float curX = getPosition().x * PPM - (textureWidthScale / 2);
        float curY = getPosition().y * PPM - (textureHeightScale / 2);

        // Draw the image of entity
        batch.draw(mySprite, curX, curY, textureWidthScale, textureHeightScale);

        batch.end();

    }

    public void dispose() {
        batch.dispose();
        mySprite.dispose();
    }

    public void inputUpdate(float delta) {
        int horizontalForce = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            horizontalForce -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            horizontalForce += 1;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
            getBody().applyForceToCenter(0, 300, false);
        }
        getBody().setLinearVelocity(horizontalForce * 5, getBody().getLinearVelocity().y);
    }
}