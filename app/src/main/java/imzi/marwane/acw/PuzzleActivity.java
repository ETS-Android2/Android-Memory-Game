package imzi.marwane.acw;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

public class PuzzleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity Lifecycle", "onCreate - PuzzleActivity");
        setContentView(R.layout.activity_puzzle);
        //create fragment
        ListPuzzleFragment content = new ListPuzzleFragment();
        //we can just pass the intent extras straight to the fragment
        content.setArguments(getIntent().getExtras());

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.content, content).commit();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity Lifecycle", "onPause - PuzzleActivity");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Activity Lifecycle", "onStart - PuzzleActivity");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity Lifecycle", "onStop - PuzzleActivity");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Activity Lifecycle", "onDestroy - PuzzleActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Activity Lifecycle", "onRestart - PuzzleActivity");
    }

    protected void onResume() {
        super.onResume();
        Log.i("Activity Lifecycle", "onResume - PuzzleActivity");
    }
}