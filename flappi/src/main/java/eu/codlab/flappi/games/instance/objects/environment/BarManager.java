package eu.codlab.flappi.games.instance.objects.environment;

import android.content.Context;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

/**
 * Created by kevin on 18/02/14.
 */
public class BarManager {
    private Bar _top;
    private Bar _bottom;
    private Bar _middle_top;
    private Bar _middle_bottom;

    int delta_player = 0;
    private static int pYorigin;

    private int _delta_player_calculated = 0;
    public BarManager(Scene mainScene, final float pX, float pY, final float scale, float bottom_set, final Context context, final TextureManager manager, final float player_size, final TextureRegion middle_texture, final TextureRegion top, final TextureRegion bottom, final VertexBufferObjectManager pVertexBufferObjectManager) {
        _top = new Bar(pX, pY + delta_player, top, pVertexBufferObjectManager);
        _bottom = new Bar(pX, pY - bottom.getHeight() - delta_player, bottom, pVertexBufferObjectManager);

        middle_texture.setTextureWidth(_top.getWidth());
        middle_texture.setTextureHeight(bottom_set);

        pYorigin = (int) pY;
        delta_player = (int) (7 * player_size / 4);

        _middle_top = new Bar(pX, 0, middle_texture, pVertexBufferObjectManager);
        _middle_bottom = new Bar(pX, 0, middle_texture, pVertexBufferObjectManager);
        _middle_top.setHeight(bottom_set);
        _middle_bottom.setHeight(bottom_set);

        setBottom(bottom_set);

        BarManager.pY = (int) pY;
        randomize();

        mainScene.attachChild(_middle_top);
        mainScene.attachChild(_middle_bottom);
        mainScene.attachChild(_top);
        mainScene.attachChild(_bottom);

        stop();
    }

    private boolean play = false;

    private final static int NORMAL = 0;
    private final static int PASSED = 1;
    private final static int BLOCK = 2;

    int _type = NORMAL;

    public void move(float pSecondsElapsed) {
        if (play) {
            _top.move(pSecondsElapsed);
            _bottom.move(pSecondsElapsed);
            _middle_top.move(pSecondsElapsed);
            _middle_bottom.move(pSecondsElapsed);
        }

    }

    public Bar getMiddleBottom() {
        return _middle_bottom;
    }

    public Bar getMiddleTop() {
        return _middle_top;
    }

    public Bar getTop() {
        return _top;
    }

    public Bar getBottom() {
        return _bottom;
    }

    public void remove(Scene scene) {
        if (_bottom != null) {
            scene.detachChild(_bottom);
            _bottom = null;
        }
        if (_middle_top != null) {
            scene.detachChild(_middle_top);
            _middle_top = null;
        }
        if (_middle_bottom != null) {
            scene.detachChild(_middle_bottom);
            _middle_bottom = null;
        }
        if (_top != null) {
            scene.detachChild(_top);
            _top = null;
        }
    }

    public boolean outOfScreen() {
        return _top.getX() + _top.getWidth() < 0;
    }

    public float getWidth() {
        return _top.getWidth();
    }

    public float getX() {
        return _top.getX();
    }

    public void setX(float x) {
        _top.setX(x);
        _bottom.setX(x);
        _middle_bottom.setX(x);
        _middle_top.setX(x);
    }

    private static final Random __random = new Random();

    private static int getNext(int total) {
        return __random.nextInt(total);
    }

    private static int pY = -1;

    public void randomize() {
        int total_height = (int) (_bottom.getHeight() + _top.getHeight() + 2 * delta_player);
        int partition = total_height >> 1;


        //given pY the actual center
        //we must find a value randomly in this gateway :
        //maxima pY+total_height minima pY-total_height
        //but pY+total_height/2 < ground value
        //but pY-total_height/2 > 0

        //X.getBottom() return the actual ground level

        pY = pY - partition + getNext(total_height);

        if (pY + partition >= _bottom.getBottom()) {
            pY = (int) (_bottom.getBottom() - total_height);
        } else if (pY - partition <= 0) {
            pY = total_height;
        } else {
        }

        _bottom.setY(pY - delta_player - _bottom.getHeight());
        _middle_top.setY(pY - delta_player - _bottom.getHeight() - _middle_top.getBottom());
        _top.setY(pY + delta_player);
        _middle_bottom.setY(pY + delta_player + _top.getHeight());

        _score = 1;
    }

    private int _score = 1;

    public int getScore(){
        int r = _score;
        _score=0;
        return r;
    }

    public void setBottom(float bottom) {

        _top.setBottom(bottom);
        _bottom.setBottom(bottom);
        _middle_bottom.setBottom(bottom);
        _middle_top.setBottom(bottom);

    }

    public void start() {
        play = true;
        _type = NORMAL;
        _top.start();
        _bottom.start();
        _middle_bottom.start();
        _middle_top.start();
    }

    public void stop() {
        play = false;
        _top.stop();
        _bottom.stop();
        _middle_bottom.stop();
        _middle_top.stop();
        pY = pYorigin;
    }

    public void onTouch() {
    }
}
