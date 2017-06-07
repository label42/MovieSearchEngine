package com.example.naphera.moviesearchengine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
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
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MyActivity";
    Toast toast;

    Spinner numberOfResultSpinner;
    Spinner languageOfResultSpinner;
    String language;

    List<String> moviesResultsTitle = new ArrayList<>();
    List<Integer> moviesResultsId = new ArrayList<>();
    List<JSONObject> jsonObjectsForAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(TAG, "debug");

        Context context = getApplicationContext();
        CharSequence text = "Hello toast!";
        int duration = Toast.LENGTH_SHORT;
        toast = Toast.makeText(context, text, duration);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerInit();



        final RequestQueue queue = Volley.newRequestQueue(this);

        final Button buttonSearch = (Button) findViewById(R.id.buttonSearch);
        final EditText searchField = (EditText) findViewById(R.id.searchField);
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String film= searchField.getText().toString().replace(' ', '+');
                System.out.print(film);
                int numberOfResult = Integer.valueOf(String.valueOf(numberOfResultSpinner.getSelectedItem()));
                language = String.valueOf(languageOfResultSpinner.getSelectedItem());
                if(language == "Francais"){
                    language = "fr-FR";
                } else {
                    language = "en-US";
                }
                StringRequest stringRequest = generateRequest(film, numberOfResult);
                queue.add(stringRequest);

            }
        });

        searchField.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // TODO Auto-generated method stub
                if (keyCode == KeyEvent.KEYCODE_ENTER) { //Whenever you got user click enter. Get text in edittext and check it equal test1. If it's true do your code in listenerevent of button3
                    buttonSearch.performClick();
                    return true;
                }
                return false;
            }
        });

        final Intent toResultDisplay = new Intent(MainActivity.this, SearchResultDisplay.class );
        ListView listResults = (ListView) findViewById(R.id.listResults);

        listResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                int selectedMovieId = moviesResultsId.get(position);
                toResultDisplay.putExtra("movieId", selectedMovieId);
                toResultDisplay.putExtra("language", language);
                startActivity(toResultDisplay);
            }
        });


    }

    public void spinnerInit(){
        numberOfResultSpinner = (Spinner) findViewById(R.id.spinner);
        Integer[] listAskedResult = {1, 5, 10, 20, 30};
        ArrayAdapter<Integer> aa = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listAskedResult);
        numberOfResultSpinner.setAdapter(aa);

        languageOfResultSpinner = (Spinner) findViewById(R.id.spinnerLanguage);
        String[] listLanguage = {"Francais", "English"};
        ArrayAdapter<String> aa2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listLanguage);
        languageOfResultSpinner.setAdapter(aa2);
    }

    public void setResultInList(List<JSONObject> listMovie){
        ListView lv = (ListView) findViewById(R.id.listResults);
        lv.setAdapter(new ListAdaptder(this, listMovie));
    }

    public StringRequest generateRequest(String film, final int numberOfResult){
        String url ="https://api.themoviedb.org/3/search/movie?api_key=9983b4ca4a856d37618c2d4a52e4a031&language="+language+"&page=1&include_adult=false&query="+film;

        // Request a string response from the provided URL.
        return new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            moviesResultsTitle = new ArrayList<>();
                            moviesResultsId = new ArrayList<>();
                            jsonObjectsForAdapter = new ArrayList<>();

                            JSONObject repObj =
                                    (JSONObject) new JSONTokener(response).nextValue();

                            JSONArray movies = repObj.getJSONArray("results");
                            for (int i=0; i< movies.length();i++) {
                                JSONObject val = movies.getJSONObject(i);
                                jsonObjectsForAdapter.add(val);
                                moviesResultsId.add(val.getInt("id"));
                                if(i == numberOfResult - 1){
                                    break;
                                }
                            }

                            setResultInList(jsonObjectsForAdapter);
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

