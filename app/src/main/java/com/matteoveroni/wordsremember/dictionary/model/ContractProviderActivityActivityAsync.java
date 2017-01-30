package com.matteoveroni.wordsremember.dictionary.model;

import android.app.ListActivity;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.widget.SimpleCursorAdapter;

/**
 * https://www.youtube.com/watch?v=0fHQd-QrVwU&t=1725s&index=26&list=PLZ9NgFYEMxp50tvT8806xllaCbd31DpDy
 * @author Matteo Veroni
 */
public class ContractProviderActivityActivityAsync extends ListActivity {

    private SimpleCursorAdapter adapter;

    abstract class CompletionHandler extends AsyncQueryHandler {
        public CompletionHandler(ContentResolver contentResolver) {
            super(getContentResolver());
        }

        abstract public void execute();
    }
}
