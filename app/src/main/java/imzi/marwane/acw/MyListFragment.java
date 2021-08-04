package imzi.marwane.acw;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.ListFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

public class MyListFragment extends ListFragment {
    int mCurCheckPosition = 0;
    boolean mSingleActivity;
    PuzzleViewModel puzzleViewModel;
    View mInflatedView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        puzzleViewModel = new ViewModelProvider(getActivity()).get(PuzzleViewModel.class);
        final Observer<List<Puzzle>> itemObserver = new Observer<List<Puzzle>>() {
            @Override
            public void onChanged(List<Puzzle> items) {
                PuzzleAdapter ItemAdapter = new PuzzleAdapter(getActivity(), puzzleViewModel.getPuzzles().getValue());
                setListAdapter(ItemAdapter);
            }
        };
        puzzleViewModel.getPuzzles().observe(this, itemObserver);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        View contentFrame = getActivity().findViewById(R.id.content);
        mSingleActivity = contentFrame != null && contentFrame.getVisibility() == View.VISIBLE;

        if(savedInstanceState != null){
            //Restore last state for checked position
            mCurCheckPosition = savedInstanceState.getInt("curChoice", 0);
        }


        if(mSingleActivity){
            showContent(puzzleViewModel.getmSelectedIndex());
        }
        else{
            showContent(puzzleViewModel.getmSelectedIndex());
            getListView().setItemChecked(puzzleViewModel.getmSelectedIndex(), true);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        puzzleViewModel.selectPuzzle(position);
        showContent(position);
    }

    void showContent (int index){
        if(mSingleActivity){
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown replace if needed
            ListPuzzleFragment content = (ListPuzzleFragment) getFragmentManager()
                    .findFragmentById(R.id.content);
            if(content == null || content.getShownIndex() != index){
                // Make new fragment to show this selection
                content = ListPuzzleFragment.newInstance(index);

                // Execute a transaction, replace any existing fragment
                // With this one inside the frame
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content, content);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
        }
        else{
            getListView().setItemChecked(index, true);

            // Check what fragment is currently shown replace if needed
            ListPuzzleFragment content = (ListPuzzleFragment) getFragmentManager()
                    .findFragmentById(R.id.content2);
            if(content == null || content.getShownIndex() != index){
                // Make new fragment to show this selection
                content = ListPuzzleFragment.newInstance(index);

                // Execute a transaction, replace any existing fragment
                // With this one inside the frame
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.content2, content);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
            else{}
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("curChoice", mCurCheckPosition);
    }
}
