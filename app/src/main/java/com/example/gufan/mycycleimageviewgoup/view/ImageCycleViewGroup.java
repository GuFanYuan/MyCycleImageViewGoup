package com.example.gufan.mycycleimageviewgoup.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by gufan on 2018/3/8.
 */

public class ImageCycleViewGroup extends ViewGroup {

    private static final String TAG = "ImageCycleViewGroup";
    private int children;
    private int ChildWidth;
    private int ChildHeight;
    private int x ;//第一次按下的位置的横坐标，每一次移动过程中 移动之前的位置的横坐标
    private int index = 0;//每张图片的索引

    private Scroller scroller;

    private ImageCycleListener imageCycleListener;

    public void setImageCycleLister(ImageCycleListener listener){
        this.imageCycleListener = listener;
    }

    public ImageCycleListener getImageCycleLister(){
        return imageCycleListener;
    }


    private ImageCycleViewGroupListener imageCycleViewGroupListener;

    public void setImageCycleViewGroupListener(ImageCycleViewGroupListener listener){
        this.imageCycleViewGroupListener = listener;
    }

    public ImageCycleViewGroupListener getImageCycleViewGroupListener(){
        return this.imageCycleViewGroupListener;
    }



    private static boolean isClick = false;

    private static boolean switchStart = true;

    private Timer timer = new Timer();
    private TimerTask timerTask;

    private Handler CycleHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    if (++index>children-1){
                        index = 0;
                    }
                    Log.e(TAG,"index :"+index);
                    scrollTo(ChildWidth * index,0);
                    imageCycleViewGroupListener.selectImage(index);
                    break;
            }
        }
    };

    private void startCycleAuto(){
        switchStart = true;
    }

    private void stopCycleAuto(){
        switchStart = false;
    }

    public ImageCycleViewGroup(Context context) {
        super(context);
        init();
    }

    public ImageCycleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageCycleViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        scroller = new Scroller(getContext());
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if(switchStart){
                    CycleHandler.sendEmptyMessage(0);
                }
            }
        };

        timer.schedule(timerTask,100,2000);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()){
            scrollTo(scroller.getCurrX(),0);
            invalidate();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        children = getChildCount();
        if(children == 0){
            setMeasuredDimension(0,0);
        }else{
            measureChildren(widthMeasureSpec,heightMeasureSpec);
            View view = getChildAt(0);
            ChildWidth = view.getMeasuredWidth();
            ChildHeight = view.getMeasuredHeight();
            Log.e(TAG,"ChildWidth :"+ChildWidth
            +"ChildHeight :"+ChildHeight);
            int width = ChildWidth*children;
            setMeasuredDimension(width,ChildHeight);
        }
    }

    @Override
    protected void onLayout(boolean change,int l,int t,int r,int b){
        if(change){

            int leftMargin = 0;
            for (int i = 0;i < children;i++){
                View view = getChildAt(i);
                view.layout(leftMargin, 0, leftMargin+r, ChildHeight);
                leftMargin += ChildWidth;
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                stopCycleAuto();
                if (scroller.isFinished()){
                    scroller.abortAnimation();
                }
                isClick = true;
                x = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getX();
                int distance = moveX - x;
                scrollBy(-distance,0);
                x = moveX;
                Log.e(TAG,"distance :"+distance);
                if (distance>5 || distance< -5){
                    isClick = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                int scrollX = getScrollX();
                index = (scrollX+ChildWidth/2)/ChildWidth;
                if (index<0){
                    index = 0;
                }else if (index>children-1){
                    index = children - 1;
                }
                if (isClick){
                    imageCycleListener.clickImageIndex(index);
                }else{
                    int dx = index*ChildWidth - scrollX;
                    scroller.startScroll(scrollX,0,dx,0);
                    postInvalidate();
                    imageCycleViewGroupListener.selectImage(index);
                }
                startCycleAuto();
                break;
            default:
                break;
        }
        return true;
    }

    public interface ImageCycleListener{
        void clickImageIndex(int postion);
    }

    public interface ImageCycleViewGroupListener{
        void selectImage(int index);
    }
}
