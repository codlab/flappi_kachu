package eu.codlab.flappi.games.instance.objects.player;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

import eu.codlab.flappi.games.instance.objects.descriptor.GameObjectAnimatedSprite;

/**
 * Created by kevin on 18/02/14.
 */
public class Particle extends GameObjectAnimatedSprite {
    private Player _player;

    public Particle(final Player player, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, int w, int h) {
        super(player.getX()+player.getWidth()/2,
                player.getY()+player.getHeight()/2,
                pTiledTextureRegion,
                pVertexBufferObjectManager);
        _player = player;
        reset();
    }

    private Random _random;

    private Random getRandom() {
        if (_random == null) _random = new Random();
        return _random;
    }

    int _waiting;
    float _time_ellapsed;

    public void reset(){
        this.setX(_player.getX() + (_player.getWidth()-this.getWidth())/2);
        this.setY(_player.getY() + (_player.getHeight() - this.getHeight()) / 2 );

        _time_ellapsed = 0;
        _waiting = 200+getRandom().nextInt(300);

        this.setCurrentTileIndex((getRandom().nextInt(4))%2);
        this.mPhysicsHandler.setVelocityX(-400-(getRandom().nextInt(250)));
        this.mPhysicsHandler.setVelocityY(100-(getRandom().nextInt(200)));
    }

    @Override
    public void move(float pSecondsElapsed) {
        _time_ellapsed+=pSecondsElapsed;
        if(_time_ellapsed*1000 > _waiting)
            reset();
    }
}
