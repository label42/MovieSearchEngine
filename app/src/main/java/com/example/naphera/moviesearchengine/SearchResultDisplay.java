package com.example.naphera.moviesearchengine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class SearchResultDisplay extends AppCompatActivity {

    JSONObject movie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_display);

        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest request = getMovieDetails(getIntent().getExtras().getInt("movieId"));

        queue.add(request);

    }

    public void updateTextDisplay() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            getMovieDetails(extras.getInt("movieId"));
            //getMovieDetails(671);
            if (this.movie != null) {
                try {
                    TextView titleDisplay = (TextView) findViewById(R.id.title_display);
                    titleDisplay.setText(this.movie.getString("title") + " - " + this.movie.getString("release_date"));

                    TextView overview = (TextView) findViewById(R.id.overview);
                    overview.setText(this.movie.getString("release_date"));

                    displayMoviePoster(this.movie.getString("backdrop_path"));

                } catch (JSONException je) {
                    final Toast toast = Toast.makeText(getBaseContext(), je.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                }

            }
        }
    }

    public StringRequest getMovieDetails(int movieId) {
        String requestUrl = "https://api.themoviedb.org/3/movie/" + movieId + "?api_key=9983b4ca4a856d37618c2d4a52e4a031&language=fr-FR";

        return new StringRequest(Request.Method.GET, requestUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            movie = (JSONObject) new JSONTokener(response).nextValue();
                            updateTextDisplay();
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

    public void displayMoviePoster(String imageCode) {
        String requestUrl = "https://image.tmdb.org/t/p/w500/" + imageCode;
        ImageView moviePicture = (ImageView) findViewById(R.id.movie_picture);
        Picasso.with(getBaseContext()).load(requestUrl).into(moviePicture);
    }
}
