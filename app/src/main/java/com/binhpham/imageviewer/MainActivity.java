package com.binhpham.imageviewer;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        list.setLayoutManager(new GridLayoutManager(this, 4));
        list.setAdapter(new ImageAdapter(this, getImages(), list));
    }

    private List<String> getImages(){
        List<String> result = new ArrayList<>();
        for (int i=0; i<1000; i++){
            result.add("https://dummyimage.com/" + (200 + i));
        }
        return  result;
    }


}