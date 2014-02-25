package eu.codlab.flappi.games.instance.engine;

import android.graphics.Bitmap;

import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.source.BaseTextureAtlasSource;

/**
 * Created by kevinleperf on 23/02/2014.
 */
public class BitmapTextureAtlasSource extends BaseTextureAtlasSource implements IBitmapTextureAtlasSource {

    private Bitmap mBitmap;

    public BitmapTextureAtlasSource(Bitmap pBitmap) {
        super(0,0, pBitmap.getWidth(), pBitmap.getHeight());

        this.mBitmap = pBitmap.copy(Bitmap.Config.ARGB_8888, false);
    }

    public int getWidth() {
        return mBitmap.getWidth();
    }

    public int getHeight() {
        return mBitmap.getHeight();
    }

    @Override
    public BitmapTextureAtlasSource clone() {
        return new BitmapTextureAtlasSource(Bitmap.createBitmap(mBitmap));
    }

    @Override
    public IBitmapTextureAtlasSource deepCopy() {
        return null;
    }

    public Bitmap onLoadBitmap(Bitmap.Config pBitmapConfig) {
        return mBitmap;
    }
}