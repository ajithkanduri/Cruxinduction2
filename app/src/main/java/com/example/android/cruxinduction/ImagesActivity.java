package com.example.android.cruxinduction;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.io.File;

public class ImagesActivity extends AppCompatActivity {
    public static String[] fileNames;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images);
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        File path = new File(Environment.getExternalStorageDirectory(),"/fav_images");
        if(path.exists())
        {
             fileNames = path.list();
        }
        else
        {
            System.out.println("NO SUCH PATH EXISTS");
        }
        ImageAdapter imageAdapter = new ImageAdapter(ImagesActivity.this);
        recyclerView.setAdapter(imageAdapter);
        for (int i=0;i<fileNames.length;i++)
        {
            System.out.println(fileNames);
        }
    }
    public static String[] getfileName()
    {
        return fileNames;
    }
}
