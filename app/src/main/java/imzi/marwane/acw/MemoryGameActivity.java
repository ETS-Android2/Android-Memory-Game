package imzi.marwane.acw;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

public class MemoryGameActivity extends AppCompatActivity {
    private Handler mHandler;

    ImageView mCurrentView = null;
    private int mPairCount = 20;
    final int[] mDrawable = new int[]{R.drawable.goldfish1_94, R.drawable.goldfish2_94, R.drawable.goldfish3_94, R.drawable.goldfish4_94,
            R.drawable.goldfish5_94, R.drawable.goldfish6_94,R.drawable.goldfish7_94,R.drawable.goldfish8_94,
            R.drawable.goldfish9_94,R.drawable.goldfish10_94,R.drawable.goldfish11_94,R.drawable.goldfish12_94,
            R.drawable.goldfish13_94,R.drawable.goldfish14_94,R.drawable.goldfish15_94,R.drawable.goldfish16_94,
            R.drawable.goldfish17_94,R.drawable.goldfish18_94,R.drawable.goldfish19_94,R.drawable.goldfish20_94};

    int[] mPosition = {0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19};
    int mCurrentPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_game);
        Log.i("Activity Lifecycle", "onCreate");

        GridView gridView = (GridView)findViewById(R.id.gridView);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(mCurrentPosition < 0){
                    mCurrentPosition = position;
                    mCurrentView = (ImageView)view;
                    ((ImageView)view).setImageResource(mDrawable[mPosition[position]]);
                }
                else
                {
                    if(position > 19){
                        position = position - 20;
                    }
                    if(mCurrentPosition == position){
                        ((ImageView)view).setImageResource(R.drawable.tileback_94);
                        ((ImageView)view).setImageResource(mDrawable[mPosition[position]]);
                        Toast.makeText(getApplicationContext(), "Match!", Toast.LENGTH_SHORT).show();
                    }
                    else if(mPosition[mCurrentPosition] != mPosition[position]){
                        mCurrentView.setImageResource(R.drawable.tileback_94);
                        Toast.makeText(getApplicationContext(), "No Match", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        ((ImageView)view).setImageResource(mDrawable[mPosition[position]]);
                        mPairCount--;
                        if(mPairCount == 0){
                            Toast.makeText(getApplicationContext(), "You Have Won!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mCurrentPosition = -1;
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("Activity Lifecycle", "onStart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("Activity Lifecycle", "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("Activity Lifecycle", "onResume");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("Activity Lifecycle", "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("Activity Lifecycle", "onDestroy");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("Activity Lifecycle", "onRestart");
    }
}