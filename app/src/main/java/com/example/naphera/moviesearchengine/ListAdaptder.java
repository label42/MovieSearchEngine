package com.example.naphera.moviesearchengine;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListAdaptder extends ArrayAdapter<JSONObject> {
    public ListAdaptder(Activity context, List<JSONObject> items) {
        super(context, R.layout.list_layout, items);
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        View row;
        LayoutInflater inflater = LayoutInflater.from(getContext());
        row=inflater.inflate(R.layout.list_layout, null);
        try {
            JSONObject js = getItem(position);
            TextView nom = (TextView)row.findViewById(R.id.text);
            nom.setText(js.getString("title"));
            String requestUrl = "https://image.tmdb.org/t/p/w500/" + js.getString("poster_path");
            ImageView image = (ImageView)row.findViewById(R.id.image);
            Picasso.with(getContext()).load(requestUrl).resize(350 ,500).into(image);
        }  catch (JSONException je) {
            je.printStackTrace();
        }
        return(row);
    }
}

