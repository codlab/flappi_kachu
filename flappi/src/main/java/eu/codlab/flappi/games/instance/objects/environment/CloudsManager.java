package eu.codlab.flappi.games.instance.objects.environment;

import android.content.Context;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.codlab.flappi.games.instance.objects.descriptor.AbstractManager;

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

        _cloud1 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);
        _cloud2 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);
        _cloud3 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);
        _cloud4 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);
        _cloud5 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);
        _cloud6 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);
        _cloud7 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);
        _cloud8 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);
        _cloud9 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);
        _cloud10 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);
        _cloud11 = new Cloud(0, 0, _tiled_cloud, vertexManager, width, height);


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
}
