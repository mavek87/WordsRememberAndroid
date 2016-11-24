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
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventDictionaryItemSelected;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventInformObserversOfItemSelected;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.factory.DictionaryFragmentFactory;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryManipulationFragment;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.provider.dao.DictionaryDAO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.matteoveroni.wordsremember.activities.dictionary_management.fragments.factory.DictionaryFragmentFactory.DictionaryFragmentType;

/**
 * Activity for handling the dictionary management.
 *
 * @author Matteo Veroni
 */

public class DictionaryManagementActivity extends AppCompatActivity {

    private static final String TAG = "A_DICTIONARY_MANAGE";

    private DictionaryDAO dictionaryDAO;

    private MenuInflater menuInflater;

    private DictionaryManagementFragment dictionaryManagementFragment;
    private DictionaryManipulationFragment dictionaryManipulationFragment;

    public DictionaryManagementActivity() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDictionaryItemSelected(EventDictionaryItemSelected event) {

        long itemSelectedID = event.getDictionaryItemIDSelected();

        if (itemSelectedID >= 0) {
            // scarico tutti i fragments in tutti i placeholders (o meglio solo quelli inutili)

            // load all fragments needed
            loadFragmentsInView();

            // TODO: Should I use another thread? Probably an Async Thread... maybe yes even if
            // every dao make use of content resolvers... (Search google => content resolvers async
            // thread for long queries )
            Word wordSelected = dictionaryDAO.getVocableById(itemSelectedID);

            // Send vocable selected to all the listening fragments
            EventBus.getDefault().postSticky(new EventInformObserversOfItemSelected(wordSelected));
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_management_view);

        if (savedInstanceState == null) {
            menuInflater = getMenuInflater();

            dictionaryManagementFragment =
                    (DictionaryManagementFragment) DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANAGEMENT);

            dictionaryManipulationFragment =
                    (DictionaryManipulationFragment) DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANIPULATION);

            dictionaryDAO = new DictionaryDAO(this);
            testDictionaryDAOCRUDOperations();
            exportDatabaseOnSd();

            loadFragment(dictionaryManagementFragment, R.id.dictionary_management_container);
        }

    }

    /***********************************************************************************************
     * MENU
     **********************************************************************************************/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_vocable:
                loadFragment(dictionaryManipulationFragment, R.id.dictionary_container_smartphone);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************************************/

    private void loadFragmentsInView() {
        // Tablet with large screen
        if (getResources().getBoolean(R.bool.LARGE_SCREEN)) {
            loadFragment(dictionaryManagementFragment, R.id.dictionary_management_container);
            loadFragment(dictionaryManipulationFragment, R.id.dictionary_manipulation_container);
        }
        // Smartphone
        else {
            loadFragment(dictionaryManagementFragment, R.id.dictionary_management_container);
        }
    }

    private void loadFragment(Fragment fragment, int containerID) {
        String fragmentToLoadTAG;

        if (fragment instanceof DictionaryManagementFragment) {
            fragmentToLoadTAG = DictionaryManagementFragment.TAG;
        } else if (fragment instanceof DictionaryManipulationFragment) {
            fragmentToLoadTAG = DictionaryManipulationFragment.TAG;
        } else {
            throw new RuntimeException("Something goes wrong. Fragment to load not recognized");
        }

        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//        if (currentFragment == null) {
        if (!fragment.isInLayout()) {
            fragmentTransaction.add(containerID, fragment, fragmentToLoadTAG);
        } else {
            fragmentTransaction.replace(containerID, fragment, fragmentToLoadTAG);
        }
        fragmentTransaction.commit();
        fragmentManager.executePendingTransactions();
    }

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
