package imzi.marwane.acw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity Lifecycle", "onCreate - MainActivity");
        setContentView(R.layout.activity_main);
        PuzzleRepository.getInstance(this.getApplicationContext()).getPuzzles();
        PuzzleRepository.getInstance(this.getApplicationContext()).getPuzzle(0);
        AcceptSSLCerts.accept();
    }

    public void openChoiceActivity(View view){
        Intent openChoiceActivityIntent = new Intent(getApplicationContext(), PuzzleSelectorActivity.class);
        startActivity(openChoiceActivityIntent);
    }

    public void openGame(View view){
        Intent openChoiceActivityIntent = new Intent(getApplicationContext(), GameChoiceActivity.class);
        startActivity(openChoiceActivityIntent);
    }

    public void setActivityBackgroundColor(int color) {
        View view = this.getWindow().getDecorView();
        view.setBackgroundColor(color);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity Lifecycle", "onPause - MainActivity");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Activity Lifecycle", "onStart - MainActivity");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity Lifecycle", "onStop - MainActivity");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Activity Lifecycle", "onDestroy - MainActivity");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Activity Lifecycle", "onRestart - MainActivity");
    }

    protected void onResume() {
        super.onResume();
        Log.i("Activity Lifecycle", "onResume - MainActivity");
    }
}