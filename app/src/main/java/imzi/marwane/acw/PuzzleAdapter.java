package imzi.marwane.acw;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PuzzleAdapter extends ArrayAdapter {
    private Context mContext;
    private List<Puzzle> mItemList;

    public PuzzleAdapter(@NonNull Context pContext, ArrayList<Puzzle> pList){
        super(pContext, 0, pList);
        mContext = pContext;
        mItemList = pList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null) {
            listItem = LayoutInflater.from(mContext).inflate(R.layout.puzzle_layout, parent, false);
        }
        Puzzle currentPuzzle = mItemList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.puzzleTextView);
        name.setText(currentPuzzle.getName());

        return listItem;
    }
}
