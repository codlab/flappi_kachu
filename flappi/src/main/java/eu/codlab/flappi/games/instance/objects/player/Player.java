package eu.codlab.flappi.games.instance.objects.player;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.codlab.flappi.games.instance.objects.descriptor.GameObjectAnimatedSprite;

/**
 * Created by kevin on 18/02/14.
 */
public class Player extends GameObjectAnimatedSprite {
    int _time_counter = 0;

    boolean play = false;
    float bottom;
    private final static int NORMAL = 0;
    private final static int MONTEE = 1;
    private final static int DESCENTE = 2;

    int _type = NORMAL;


    public Player(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
    }

    private float montee(int tmp) {
        return 2 * tmp - 800;
    }

    private float descente(int tmp) {
        return 550 * tmp / 400;
    }

    @Override
    public void move(float pSecondsElapsed) {
        if (play) {
            if (_time_counter <= 0) {
                _time_counter = 0;
                if (_type == MONTEE) {
                    _type = DESCENTE;
                    _time_counter = 400;
                } else if (_type == DESCENTE) {
                    _type = NORMAL;
                }
            } else {
                _time_counter -= pSecondsElapsed * 1000;
            }

            if (_type == MONTEE) {
                this.mPhysicsHandler.setVelocityY(montee(400 - _time_counter));
            } else if (_type == DESCENTE) {
                this.mPhysicsHandler.setVelocityY(descente(400 - _time_counter));
            } else {
                this.mPhysicsHandler.setVelocityY(1000);
            }
        }
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public void start() {
        this.mPhysicsHandler.setVelocityX(0);
        _time_counter = 0;
        play = true;
    }

    public void stop() {
        play = false;
        this.mPhysicsHandler.setVelocityY(0);
        this.mPhysicsHandler.setVelocityX(0);
        _time_counter = 0;
    }

    public void failed() {
        if (play == true) {
            play = false;
            this.mPhysicsHandler.setVelocityY(1200);
            this.mPhysicsHandler.setVelocityX(-400);
        }
    }

    public void stopFall() {
        play = false;
        this.mPhysicsHandler.setVelocityY(0);
        this.mPhysicsHandler.setVelocityX(-400);
    }

    public boolean isPlaying() {
        return play;
    }

    public void onTouch() {
        _type = MONTEE;
        _time_counter = 400;
        play = true;
    }
}
