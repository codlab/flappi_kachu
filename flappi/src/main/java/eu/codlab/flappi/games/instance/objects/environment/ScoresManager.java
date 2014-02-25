package eu.codlab.flappi.games.instance.objects.environment;

import android.content.Context;

import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.ArrayList;

import eu.codlab.flappi.games.instance.objects.descriptor.AbstractManager;

/**
 * Created by kevin on 25/02/14.
 */
public class ScoresManager extends AbstractManager{
    private int _score;
    private float _width;
    private float _height;
    private float _title_bottom;
    private Scene _scene;
    private BitmapTextureAtlas _bitmap_scores;
    private TiledTextureRegion _texture_scores;
    private VertexBufferObjectManager _vertex_object_manager;

    public ScoresManager(Context context){
        super(context);
        _score = 0;
    }
    @Override
    public void onCreateResources(TextureManager textureManager) {
        _bitmap_scores = new BitmapTextureAtlas(textureManager, 280, 44);
        _texture_scores = new BitmapTextureAtlasTextureRegionFactory().createTiledFromAsset(_bitmap_scores, getContext(), "font.png", 0, 0, 10, 1);
        _bitmap_scores.load();
    }

    @Override
    public void onCreateScene(Scene scene, VertexBufferObjectManager vertexManager, int width, int height) throws IllegalAccessException {
        throw new IllegalAccessException("Must use the method with title_bottom");
    }
    public void onCreateScene(Scene scene, VertexBufferObjectManager vertexManager, int width, int height, float title_bottom) {
        _title_bottom = title_bottom;
        _scene = scene;
        _width = width;
        _height = height;
        _title_bottom = title_bottom;
        _vertex_object_manager = vertexManager;
    }




    ArrayList<TiledSprite> _scores;

    private ArrayList<TiledSprite> getScores() {
        if (_scores == null) _scores = new ArrayList<TiledSprite>();
        return _scores;
    }

    public void prepareScore() {
        if (getScores() != null) {
            for (int i = getScores().size() - 1; i >= 0; i--) {
                _scene.detachChild(getScores().get(i));
            }
            getScores().clear();
            _scores = null;
        }
    }

    public void updateScoreSetYStd() {
        for (int i = getScores().size() - 1; i >= 0; i--) {
            getScores().get(i).setY(_height / 6);
        }
    }

    public void updateScoreSetYFinish() {
        for (int i = getScores().size() - 1; i >= 0; i--) {
            getScores().get(i).setY(_title_bottom);
        }
    }

    /**
     * @return the remaining value from add for the next power of 10
     */
    private int updateScore(ArrayList<TiledSprite> scores, int DX, int add) {
        //DX correspond to the 10^DX value
        if (scores.size() <= DX) {
            //add new value
            TiledSprite new_tiled = new TiledSprite(DX * (_texture_scores.getWidth()), 0, _texture_scores, _vertex_object_manager);
            new_tiled.setCurrentTileIndex(9);
            scores.add(new_tiled);
            _scene.attachChild(new_tiled);
            if (28 * 2 * 5 < _width) {
                new_tiled.setScale(2);
            }
            new_tiled.setY(_height / 6);

            //update positions
            int total_width = (int) (scores.size() * new_tiled.getWidthScaled());
            int start_x = (int) ((_width - total_width) / 2);
            int idx;
            for (int i = scores.size() - 1; i >= 0; i--) {
                idx = scores.size() - 1 - i;
                scores.get(i).setX(start_x + idx * new_tiled.getWidthScaled());
            }
        }
        int rest = 0;
        int current_value = 9 - scores.get(DX).getCurrentTileIndex();
        current_value += add;
        while (current_value >= 10) {
            rest += 1;
            current_value -= 10;
        }
        current_value = 9 - current_value;
        scores.get(DX).setCurrentTileIndex(current_value);
        return rest;
    }

    public void updateScore0() {
        ArrayList<TiledSprite> scores = getScores();
        updateScore(scores, 0, 0);
        updateScoreSetYStd();
        _score = 0;
    }

    public void updateScore(int score) {
        _score += score;
        ArrayList<TiledSprite> scores = getScores();
        int DX = 0;
        while (score != 0) {
            score = updateScore(scores, DX, score);
            DX++;
        }
    }

    public int getScore(){
        return _score;
    }

    public void resetScore() {
        prepareScore();
        updateScore0();
    }

}
