package eu.codlab.flappi.games.instance.scene;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;

import java.io.IOException;
import java.util.ArrayList;

import eu.codlab.flappi.R;
import eu.codlab.flappi.games.instance.objects.environment.BackgroundManager;
import eu.codlab.flappi.games.instance.objects.environment.BarManager;
import eu.codlab.flappi.games.instance.objects.environment.CloudsManager;
import eu.codlab.flappi.games.instance.objects.environment.ScoresManager;
import eu.codlab.flappi.games.instance.objects.environment.TitleManager;
import eu.codlab.flappi.games.instance.objects.player.Particle;
import eu.codlab.flappi.games.instance.objects.player.Player;

public class MainActivity extends GoogleServicesActivity implements IOnSceneTouchListener, IUpdateHandler,IGetWidthHeight {
    private BitmapTextureAtlas _texture_pikachu;
    private TiledTextureRegion _tiled_pikachu;
    private Player _pikachu;

    private BitmapTextureAtlas _texture_particle;
    private TiledTextureRegion _tiled_particle;
    private ArrayList<Particle> _particles;

    private ArrayList<Particle> getParticles() {
        if (_particles == null) _particles = new ArrayList<Particle>();
        return _particles;
    }

    private BitmapTextureAtlas _bitmap_bar_top;
    private TextureRegion _texture_bar_top;
    private BitmapTextureAtlas _bitmap_bar_bottom;
    private TextureRegion _texture_bar_bottom;
    private BitmapTextureAtlas _bitmap_repeat;
    private TextureRegion _texture_repeat;

    private int _type = MAIN_SCREEN;
    private static final int MAIN_SCREEN = 0;
    private static final int MAIN_GAME = 1;
    private static final int MAIN_SCORE = 2;

    private int _bar_deltaX = 500;
    private ArrayList<BarManager> _bars;

    /**
     * Manage the scores in the game
     */
    private ScoresManager _scores;

    private ScoresManager getScoresManager() {
        if (_scores == null) _scores = new ScoresManager(this);
        return _scores;
    }

    /**
     * Manage the clouds
     */
    private CloudsManager _clouds;

    private CloudsManager getCloudsManager() {
        if (_clouds == null) _clouds = new CloudsManager(this);
        return _clouds;
    }

    /**
     * Manage the title
     */
    private TitleManager _title_manager;

    private TitleManager getTitleManager() {
        if (_title_manager == null) _title_manager = new TitleManager(this);
        return _title_manager;
    }

    /**
     * Manage the background
     */
    private BackgroundManager _background_manager;

    private BackgroundManager getBackgroundManager() {
        if (_background_manager == null) _background_manager = new BackgroundManager(this);
        return _background_manager;
    }


    private static int CAMERA_WIDTH = 480;
    private static int CAMERA_HEIGHT = 800;

    private static int _screen_width = 0;
    private static int _screen_height = 0;

    private Camera _camera;
    private Scene _scene;

    private float bottom = 0;

