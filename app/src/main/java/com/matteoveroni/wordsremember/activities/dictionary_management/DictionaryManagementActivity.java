package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private MenuInflater menuInflater;

    private Fragment currentFragment;

    private Fragment dictionaryCreateVocableFragment;
    private Fragment dictionaryManagementFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();

        if (savedInstanceState == null) {
            dictionaryCreateVocableFragment = DictionaryVocableManipulationFragment.getInstance();
            dictionaryManagementFragment = DictionaryManagementFragment.getInstance();

            dictionaryDAO = new DictionaryDAO(getBaseContext());
            menuInflater = getMenuInflater();

            testDictionaryDAOCRUDOperations();
            exportDatabaseOnSd();

            loadFragment(dictionaryManagementFragment);

//            loadDictionaryManagementFragment();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_vocable:
                loadFragment(dictionaryCreateVocableFragment);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment) {
        String fragmentToLoadTAG;

        if (fragment instanceof DictionaryVocableManipulationFragment) {
            fragmentToLoadTAG = "";
        } else if (fragment instanceof DictionaryManagementFragment) {
            fragmentToLoadTAG = DictionaryManagementFragment.TAG;
        } else {
            throw new RuntimeException("Something goes wrong. Fragment to load not recognized");
        }

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (currentFragment == null) {
            fragmentTransaction
                    .add(
                            R.id.dictionaryManagementContainer,
                            fragment,
                            fragmentToLoadTAG
                    );
        } else {
            fragmentTransaction.replace(
                    R.id.dictionaryManagementContainer,
                    fragment,
                    fragmentToLoadTAG
            );
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        currentFragment = fragment;
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
