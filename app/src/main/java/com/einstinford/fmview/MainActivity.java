package com.einstinford.fmview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.einstinford.fmview.bean.ResultBean;
import com.einstinford.fmview.fmview.FmView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private ObjectAnimator mAnimator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EventBus建立索引，然后作为默认单例
//        EventBus.builder().addIndex(new MyEventBusIndex()).installDefaultEventBus();
        // Now the default instance uses the given index. Use it like this: EventBus eventBus = EventBus.getDefault();

        TangramViewMetrics.initWith(getApplicationContext());
        BaseData.ScreenWidth = TangramViewMetrics.screenWidth();
        setContentView(R.layout.activity_main);
        LinearLayout root = findViewById(R.id.root);
        TextView textView = (TextView) findViewById(R.id.textView);

        FmView fmView = new FmView(MainActivity.this);

        root.addView(fmView);
        try {
            String jsonData = new String(getAssetsFile(MainActivity.this, "fminfo.json"));
            ResultBean result = JSON.parseObject(jsonData, ResultBean.class);
            fmView.setFmList(result.getList().getItem());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static byte[] getAssetsFile(Context context, String fileName) {
        InputStream inputStream = null;
        AssetManager assetManager = context.getAssets();

        try {
            inputStream = assetManager.open(fileName);
            if (inputStream == null) {
                return null;
            }

            BufferedInputStream bis = null;
            int length;
            try {
                bis = new BufferedInputStream(inputStream);
                length = bis.available();
                byte[] data = new byte[length];
                bis.read(data);

                return data;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
