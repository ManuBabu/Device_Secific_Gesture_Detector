package com.example.admin.scrollview;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static String TAG = MainActivity.class.getSimpleName();

    private LinearLayout mTopLayout;
    private LinearLayout mMiddleLayout;
    private LinearLayout mBottomLayout;

    private ScrollView mScrollView;

    private boolean possitionTop;
    private boolean possitionMiddle = true;

    int mLayoutHeight;
    float mDeviceHeight;
    Button b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);


        // final float height=displayMetrics.heightPixels/displayMetrics.xdpi;

        // device height in pixels
        mDeviceHeight = displayMetrics.heightPixels;
        mLayoutHeight = (int) mDeviceHeight / 3;

        mScrollView = (ScrollView) findViewById(R.id.scrv);

        mTopLayout = (LinearLayout) findViewById(R.id.top);
        mMiddleLayout = (LinearLayout) findViewById(R.id.middle);
        mBottomLayout = (LinearLayout) findViewById(R.id.bottom);

        mTopLayout.setLayoutParams(new LinearLayout.LayoutParams(mTopLayout
                .getLayoutParams().width, mLayoutHeight));
        mBottomLayout.setLayoutParams(new LinearLayout.LayoutParams(
                mBottomLayout.getLayoutParams().width, mLayoutHeight));
        mMiddleLayout.setLayoutParams(new LinearLayout.LayoutParams(
                mMiddleLayout.getLayoutParams().width, (int) mDeviceHeight));

        mScrollView.setHorizontalFadingEdgeEnabled(false);
        mScrollView.setVerticalFadingEdgeEnabled(false);
        mScrollView.post(new Runnable() {
            public void run() {
                mScrollView.scrollTo(0, mLayoutHeight);
            }
        });

        b = (Button) findViewById(R.id.button2);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("try", "button clicked");
                mScrollView.post(new Runnable() {
                    public void run() {
                        mScrollView.scrollTo(0, mLayoutHeight);
                    }
                });
            }
        });

        // findViewById(R.id.button).setOnClickListener(new
        // View.OnClickListener() {
        //
        // @Override
        // public void onClick(View v) {
        // Toast.makeText(ScrollLayoutActivity.this, "height : " +
        // mDeviceHeight, Toast.LENGTH_SHORT).show();
        // }
        // });

        Toast.makeText(this, "Device Height : " + mDeviceHeight,
                Toast.LENGTH_SHORT).show();

    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        return mGestureDetector.onTouchEvent(ev);
    }

    SimpleOnGestureListener simpleOnGestureListener = new SimpleOnGestureListener() {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            String swipe = "";
            float sensitvity = 100;

            if ((e1.getX() - e2.getX()) > sensitvity) {
                swipe += "Swipe Left\n";
            } else if ((e2.getX() - e1.getX()) > sensitvity) {
                swipe += "Swipe Right\n";
            } else {
                swipe += "\n";
            }

            if ((e1.getY() - e2.getY()) > sensitvity) {
                swipe += "Swipe Up\n";
                if (!possitionMiddle && possitionTop) {
                    mScrollView.scrollBy(0, mLayoutHeight);
                    possitionTop = false;
                    possitionMiddle = true;
                } else if (possitionMiddle && !possitionTop) {
                    mScrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    possitionTop = false;
                    possitionMiddle = false;
                }
            } else if ((e2.getY() - e1.getY()) > sensitvity) {
                swipe += "Swipe Down\n";
                if (possitionMiddle && !possitionTop) {
                    mScrollView.fullScroll(ScrollView.FOCUS_UP);
                    possitionTop = true;
                    possitionMiddle = false;
                }
                if (!possitionMiddle && !possitionTop) {
                    mScrollView
                            .scrollTo(mScrollView.getBottom(), mLayoutHeight);
                    possitionTop = false;
                    possitionMiddle = true;
                }
            } else {
                swipe += "\n";
            }
            Log.d(TAG, swipe);
            return super.onFling(e1, e2, velocityX, velocityY);
        }

    };

    GestureDetector mGestureDetector = new GestureDetector(
            simpleOnGestureListener);
}