package com.linone.wb_demo;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private int result = 30;
    private jsonObject[] Data = null;
    private TextView Watertemp,Temp,Humi,pH;
    private LineChart watertempchart;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Watertemp = findViewById(R.id.WT_Data);
        pH = findViewById(R.id.WP_Data);
        Temp = findViewById(R.id.T_Data);
        Humi = findViewById(R.id.H_Data);
        watertempchart = findViewById(R.id.WaterTempChart);

        XAxis WatertempxAxis = watertempchart.getXAxis();
        WatertempxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        WatertempxAxis.setEnabled(true);
        WatertempxAxis.setDrawAxisLine(true);
        WatertempxAxis.setDrawGridLines(false);
        getdata();
    }

    private void getdata(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        String url = "https://api.thingspeak.com/channels/700874/feeds.html?results=" + String.valueOf(result);
                        Document document = Jsoup.connect(url).get();
                        Elements elements = document.select("body");
                        String S = elements.text();
                        JsonObject j = gson.fromJson(S, JsonObject.class);
                        JsonArray K = j.get("feeds").getAsJsonArray();
                        Data = gson.fromJson(K, jsonObject[].class);
                    } catch (IOException e) {
                        Log.e("getdata()", "connecterror");
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Watertemp.setText(Data[29].getWatertemp() + "  °C");
                            pH.setText(Data[29].getPH() + "      ");
                            Temp.setText(Data[29].getTemp() + "  °C");
                            Humi.setText(Data[29].getHumi() + "   %");
                        }
                    });
                    try {
                        Thread.sleep(15000);
                    }catch (InterruptedException e){
                        Log.e("Sleep","sleeperror");
                    }
                }
            }
        }).start();
    }
}
