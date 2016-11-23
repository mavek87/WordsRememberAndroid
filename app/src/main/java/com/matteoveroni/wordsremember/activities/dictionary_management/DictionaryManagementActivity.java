package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryFragmentFactory;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryVocableManipulationFragment;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.provider.dao.DictionaryDAO;

import static com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryFragmentFactory.DictionaryFragmentType;

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

        setContentView(R.layout.activity_dictionary_management_view);

        if (savedInstanceState == null) {
            dictionaryCreateVocableFragment = DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANIPULATION);
            dictionaryManagementFragment = DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANAGEMENT);

            dictionaryDAO = new DictionaryDAO(this);
            menuInflater = getMenuInflater();

            testDictionaryDAOCRUDOperations();
            exportDatabaseOnSd();
        }

        loadFragmentsInView();
    }

    private void loadFragmentsInView() {
        // Tablet with large screen
        if (getResources().getBoolean(R.bool.LARGE_SCREEN)) {
            loadFragment(dictionaryManagementFragment, R.id.dictionary_management_container);
            loadFragment(dictionaryCreateVocableFragment, R.id.dictionary_manipulation_container);
        }
        // Smartphone
        else{
            loadFragment(dictionaryManagementFragment, R.id.dictionary_management_container);
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
                loadFragment(dictionaryCreateVocableFragment, R.id.dictionary_container_smartphone);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadFragment(Fragment fragment, int containerID) {
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
                            containerID,
                            fragment,
                            fragmentToLoadTAG
                    );
        } else {
            fragmentTransaction.replace(
                    containerID,
                    fragment,
                    fragmentToLoadTAG
            );
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
        currentFragment = fragment;
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
