package imzi.marwane.acw;

import android.graphics.Bitmap;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Collections;

public class Game {
    private String mPuzzleName;
    private boolean mGameWon;
    private int mFoundTiles;
    private MutableLiveData<Game> mGameMutableLiveData;
    ArrayList<Tile> mFirstTileList;
    ArrayList<Tile> mSecondTileList;

    public Game(Puzzle pPuzzle){
        mFirstTileList = new ArrayList<>();
        mSecondTileList = new ArrayList<>();
        mGameWon = false;
        mFoundTiles = 0;
        mGameMutableLiveData = new MutableLiveData<>();
        setPuzzleName(pPuzzle);
        generateTile(pPuzzle);
        mGameMutableLiveData.setValue(this);
    }

    private void generateTile(Puzzle pPuzzle){
        mFirstTileList = new ArrayList<>();
        Bitmap tileBack = pPuzzle.getTileBack();
        ArrayList<Bitmap> tileImageArrayList = pPuzzle.PictureSet;
        for (int i = 0; i < tileImageArrayList.size(); i++){
            Tile fTile = new Tile(tileBack, tileImageArrayList.get(i), i);
            Tile sTile = new Tile(tileBack, tileImageArrayList.get(i), i);
            mFirstTileList.add(fTile);
            mFirstTileList.add(sTile);
        }
        Collections.shuffle(mFirstTileList);
        mGameMutableLiveData.setValue(this);
    }

    public String getPuzzleName() {
        return mPuzzleName;
    }

    public void setPuzzleName(Puzzle pPuzzle) {
        mPuzzleName = pPuzzle.getName();
    }

    public ArrayList<Tile> getFirstTileList() {
        return mFirstTileList;
    }
    public ArrayList<Tile> getSecondTileList() {
        return mSecondTileList;
    }

    public void setSecondTileList(Tile pTile) {
        mSecondTileList.add(pTile);
        mGameMutableLiveData.setValue(this);
    }

    public void resetSecondTileList(){
        mSecondTileList.clear();
        mGameMutableLiveData.setValue(this);
    }

    public LiveData<Game> getGameLiveData() {
        return mGameMutableLiveData;
    }

    public boolean isGameWon() {
        return mGameWon;
    }

    public void setGameWon(boolean pGameWon) {
        mGameWon = pGameWon;
        mGameMutableLiveData.setValue(this);
    }

    public int getFoundTiles() {
        return mFoundTiles;
    }

    public void setFoundTiles() {
        mFoundTiles++;
        mGameMutableLiveData.setValue(this);
    }

    public LiveData<Tile> getTileIndex(int pIndex){
        return mFirstTileList.get(pIndex).getTileData();
    }
}
