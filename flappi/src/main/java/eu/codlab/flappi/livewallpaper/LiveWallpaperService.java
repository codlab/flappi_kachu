package eu.codlab.flappi.livewallpaper;

import android.content.res.Configuration;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.extension.ui.livewallpaper.BaseLiveWallpaperService;
import org.andengine.input.sensor.orientation.OrientationData;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.IGameInterface;

import java.io.IOException;
import java.util.ArrayList;

import eu.codlab.flappi.games.instance.objects.environment.BackgroundManager;
import eu.codlab.flappi.games.instance.objects.environment.BarManager;
import eu.codlab.flappi.games.instance.objects.environment.CloudsManager;
import eu.codlab.flappi.games.instance.objects.environment.ScoresManager;
import eu.codlab.flappi.games.instance.objects.environment.TitleManager;
import eu.codlab.flappi.games.instance.objects.player.Particle;
import eu.codlab.flappi.games.instance.objects.player.Player;
import eu.codlab.flappi.games.instance.scene.IGetWidthHeight;

/**
 * Created by kevinleperf on 05/03/2014.
 */
public class LiveWallpaperService extends BaseLiveWallpaperService implements IUpdateHandler,IGetWidthHeight {
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


    /**
     * Manage the clouds
     */
    private CloudsManager _clouds;

    private CloudsManager getCloudsManager() {
        if (_clouds == null) _clouds = new CloudsManager(this);
        return _clouds;
    }

    /**
     * Manage the background
     */
    private BackgroundManager _background_manager;

    private BackgroundManager getBackgroundManager() {
        if (_background_manager == null) _background_manager = new BackgroundManager(this);
        return _background_manager;
    }

    public static int CAMERA_WIDTH = 480;
    public static int CAMERA_HEIGHT = 800;

    private static int _screen_width = 0;
    private static int _screen_height = 0;

    private Camera _camera;
    private Scene _scene;

    private float bottom = 0;

    private float _scale_to_main_ui;
    private boolean REVERSED;

    @Override
    public EngineOptions onCreateEngineOptions() {

        CAMERA_HEIGHT = 1280;
        CAMERA_WIDTH = 720;

        DisplayMetrics dm = getBaseContext().getResources().getDisplayMetrics();

        _screen_width = dm.widthPixels;
        _screen_height = dm.heightPixels;
        REVERSED = false;

        if(_screen_height < _screen_width){
            CAMERA_HEIGHT = 720;
            CAMERA_WIDTH = 1280;
            REVERSED = true;
        }

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


        ScreenOrientation orientation=ScreenOrientation.PORTRAIT_FIXED;
        EngineOptions engineoptions = null;
        if(REVERSED){
            float height = CAMERA_WIDTH;
            float width = CAMERA_HEIGHT;
            CAMERA_WIDTH = (int) (width);
            CAMERA_HEIGHT = (int) (height);
            _camera = new Camera(0, 0, CAMERA_HEIGHT, CAMERA_WIDTH);
            orientation = ScreenOrientation.LANDSCAPE_FIXED;
            _camera.offsetCenter(-(CAMERA_HEIGHT-CAMERA_WIDTH)/2,(CAMERA_HEIGHT-CAMERA_WIDTH)/3);
            //_camera.setCenter(CAMERA_HEIGHT/2, CAMERA_WIDTH/2);
            //_camera.offsetCenter(CAMERA_HEIGHT/2, CAMERA_WIDTH/2);
            engineoptions = new EngineOptions(true, orientation, new RatioResolutionPolicy(CAMERA_HEIGHT, CAMERA_WIDTH), _camera);
        }else{
            _camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
            engineoptions = new EngineOptions(true, orientation, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), _camera);
        }
        _camera.setResizeOnSurfaceSizeChanged(true);




        return engineoptions;
    }

    @Override
    public void onSurfaceChanged(final GLState pGLState, final int pWidth, final int pHeight) {
        super.onSurfaceChanged(pGLState, pWidth, pHeight);
        _camera.setSurfaceSize(0,0, pWidth, pHeight);

    }

    @Override
    public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
        if(_scene != null){
            _scene.detachChild(_pikachu);
            _pikachu.detachSelf();
            _pikachu.dispose();
            _pikachu = null;
            for(Particle particle : _particles){
                particle.detachChild(_scene);
                particle.dispose();
                particle = null;
            }
            _particles = null;
            _clouds.detach(_scene);
            _clouds = null;
            _background_manager.detach(_scene);
            _background_manager = null;
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

        _bitmap_bar_bottom = new BitmapTextureAtlas(getTextureManager(), 132, 117);
        _texture_bar_bottom = BitmapTextureAtlasTextureRegionFactory.createFromAsset(_bitmap_bar_bottom, this, "bar_bottom.png", 0, 0);
        _bitmap_bar_bottom.load();

        _bitmap_bar_top = new BitmapTextureAtlas(getTextureManager(), 132, 63);
        _texture_bar_top = BitmapTextureAtlasTextureRegionFactory.createFromAsset(_bitmap_bar_top, this, "bar_top.png", 0, 0);
        _bitmap_bar_top.load();


        pOnCreateResourcesCallback.onCreateResourcesFinished();
    }

    @Override
    public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {

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

        if (CAMERA_HEIGHT > 600 && !REVERSED) {
            bottom = CAMERA_HEIGHT - getBackgroundManager().getGroundHeight();
        } else {
            bottom = CAMERA_HEIGHT - 10;
        }
        bottom = CAMERA_HEIGHT - getBackgroundManager().getGroundHeight();

        getBackgroundManager().setBottom(bottom);
        getBackgroundManager().onCreateSceneBack(_scene, getVertexBufferObjectManager(), CAMERA_WIDTH, CAMERA_HEIGHT);

        getCloudsManager().onCreateScene(_scene, getVertexBufferObjectManager(), this);

        for (int i = 0; i < getParticles().size(); i++)
            _scene.attachChild(getParticles().get(i));

        _scene.attachChild(_pikachu);


        //_scene.setOnSceneTouchListener(this);

        _pikachu.setBottom(bottom);

        _scene.setTouchAreaBindingOnActionDownEnabled(true);
        _scene.registerUpdateHandler(this);


        getBackgroundManager().onCreateScene(_scene, getVertexBufferObjectManager(), CAMERA_WIDTH, CAMERA_HEIGHT);

        pOnCreateSceneCallback.onCreateSceneFinished(_scene);
    }

    @Override
    public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
        pOnPopulateSceneCallback.onPopulateSceneFinished();
    }

    @Override
    public void onUpdate(float pSecondsElapsed) {

    }

    @Override
    public void reset() {

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
        if(_camera.getWidth() > _camera.getHeight()){
            return -((CAMERA_HEIGHT - CAMERA_WIDTH)/2);
        }else{
            return 0;
        }
    }
}
