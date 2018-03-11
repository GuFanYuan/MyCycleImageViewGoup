package com.example.gufan.mycycleimageviewgoup.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.gufan.mycycleimageviewgoup.R;

import java.util.List;

/**
 * Created by gufan on 2018/3/10.
 */

public class ImageFrameLayout extends FrameLayout implements ImageCycleViewGroup.ImageCycleViewGroupListener ,ImageCycleViewGroup.ImageCycleListener{



    private static final String TAG = "ImageFrameLayout" ;

    private ImageCycleViewGroup imageCycleViewGroup;
    private LinearLayout linearLayout;

    private onClickListener listener;

    public void setListener(onClickListener listener){
        this.listener = listener;
    }

    public onClickListener getListener(){
        return listener;
    }

    public ImageFrameLayout(@NonNull Context context) {
        super(context);
        initImageCycleViewGroup();
        initDotLineralLayout();
    }

    public ImageFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initImageCycleViewGroup();
        initDotLineralLayout();
    }

    public ImageFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initImageCycleViewGroup();
        initDotLineralLayout();
    }

    public void addBitmaps(List<Bitmap> list){
        if (list.isEmpty()){
            Log.e(TAG,"this list is empty");
            return;
        }
        for (Bitmap bitmap : list) {
            addBitmapToImageCycleGroup(bitmap);
            addBottomPointLinearLayout();
        }
    }

    private void addBottomPointLinearLayout(){
        ImageView imageView = new ImageView(getContext());
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(3,3,3,3);
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.drawable.dot_normal);
        linearLayout.addView(imageView);
    }

    private void addBitmapToImageCycleGroup(Bitmap bitmap){
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(GlobalVariables.windowWidth, ViewGroup.LayoutParams.MATCH_PARENT));
        Log.e(TAG,"imageView width :"+imageView.getMeasuredWidth()+" imageView height :"+imageView.getMeasuredHeight()
                +" GlobalVariables.windowWiath :"+GlobalVariables.windowWidth);
        imageView.setImageBitmap(bitmap);
        imageCycleViewGroup.addView(imageView);
    }

    private void initImageCycleViewGroup(){
        imageCycleViewGroup = new ImageCycleViewGroup(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        imageCycleViewGroup.setLayoutParams(layoutParams);
        imageCycleViewGroup.setImageCycleViewGroupListener(this);
        imageCycleViewGroup.setImageCycleLister(this);
        addView(imageCycleViewGroup);
    }

    private void initDotLineralLayout(){
        linearLayout = new LinearLayout(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,40);
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.RED);
        addView(linearLayout);

        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams)linearLayout.getLayoutParams();
        lp.gravity = Gravity.BOTTOM;
        linearLayout.setLayoutParams(lp);
        linearLayout.getBackground().setAlpha(0);
    }

    @Override
    public void selectImage(int index) {
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            ImageView iv = (ImageView) linearLayout.getChildAt(i);
            if (i == index){
                iv.setImageResource(R.drawable.dot_select);
            }else{
                iv.setImageResource(R.drawable.dot_normal);
            }
        }
    }

    @Override
    public void clickImageIndex(int postion) {
        listener.clickListener(postion);
    }

    public interface onClickListener{
        void clickListener(int postion);
    }

}
