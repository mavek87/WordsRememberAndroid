package com.matteoveroni.wordsremember.items;

import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Checkable;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.model.Word;

import static android.graphics.Color.GRAY;
import static android.graphics.Color.TRANSPARENT;
import static android.graphics.Color.WHITE;

public class WordListViewItem extends RelativeLayout implements Checkable {

    private final TextView lbl_wordName;
    private boolean isChecked = false;

    public WordListViewItem(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_word_list_view, this, true);
        lbl_wordName = (TextView) findViewById(R.id.lbl_wordName);
    }

    public WordListViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_word_list_view, this, true);
        lbl_wordName = (TextView) findViewById(R.id.lbl_wordName);
    }

    public WordListViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_word_list_view, this, true);
        lbl_wordName = (TextView) findViewById(R.id.lbl_wordName);
    }

    public void draw(Word word) {
        draw(word.getName());
    }

    public void draw(String wordName) {
        lbl_wordName.setText(wordName);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
        changeColor(isChecked);
    }

    public void toggle() {
        this.isChecked = !this.isChecked;
        changeColor(this.isChecked);
    }

    private void changeColor(boolean isChecked) {
        if (isChecked) {
            setBackgroundColor(GRAY);
            lbl_wordName.setTextColor(ContextCompat.getColor(getContext(), android.R.color.white));
        } else {
            setBackgroundColor(TRANSPARENT);
            lbl_wordName.setTextColor(ContextCompat.getColor(getContext(), android.R.color.black));
        }
    }
}