package eu.codlab.flappi.games.instance.objects.descriptor;

import android.content.Context;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import eu.codlab.flappi.games.instance.scene.IGetWidthHeight;

/**
 * Created by kevin on 25/02/14.
 */
public abstract class AbstractManager {
    private Context _context;
    protected Context getContext(){
        return _context;
    }

    protected AbstractManager(){

    }

    protected AbstractManager(Context context){
        _context = context;
    }

    public abstract void onCreateResources(TextureManager textureManager);
    public abstract void onCreateScene(Scene scene, VertexBufferObjectManager vertexManager, int width, int height) throws IllegalAccessException;
    public abstract void onCreateScene(Scene scene, VertexBufferObjectManager vertexManager, IGetWidthHeight screen) throws IllegalAccessException;
}