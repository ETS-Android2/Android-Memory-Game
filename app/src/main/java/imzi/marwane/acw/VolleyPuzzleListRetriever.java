package imzi.marwane.acw;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class VolleyPuzzleListRetriever implements VolleyJSONObjectResponse, VolleyPuzzleImageResponse{
    private String mUrl;
    private MutableLiveData<ArrayList<Puzzle>> mPuzzlesData;
    private ArrayList<Puzzle> mPuzzles = new ArrayList<>();
    private ArrayList<Bitmap> currentImages = new ArrayList<>();
    private RequestQueue mQueue;
    private Context mContext;
    private PuzzleRepository mPuzzleRepository;
    private MutableLiveData<Puzzle> mutableLiveData;

    public VolleyPuzzleListRetriever(String pUrl, Context pContext){
        mUrl = pUrl;
        mContext = pContext;
        mQueue = Volley.newRequestQueue(pContext);
    }

    public LiveData<ArrayList<Puzzle>> getPuzzles() {
        mPuzzlesData = new MutableLiveData<ArrayList<Puzzle>>();
        CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, mUrl, null, "PuzzleListJSON", this);
        mQueue.add(request.getJsonObjectRequest());
        return mPuzzlesData;
    }

    @Override
    public void onResponse(Bitmap pImage, Puzzle pPuzzle, int pIndex) {
        Log.i("VolleyItemListRetriever", "Image retrieved for: " + pPuzzle.getName());
        currentImages.add(pImage);
        if(pIndex == -1){
            pPuzzle.setTileBack(pImage);
        } else{
            pPuzzle.setPictureSet(pImage, pIndex);
        }
        mPuzzlesData.setValue(mPuzzles);
    }

    @Override
    public void onResponse(JSONObject pObject, String pTag) throws JSONException {
        Log.i("VolleyItemListRetriever", pTag);
        if (pTag.equals("PuzzleListJSON")) {
            mPuzzleRepository = PuzzleRepository.getInstance(mContext);
            parsePuzzleInfo(pObject);
        } else {
            mPuzzleRepository.saveIndexLocally(pObject, pObject.getString("name") + "json");
            mPuzzles.add(parseJSONResponse(pObject));
            mPuzzlesData.setValue(mPuzzles);
        }
    }

    @Override
    public void onError(VolleyError pError, String pTag) {
        Log.e("VolleyItemListRetriever", pTag);
    }

    private void parsePuzzleInfo(JSONObject pResponse){
        try {
            JSONArray arrayOfPuzzle = pResponse.getJSONArray("PuzzleIndex");
            for (int i = 0; i <arrayOfPuzzle.length(); i++) {
                String indexString = arrayOfPuzzle.getString(i);
                String url = "https://www.goparker.com/600096/moag/puzzles/" + indexString;
                CustomJSONObjectRequest request = new CustomJSONObjectRequest(Request.Method.GET, url, null, "PuzzleRetriver", this);
                mQueue.add(request.getJsonObjectRequest());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Figure out where the Bitmap array of puzzles is held then add that to the puzzle object

    public Puzzle parseJSONResponse(JSONObject pResponse){
        Puzzle puzzle = new Puzzle();
        String url = "";

        try {
            JSONArray arrayOfPuzzle = pResponse.getJSONArray("PictureSet");
            String nameOfPuzzle = pResponse.getString("name");
            for (int i = 0; i <arrayOfPuzzle.length(); i++) {
                String imageName = arrayOfPuzzle.getString(i);

                url = "https://www.goparker.com/600096/moag/images/" + imageName + ".png";
                puzzle.setName(nameOfPuzzle);
                mutableLiveData = new MutableLiveData<>();
                if (!loadImageLocally(Uri.parse(url).getLastPathSegment(), mutableLiveData, i, puzzle)) {
                    CustomPuzzleImageRequest customRequest = new CustomPuzzleImageRequest(url, puzzle, this, i);
                    mQueue.add(customRequest.getImageRequest());
                }
            }
            int pIndex = -1;
            String TilePuzzle = pResponse.getString("TileBack");
            url = "https://www.goparker.com/600096/moag/images/" + TilePuzzle + ".png";
            if (!loadImageLocally(Uri.parse(url).getLastPathSegment(), mutableLiveData, pIndex, puzzle)) {
                CustomPuzzleImageRequest customRequest = new CustomPuzzleImageRequest(url, puzzle, this, pIndex);
                mQueue.add(customRequest.getImageRequest());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return puzzle;
    }

    public boolean loadImageLocally(String pFilename, MutableLiveData<Puzzle> pPuzzleData, int pIndex, Puzzle puzzle){
        boolean loaded = false;
        ContextWrapper contextWrapper = new ContextWrapper(mContext);
        File directory = contextWrapper.getDir("puzzleImages", Context.MODE_PRIVATE);
        File file = new File(directory, pFilename);

        if(file.exists()) {
            FileInputStream fileInputStream = null;
            try{
                fileInputStream = new FileInputStream(file);
                Bitmap bitmap = BitmapFactory.decodeStream(fileInputStream);
                puzzle.setPictureSet(bitmap, pIndex);
                pPuzzleData.setValue(puzzle);

                fileInputStream.close();
                loaded = true;
            }
            catch (java.io.IOException e){
                e.printStackTrace();
            }
        }
        return loaded;
    }
}
