package com.example.naphera.moviesearchengine;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView mTextView = (TextView) findViewById(R.id.text1);

        String film="harry";
        String apiKey="9983b4ca4a856d37618c2d4a52e4a031";

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="https://api.themoviedb.org/3/search/movie?api_key="+apiKey+"&language=en-US&page=1&include_adult=false&query="+film;

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        String s = "";
                        try {
                            JSONObject repObj =
                                    (JSONObject) new JSONTokener(response).nextValue();

                            JSONArray movies = repObj.getJSONArray("results");
                            for (int i=0; i< 10;i++) {
                                JSONObject val = movies.getJSONObject(i);
                                s+= val.getString("title") + "\n\n";
                            }
                            mTextView.setText("Name is: " + s);
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mTextView.setText("That didn't work!");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user", "abel");

                return params;
            }
        };
        queue.add(stringRequest);
    }
    }

