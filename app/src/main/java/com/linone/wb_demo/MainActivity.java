package com.linone.wb_demo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.IMarker;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnDrawLineChartTouchListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.GsonBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int result = 30;
    private jsonObject[] Data = null;
    private TextView Watertemp,Temp,Humi,pH,datemarker;
    private LineChart lineChart;
    private Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    private Description description = new Description();
    private int datac = 0;
    private Button Previous,Next;
    private String[] Date = new String[30];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Watertemp = findViewById(R.id.WT_Data);
        pH = findViewById(R.id.WP_Data);
        Temp = findViewById(R.id.T_Data);
        Humi = findViewById(R.id.H_Data);
        lineChart = findViewById(R.id.LineChart);
        Previous = findViewById(R.id.Previous);
        Next = findViewById(R.id.Next);
        datemarker = findViewById(R.id.Marker);
        description.setText("");

        Previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datac--;
                if(datac<0) datac = 3;
                setData(Data.length);
                switch (datac){
                    case 0:
                        description.setText("最近水溫變化");
                        description.setTextSize(12);
                        lineChart.setDescription(description);
                        break;
                    case 1:
                        description.setText("最近pH變化");
                        description.setTextSize(12);
                        lineChart.setDescription(description);
                        break;
                    case 2:
                        description.setText("最近室溫變化");
                        description.setTextSize(12);
                        lineChart.setDescription(description);
                        break;
                    case 3:
                        description.setText("最近濕度變化");
                        description.setTextSize(12);
                        lineChart.setDescription(description);
                        break;
                    default:break;
                }
                lineChart.invalidate();
            }
        });

        Next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datac++;
                if(datac>3) datac=0;
                setData(Data.length);
                switch (datac){
                    case 0:
                        description.setText("最近水溫變化");
                        description.setTextSize(12);
                        lineChart.setDescription(description);
                        break;
                    case 1:
                        description.setText("最近pH變化");
                        description.setTextSize(12);
                        lineChart.setDescription(description);
                        break;
                    case 2:
                        description.setText("最近室溫變化");
                        description.setTextSize(12);
                        lineChart.setDescription(description);
                        break;
                    case 3:
                        description.setText("最近濕度變化");
                        description.setTextSize(12);
                        lineChart.setDescription(description);
                        break;
                    default:break;
                }
                lineChart.invalidate();
            }
        });

        XAxis WatertempxAxis = lineChart.getXAxis();

        Legend legend = lineChart.getLegend();
        legend.setEnabled(false);

        WatertempxAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        WatertempxAxis.setEnabled(true);
        WatertempxAxis.setDrawAxisLine(true);
        WatertempxAxis.setDrawGridLines(false);

        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setEnabled(false);


        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setHighlightPerDragEnabled(true);
        description.setText("最近水溫變化");
        description.setTextSize(12);
        lineChart.setDescription(description);

        getdata();
        IMarker marker = new marker(this,R.layout.makerview,datemarker,Date);
        lineChart.setMarker(marker);

    }

    private void getdata(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        String url = "https://api.thingspeak.com/channels/700874/feeds.html?timezone=Asia%2FTaipei&results=" + String.valueOf(result);
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
                            Watertemp.setText(Data[Data.length-1].getWatertemp() + "  °C");
                            pH.setText(Data[Data.length-1].getPH() + "      ");
                            Temp.setText(Data[Data.length-1].getTemp() + "  °C");
                            Humi.setText(Data[Data.length-1].getHumi() + "   %");
                            setData(Data.length);
                            lineChart.invalidate();
                            Previous.setEnabled(true);
                            Next.setEnabled(true);
                            lineChart.setVisibility(View.VISIBLE);
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
    private void setData(int count) {

        ArrayList<Entry> values = new ArrayList<Entry>();

        int from = 0;

        // count = hours
        int to = 0 + count;

        // increment by 1 hour
        for (int x = from; x < to; x++) {
            float y;
            switch (datac) {
                case 0:
                    y = Float.valueOf(Data[x].getWatertemp());
                    break;
                case 1:
                    y = Float.valueOf(Data[x].getPH());
                    break;
                case 2:
                    y = Float.valueOf(Data[x].getTemp());
                    break;
                case 3:
                    y = Float.valueOf(Data[x].getHumi());
                    break;
                 default:
                     y=0;
                     break;
            }
            Date[x]=Data[x].getCreated_at();
            values.add(new Entry(x, y)); // add one entry per hour
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(values, "DataSet 1");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setValueTextColor(ColorTemplate.getHoloBlue());
        set1.setLineWidth(1.5f);
        set1.setDrawCircles(false);
        set1.setDrawValues(false);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);

        // create a data object with the datasets
        LineData data = new LineData(set1);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        lineChart.setData(data);
    }
}
