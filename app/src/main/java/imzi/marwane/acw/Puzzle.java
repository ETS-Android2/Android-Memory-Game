package imzi.marwane.acw;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Puzzle {
    private String name;
    private Bitmap TileBack;
    ArrayList<Bitmap> PictureSet = new ArrayList<>();
    private Game mGame;

    public Puzzle()
    {

    }

    public String getName(){return name;}
    public void setName(String pName){name = pName;}

    public Bitmap getTileBack() { return TileBack;}
    public void setTileBack(Bitmap pTileBack) { TileBack = pTileBack;}

    public Bitmap getPictureSet (int index) { return PictureSet.get(index);}
    public void setPictureSet(Bitmap pImage, int pIndex) {
        if (pIndex == -1) {
            setTileBack(pImage);
        } else {
            PictureSet.add(pImage);
        }
    }

    public Game getGame() {
        return mGame;
    }

    public void setGame() {
        mGame = new Game(this);
    }


    public int pictureSetCount(){
        return PictureSet.size();
    }

    public void pictureSetClear(){
        PictureSet.clear();
    }

}
