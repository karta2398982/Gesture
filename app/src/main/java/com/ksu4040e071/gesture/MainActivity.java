package com.ksu4040e071.gesture;

import android.gesture.Gesture; //觸摸屏上的手繪形狀
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.gesture.GestureOverlayView.OnGesturePerformedListener;
import android.gesture.Prediction;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<Integer> colorList;
    private int index;
    private RelativeLayout relativeLayout;
    private TextView tvMessage;
    private LinearLayout linearLayout;
    private GestureLibrary gestureLibrary;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initColorList();
        //顏色陣列
        gestureLibrary = GestureLibraries.fromRawResource(this, R.raw.gestures);
        if (!gestureLibrary.load()) {
            finish();
        }
        //手勢檔案
        findViews();
    }

    private void initColorList() {
        colorList = new ArrayList<>();
        //新增空的陣列
        colorList.add(Color.RED);
        colorList.add(Color.GRAY);
        colorList.add(Color.GREEN);
        colorList.add(Color.BLUE);
        colorList.add(Color.CYAN);
    }
    //新增顏色

    private void findViews() {
        relativeLayout = (RelativeLayout) findViewById(R.id.relativeLayout);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);
        GestureOverlayView govColor = (GestureOverlayView) findViewById(R.id.govColor);
        //尋找物件並顯示

        linearLayout.setBackgroundColor(colorList.get(index));
        //背景顏色變化設定，index為顏色

        govColor.addOnGesturePerformedListener(new OnGesturePerformedListener() {
            //設一個手勢監聽器
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, Gesture gesture) {
                ArrayList<Prediction> predictions = gestureLibrary.recognize(gesture);
                if (predictions == null || predictions.size() <= 0) {
                    tvMessage.setText("cannot recognize your gesture; make a gesture again");
                    return;
                }
                String gestureName = predictions.get(0).name;
                double gestureScore = predictions.get(0).score;
                String text = String.format(" name: %s %n score: %.1f", gestureName, gestureScore);
                tvMessage.setText(text);
                //顯示顏色名稱和分數
                switch (gestureName) {
                    case "swipe_left":
                        if (gestureScore >= 30) {
                            index++;
                            if (index >= colorList.size()) {
                                index = 0;
                            }
                            linearLayout.setBackgroundColor(colorList.get(index));
                        }
                        break;
                    case "swipe_right":
                        if (gestureScore >= 30) {
                            index--;
                            if (index < 0) {
                                index = colorList.size() - 1;
                            }
                            //index代表顏色
                            linearLayout.setBackgroundColor(colorList.get(index));
                        }
                        break;
                    //由左向右水平滑，顏色會跳到前一個
                    case "check":
                        if (gestureScore >= 3) {
                            relativeLayout.setBackgroundColor(Color.DKGRAY);
                        }
                        break;
                    //打勾，如果分數大於3，顏色會變DKGRAY
                    case "circle":
                        if (gestureScore >= 3) {
                            relativeLayout.setBackgroundColor(Color.WHITE);
                        }
                        break;
                    //圓形，如果分數大於3，顏色會變WHITE
                }
            }
        });
    }
}
