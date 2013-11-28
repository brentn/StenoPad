package com.brentandjody.stenopad.Translation;

/**
 * Created by brentn on 18/11/13.
 * a class to store the state of the translation stream
 */
public class State {

    public static final int NORMAL=0;
    public static final int CAPITAL=1;
    public static final int LOWER=2;

    private boolean glue=false;
    private int capitalization=NORMAL;
    private boolean attachStart=false;
    private boolean attachEnd=false;
    // backspaces and replace together allow a stroke to be undone
    private int backspaces=0;
    private String replay=null;

    public State() {
    }

    public State setGlue() {
        glue = true;
        return this;
    }
    public State setCapitalize() {
        capitalization = CAPITAL;
        return this;
    }
    public State setLowercase() {
        capitalization = LOWER;
        return this;
    }
    public State attachStart() {
        attachStart=true;
        return this;
    }
    public State attachEnd() {
        attachEnd=true;
        return this;
    }
    public State addBackspaces(int x) {
        backspaces+=x;
        return this;
    }
    public State setReplay(String s) {
        replay=s;
        return this;
    }


    public boolean hasGlue() {
        return glue;
    }
    public boolean isCapitalized() {
        return capitalization==CAPITAL;
    }
    public boolean isLowercased() {
        return capitalization==LOWER;
    }
    public boolean isAttachedStart() {
        return attachStart;
    }
    public boolean isAttachedEnd() {
        return attachEnd;
    }
    public int getBackspaces() {
        return backspaces;
    }
    public String getReplay() {if (replay==null) return ""; else return replay; }
}
