package philippmatthes.com.manni.vvo;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.Optional;

public class Connection {

    public static void makeRequest(
            String url,
            Map<String, String> data,
            Response.Listener<String> listener,
            Response.ErrorListener errorListener) {
        try {
            StringRequest request = new StringRequest(Request.Method.POST, url, listener, errorListener) {
                protected Map<String, String> getParams() {
                    return data;
                }
            };
        } catch (UnsatisfiedLinkError e) {
            System.out.println(e);
            errorListener.onErrorResponse(new VolleyError(e.getMessage()));
        }
    }

    public static <T extends philippmatthes.com.manni.vvo.Models.Response> void post(
            String url,
            Map<String, String> data,
            Response.Listener<Result<T>> responseListener
    ) {
        Type typeOfT = new TypeToken<T>(){}.getType();
        makeRequest(url, data,
                response -> {
                    T findResponse = new Gson().fromJson(response, typeOfT);
                    Result<T> result = new Result<>(Optional.of(findResponse), Optional.empty());
                    responseListener.onResponse(result);
                }, error -> {
                    Result<T> result = new Result<>(Optional.empty(), Optional.of(DVBError.response));
                    responseListener.onResponse(result);
                }
        );
    }
}
