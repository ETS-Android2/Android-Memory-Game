package imzi.marwane.acw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class GameChoiceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_choice);
    }

    public void openTimeHighScore(View view){
        Intent openGoldfishGameIntent = new Intent(getApplicationContext(), HighScore.class);
        startActivity(openGoldfishGameIntent);
    }
    public void openTurnsHighscore(View view){
        Intent openGoldfishGameIntent = new Intent(getApplicationContext(), HighScoreNumTurns.class);
        startActivity(openGoldfishGameIntent);
    }
    public void openLongestSequence(View view){
        Intent openGoldfishGameIntent = new Intent(getApplicationContext(), HighScoreLongestSeq.class);
        startActivity(openGoldfishGameIntent);
    }
}