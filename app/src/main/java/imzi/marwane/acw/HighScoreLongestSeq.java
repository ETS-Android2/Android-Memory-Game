package imzi.marwane.acw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class HighScoreLongestSeq extends AppCompatActivity {
    private String mName = "";
    private String mScore ="";
    private int mNameIndex =0;
    private int mScoreIndex =0;
    private boolean mSwapped = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Activity Lifecycle", "onCreate - HighScoreLongestSequence");
        setContentView(R.layout.activity_high_score);
        ArrayList<String> playerScores = new ArrayList<String>();
        String receiveString ="";
        try {
            InputStream inputStream = getApplicationContext().openFileInput("HighScoreLongestSequence.txt");
            InputStreamReader streamReader = new InputStreamReader(inputStream) ;
            BufferedReader bufferedReader = new BufferedReader(streamReader);

            while((receiveString = bufferedReader.readLine())!=null){playerScores.add(receiveString); }

            inputStream.close();

            for (int i=0; i< playerScores.size();i++)
            {
                int playerScoreContainer = Integer.parseInt(playerScores.get(i+1));
                for (int x=i; x < playerScores.size()-3; x++)
                {
                    int secondPlayerScoreContainer = Integer.parseInt(playerScores.get(x+3));
                    if(playerScoreContainer > secondPlayerScoreContainer)
                    {
                        playerScoreContainer=Integer.parseInt(playerScores.get(x+3));
                        mNameIndex = x+2;
                        mScoreIndex =x+3;
                        mName = playerScores.get(x+2);
                        mScore =playerScores.get(x+3);
                        mSwapped =true;
                    }
                    x++;
                }
                if(mSwapped == true) {
                    playerScores.set(mNameIndex, playerScores.get(i));
                    playerScores.set(mScoreIndex, playerScores.get(i + 1));
                    playerScores.set(i, mName);
                    playerScores.set(i + 1, mScore);
                }
                i++;
                mSwapped = false;
            }

            try {
                LinearLayout linearLayout = new LinearLayout(this);
                setContentView(linearLayout);
                linearLayout.setOrientation(linearLayout.VERTICAL);
                Resources res = getResources();
                for (int i = 0; i  < playerScores.size()-1; i++)
                {
                    if(i == 0)
                    {
                        TextView textView = new TextView(this);
                        textView.setText(String.format(res.getString(R.string.highscore_longest_sequence))+"\n");
                        linearLayout.addView(textView);
                        textView.setTextSize(30);
                        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                        textView.setTextColor(Color.BLACK);
                    }
                    TextView textView = new TextView(this);
                    textView.setText(playerScores.get(i)+":    "+playerScores.get(i+1)+" Turns");
                    linearLayout.addView(textView);
                    textView.setTextSize(30);
                    textView.setGravity(Gravity.LEFT);
                    textView.setPadding(20,0,0,0);
                    textView.setTextColor(Color.BLACK);
                    i++;
                }
            }catch (Exception r) { r.printStackTrace(); }

        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (IOException e) { e.printStackTrace(); }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity Lifecycle", "onPause - HighScoreLongestSequence");
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Activity Lifecycle", "onStart - HighScoreLongestSequence");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity Lifecycle", "onStop - HighScoreLongestSequence");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Activity Lifecycle", "onDestroy - HighScoreLongestSequence");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Activity Lifecycle", "onRestart - HighScoreLongestSequence");
    }

    protected void onResume() {
        super.onResume();
        Log.i("Activity Lifecycle", "onResume - HighScoreLongestSequence");
    }
}