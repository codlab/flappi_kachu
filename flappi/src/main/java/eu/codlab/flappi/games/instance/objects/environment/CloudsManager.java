package eu.codlab.flappi.games.instance.objects.environment;

import android.content.Context;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.codlab.flappi.games.instance.objects.descriptor.AbstractManager;
import eu.codlab.flappi.games.instance.scene.IGetWidthHeight;

/**
 * Created by kevin on 25/02/14.
 */
public class CloudsManager extends AbstractManager{
    private BitmapTextureAtlas _texture_clouds;
    private TiledTextureRegion _tiled_cloud;
    private Cloud _cloud1;
    private Cloud _cloud2;
    private Cloud _cloud3;
    private Cloud _cloud4;
    private Cloud _cloud5;
    private Cloud _cloud6;
    private Cloud _cloud7;
    private Cloud _cloud8;
    private Cloud _cloud9;
    private Cloud _cloud10;
    private Cloud _cloud11;

    public CloudsManager(Context context){
        super(context);
    }
    @Override
    public void onCreateResources(TextureManager textureManager) {

        _texture_clouds = new BitmapTextureAtlas(textureManager, 560, 96);
        _tiled_cloud = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(_texture_clouds, getContext(), "clouds.png", 0, 0, 4, 1);
        _texture_clouds.load();
    }

    @Override
    public void onCreateScene(Scene scene, VertexBufferObjectManager vertexManager, int width, int height) {
    }

    @Override
    public void onCreateScene(Scene scene, VertexBufferObjectManager vertexManager, IGetWidthHeight screenInterface) {

        _cloud1 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);
        _cloud2 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);
        _cloud3 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);
        _cloud4 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);
        _cloud5 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);
        _cloud6 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);
        _cloud7 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);
        _cloud8 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);
        _cloud9 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);
        _cloud10 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);
        _cloud11 = new Cloud(0, 0, _tiled_cloud, vertexManager, screenInterface);


        scene.attachChild(_cloud1);
        scene.attachChild(_cloud2);
        scene.attachChild(_cloud3);
        scene.attachChild(_cloud4);
        scene.attachChild(_cloud5);
        scene.attachChild(_cloud6);
        scene.attachChild(_cloud7);
        scene.attachChild(_cloud8);
        scene.attachChild(_cloud9);
        scene.attachChild(_cloud10);
        scene.attachChild(_cloud11);
    }

    public void detach(Scene scene){
        scene.detachChild(_cloud1);
        scene.detachChild(_cloud2);
        scene.detachChild(_cloud3);
        scene.detachChild(_cloud4);
        scene.detachChild(_cloud5);
        scene.detachChild(_cloud6);
        scene.detachChild(_cloud7);
        scene.detachChild(_cloud8);
        scene.detachChild(_cloud9);
        scene.detachChild(_cloud10);
        scene.detachChild(_cloud11);
        _cloud1.dispose();
        _cloud2.dispose();
        _cloud3.dispose();
        _cloud4.dispose();
        _cloud5.dispose();
        _cloud6.dispose();
        _cloud7.dispose();
        _cloud8.dispose();
        _cloud9.dispose();
        _cloud10.dispose();
        _cloud11.dispose();
        _cloud1 = null;
        _cloud2 = null;
        _cloud3 = null;
        _cloud4 = null;
        _cloud5 = null;
        _cloud6 = null;
        _cloud7 = null;
        _cloud8 = null;
        _cloud9 = null;
        _cloud10 = null;
        _cloud11 = null;
    }
}
