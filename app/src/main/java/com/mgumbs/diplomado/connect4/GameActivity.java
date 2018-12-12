package com.mgumbs.diplomado.connect4;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
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
import java.util.ArrayList;
import java.util.List;

import static android.view.View.generateViewId;


public class GameActivity extends AppCompatActivity {

    private static float x = 0;
    private List<BoardPoint> Board;
    private BoardPoint.aPlayer turn = BoardPoint.aPlayer.NONE;
    private boolean winner = false;
    private int coinheight = 0;
    private int coinWidth = 0;
    private int viewWidth = 0;
    private int viewHeight = 0;


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
                    viewWidth = tl.getWidth();
                    viewHeight = tl.getHeight();
                    coinWidth = viewWidth / 8;
                    coinheight = viewWidth / 7;
                }
            });
        }

        drawBoard(viewWidth, viewHeight);

    }

    private void drawBoard(int w, int h){
        TableRow row;// = new TableRow(this.getApplicationContext());
        TableLayout tlayout = (TableLayout) findViewById(R.id.tvLay);


        this.Board = new ArrayList<BoardPoint>();
        //tlayout.setGravity(Gravity.CENTER);

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

               TextView tv = new TextView(this);
               tv.setText(r + "-" + c);
               //row.addView(tv);
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
        /*Drawable adrawable = aCoin.getDrawable();
        aCoin.setImageDrawable(null);
        if (adrawable instanceof BitmapDrawable){
            BitmapDrawable abitmap = (BitmapDrawable) adrawable;
            abitmap.getBitmap().recycle();
        }*/
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
        int topOffset = dm.heightPixels - findViewById(R.id.GameLayout).getMeasuredHeight();

        ConstraintLayout tlayout =  findViewById(R.id.GameLayout);
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

//        cSet.applyTo(tlayout);

        ImageView winner = new ImageView(this);
        winner.setId(generateViewId());
        winner.setImageDrawable(getDrawable(R.drawable.win));

        tlayout.addView(winner);
        cSet.connect(winner.getId(), ConstraintSet.TOP, R.id.c4Logo, ConstraintSet.BOTTOM, 0);
        cSet.connect(winner.getId(), ConstraintSet.LEFT, winnerPlayer.getId(), ConstraintSet.RIGHT, 0);
        cSet.constrainHeight(winner.getId(), 300);
        cSet.constrainWidth(winner.getId(), 800);

        cSet.applyTo(tlayout);
        animate(findViewById(winnerPlayer.getId()));
//        winnerPlayer.setY(100);
//        tlayout.addView(winner);


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


    public void doReset(View view){
        BoardPoint lostPoint = new BoardPoint();
        for (BoardPoint aPoint : this.Board){
            if (aPoint.isFilled()){
                looseCoin(aPoint,500);
                aPoint.setFilled(false);
                aPoint.setCoinId(0);
            }
        }
//        Board.clear();
        winner = false;
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
        float dest = 0;
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
}
