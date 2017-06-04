package com.example.naphera.moviesearchengine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SearchResultDisplay extends AppCompatActivity {

    JSONObject movie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_display);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getMovieDetails(extras.getInt("movieId"));
            //getMovieDetails(671);
            if (this.movie != null) {
                try {
                    TextView titleDisplay = (TextView) findViewById(R.id.title_display);
                    titleDisplay.setText(this.movie.getString("title"));


                } catch (JSONException je) {
                    final Toast toast = Toast.makeText(getBaseContext(), je.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        }


    }

    public StringRequest getMovieDetails(int movieId) {
        String requestUrl = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=9983b4ca4a856d37618c2d4a52e4a031&language=fr-FR";

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            movie = (JSONObject) new JSONTokener(response).nextValue();
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                final Toast toast = Toast.makeText(getBaseContext(), error.getMessage(), Toast.LENGTH_LONG);
                toast.show();
            }
        });


    }
}
