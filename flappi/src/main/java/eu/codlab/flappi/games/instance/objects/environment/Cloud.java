package eu.codlab.flappi.games.instance.objects.environment;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

import eu.codlab.flappi.games.instance.objects.descriptor.GameObjectAnimatedSprite;

/**
 * Created by kevin on 18/02/14.
 */
public class Cloud extends GameObjectAnimatedSprite {
    private int _w;
    private int _h;

    public Cloud(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int w, int h) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        _w = w;
        _h = h;
        reset();
    }

    private Random _random;

    private Random getRandom() {
        if (_random == null) _random = new Random();
        return _random;
    }

    public void reset(){
        this.setX(getRandom().nextInt(_w)+_w);
        this.setY(getRandom().nextInt(_h/4));
        this.setCurrentTileIndex((getRandom().nextInt(4))%4);
        this.mPhysicsHandler.setVelocityX(-100-getRandom().nextInt(200));
    }
    @Override
    public void move(float pSecondsElapsed) {
        if(this.getX() + this.getWidth()*2 < 0)
            reset();
    }
}
