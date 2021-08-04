package imzi.marwane.acw;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.ImageView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;

public class CustomPuzzleImageRequest implements Response.Listener<Bitmap>, Response.ErrorListener{
    private VolleyPuzzleImageResponse mVolleyItemImageResponse;
    private Puzzle mPuzzle;
    private int mIndex;
    private ImageRequest mImageRequest;
    private PuzzleRepository mPuzzleRepository;
    private Context mContext;
    private String mUrl;

    public ImageRequest getImageRequest() {
        return mImageRequest;
    }

    public CustomPuzzleImageRequest(String pUrl, Puzzle pPuzzle, VolleyPuzzleImageResponse pVolleyItemImageResponse, int pIndex){
        mVolleyItemImageResponse = pVolleyItemImageResponse;
        mPuzzle = pPuzzle;
        mIndex = pIndex;
        mUrl = pUrl;
        mImageRequest = new ImageRequest(pUrl, this, 0, 0, ImageView.ScaleType.CENTER_CROP, Bitmap.Config.RGB_565, this);
    }

    @Override
    public void onErrorResponse(VolleyError pError) {
        mVolleyItemImageResponse.onError(pError, mPuzzle.getName());
    }

    @Override
    public void onResponse(Bitmap pResponse) {
        mPuzzleRepository = PuzzleRepository.getInstance(mContext);
        mPuzzleRepository.saveImageLocally(pResponse, Uri.parse(mUrl).getLastPathSegment());
        mVolleyItemImageResponse.onResponse(pResponse, mPuzzle, mIndex);
    }
}
