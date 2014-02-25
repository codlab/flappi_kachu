package eu.codlab.flappi.games.instance.objects.descriptor;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by kevin on 18/02/14.
 */
public abstract class GameObjectAnimatedSprite extends AnimatedSprite {

    public PhysicsHandler mPhysicsHandler;

    public GameObjectAnimatedSprite(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.mPhysicsHandler = new PhysicsHandler(this);
        this.registerUpdateHandler(this.mPhysicsHandler);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        move(pSecondsElapsed);

        super.onManagedUpdate(pSecondsElapsed);
    }

    public abstract void move(float pSecondsElapsed);
}
