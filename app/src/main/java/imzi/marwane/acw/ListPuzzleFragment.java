package imzi.marwane.acw;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ListPuzzleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListPuzzleFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_INDEX = "index";

    private int mIndex;
    PuzzleViewModel myViewModel;
    View mInflatedView;

    public int getShownIndex(){
        return mIndex;
    }

    public ListPuzzleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param index Parameter 1.
     * @return A new instance of fragment PuzzlePreviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ListPuzzleFragment newInstance(int index) {
        ListPuzzleFragment fragment = new ListPuzzleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_INDEX, index);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mIndex = getArguments().getInt(ARG_INDEX);
        }
        myViewModel = new ViewModelProvider(getActivity()).get(PuzzleViewModel.class);
        myViewModel.selectPuzzle(mIndex);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.i(this.getClass().getSimpleName() + " Observer", "onCreateView");
        mInflatedView = inflater.inflate(R.layout.fragment_puzzle_list, container, false);

        Button button = (Button) mInflatedView.findViewById(R.id.startButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GameActivity.class);
                startActivity(intent);
            }
        });


        final Observer<Puzzle> itemObserver = new Observer<Puzzle>() {
            @Override
            public void onChanged(Puzzle puzzle) {
                GridLayout gridLayout = (GridLayout) mInflatedView.findViewById(R.id.PuzzleGridLayout);
                gridLayout.removeAllViews();
                for (int i = 0; i < puzzle.pictureSetCount(); i++) {
                    ImageView imageView = new ImageView(gridLayout.getContext());
                    imageView.setImageBitmap(puzzle.getPictureSet(i));
                    imageView.setLayoutParams(new LinearLayout.LayoutParams(220, 220));
                    gridLayout.addView(imageView);
                }
            }
        };
        myViewModel.getSelectedPuzzle().observe(getViewLifecycleOwner(), itemObserver);
        return mInflatedView;
    }
}