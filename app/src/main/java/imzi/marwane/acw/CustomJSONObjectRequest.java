package imzi.marwane.acw;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class CustomJSONObjectRequest implements Response.Listener<JSONObject>, Response.ErrorListener{
    private VolleyJSONObjectResponse mVolleyJSONObjectResponse;
    private String mTag;
    private JsonObjectRequest mJsonObjectRequest;

    public JsonObjectRequest getJsonObjectRequest(){
        return mJsonObjectRequest;
    }

    public CustomJSONObjectRequest(int pMethod, String pUrl, JSONObject pJSONObject, String pTag, VolleyJSONObjectResponse pVolleyJSONObjectResponse){
        this.mVolleyJSONObjectResponse = pVolleyJSONObjectResponse;
        this.mTag = pTag;
        mJsonObjectRequest = new JsonObjectRequest(pMethod, pUrl, pJSONObject, this, this);
    }

    @Override
    public void onErrorResponse(VolleyError pError) {
        mVolleyJSONObjectResponse.onError(pError, mTag);
    }

    @Override
    public void onResponse(JSONObject pResponse) {
        try {
            mVolleyJSONObjectResponse.onResponse(pResponse, mTag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
