package imzi.marwane.acw;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class TileViewModel extends AndroidViewModel {
    private LiveData<Puzzle> mPuzzleSelected;
    private PuzzleRepository mPuzzleRepository;
    LiveData<Game> mGameLiveData;

    public TileViewModel(@NonNull Application pApplication) {
        super(pApplication);
        mPuzzleRepository = PuzzleRepository.getInstance(getApplication());
        mPuzzleSelected = mPuzzleRepository.getSelectPuzzle();
        mPuzzleSelected.getValue().setGame();
        mGameLiveData = mPuzzleSelected.getValue().getGame().getGameLiveData();
    }

    public LiveData<Game> getGameLiveData() {
        return mGameLiveData;
    }

    public LiveData<Game> gameLiveData() {
        return mGameLiveData.getValue().getGameLiveData();
    }

    public void flipTile(int pIndex) {
        getTileIndex(pIndex).getValue().flip();
    }

    public LiveData<Game> getNewGameLiveData() {
        mPuzzleSelected.getValue().setGame();
        mGameLiveData = mPuzzleSelected.getValue().getGame().getGameLiveData();
        return mGameLiveData;
    }

    public LiveData<Tile> getTileIndex(int pIndex){
        return mGameLiveData.getValue().getTileIndex(pIndex);
    }
}
