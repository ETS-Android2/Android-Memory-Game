package imzi.marwane.acw;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.view.GestureDetectorCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TileFragment extends Fragment implements GestureDetector.OnGestureListener {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_INDEX = "index";
    private static final String TAG = "Game Activity";
    static int mNumFlips;
    private int mIndex;
    private TileViewModel mViewModel;
    private View mInflatedView;

    private Animator mFlipInAnimator;
    private Animator mFlipOutAnimator;
    private Animation mSpinAnimator;
    private int mAnimationCompleteCount;
    private GestureDetectorCompat mDetector;

    private ImageView mFrontImageView;
    private ImageView mBackImageView;

    String userName ="";
    String time ="";
    String puzzleName = "";
    String start_Date = "";
    String end_Date = "";

    int counter_Time = 0;

    Date start_Time;
    Date end_Time;

    static int mTurns;
    static int mLongestSequence = 0;
    static int mCurrentLongestSequence;

    private Random random = new Random();

    public TileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param index Parameter 1 is the index of the content data we want to display.
     * @return A new instance of fragment TileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TileFragment newInstance(int index) {
        TileFragment fragment = new TileFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getTime();
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_INDEX);
        }
        mViewModel = new ViewModelProvider(getActivity()).get(TileViewModel.class);
        mFlipInAnimator = AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_vertically_top_in);
        mFlipOutAnimator = AnimatorInflater.loadAnimator(getActivity(), R.animator.flip_vertically_top_out);
        mSpinAnimator = AnimationUtils.loadAnimation(getActivity(), R.anim.spin_animation);
        mDetector = new GestureDetectorCompat(getContext(),this);
        addAnimationListeners();
        mTurns = 0;
        mLongestSequence = 0;
        mCurrentLongestSequence = 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mInflatedView = inflater.inflate(R.layout.fragment_tile, container, false);

        FrameLayout TileFrame = mInflatedView.findViewById(R.id.container);

        mFrontImageView = mInflatedView.findViewById(R.id.front);
        mBackImageView = mInflatedView.findViewById(R.id.back);
        mFrontImageView.setImageBitmap(mViewModel.gameLiveData().getValue().getFirstTileList().get(mIndex).getFrontImage());
        mBackImageView.setImageBitmap(mViewModel.gameLiveData().getValue().getFirstTileList().get(mIndex).getBackImage());
        mFrontImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mFrontImageView.setLayoutParams(new FrameLayout.LayoutParams(220, 198));
        mBackImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        mBackImageView.setLayoutParams(new FrameLayout.LayoutParams(220, 198));



        View.OnTouchListener touchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // pass the events to the gesture detector
                // a return value of true means the detector is handling it
                // false means the detector didn't recognise the event
                return mDetector.onTouchEvent(event);
            }
        };
        TileFrame.setOnTouchListener(touchListener);
        final Observer<Tile> TileObserver = new Observer<Tile>() {
            @Override
            public void onChanged(@Nullable final Tile tile) {
                String result = "Heads!";
                Log.i(this.getClass().getSimpleName() + "Observer", "Generating Tile: " + mIndex);
                ImageView backImageView = mInflatedView.findViewById(R.id.back);
                ImageView frontImageView = mInflatedView.findViewById(R.id.front);

                if (tile.isFound()) {
                    frontImageView.startAnimation(mSpinAnimator);
                    backImageView.startAnimation(mSpinAnimator);
                }
                //else if(!tile.isFound())
                else{
                    // check to see the game state.
                    if (!tile.getInvisible()) {
                        mFlipInAnimator.setTarget(backImageView);
                        mFlipOutAnimator.setTarget(frontImageView);
                    } else {
                        mFlipInAnimator.setTarget(frontImageView);
                        mFlipOutAnimator.setTarget(backImageView);
                    }
                    mFlipInAnimator.start();
                    mFlipOutAnimator.start();
                }

            }
        };
        mViewModel.getTileIndex(mIndex).observe(getViewLifecycleOwner(), TileObserver);

        return mInflatedView;
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void animationComplete() {
        if(mNumFlips == 2) {
            mTurns++;
            ArrayList<Tile> tiles = new ArrayList<>();

            tiles = mViewModel.mGameLiveData.getValue().getSecondTileList();
            Tile tile1 = tiles.get(0);
            Tile tile2 = tiles.get(1);

            if (tile1.getIdOfImage() != tile2.getIdOfImage()) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        tile1.setInvisible(false);
                        tile2.setInvisible(false);
                        tile1.setFound(false);
                        tile2.setFound(false);
                        mViewModel.mGameLiveData.getValue().resetSecondTileList();
                    }
                }, 500);
                mNumFlips = 0;
                if(mCurrentLongestSequence > mLongestSequence){
                    mLongestSequence = mCurrentLongestSequence;
                    mCurrentLongestSequence = 0;
                } else{
                    mCurrentLongestSequence = 0;
                }
            }
            if (tile1.getIdOfImage() == tile2.getIdOfImage()){
                tile1.setInvisible(true);
                tile2.setInvisible(true);
                tile1.setFound(true);
                tile2.setFound(true);
                mViewModel.mGameLiveData.getValue().setFoundTiles();
                mViewModel.mGameLiveData.getValue().resetSecondTileList();
                mNumFlips = 0;
                mCurrentLongestSequence++;
            }
            if(mViewModel.mGameLiveData.getValue().getFoundTiles() == 20){
                mViewModel.mGameLiveData.getValue().setGameWon(true);
                youWin();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean youWin(){
        getTime();
        boolean youWin = mViewModel.getGameLiveData().getValue().isGameWon();

        if (mViewModel.getGameLiveData().getValue().isGameWon()) {
            Log.i(TAG, "You Win!");
//                    JOptionPane.showInputDialog();
            Resources res = getResources();
            //String temp = String.valueOf(mTurns);
            //String tempTwo = String.valueOf(mLongestSequence);

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            TextView textView = new TextView(getContext());
            textView.setText(String.format(res.getString(R.string.you_win_part), time, mTurns, mLongestSequence));
            textView.setHeight(200);
            builder.setCustomTitle(textView);
            //builder.setTitle(String.format(res.getString(R.string.you_win_part), time, mTurns, mLongestSequence));


            View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.fragment_win_window,(ViewGroup) getView(), false);
            final EditText nameView = (EditText) viewInflated.findViewById(R.id.usernameView);
            builder.setView(viewInflated);

            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    userName = nameView.getText().toString();
                    try
                    {
                        OutputStreamWriter outputStreamWriter = new OutputStreamWriter(getContext().openFileOutput("HighScore.txt", Context.MODE_APPEND));
                        outputStreamWriter.write(userName+"\n"+time+"\n");
                        outputStreamWriter.flush();
                        outputStreamWriter.close();

                        OutputStreamWriter outputStreamWriterTwo = new OutputStreamWriter(getContext().openFileOutput("HighScoreNumTurns.txt", Context.MODE_APPEND));
                        outputStreamWriterTwo.write(userName+"\n"+mTurns+"\n");
                        outputStreamWriterTwo.flush();
                        outputStreamWriterTwo.close();

                        OutputStreamWriter outputStreamWriterThree = new OutputStreamWriter(getContext().openFileOutput("HighScoreLongestSequence.txt", Context.MODE_APPEND));
                        outputStreamWriterThree.write(userName+"\n"+mLongestSequence+"\n");
                        outputStreamWriterThree.flush();
                        outputStreamWriterThree.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Intent main = new Intent(getActivity(), MainActivity.class);
                    main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(main);

                    dialog.dismiss();
                }
            });
            builder.show();
        }
        return youWin;
    }
    private void addAnimationListeners() {
        mFlipInAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationCompleteCount++;
                animationComplete();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mFlipOutAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onAnimationEnd(Animator animation) {
                mAnimationCompleteCount++;
                animationComplete();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @Override
    public boolean onDown(MotionEvent e) {

        if(mViewModel.getTileIndex(mIndex).getValue().getInvisible())
        {
            return false;
        }
        else {
            mNumFlips++;
            mViewModel.mGameLiveData.getValue().setSecondTileList(mViewModel.getTileIndex(mIndex).getValue());
            mViewModel.flipTile(mIndex);
            return true;
        }
    }


    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getTime(){
        counter_Time++;
        if (counter_Time == 1){
            start_Time = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("hh.mm.ss");
            start_Date = formatter.format(start_Time);
        }
        if (counter_Time == 2){
            end_Time = Calendar.getInstance().getTime();
            SimpleDateFormat formatter = new SimpleDateFormat("hh.mm.ss");
            end_Date = formatter.format(end_Time);
            try {
                Date startDate = formatter.parse(start_Date);
                Date endDate = formatter.parse(end_Date);
                long temp = (endDate.getTime() - startDate.getTime()) / 1000;
                time +=temp;
            }
            catch (Exception e) {
                throw new IllegalArgumentException();
            }

            counter_Time = 0;
        }
    }
}