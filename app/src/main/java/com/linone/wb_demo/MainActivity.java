package com.linone.wb_demo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private int result = 1;
    private jsonObject[] Data = null;
    private TextView Watertemp,Temp,Humi;
    private Button get;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Watertemp = findViewById(R.id.textView);
        Temp = findViewById(R.id.textView2);
        Humi = findViewById(R.id.textView3);
        get = findViewById(R.id.get);
        get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getdata();
            }
        });

    }

    private void getdata(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String url = "https://api.thingspeak.com/channels/700874/feeds.html?results="+String.valueOf(result);
                    Document document = Jsoup.connect(url).get();
                    Elements elements = document.select("body");
                    String S = elements.text();
                    JsonObject j = gson.fromJson(S,JsonObject.class);
                    JsonArray K = j.get("feeds").getAsJsonArray();
                    Data = gson.fromJson(K,jsonObject[].class);
                }catch (IOException e){
                    Log.e("getdata()","connecterror");
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Watertemp.setText("Watertemp : "+Data[0].getWatertemp());
                        Temp.setText("Temp : "+Data[0].getTemp());
                        Humi.setText("Humi : "+Data[0].getHumi());
                    }
                });
            }
        }).start();
    }
}
