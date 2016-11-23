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
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryFragmentFactory;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryVocableManipulationFragment;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.provider.dao.DictionaryDAO;
import com.matteoveroni.wordsremember.utilities.GraphicsUtil;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;
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

    private RelativeLayout dictionaryManagementSmartphoneLayout;
    private RelativeLayout dictionaryManagementLargeLayout;
    private RelativeLayout dictionaryManagementLargeLandLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupView();

        if (savedInstanceState == null) {
            dictionaryCreateVocableFragment = DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANIPULATION);
            dictionaryManagementFragment = DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANAGEMENT);

            dictionaryDAO = new DictionaryDAO(this);
            menuInflater = getMenuInflater();

            testDictionaryDAOCRUDOperations();
            exportDatabaseOnSd();
        }

//        loadView();
    }


//    private void loadView() {
//        // Tablet with large screen
//        if (GraphicsUtil.isTablet(getApplicationContext())) {
//            if (GraphicsUtil.getOrientation(getApplicationContext()) == ORIENTATION_LANDSCAPE) {
//                loadFragment(dictionaryManagementFragment, R.id.dictionary_two_columns_management_container_large_land);
//                loadFragment(dictionaryCreateVocableFragment, R.id.dictionary_two_columns_manipulation_container_large_land);
//            } else {
//                loadFragment(dictionaryManagementFragment, R.id.dictionary_management_container);
//                loadFragment(dictionaryCreateVocableFragment, R.id.dictionary_manipulation_container);
//            }
//            // Smartphone
//        } else {
//            loadFragment(dictionaryManagementFragment, R.id.dictionary_management_container);
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_vocable:
                clearViews();
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

    private void setupView() {
        setContentView(R.layout.activity_dictionary_management_view);

        dictionaryManagementSmartphoneLayout = (RelativeLayout) findViewById(R.id.layout_dictionary_management_smartphone);
        dictionaryManagementLargeLayout = (RelativeLayout) findViewById(R.id.layout_dictionary_management_large);
        dictionaryManagementLargeLandLayout = (RelativeLayout) findViewById(R.id.layout_dictionary_management_large_land);

    }

    private void clearViews() {
        if (dictionaryManagementSmartphoneLayout != null) {
            dictionaryManagementSmartphoneLayout.removeAllViews();
        }
        if (dictionaryManagementLargeLayout != null) {
            dictionaryManagementLargeLayout.removeAllViews();
        }
        if (dictionaryManagementLargeLandLayout != null) {
            dictionaryManagementLargeLandLayout.removeAllViews();
        }
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
