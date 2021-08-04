package imzi.marwane.acw;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class PuzzleRepository {
    private static PuzzleRepository sPuzzleRepository;
    private Context mApplicationContext;

    private MediatorLiveData<ArrayList<Puzzle>> mPuzzle;
    private LiveData<Puzzle> mSelectPuzzle;
    private MutableLiveData<Puzzle> mSelectPuzzleMutableLiveData;
    private VolleyPuzzleListRetriever mRemotePuzzleList;
    private LiveData<Tile> mTileData;

    private ArrayList<Puzzle> puzzleArray = new ArrayList<>();

    private PuzzleRepository(Context pApplicationContext){
        this.mApplicationContext = pApplicationContext;
        //mPuzzle = new MediatorLiveData<>();
        mRemotePuzzleList = new VolleyPuzzleListRetriever("https://goparker.com/600096/moag/index.json", pApplicationContext);
    }

    public static PuzzleRepository getInstance(Context pApplicationContext){
        if (sPuzzleRepository == null) {
            sPuzzleRepository = new PuzzleRepository(pApplicationContext);
        }
        return sPuzzleRepository;
    }

    public LiveData<ArrayList<Puzzle>> getPuzzles() {
        if(mPuzzle == null) {
            mPuzzle = new MediatorLiveData<>();

            ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
            File file = contextWrapper.getFilesDir();
            File[] puzzleStuff = file.listFiles();

            LiveData<ArrayList<Puzzle>> remoteData = mRemotePuzzleList.getPuzzles();
            LiveData<ArrayList<Puzzle>> localData = new LiveData<ArrayList<Puzzle>>() {};
            for (int i = 0; i< puzzleStuff.length; i++) {
                localData = loadIndexLocally(puzzleStuff[i].getName());
            }
            mPuzzle.addSource(remoteData, value-> mPuzzle.setValue(value));
            mPuzzle.addSource(localData, value-> mPuzzle.setValue(value));
        }
        return mPuzzle;
    }

    public LiveData<Puzzle> getPuzzle(final int pPuzzleIndex) {

        LiveData<Puzzle> transformedPuzzle = Transformations.switchMap(mPuzzle, puzzles -> {
            if(mSelectPuzzleMutableLiveData == null){
                mSelectPuzzleMutableLiveData = new MutableLiveData<>();
            }
            if(puzzles.size() > 0){
                Puzzle puzzle = puzzles.get(pPuzzleIndex);
                mSelectPuzzleMutableLiveData.setValue(puzzle);
            }

            return mSelectPuzzleMutableLiveData;

        });

        mSelectPuzzle = transformedPuzzle;
        return mSelectPuzzle;
    }

    public LiveData<Puzzle> getSelectPuzzle(){
        return mSelectPuzzle;
    }

    // -------------------------------------Internal Storage---------------------------------------

    public void saveIndexLocally(JSONObject pIndexObject, String pFilename){
        ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
        OutputStreamWriter outputStreamWriter  = null;
        try {
            outputStreamWriter = new OutputStreamWriter(
                    contextWrapper.openFileOutput(pFilename, Context.MODE_PRIVATE));
            outputStreamWriter.write(pIndexObject.toString());
            outputStreamWriter.flush();
            outputStreamWriter.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    private LiveData<ArrayList<Puzzle>> loadIndexLocally(String pFilename){
        JSONObject indexObject = null;
        MutableLiveData<ArrayList<Puzzle>> mutableItem = new MutableLiveData<ArrayList<Puzzle>>();
        try {
            InputStream inputStream = mApplicationContext.openFileInput(pFilename);

            if(inputStream != null){
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null){
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                String builtString = stringBuilder.toString();
                indexObject = new JSONObject(builtString);
            }
        }
        catch (FileNotFoundException e){
            Log.e("JSONLoading", "File not found: " + e.toString());
        }
        catch (IOException e){
            Log.e("JSONLoading", "Can not read file: " + e.toString());
        }
        catch (JSONException e){
            Log.e("JSONLoading", "json error: " + e.toString());
        }

        if(indexObject != null){
            //ArrayList<Puzzle> items = new ArrayList<Puzzle>();
            puzzleArray.add(mRemotePuzzleList.parseJSONResponse(indexObject));
            mutableItem.setValue(puzzleArray);
        }
        return mutableItem;
    }



    public void saveImageLocally(Bitmap pBitmap, String pFilename){
        ContextWrapper contextWrapper = new ContextWrapper(mApplicationContext);
        File directory = contextWrapper.getDir("puzzleImages", Context.MODE_PRIVATE);
        File file = new File(directory, pFilename);

        if(!file.exists()){
            FileOutputStream fileOutputStream = null;
            try{
                fileOutputStream = new FileOutputStream(file);
                pBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
            }
            catch (java.io.IOException e){
                e.printStackTrace();
            }
        }
    }

    public LiveData<Tile> getTile() {
        return mTileData;
    }
}
