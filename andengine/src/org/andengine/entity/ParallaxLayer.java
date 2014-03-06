package org.andengine.entity;

import android.util.Log;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.opengl.util.GLState;

import java.util.ArrayList;

public class ParallaxLayer extends Entity {
    // ===========================================================
    // Constants
    // ===========================================================

    // ===========================================================
    // Fields
    // ===========================================================

    private final ArrayList<ParallaxEntity> mParallaxEntities = new ArrayList<ParallaxEntity>();
    private int mParallaxEntityCount;

    protected float mParallaxValue;
    protected float mParallaxScrollValue;

    protected float mParallaxChangePerSecond;

    protected float mParallaxScrollFactor = 0.2f;

    //private Camera mCamera;

    private float mCameraPreviousX;
    private float mCameraOffsetX;

    private float	mLevelWidth = 0;

    private boolean mIsScrollable = false;


    // ===========================================================
    // Constructors
    // ===========================================================
    public ParallaxLayer() {
    }

    public ParallaxLayer(final boolean mIsScrollable){//final Camera camera,
        //this.mCamera = camera;
        this.mIsScrollable = mIsScrollable;

        mCameraPreviousX = 0;//camera.getCenterX();
    }

    public ParallaxLayer(final boolean mIsScrollable, final int mLevelWidth){//final Camera camera,
        //this.mCamera = camera;
        this.mIsScrollable = mIsScrollable;
        this.mLevelWidth = mLevelWidth;

        mCameraPreviousX = 0;//camera.getCenterX();
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    public void setParallaxValue(final float pParallaxValue) {
        this.mParallaxValue = pParallaxValue;
    }

    public void setParallaxChangePerSecond(final float pParallaxChangePerSecond) {
        this.mParallaxChangePerSecond = pParallaxChangePerSecond;
    }

    public void setParallaxScrollFactor(final float pParallaxScrollFactor){
        this.mParallaxScrollFactor = pParallaxScrollFactor;
    }

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================
    @Override
    public void onManagedDraw(GLState pGLState, Camera pCamera) {
        super.preDraw(pGLState, pCamera);


        final float parallaxValue = this.mParallaxValue;
        final float parallaxScrollValue = this.mParallaxScrollValue;
        final ArrayList<ParallaxEntity> parallaxEntities = this.mParallaxEntities;

        for(int i = 0; i < this.mParallaxEntityCount; i++) {
            if(parallaxEntities.get(i).mIsScrollable){
                parallaxEntities.get(i).onDraw(pGLState, pCamera, parallaxScrollValue, mLevelWidth);
            } else {
                parallaxEntities.get(i).onDraw(pGLState, pCamera, parallaxValue, mLevelWidth);
            }

        }
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {

        /*if(mIsScrollable && mCameraPreviousX != this.mCamera.getCenterX()){
            mCameraOffsetX = mCameraPreviousX - this.mCamera.getCenterX();
            mCameraPreviousX = this.mCamera.getCenterX();

            this.mParallaxScrollValue += mCameraOffsetX * this.mParallaxScrollFactor;
            mCameraOffsetX = 0;
        }*/

        this.mParallaxValue += this.mParallaxChangePerSecond * pSecondsElapsed;
        super.onManagedUpdate(pSecondsElapsed);
    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void attachParallaxEntity(final ParallaxEntity parallaxEntity) {
        this.mParallaxEntities.add(parallaxEntity);
        this.mParallaxEntityCount++;
    }

    public boolean detachParallaxEntity(final ParallaxEntity pParallaxEntity) {
        this.mParallaxEntityCount--;
        final boolean success = this.mParallaxEntities.remove(pParallaxEntity);
        if(!success) {
            this.mParallaxEntityCount++;
        }
        return success;
    }

    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    public static class ParallaxEntity {
        // ===========================================================
        // Constants
        // ===========================================================

        // ===========================================================
        // Fields
        // ===========================================================

        final float mParallaxFactor;
        final IAreaShape mAreaShape;
        final boolean mIsScrollable;

        final float shapeWidthScaled;

        // ===========================================================
        // Constructors
        // ===========================================================

        public ParallaxEntity(final float pParallaxFactor, final IAreaShape pAreaShape) {
            this.mParallaxFactor = pParallaxFactor;
            this.mAreaShape = pAreaShape;
            this.mIsScrollable = false;
            shapeWidthScaled = this.mAreaShape.getWidthScaled();
        }

        public ParallaxEntity(final float pParallaxFactor, final IAreaShape pAreaShape, final boolean mIsScrollable) {
            this.mParallaxFactor = pParallaxFactor;
            this.mAreaShape = pAreaShape;
            this.mIsScrollable = mIsScrollable;
            shapeWidthScaled = this.mAreaShape.getWidthScaled();
        }

        public ParallaxEntity(final float pParallaxFactor, final IAreaShape pAreaShape, final boolean mIsScrollable, final int mReduceFrequency) {
            this.mParallaxFactor = pParallaxFactor;
            this.mAreaShape = pAreaShape;
            this.mIsScrollable = mIsScrollable;
            shapeWidthScaled = this.mAreaShape.getWidthScaled() * mReduceFrequency;
        }

        // ===========================================================
        // Getter & Setter
        // ===========================================================

        // ===========================================================
        // Methods for/from SuperClass/Interfaces
        // ===========================================================

        // ===========================================================
        // Methods
        // ===========================================================

        public void onDraw(final GLState pGLState, final Camera pCamera, final float pParallaxValue, final float mLevelWidth) {
            pGLState.pushModelViewGLMatrix();
            {
                final float cameraWidth = Math.max(pCamera.getHeight(), pCamera.getWidth());




                //----
                float base0 = 0;
                //if(pCamera.getWidth() > pCamera.getHeight()){
                //    base0= -((pCamera.getWidth() - pCamera.getHeight())/2);
                //}


                /*if(mLevelWidth != 0){
                    widthRange = mLevelWidth;
                } else {
                    widthRange = pCamera.getWidth();
                }*/

                float baseOffset = (pParallaxValue * this.mParallaxFactor) % shapeWidthScaled;

                while(baseOffset > base0) {
                    baseOffset -= shapeWidthScaled;
                }

                Log.d("baseOffset", "" + baseOffset);
                pGLState.translateModelViewGLMatrixf(baseOffset-pCamera.getWidth() - pCamera.getHeight(), 0, 0);

                float currentMaxX = baseOffset;
                if(pCamera.getWidth() > pCamera.getHeight()){
                    base0= -((pCamera.getWidth() - pCamera.getHeight())/2);
                }

                currentMaxX -= pCamera.getWidth() + pCamera.getHeight();
                do {
                    this.mAreaShape.onDraw(pGLState, pCamera);
                    pGLState.translateModelViewGLMatrixf(shapeWidthScaled, 0, 0);
                    currentMaxX += shapeWidthScaled;
                } while(currentMaxX < cameraWidth);

            }
            pGLState.popModelViewGLMatrix();
        }

        // ===========================================================
        // Inner and Anonymous Classes
        // ===========================================================
    }


}