package eu.codlab.flappi.games.instance.objects.environment;

import android.content.Context;

import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.EntityModifierList;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import javax.microedition.khronos.opengles.GL10;

import eu.codlab.flappi.games.instance.objects.descriptor.AbstractManager;
import eu.codlab.flappi.games.instance.scene.IGetWidthHeight;

/**
 * Created by kevin on 25/02/14.
 */
public class TitleManager extends AbstractManager{
    private BitmapTextureAtlas mBitmapTextureAtlas;


    private TextureRegion _title_texture;
    private Sprite _title_sprite;
    private TextureRegion _title_texture2;
    private Sprite _title_sprite2;


    public TitleManager(Context context){
        super(context);
    }
    @Override
    public void onCreateResources(TextureManager textureManager) {
        mBitmapTextureAtlas = new BitmapTextureAtlas(textureManager, 512, 240);
        _title_texture = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, getContext(), "logo1.png", 0, 0);
        _title_texture2 = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mBitmapTextureAtlas, getContext(), "logo2.png", 0, 132);
        mBitmapTextureAtlas.load();
    }

    @Override
    public void onCreateScene(Scene scene, VertexBufferObjectManager vertexManager, int width, int height) {

        _title_sprite = new Sprite(0, 0, _title_texture, vertexManager);
        _title_sprite2 = new Sprite(0, 0, _title_texture2, vertexManager);


        scene.attachChild(_title_sprite);
        scene.attachChild(_title_sprite2);

    }

    @Override
    public void onCreateScene(Scene scene, VertexBufferObjectManager vertexManager, IGetWidthHeight screen) throws IllegalAccessException {

    }


    public void updateTitleSprites(float cwidth, float cheight) {
        int width = (int) (cwidth / 2);
        int sprite_width = 512;
        int sprite_height = 132;
        int height = width * sprite_height / sprite_width;

        int width2 = (int) (cwidth / 3);
        int sprite2_width = 272;
        int sprite2_height = 108;
        int height2 = width2 * sprite2_height / sprite2_width;

        _title_sprite.getTextureRegion().setTextureHeight(height);
        _title_sprite.getTextureRegion().setTextureWidth(width);
        _title_sprite.setWidth(width);
        _title_sprite.setHeight(height);
        _title_sprite2.getTextureRegion().setTextureHeight(height2);
        _title_sprite2.getTextureRegion().setTextureWidth(width2);
        _title_sprite2.setWidth(width2);
        _title_sprite2.setHeight(height2);
        _title_sprite2.setRotation(-40);

        resetTitle(cwidth, cheight);
    }

    public void resetTitle(float cwidth, float cheight) {
        _title_sprite.setX((cwidth - _title_sprite.getWidth()) / 2);
        _title_sprite.setY((cheight - _title_sprite.getHeight()) / 3);
        _title_sprite2.setX((cwidth - _title_sprite2.getWidth()) / 2 + _title_sprite.getWidth() / 2);
        _title_sprite2.setY((cheight - _title_sprite2.getHeight()) / 3 + _title_sprite.getHeight());
        _title_sprite.setAlpha(1.f);
        _title_sprite2.setAlpha(1.f);
    }


    public void setTitleDisappear() {
        _title_sprite.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        _title_sprite2.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        EntityModifierList iem = new EntityModifierList(_title_sprite);
        EntityModifierList iem2 = new EntityModifierList(_title_sprite2);
        AlphaModifier alpha1 = new AlphaModifier(1, 1.f, 0.f);
        alpha1.setAutoUnregisterWhenFinished(true);
        AlphaModifier alpha2 = alpha1.deepCopy();

        int deltaY = (int) (_title_sprite.getY() + (_title_sprite.getHeight() + _title_sprite2.getHeight()));

        MoveYModifier move1 = new MoveYModifier(1, _title_sprite.getY(), _title_sprite.getY() - deltaY);
        move1.setAutoUnregisterWhenFinished(true);
        MoveYModifier move2 = new MoveYModifier(1, _title_sprite2.getY(), _title_sprite2.getY() - deltaY);
        move2.setAutoUnregisterWhenFinished(true);
        iem.add(alpha1);
        iem2.add(alpha2);
        iem.add(move1);
        iem2.add(move2);

        _title_sprite.registerEntityModifier(alpha1);
        _title_sprite.registerEntityModifier(move1);
        _title_sprite2.registerEntityModifier(alpha2);
        _title_sprite2.registerEntityModifier(move2);
    }

    public float getTitleBottom(){
        return _title_sprite2.getY() + 2 * _title_sprite2.getHeight();
    }

}
