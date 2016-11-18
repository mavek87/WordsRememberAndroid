package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.provider.dao.DictionaryDAO;

/**
 * Activity for handling the dictionary management.
 *
 * @author Matteo Veroni
 */

public class DictionaryManagementActivity extends AppCompatActivity {

    private static final String TAG = "A_DICTIONARY_MANAGER";

    private DictionaryDAO dictionaryDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();
        dictionaryDAO = new DictionaryDAO(getBaseContext());
        testDictionaryDAOCRUDOperations();
        exportDatabaseOnSd();

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

    private void setupView() {
        setContentView(R.layout.activity_dictionary_management);

        Button btn_addDictionaryVocable = (Button) findViewById(R.id.dictionary_add_vocable);
        btn_addDictionaryVocable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isVocablePresent = dictionaryDAO.isVocablePresent(new Word("test123"));
                Toast.makeText(getBaseContext(), "isVocablePresent? => " + isVocablePresent, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * TODO: remove this test methods
     */
    private void testDictionaryDAOCRUDOperations() {
        Word newVocableToSave = new Word("test123");
        long savedVocableId = dictionaryDAO.saveVocable(newVocableToSave);
        if (savedVocableId < 0) {
            Toast.makeText(this, "Vocable " + newVocableToSave.getName() + " not inserted.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vocable " + newVocableToSave.getName() + " inserted. His ID in the database is => " + savedVocableId, Toast.LENGTH_SHORT).show();
        }
    }

    private void exportDatabaseOnSd() {
        DatabaseManager.getInstance(getBaseContext()).exportDBOnSD();
    }
}
