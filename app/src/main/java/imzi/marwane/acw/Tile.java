package imzi.marwane.acw;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Tile {
    MutableLiveData<Tile> mTileData;
    boolean mInvisible;
    boolean mFound;
    private int mIdOfImage;
    private Bitmap mFrontImage;
    private Bitmap mBackImage;


    public Tile() {
        mTileData = new MutableLiveData<>();
        mTileData.setValue(this);
        mInvisible = false;
    }

    public Tile(Bitmap pBackImage, Bitmap pFrontImage, int pId) {
        setFrontImage(pFrontImage);
        setBackImage(pBackImage);
        setIdOfImage(pId);
        mTileData = new MutableLiveData<>();
        mTileData.setValue(this);
        mFound = false;
        mInvisible = false;
    }

    public boolean getInvisible() {
        return mInvisible;
    }
    public void flip() {
        mInvisible = !mInvisible;
        mTileData.setValue(this);
    }
    public void setInvisible(boolean pNotFound) {
        mInvisible = pNotFound;
    }


    public int getIdOfImage() {
        return mIdOfImage;
    }

    public void setIdOfImage(int mIdOfImage) {
        this.mIdOfImage = mIdOfImage;
    }

    public Bitmap getFrontImage() {
        return mFrontImage;
    }

    public void setFrontImage(Bitmap mFrontImage) { this.mFrontImage = mFrontImage; }


    public Bitmap getBackImage() {
        return mBackImage;
    }

    public void setBackImage(Bitmap mBackImage) {
        this.mBackImage = mBackImage;
    }


    public boolean isFound() {
        return mFound;
    }

    public void setFound(boolean mFound) {
        this.mFound = mFound;
        mTileData.setValue(this);
    }

    public LiveData<Tile> getTileData() {
        return mTileData;
    }

}
