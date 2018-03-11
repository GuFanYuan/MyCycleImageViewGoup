package com.example.gufan.mycycleimageviewgoup;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.gufan.mycycleimageviewgoup.view.GlobalVariables;
import com.example.gufan.mycycleimageviewgoup.view.ImageCycleViewGroup;
import com.example.gufan.mycycleimageviewgoup.view.ImageFrameLayout;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements ImageFrameLayout.onClickListener{

    //private ImageCycleViewGroup imageCycleViewGroup;
    private ImageFrameLayout imageFrameLayout;
    private int[] imageId = new int[]{
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        GlobalVariables.windowWidth = dm.widthPixels;
        imageFrameLayout = (ImageFrameLayout)findViewById(R.id.myImageRotationView);
        imageFrameLayout.setListener(this);
        List<Bitmap> list = new ArrayList<Bitmap>();
        for (int id:imageId) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),id);
            list.add(bitmap);
        }
        imageFrameLayout.addBitmaps(list);
//        imageFrameLayout.setImageCycleLister(this);
    }

    @Override
    public void clickListener(int postion){
        Toast.makeText(this,"postion :"+postion,Toast.LENGTH_SHORT).show();
    }
}

