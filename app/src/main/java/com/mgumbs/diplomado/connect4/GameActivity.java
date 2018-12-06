package com.mgumbs.diplomado.connect4;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.view.View.generateViewId;

public class GameActivity extends AppCompatActivity {

    private static float x = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        drawBoard();
    }

    private void drawBoard(){
        ConstraintLayout aLayout = findViewById(R.id.GameLayout);
        int w = 100;//((View)aLayout).getWidth() / 6;
        int h = w;
        int lastid = 0;
        int topid = R.id.imageView;
        for (int row = 0; row < 6; row ++){
            for (int col = 0; col < 7; col ++){
                ImageView brdPoint = new ImageView(getApplicationContext());
                brdPoint.setId(generateViewId());
                ConstraintSet cSet = new ConstraintSet();
                cSet.clone(aLayout);
                brdPoint.setImageDrawable(getDrawable(R.drawable.board_point));
                if (lastid == 0) lastid = aLayout.getId();
                cSet.connect(lastid,ConstraintSet.RIGHT,aLayout.getId(),ConstraintSet.RIGHT,1);
                cSet.connect(topid,ConstraintSet.BOTTOM,aLayout.getId(),ConstraintSet.BOTTOM,1);

                ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
//                params.constrain TOP = R.id.imageView;
//                params.addRule(RelativeLayout.RIGHT_OF, lastid);
                brdPoint.setLayoutParams(params);
                brdPoint.getLayoutParams().height = h;
                brdPoint.getLayoutParams().width = w;
                aLayout.addView(brdPoint);
                cSet.applyTo(aLayout);
                lastid = brdPoint.getId();

            }
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
