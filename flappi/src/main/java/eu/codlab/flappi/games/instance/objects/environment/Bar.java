package eu.codlab.flappi.games.instance.objects.environment;

import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.codlab.flappi.games.instance.objects.descriptor.GameObjectSprite;

/**
 * Created by kevin on 18/02/14.
 */
public class Bar extends GameObjectSprite {
    private float _bottom=0;
    private boolean _play = true;

    private final static int NORMAL= 0;
    private final static int PASSED = 1;
    private final static int BLOCK = 2;

    int _type = NORMAL;


    public Bar(final float pX, final float pY, final TextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    public void move(float pSecondsElapsed) {
        if (_play) {
            int velocity = -400;
            if(_type == BLOCK){
                velocity = 0;
            }
            mPhysicsHandler.setVelocityX(velocity);
        }

    }

    public void setBottom(float bottom){
        _bottom = bottom;
    }

    public float getBottom(){
        return _bottom;
    }

    public void start(){
        _play = true;
        _type = NORMAL;
        mPhysicsHandler.setVelocityX(-400);
    }

    public void stop(){
        _play = false;
        mPhysicsHandler.setVelocityX(0);
    }
}
