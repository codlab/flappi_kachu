package eu.codlab.flappi.games.instance.objects.environment;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

import eu.codlab.flappi.games.instance.objects.descriptor.GameObjectAnimatedSprite;
import eu.codlab.flappi.games.instance.scene.IGetWidthHeight;

/**
 * Created by kevin on 18/02/14.
 */
public class Cloud extends GameObjectAnimatedSprite {
    private IGetWidthHeight _interface_screen;
    public Cloud(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, IGetWidthHeight screenInterface) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        _interface_screen = screenInterface;
        reset();
    }

    private Random _random;

    private Random getRandom() {
        if (_random == null) _random = new Random();
        return _random;
    }

    public void reset(){
        this.setX(getRandom().nextInt((int) _interface_screen.getWidth())+_interface_screen.getWidth());
        this.setY(getRandom().nextInt((int) (_interface_screen.getHeight()/4)));
        this.setCurrentTileIndex((getRandom().nextInt(4))%4);
        this.mPhysicsHandler.setVelocityX(-100-getRandom().nextInt(200));
    }
    @Override
    public void move(float pSecondsElapsed) {
        if(this.getX() + this.getWidth()*2 < _interface_screen.get0())
            reset();
    }
}
