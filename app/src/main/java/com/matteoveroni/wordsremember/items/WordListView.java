package com.matteoveroni.wordsremember.items;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.model.Word;

public class WordListView extends RelativeLayout {

    private TextView lbl_wordName;

    public WordListView(Context context) {
        super(context);
        inflateView(context);
    }

    public WordListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflateView(context);
    }

    public WordListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context);
    }

    public void draw(Word word) {
        lbl_wordName.setText(word.getName());
    }

    private void inflateView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.item_word_list, this, true);
        lbl_wordName = (TextView) findViewById(R.id.lbl_wordName);
    }

}