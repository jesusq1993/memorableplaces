package com.example.memorableplaces;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.memorableplaces.databinding.ActivityMainBinding;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Array;
import java.security.spec.ECField;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    ActivityMainBinding activityMainBinding;

    //Global Variables
    static ArrayList<String> places = new ArrayList<String>();      //title our places
    static ArrayList<LatLng> locations = new ArrayList<LatLng>();   //lat long of places
    static ArrayAdapter arrayAdapter;                          //Array adapter for our listview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = activityMainBinding.getRoot();
        setContentView(view);

        SharedPreferences sharedPreferences = this.getSharedPreferences(getPackageName().toString(), Context.MODE_PRIVATE);

        ArrayList<String> latitudes = new ArrayList<>();
        ArrayList<String> longitudes = new ArrayList<>();

        places.clear();
        latitudes.clear();
        longitudes.clear();
        locations.clear();

        try{
            places = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("places",ObjectSerializer.serialize(new ArrayList<String>())));
            latitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("lats",ObjectSerializer.serialize(new ArrayList<String>())));
            longitudes = (ArrayList<String>) ObjectSerializer.deserialize(sharedPreferences.getString("longs",ObjectSerializer.serialize(new ArrayList<String>())));

        }catch (Exception e){
            e.printStackTrace();
        }

        if(places.size()>0 && latitudes.size()>0 && longitudes.size()>0){
            if(places.size() == latitudes.size() && places.size()==longitudes.size()){
                for(int i = 0;i < latitudes.size(); i++){
                    locations.add(new LatLng(Double.parseDouble(latitudes.get(i)),Double.parseDouble(longitudes.get(i))));
                }
            }
        }else{
            //first item in listview to add new location
            places.add("Add a new location...");
            locations.add(new LatLng(0,0));
        }

        arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1,places);
        activityMainBinding.listView.setAdapter(arrayAdapter);

        //if first item is clicked add new item else go to maps activity and display memorable place
        activityMainBinding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                    intent.putExtra("locationNumber",position);
                    startActivity(intent);
                }else{
                    Intent intent = new Intent(getApplicationContext(),MapsActivity.class).putExtra("locationNumber",position);
                    startActivity(intent);
                }
            }
        });

    }
}