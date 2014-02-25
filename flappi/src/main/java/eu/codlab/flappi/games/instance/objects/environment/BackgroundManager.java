package eu.codlab.flappi.games.instance.objects.environment;

import android.content.Context;

import org.andengine.entity.ParallaxLayer;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.codlab.flappi.games.instance.objects.descriptor.AbstractManager;

/**
 * Created by kevin on 25/02/14.
 */
public class BackgroundManager extends AbstractManager{
    private float _bottom;

    private Sprite _ground_sprite;
    private BitmapTextureAtlas _bitmap_ground;
    private TextureRegion _texture_ground;
    private ParallaxLayer.ParallaxEntity _ground;

    private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
    private TextureRegion mParallaxLayerBack;
    private TextureRegion mParallaxLayerMid;
    private TextureRegion mParallaxLayerFront;
    private TextureRegion mParallaxLayerBushes;
    private TextureRegion mParallaxLayerGround;

    public BackgroundManager(Context context){
        super(context);
    }

    @Override
    public void onCreateResources(TextureManager textureManager) {

        this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(textureManager, 768, 2048);
        this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, getContext(), "parallax3.png", 0, 0);
        this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, getContext(), "parallax1.png", 0, 180);
        this.mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, getContext(), "parallax2.png", 0, 544);
        this.mParallaxLayerGround = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, getContext(), "ground.png", 0, 908);
        this.mParallaxLayerBushes = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, getContext(), "bushes.png", 0, 1180);

        mAutoParallaxBackgroundTexture.load();

        _bitmap_ground = new BitmapTextureAtlas(textureManager, 768, 272);
        _texture_ground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(_bitmap_ground, getContext(), "ground.png", 0, 0);
    }

    public float getBottom(){
        return _bottom;
    }
    public void setBottom(float bottom){
        _bottom = bottom;
    }

    public float getGroundHeight(){
        return this.mParallaxLayerGround.getHeight();
    }
    public Sprite getGroundSprite(){
        return _ground_sprite;
    }

    @Override
    public void onCreateScene(Scene scene, VertexBufferObjectManager vertexManager, int width, int height) {

        final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(196f / 255f, 233f / 255f, 1f, 8);
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-5.0f, new Sprite(0, _bottom - this.mParallaxLayerBack.getHeight() - (this.mParallaxLayerBushes.getHeight() / 2) - (this.mParallaxLayerMid.getHeight() / 2), this.mParallaxLayerBack, vertexManager)));
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-20.0f, new Sprite(0, _bottom - this.mParallaxLayerMid.getHeight() - (this.mParallaxLayerBushes.getHeight() / 2), this.mParallaxLayerMid, vertexManager)));
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-30.0f, new Sprite(0, _bottom - this.mParallaxLayerFront.getHeight() - (this.mParallaxLayerBushes.getHeight() / 2), this.mParallaxLayerFront, vertexManager)));
        autoParallaxBackground.attachParallaxEntity(new ParallaxBackground.ParallaxEntity(-50.0f, new Sprite(0, _bottom - this.mParallaxLayerBushes.getHeight(), this.mParallaxLayerBushes, vertexManager)));
        scene.setBackground(autoParallaxBackground);

        final ParallaxLayer parallaxLayer = new ParallaxLayer(true, 4000);
        _ground_sprite = new Sprite(0, _bottom, _texture_ground, vertexManager);//, CAMERA_WIDTH, CAMERA_HEIGHT - bottom,
        _ground = new ParallaxLayer.ParallaxEntity(-50.0f, new Sprite(0, _bottom, this.mParallaxLayerGround, vertexManager));
        parallaxLayer.attachParallaxEntity(_ground);
        parallaxLayer.setParallaxChangePerSecond(8);
        scene.attachChild(parallaxLayer);
    }
}
