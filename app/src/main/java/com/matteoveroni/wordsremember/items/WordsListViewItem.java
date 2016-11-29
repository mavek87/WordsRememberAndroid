package com.matteoveroni.wordsremember.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matteoveroni.wordsremember.R;

public class WordsListViewItem extends RelativeLayout {

    private final TextView lbl_wordName;

    public WordsListViewItem(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.item_word_list_view, this, true);
        lbl_wordName = (TextView) findViewById(R.id.lbl_wordName);
    }

    public WordsListViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.item_word_list_view, this, true);
        lbl_wordName = (TextView) findViewById(R.id.lbl_wordName);
    }

    public WordsListViewItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.item_word_list_view, this, true);
        lbl_wordName = (TextView) findViewById(R.id.lbl_wordName);
    }

    public void draw(String wordName) {
        lbl_wordName.setText(wordName);
    }
}