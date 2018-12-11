package com.mgumbs.diplomado.connect4;

public class BoardPoint {
    public enum aPlayer {RED,YELLOW,NONE}

    private int Id;
    private int Row;
    private int Column;
    private boolean Filled;
    private float x;
    private float y;
    private int Width;
    private int Height;
    private int CoinId;

    public int getCoinId() {
        return CoinId;
    }

    public void setCoinId(int coinId) {
        CoinId = coinId;
    }

    private aPlayer Player = aPlayer.NONE;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }


    public int getRow() {
        return Row;
    }

    public void setRow(int row) {
        Row = row;
    }

    public int getColumn() {
        return Column;
    }

    public void setColumn(int column) {
        Column = column;
    }

    public boolean isFilled() {
        return Filled;
    }

    public void setFilled(boolean filled) {
        Filled = filled;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public int getWidth() {return Width;
    }

    public void setWidth(int width) {Width = width;}

    public int getHeight() {
        return Height;
    }

    public void setHeight(int height) {
        Height = height;
    }

    public aPlayer getPlayer() {
        return Player;
    }

    public void setPlayer(aPlayer player) {
        Player = player;
    }

}
