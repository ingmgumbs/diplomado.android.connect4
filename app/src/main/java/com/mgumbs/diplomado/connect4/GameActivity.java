package com.mgumbs.diplomado.connect4;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.view.View.generateViewId;


public class GameActivity extends AppCompatActivity {

    private static float x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        final TableLayout tl = (TableLayout)findViewById(R.id.tvLay);
        ViewTreeObserver viewTreeObserver = tl.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < 16) {
                        tl.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        tl.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    int viewWidth = tl.getWidth();
                    int viewHeight = tl.getHeight();
                    drawBoard(viewWidth, viewHeight);
                }
            });
        }

    }

    private void drawBoard(int w, int h){
        TableRow row;// = new TableRow(this.getApplicationContext());
        TableLayout tlayout = (TableLayout) findViewById(R.id.tvLay);

        //tlayout.setGravity(Gravity.CENTER);
//        tlayout.setBackgroundResource(R.color.colorPrimary);

        for (int r = 0; r < 6; r ++) {
            row = new TableRow (this.getApplicationContext());
            row.setPadding(0,0,0,0);
            row.setBackgroundColor(Color.GREEN);
            for (int c = 0; c < 7; c ++){
               ImageView brdPoint = new ImageView(getApplicationContext());
               brdPoint.setId(generateViewId());
               brdPoint.setImageDrawable(getDrawable(R.drawable.board_point));
               TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
               brdPoint.setLayoutParams(params);
               brdPoint.getLayoutParams().width = w / 7;
               brdPoint.getLayoutParams().height = brdPoint.getLayoutParams().width;
               row.addView(brdPoint);
            }
            tlayout.addView(row);
        }
    }

    public void animate(View view){
        float dest = 0;
        ImageView aniView = (ImageView) findViewById(R.id.imageView);
        dest = 360;
        if (aniView.getRotation() == 360) {
            System.out.println(aniView.getAlpha());
            dest = 0;
        }
        ObjectAnimator animation1 = ObjectAnimator.ofFloat(aniView,
                "rotation", dest);
        animation1.setDuration(2000);
        animation1.start();

/////
        Paint paint = new Paint();
//        TextView aniTextView = (TextView) findViewById(R.id.textView);
//
//        if (x == 0) x = aniTextView.getX();
//
//        float measureTextCenter = paint.measureText(aniTextView.getText()
//                .toString());
//        dest = 0 - measureTextCenter;
//        if (aniTextView.getX() < 0) {
//            dest = x;
//        }
//        ObjectAnimator animation2 = ObjectAnimator.ofFloat(aniTextView,
//                "x", dest);
//        animation2.setDuration(2000);
//        animation2.start();


//////////////


        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(aniView, "alpha",
                0f);
        fadeOut.setDuration(2000);
        ObjectAnimator mover = ObjectAnimator.ofFloat(aniView,
                "translationY", -500f, ((View)view.getParent()).getHeight()-view.getHeight());
        mover.setDuration(900);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(aniView, "alpha",
                0f, 1f);
        fadeIn.setDuration(2000);
        AnimatorSet animatorSet = new AnimatorSet();

        animatorSet.play(mover).with(fadeIn).after(fadeOut);
        animatorSet.start();
    }
}
