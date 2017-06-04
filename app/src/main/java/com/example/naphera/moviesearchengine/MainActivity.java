package com.example.naphera.moviesearchengine;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    Toast toast;

    Spinner numberOfResultSpinner;
    List<String> moviesResults = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "debug");

        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, text, duration);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //final TextView mTextView = (TextView) findViewById(R.id.text1);

        spinnerInit();



        final RequestQueue queue = Volley.newRequestQueue(this);

        Button b = (Button) findViewById(R.id.buttonSearch);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText searchField = (EditText) findViewById(R.id.searchField);
                String film= searchField.getText().toString().replace(' ', '+');
                System.out.print(film);
                int numberOfResult = Integer.valueOf(String.valueOf(numberOfResultSpinner.getSelectedItem()));
                StringRequest stringRequest = generateRequest(film, numberOfResult);
                queue.add(stringRequest);

            }
        });


    }

    public void spinnerInit(){
        numberOfResultSpinner = (Spinner) findViewById(R.id.spinner);
        Integer[] liste = {1, 2 , 3, 4, 5, 6, 7, 8, 9, 10, 20};
        ArrayAdapter<Integer> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, liste);
        numberOfResultSpinner.setAdapter(aa);
    }

    public void setResultInList(List l){
        ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, l);
        ListView lv = (ListView) findViewById(R.id.listResults);
        lv.setAdapter(aa);
    }

    public StringRequest generateRequest(String film, final int numberOfResult){
        String url ="https://api.themoviedb.org/3/search/movie?api_key=9983b4ca4a856d37618c2d4a52e4a031&language=en-US&page=1&include_adult=false&query="+film;

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            moviesResults = new ArrayList<>();
                            JSONObject repObj =
                                    (JSONObject) new JSONTokener(response).nextValue();

                            JSONArray movies = repObj.getJSONArray("results");
                            addResultToList : for (int i=0; i< movies.length();i++) {
                                JSONObject val = movies.getJSONObject(i);
                                moviesResults.add(val.getString("title"));
                                if(i == numberOfResult){
                                    break addResultToList;
                                }
                            }
                            setResultInList(moviesResults);
                        } catch (JSONException je) {
                            je.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                toast.setText(error.getMessage());
                toast.show();
            }
        });
    }
    }

