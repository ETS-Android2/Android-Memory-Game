package imzi.marwane.acw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    TileViewModel mTileViewModel;
    private LiveData<Game> mGameLiveData;
    ArrayList<Tile> mTileArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity Lifecycle", "onCreate - GameActivity");
        setContentView(R.layout.activity_game);
        mTileViewModel = new ViewModelProvider(this).get(TileViewModel.class);
        mGameLiveData = mTileViewModel.getNewGameLiveData();
        tileFragment();
    }

    private void tileFragment(){
        mTileArrayList = new ArrayList<>();
        mTileArrayList = mGameLiveData.getValue().getFirstTileList();
        for (int i = 0; i < mTileArrayList.size(); i++){
            TileFragment content = TileFragment.newInstance(i);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.GridLayout, content).commit();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity Lifecycle", "onPause - GameActivity");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Activity Lifecycle", "onStart - GameActivity");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity Lifecycle", "onStop - GameActivity");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Activity Lifecycle", "onDestroy - GameActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Activity Lifecycle", "onRestart - GameActivity");
    }

    protected void onResume() {
        super.onResume();
        Log.i("Activity Lifecycle", "onResume - GameActivity");
    }

}