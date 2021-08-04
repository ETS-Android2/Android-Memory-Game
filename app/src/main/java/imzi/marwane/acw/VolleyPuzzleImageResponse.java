package imzi.marwane.acw;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.android.volley.VolleyError;

public interface VolleyPuzzleImageResponse {

    void onResponse(Bitmap pImage, Puzzle puzzle, int index);
    void onError(VolleyError error, String tag);
}