    private static Sound _jump;
    private static Music _music;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        hideShare();
    }

    private float _scale_to_main_ui;

    @Override
    public EngineOptions onCreateEngineOptions() {

        CAMERA_HEIGHT = 1280;
        CAMERA_WIDTH = 720;

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        _screen_width = dm.widthPixels;
        _screen_height = dm.heightPixels;

        if (CAMERA_HEIGHT * 5 / 4 >= _screen_height || CAMERA_WIDTH * 5 / 4 >= _screen_width) {
            CAMERA_WIDTH = _screen_width * 2;
            CAMERA_HEIGHT = _screen_height * 2;
            _scale_to_main_ui = 0.5f;
        } else if (_screen_height * 5 / 4 >= CAMERA_HEIGHT || _screen_width * 5 / 4 >= CAMERA_WIDTH) {
            CAMERA_WIDTH = _screen_width * 2 / 3;
            CAMERA_HEIGHT = _screen_height*2 / 3;
            _scale_to_main_ui = 3.f / 2;
        } else {
            CAMERA_WIDTH = _screen_width;
            CAMERA_HEIGHT = _screen_height;
            _scale_to_main_ui = 1.f;
        }

        _camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

        EngineOptions engineoptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), _camera);
        engineoptions.getAudioOptions().setNeedsMusic(true);
        engineoptions.getAudioOptions().setNeedsSound(true);
        return engineoptions;
    }

    private void hideShare(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageButton btn_share = (ImageButton)findViewById(R.id.button_share);
                if(btn_share != null){
                    btn_share.setVisibility(View.GONE);
                }

            }
        });
    }


    private void showShare(final int score, final float y){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ImageButton btn_share = (ImageButton)findViewById(R.id.button_share);
                if(btn_share != null){
                    if(Build.VERSION.SDK_INT >= 11){
                        btn_share.setY(y*_scale_to_main_ui);
                    }else{
                        FrameLayout.LayoutParams params = ((FrameLayout.LayoutParams)btn_share.getLayoutParams());
                        params.topMargin = (int) (y*_scale_to_main_ui);
                        btn_share.requestLayout();
                    }
                    btn_share.setVisibility(View.VISIBLE);
                    btn_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String message = getString(R.string.share).replace("%",score+"");
                            Intent share = new Intent(Intent.ACTION_SEND);
                            share.setType("text/plain");
                            share.putExtra(Intent.EXTRA_TEXT, message);

                            startActivity(Intent.createChooser(share, getString(R.string.share_title)));
                        }
                    });
                }
            }
        });
    }

    private void stopMusic() {
        if (_music != null && _music.isPlaying())
            _music.stop();
    }
    private void pauseMusic() {
        if (_music != null && _music.isPlaying())
            _music.pause();
    }

    private void playMusic() {
        if (_music != null &&!_music.isPlaying()) {
            _music.play();
        }
    }

    private void playJump() {
        if (_jump != null)
            _jump.play();
    }

    /**
     * First game initialization step
     *
     * @param pOnCreateResourcesCallback
     * @throws Exception
     */
    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        getTitleManager().onCreateResources(getTextureManager());

        try {
            if(_music == null || _music.isReleased())
                _music = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this, "music.ogg");
            _music.setLooping(true);
            _jump = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "jump.ogg");
        } catch (final IOException e) {
            _music = null;
            _jump = null;
        }

        getCloudsManager().onCreateResources(getTextureManager());
        getBackgroundManager().onCreateResources(getTextureManager());


        _bitmap_repeat = new BitmapTextureAtlas(getTextureManager(), 128, 32, TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
        _texture_repeat = BitmapTextureAtlasTextureRegionFactory.createFromAsset(_bitmap_repeat, this, "bar_middle_2.png", 0, 0);
        _bitmap_repeat.load();

        _texture_pikachu = new BitmapTextureAtlas(getTextureManager(), 150, 81);
        _tiled_pikachu = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(_texture_pikachu, this, "player.png", 0, 0, 2, 1);
        _texture_pikachu.load();


        _texture_particle = new BitmapTextureAtlas(getTextureManager(), 24, 12);
        _tiled_particle = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(_texture_particle, this, "particles.png", 0, 0, 2, 1);
        _texture_particle.load();

        getScoresManager().onCreateResources(getTextureManager());

        _bitmap_bar_bottom = new BitmapTextureAtlas(getTextureManager(), 132, 117);
        _texture_bar_bottom = BitmapTextureAtlasTextureRegionFactory.createFromAsset(_bitmap_bar_bottom, this, "bar_bottom.png", 0, 0);
        _bitmap_bar_bottom.load();

        _bitmap_bar_top = new BitmapTextureAtlas(getTextureManager(), 132, 63);
        _texture_bar_top = BitmapTextureAtlasTextureRegionFactory.createFromAsset(_bitmap_bar_top, this, "bar_top.png", 0, 0);
        _bitmap_bar_top.load();


        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onPause() {
        mEngine.getMusicManager().onPause();
        pauseMusic();
        super.onPause();

    }

    private static boolean _has_focus;

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus){
        super.onWindowFocusChanged(hasWindowFocus);
        _has_focus = hasWindowFocus;
        if(_has_focus)
            mEngine.getMusicManager().onResume();
    }
    @Override
    public void onResume() {
        super.onResume();
        if(_has_focus)
            mEngine.getMusicManager().onResume();
    }

    /*public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (requestCode == 0x42) {
            playMusic();
        }
    }*/

    /**
     * Scene ok
     *
     * @param pOnCreateSceneCallback
     * @throws Exception
     */
    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {

        _bars = new ArrayList<BarManager>();

        _scene = new Scene();
        _scene.registerUpdateHandler(new FPSLogger());

        final int iStartX = (CAMERA_WIDTH - _texture_pikachu.getWidth()) / 3;
        final int iStartY = (CAMERA_HEIGHT - _texture_pikachu.getHeight() * 2) / 2;


        _pikachu = new Player(iStartX, iStartY, _tiled_pikachu, getVertexBufferObjectManager());
        _pikachu.setScale(1);
        _pikachu.animate(250, true);
        _pikachu.stop();


        for (; getParticles().size() < 20; )
            getParticles().add(new Particle(_pikachu, _tiled_particle, getVertexBufferObjectManager(), CAMERA_WIDTH, CAMERA_HEIGHT));

        if (CAMERA_HEIGHT > 600) {
            bottom = CAMERA_HEIGHT - getBackgroundManager().getGroundHeight();
        } else {
            bottom = CAMERA_HEIGHT - 10;
        }
        bottom = CAMERA_HEIGHT - getBackgroundManager().getGroundHeight();

        getBackgroundManager().setBottom(bottom);
        getBackgroundManager().onCreateSceneBack(_scene, getVertexBufferObjectManager(), CAMERA_WIDTH, CAMERA_HEIGHT);


        getCloudsManager().onCreateScene(_scene, getVertexBufferObjectManager(), this);


        for (int dx = 0; dx < CAMERA_WIDTH; dx += _bar_deltaX) {
            _bars.add(new BarManager(_scene, CAMERA_WIDTH + dx, CAMERA_HEIGHT / 2, 1, bottom, this, getTextureManager(), _pikachu.getHeight(), _texture_repeat, _texture_bar_top, _texture_bar_bottom, getVertexBufferObjectManager()));
        }
        for (int i = 0; i < getParticles().size(); i++)
            _scene.attachChild(getParticles().get(i));

        _scene.attachChild(_pikachu);


        _scene.setOnSceneTouchListener(this);

        _pikachu.setBottom(bottom);

        _scene.setTouchAreaBindingOnActionDownEnabled(true);
        _scene.registerUpdateHandler(this);


        getTitleManager().onCreateScene(_scene, getVertexBufferObjectManager(), CAMERA_WIDTH, CAMERA_HEIGHT);
        getTitleManager().updateTitleSprites(CAMERA_WIDTH, CAMERA_HEIGHT);

        getScoresManager().onCreateScene(_scene, getVertexBufferObjectManager(), CAMERA_WIDTH, CAMERA_HEIGHT, getTitleManager().getTitleBottom());
        getScoresManager().prepareScore();


        getBackgroundManager().onCreateScene(_scene, getVertexBufferObjectManager(), CAMERA_WIDTH, CAMERA_HEIGHT);

        pOnCreateSceneCallback.onCreateSceneFinished(_scene);
    }


    /**
     * Last initialization step
     *
     * @param pScene
     * @param pOnPopulateSceneCallback
     * @throws Exception
     */
    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        playMusic();

        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    /**
     * Manage touch events
     *
     * @param pScene           The {@link Scene} that the {@link TouchEvent} has been dispatched to.
     * @param pSceneTouchEvent The {@link TouchEvent} object containing full information about the event.
     * @return
     */
    @Override
    public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
        switch (pSceneTouchEvent.getAction()) {

            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                if (_pikachu != null) {

                    if (_type != MAIN_GAME && !_pikachu.isPlaying()) {
                        start();
                        _pikachu.onTouch();
                        playJump();
                    } else if (_pikachu.isPlaying()) {
                        _pikachu.onTouch();
                        playJump();
                    }
                    return true;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_CANCEL: {
                break;
            }
        }

        return false;
    }

    /**
     * Call at regular interval
     *
     * @param pSecondsElapsed
     */
    @Override
    public void onUpdate(float pSecondsElapsed) {
        if (_type == MAIN_GAME && _pikachu != null) {
            if (_pikachu.getY() - (_pikachu.getHeight()) < -(2 * _pikachu.getHeight())) {
                _pikachu.setY(-10 - (_pikachu.getHeight()));
            }


            if (_pikachu.isPlaying()) {
                if (_pikachu.collidesWith(getBackgroundManager().getGroundSprite())) {
                    _pikachu.failed();
                    showScore();
                    _type = MAIN_SCORE;
                }
                int score = 0;
                BarManager tmp;
                for (int i = 0; i < _bars.size(); i++) {
                    tmp = _bars.get(i);
                    if (tmp.outOfScreen()) {
                        tmp.setX(_bars.get((i - 1 + _bars.size()) % _bars.size()).getX() + _bar_deltaX);
                        tmp.randomize();
                    } else {
                        //first we check the X collision with one element in order to prevent 4 possible tests
                        //in worst case we have 5 collision tests but most of the time only 1!
                        boolean collide_left_pikachu = _pikachu.getX() < tmp.getBottom().getX() && _pikachu.getX() + _pikachu.getWidth() > tmp.getBottom().getX();
                        boolean collide_right_pikachu = tmp.getBottom().getX() < _pikachu.getX() && tmp.getBottom().getX() + tmp.getBottom().getWidth() > _pikachu.getX();
                        if ((collide_left_pikachu || collide_right_pikachu)
                                &&
                                (_pikachu.collidesWith(tmp.getBottom()) ||
                                        _pikachu.collidesWith(tmp.getTop()) ||
                                        _pikachu.collidesWith(tmp.getMiddleBottom()) ||
                                        _pikachu.collidesWith(tmp.getMiddleTop()))) {
                            _pikachu.failed();
                            showScore();
                        } else if (tmp.getX() < _pikachu.getX()) {
                            score += tmp.getScore();
                        }
                    }
                }

                getScoresManager().updateScore(score);
            }
            if (_pikachu.getY() + _pikachu.getHeight() > bottom + 5) {
                _pikachu.setY(bottom - _pikachu.getHeight());
                _pikachu.stopFall();
            }
        } else if (_type == MAIN_SCORE) {
            if (_pikachu.getX() < -(2 * _pikachu.getWidth())) {
                _pikachu.stop();
                _pikachu.setX(-_pikachu.getWidth() - 2);
            }
            if (_pikachu.getY() + _pikachu.getHeight() > bottom) {
                _pikachu.setY(bottom - _pikachu.getHeight());
                _pikachu.stopFall();
            }

            BarManager tmp;
            for (int i = 0; i < _bars.size(); i++) {
                tmp = _bars.get(i);
                if (tmp.outOfScreen()) {
                    tmp.setX(_bars.get((i - 1 + _bars.size()) % _bars.size()).getX() + _bar_deltaX);
                    tmp.randomize();
                } else if (tmp.getX() > 0 && (_pikachu.collidesWith(tmp.getBottom()) ||
                        _pikachu.collidesWith(tmp.getTop()) ||
                        _pikachu.collidesWith(tmp.getMiddleBottom()) ||
                        _pikachu.collidesWith(tmp.getMiddleTop()))) {
                    _pikachu.failed();
                    _pikachu.setX(_pikachu.getX() - 1);
                    showScore();
                }
            }
        }
    }

    /**
     * Start a new game
     */
    public void start() {
        hideAds();
        hideShare();

        getScoresManager().resetScore();

        getTitleManager().setTitleDisappear();
        _type = MAIN_GAME;
        _pikachu.start();
        _pikachu.setX((CAMERA_WIDTH - _texture_pikachu.getWidth()) / 3);
        _pikachu.setY((CAMERA_HEIGHT - _texture_pikachu.getHeight() * 2) / 2);

        BarManager tmp;
        for (int i = 0; i < _bars.size(); i++) {
            tmp = _bars.get(i);
            tmp.setX(CAMERA_WIDTH + (i * _bar_deltaX));
            tmp.randomize();
            tmp.start();
        }
    }

    /**
     * The game have been lost
     */
    private void showScore() {
        if (_type != MAIN_SCORE) {
            showAds();

            _pikachu.failed();
            _type = MAIN_SCORE;

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    unlockAchievement(getScoresManager().getScore());
                }
            });
            getTitleManager().resetTitle(CAMERA_WIDTH, CAMERA_HEIGHT);
            getScoresManager().updateScoreSetYFinish();
            showShare(getScoresManager().getScore(), getScoresManager().getScoreBottom());

        }
    }

    @Override
    public void reset() {

    }

    @Override
    public void onAchievementUpdated(final int i, final String s) {
    }


    @Override
    public float getHeight() {
        return CAMERA_HEIGHT;
    }

    @Override
    public float getWidth() {
        return CAMERA_WIDTH;
    }

    @Override
    public float get0() {
        return 0;
    }
}
