package com.mgumbs.diplomado.connect4;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.generateViewId;


public class GameActivity extends AppCompatActivity {

    private static float x = 0;
    private List<BoardPoint> Board;
    private BoardPoint.aPlayer turn = BoardPoint.aPlayer.NONE;
    private boolean winner = false;
    private int winnerplayercoin = 0;
    private int winnernotification = 0;
    private int coinheight = 0;
    private int coinWidth = 0;
    private int viewWidth = 0;
    private int viewHeight = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /* do this in onCreate */
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
        mAccel = 0.00f;
        mAccelCurrent = SensorManager.GRAVITY_EARTH;
        mAccelLast = SensorManager.GRAVITY_EARTH;

        final TableLayout tl = findViewById(R.id.tvLay);
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
                    viewWidth = tl.getWidth();
                    viewHeight = tl.getHeight();
                    coinWidth = viewWidth / 8;
                    coinheight = viewWidth / 7;
                    drawBoard(viewWidth, viewHeight);
                }
            });
        }


    }

    private void drawBoard(int w, int h){
        TableRow row;
        TableLayout tlayout = findViewById(R.id.tvLay);


        this.Board = new ArrayList<>();

        for (int r = 0; r < 6; r ++) {
            row = new TableRow (this.getApplicationContext());
            row.setPadding(0,0,0,0);
            for (int c = 0; c < 7; c ++){
                ImageView brdPoint = new ImageView(getApplicationContext());
                brdPoint.setId(generateViewId());
                brdPoint.setImageDrawable(getDrawable(R.drawable.board_point));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                brdPoint.setLayoutParams(params);
                brdPoint.getLayoutParams().width = w / 7;
                brdPoint.getLayoutParams().height = brdPoint.getLayoutParams().width;
                brdPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hitPoint(v);
                    }
                });
                row.addView(brdPoint);

                // Logical Matrix
                BoardPoint aPoint = new BoardPoint();
                aPoint.setId(brdPoint.getId());
                aPoint.setRow(r);
                aPoint.setColumn(c);
                aPoint.setFilled(false);
                aPoint.setPlayer(BoardPoint.aPlayer.NONE);
                aPoint.setX(brdPoint.getX());
                aPoint.setY(brdPoint.getY());
                aPoint.setWidth(brdPoint.getLayoutParams().width);
                aPoint.setHeight(brdPoint.getLayoutParams().height);

                this.Board.add(aPoint);
            }
            tlayout.addView(row);
        }
    }

    public void looseCoin(BoardPoint aPoint, int speed){
        int outside = findViewById(R.id.GameLayout).getHeight() + 200;
        ImageView aCoin = findViewById(aPoint.getCoinId());
        ObjectAnimator mover = ObjectAnimator.ofFloat(aCoin, "translationY", aCoin.getY(), outside);
        mover.setDuration(speed);
        mover.start();
    }

    public void dropCoin(BoardPoint toPoint, int speed){
        ImageView aCoin = new ImageView(this);
        aCoin.setImageDrawable(getDrawable(getTurnCoin()));
        TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
        aCoin.setLayoutParams(params);
        aCoin.getLayoutParams().width = toPoint.getWidth();
        aCoin.getLayoutParams().height = toPoint.getHeight();
        ImageView iv = findViewById(toPoint.getId());
        aCoin.setId(generateViewId());
        toPoint.setCoinId(aCoin.getId());

        int [] location = new int[2] ;
        iv.getLocationInWindow(location);

        aCoin.setX(location[0]);
        aCoin.setY(200);

        DisplayMetrics dm = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(dm);
        ConstraintLayout tlayout =  findViewById(R.id.GameLayout);
        int topOffset = dm.heightPixels - tlayout.getMeasuredHeight();

        tlayout.addView(aCoin, 1);

        ObjectAnimator mover = ObjectAnimator.ofFloat(aCoin, "translationY", 0f, location[1]-topOffset);
        mover.setDuration(speed);
        mover.start();
    }

    public void playwinner(){

//        USED TO ADD A BORDER TO THE IMAGE
//        GradientDrawable border = new GradientDrawable();
//        border.setColor(0xFFFFFFFF); //white background
//        border.setStroke(1, 0xFF000000); //black border with full opacity

        ConstraintLayout tlayout =  findViewById(R.id.GameLayout);
        ImageView winnerPlayer = new ImageView(this);
        winnerPlayer.setId(generateViewId());
        winnerplayercoin = winnerPlayer.getId();
        winnerPlayer.setImageDrawable(getDrawable(getWinnerCoin()));
//        USED TO ADD A BORDER TO THE IMAGE
//        winnerPlayer.setBackground(border);

        ConstraintSet cSet = new ConstraintSet();
        cSet.clone(tlayout);

        tlayout.addView(winnerPlayer);

        cSet.connect(winnerPlayer.getId(), ConstraintSet.TOP, R.id.c4Logo, ConstraintSet.BOTTOM, 0);
        cSet.connect(winnerPlayer.getId(), ConstraintSet.LEFT, R.id.c4Logo, ConstraintSet.LEFT, 0);
        cSet.constrainHeight(winnerPlayer.getId(), (int) (coinheight * 1.5));
        cSet.constrainWidth(winnerPlayer.getId(), (int) (coinWidth * 1.5));

        ImageView winner = new ImageView(this);
        winner.setId(generateViewId());
        winnernotification = winner.getId();
        winner.setImageDrawable(getDrawable(R.drawable.win));

        tlayout.addView(winner);
        cSet.connect(winner.getId(), ConstraintSet.TOP, R.id.c4Logo, ConstraintSet.BOTTOM, 0);
        cSet.connect(winner.getId(), ConstraintSet.LEFT, winnerPlayer.getId(), ConstraintSet.RIGHT, 0);
        cSet.constrainHeight(winner.getId(), 300);
        cSet.constrainWidth(winner.getId(), 800);

        cSet.applyTo(tlayout);
        animate(findViewById(winnerPlayer.getId()));
    }


    public void setWinnerSequence(List<Integer> sequence){
        for (Integer id : sequence){
            ((ImageView) findViewById(id)).setImageDrawable(getDrawable(getWinnerCoin()));
        }

    }

    private class PlayersCount {
        int RedCount = 0;
        int YellowCount = 0;
    }

    public void validateSequence(BoardPoint aPoint,  PlayersCount playersCount, List<Integer> sequencelist){
        switch (aPoint.getPlayer()) {
            case NONE:
                playersCount.RedCount = 0;
                playersCount.YellowCount = 0;
                break;
            case RED:
                playersCount.RedCount ++;
                if (playersCount.YellowCount > 0) {
                    playersCount.YellowCount = 0;
                    sequencelist.clear();
                } // if
                sequencelist.add(aPoint.getCoinId());
                break;
            case YELLOW:
                playersCount.YellowCount ++;
                if (playersCount.RedCount > 0) {
                    playersCount.RedCount = 0;
                    sequencelist.clear();
                } // if
                sequencelist.add(aPoint.getCoinId());
        } // switch
    }

    public boolean isWinner() {
        PlayersCount playersCount = new PlayersCount();
        List<Integer> winnerSequence = new ArrayList<>();

        // by row
        for (int r = 6; r >= 0; r --){
            playersCount.RedCount = 0;
            playersCount.YellowCount = 0;
            winnerSequence.clear();
            for (int c = 0; c < 7; c ++){
                for (BoardPoint aPoint : Board) {
                    if (aPoint.getRow() == r && aPoint.getColumn() == c) {
                        validateSequence(aPoint, playersCount, winnerSequence);
                    } // if
                } // for aPoint
                winner = (playersCount.RedCount >= 4 || playersCount.YellowCount >= 4);
                if (winner) {
                    setWinnerSequence(winnerSequence);
                    return (winner);
                } // if
            } // for c
        } // for r

        // by column
        for (int c = 0; c < 7; c ++){
            playersCount.RedCount = 0;
            playersCount.YellowCount = 0;
            winnerSequence.clear();
            for (int r = 6; r >= 0; r --) {
                for (BoardPoint aPoint : Board) {
                    if (aPoint.getRow() == r && aPoint.getColumn() == c) {
                        validateSequence(aPoint, playersCount, winnerSequence);
                    } // if
                } // for aPoint
                winner = (playersCount.RedCount >= 4 || playersCount.YellowCount >= 4);
                if (winner) {
                    setWinnerSequence(winnerSequence);
                    return (winner);
                }
            } // for r
        } // for c

        // diagonally - right
        int n = 7;
        for (int slice = 0; slice < 2 * n - 1; ++slice) {
           int z = (slice < n) ? 0 : slice - n + 1;
            playersCount.RedCount = 0;
            playersCount.YellowCount = 0;
            winnerSequence.clear();
            for (int j = z; j <= slice - z; ++j) {
                for (BoardPoint aPoint : Board){
                    if (aPoint.getRow() == (slice - j) &&  aPoint.getColumn() == (j)){
                        validateSequence(aPoint, playersCount, winnerSequence);
                    } // if
                } // for boardpoint
                winner = (playersCount.RedCount >= 4 || playersCount.YellowCount >= 4);
                if (winner) {
                    setWinnerSequence(winnerSequence);
                    return (winner);
                } // if
            }// for j
        } // for slice

        // diagonally - left

        for (int slice = 0; slice < 2 * n - 1; ++slice) {
            int z = (slice < n) ? 0 : slice - n + 1;
            playersCount.RedCount = 0;
            playersCount.YellowCount = 0;
            winnerSequence.clear();
            for (int j = z; j <= slice - z; ++j) {
                for (BoardPoint aPoint : Board){
                    if (aPoint.getRow() == j && aPoint.getColumn() == ((n-1) - (slice - j))){
                        validateSequence(aPoint, playersCount, winnerSequence);
                    } // if
                } // for boardpoint
                winner = (playersCount.RedCount >= 4 || playersCount.YellowCount >= 4);
                if (winner) {
                    setWinnerSequence(winnerSequence);
                    return (winner);
                } // if
            }// for j
        } // for slice

        return winner;
    }

    public BoardPoint availablePoint(int col){
        for (int i = 6; i >= 0; i --){
            for (BoardPoint aPoint : this.Board){
                if (aPoint.getRow() == i && aPoint.getColumn() == col && !aPoint.isFilled()){
                    return aPoint;
                }
            }
        }
        return null;
    }


    public void resetActivate(View view){
        if (!winner){
            new AlertDialog.Builder(this)
                    .setTitle("Confirm Reset")
                    .setMessage("Do you really want to reset the game?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            doReset();
                        }})
                    .setNegativeButton(android.R.string.no, null).show();
        }
        else {doReset();}

    }

    public void doReset(){
        Toast.makeText(this, "Reset Board in Progress...", Toast.LENGTH_SHORT).show();

        for (BoardPoint aPoint : this.Board){
            if (aPoint.isFilled()){
                looseCoin(aPoint,700);
                aPoint.setFilled(false);
                aPoint.setCoinId(0);
            }
        }

        winner = false;


        findViewById(R.id.c4Logo).postDelayed(new Runnable() {
            @Override
            public void run() {
                recreate();
            }
        }, 500);

//        findViewById(R.id.tvLay).requestLayout();
        //drawBoard(viewWidth, viewHeight);


    }

    public void hitPoint(View view){
        if (winner) return;
        for (BoardPoint aPoint : this.Board) {
            if (aPoint.getId() == view.getId()) {
                setTurn();
                aPoint = availablePoint(aPoint.getColumn());
                if (aPoint != null) {
                    aPoint.setFilled(true);
                    aPoint.setPlayer(this.turn);
                    dropCoin(aPoint,900);
                    if (isWinner()) playwinner();
                }
            }
        }
    }

    public void setTurn(){
        switch (this.turn)
        {
            case NONE: case RED:
                this.turn = BoardPoint.aPlayer.YELLOW;
                break;

            default:
                this.turn = BoardPoint.aPlayer.RED;
        }
    }

    public BoardPoint.aPlayer getTurn() {
        return turn;
    }

    public int getTurnCoin() {
        switch (getTurn())
        {
            case RED:
                return R.drawable.coin_red;

            default:
                return R.drawable.coin_yellow;
        }
    }

    public int getWinnerCoin() {
        switch (getTurn())
        {
            case RED:
                return R.drawable.coin_red_win;

            default:
                return R.drawable.coin_yellow_win;
        }
    }

    public void animate(View view){
        float dest;
        ImageView aniView = (ImageView) view;
        dest = 360;
        if (aniView.getRotation() == 360) {
            System.out.println(aniView.getAlpha());
            dest = 0;
        }
        ObjectAnimator animation1 = ObjectAnimator.ofFloat(aniView,
                "rotation", dest);
        animation1.setDuration(2500);
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


//        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(aniView, "alpha",
//                0f);
//        fadeOut.setDuration(2000);
//        ObjectAnimator mover = ObjectAnimator.ofFloat(aniView,
//                "translationY", -500f, ((View)view.getParent()).getHeight()-view.getHeight());
//        mover.setDuration(900);
//        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(aniView, "alpha",
//                0f, 1f);
//        fadeIn.setDuration(2000);
//        AnimatorSet animatorSet = new AnimatorSet();
//
//        animatorSet.play(mover).with(fadeIn).after(fadeOut);
//        animatorSet.start();
    }

    /* put this into your activity class */
    private SensorManager mSensorManager;
    private float mAccel; // acceleration apart from gravity
    private float mAccelCurrent; // current acceleration including gravity
    private float mAccelLast; // last acceleration including gravity

    private final SensorEventListener mSensorListener = new SensorEventListener() {

        public void onSensorChanged(SensorEvent se) {
            float x = se.values[0];
            float y = se.values[1];
            float z = se.values[2];
            mAccelLast = mAccelCurrent;
            mAccelCurrent = (float) Math.sqrt((double) (x*x + y*y + z*z));
            float delta = mAccelCurrent - mAccelLast;
            mAccel = mAccel * 0.9f + delta; // perform low-cut filter

            if (mAccel > 12) {
                Toast toast = Toast.makeText(getApplicationContext(), "Device has shaken.", Toast.LENGTH_SHORT);
                toast.show();
                resetActivate(findViewById(R.id.GameLayout));
            }
        }

        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener, mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }
}
