package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.content.ContentValues;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;
import com.matteoveroni.wordsremember.provider.dao.DictionaryDAO;

public class DictionaryManagementActivity extends AppCompatActivity {

    private static final String TAG = "A_Dictionary_Management";

    private DictionaryDAO dictionaryDAO;

    Button btn_add_dictionary_vocable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dictionary_management);
        dictionaryDAO = new DictionaryDAO(getBaseContext());
        btn_add_dictionary_vocable = (Button) findViewById(R.id.dictionary_add_vocable);
        btn_add_dictionary_vocable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isVocablePresent = dictionaryDAO.isVocablePresent(new Word("test123"));
                Toast.makeText(getBaseContext(), "isVocablePresent? => " + isVocablePresent, Toast.LENGTH_LONG).show();
            }
        });
        testDb();
        if (savedInstanceState == null) {
            loadDictionaryManagementFragment();
        }
    }

    private void loadDictionaryManagementFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.dictionaryManagementContainer, new DictionaryManagementFragment(), DictionaryManagementFragment.TAG)
                .commit();
    }

    /**
     * TODO: remove this test methods
     */
    private void testDb() {
//        DictionaryDAO dictionaryDAO = new DictionaryDAO(getBaseContext());
//
//        Word vocable = new Word("test123");
//
////        dictionaryDAO.getDbManager().resetDatabase();
////        dictionaryDAO.removeVocable(vocable);
//
//        printIfVocableIsPresent(vocable, dictionaryDAO);
//        printAllVocablesFromDB(dictionaryDAO);
//        dictionaryDAO.saveVocable(vocable);
//        printAllVocablesFromDB(dictionaryDAO);
//        printIfVocableIsPresent(vocable, dictionaryDAO);
//
//        dictionaryDAO.getDbManager().exportDBOnSD();
    }

//    private void printAllVocablesFromDB(DictionaryDAO dictionaryDAO) {
//        for (Word vocable : dictionaryDAO.getAllVocablesList()) {
//            Log.i(TAG, vocable.getName());
//        }
//    }
//
//    private void printIfVocableIsPresent(Word vocable, DictionaryDAO dictionaryDAO) {
//        String logMessageVocablePresent = "Vocable " + vocable + " ";
//        if (dictionaryDAO.isVocablePresent(vocable)) {
//            logMessageVocablePresent += "is present";
//        } else {
//            logMessageVocablePresent += "is not present";
//        }
//        Log.i(TAG, logMessageVocablePresent);
//    }
}
