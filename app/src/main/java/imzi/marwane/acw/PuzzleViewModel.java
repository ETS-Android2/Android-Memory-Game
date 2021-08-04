package imzi.marwane.acw;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

public class PuzzleViewModel extends AndroidViewModel {
    private LiveData<ArrayList<Puzzle>> mPuzzles;
    private static LiveData<Puzzle> mSelectedPuzzles;
    private PuzzleRepository mPuzzlesRepository;
    private static int mSelectedIndex;

    public  PuzzleViewModel (@NonNull Application pApplication) {
        super(pApplication);
        mPuzzlesRepository = PuzzleRepository.getInstance(getApplication());
        getPuzzles();
    }

    public LiveData<ArrayList<Puzzle>> getPuzzles() {
        if (mPuzzles == null){
            mPuzzles = mPuzzlesRepository.getPuzzles();
        }
        return mPuzzles;
    }

    public LiveData<Puzzle> getPuzzle(int pPuzzleIndex) {
        return mPuzzlesRepository.getPuzzle(pPuzzleIndex);
    }

    public void selectPuzzle(int pIndex){
        if (pIndex != mSelectedIndex || mSelectedPuzzles == null){
            mSelectedIndex = pIndex;
            mSelectedPuzzles = getPuzzle(mSelectedIndex);
        }
    }

    public int getmSelectedIndex(){
        return mSelectedIndex;
    }

    public LiveData<Puzzle> getSelectedPuzzle() {
        selectPuzzle(mSelectedIndex);
        return mSelectedPuzzles;
    }
}
