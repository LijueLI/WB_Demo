package com.linone.wb_demo;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class marker extends MarkerView {
    private TextView tvContent,tvDate;
    private String[] date;
    public marker(Context context, int layoutResource, TextView tvDate,String[] date) {
        super(context, layoutResource);

        // find your layout components
        tvContent = (TextView) findViewById(R.id.Ydata);
        this.tvDate = tvDate;
        this.date = date;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        tvContent.setText("" + e.getY());
        String[] s = date[(int)e.getX()].split("T");
        s[1] = s[1].split("\\+")[0];
        tvDate.setText(""+s[0]+"  "+s[1]);
        // this will perform necessary layouting
        super.refreshContent(e, highlight);
    }

    private MPPointF mOffset;

    @Override
    public MPPointF getOffset() {

        if(mOffset == null) {
            // center the marker horizontally and vertically
            mOffset = new MPPointF(-(getWidth() / 2), -getHeight());
        }

        return mOffset;
    }
}
