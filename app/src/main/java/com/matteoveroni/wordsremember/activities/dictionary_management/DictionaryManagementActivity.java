package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventVocableSelected;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventNotifySelectedVocableToObservers;
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

    private FrameLayout dictionaryManagementContainer;
    private FrameLayout dictionaryManipulationContainer;

    private boolean isVocableSelected = false;

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

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
    public void onDictionaryItemSelected(EventVocableSelected event) {

        Word selectedVocable = new Word("ciaone");
        long selectedVocableID = event.getSelectedVocableID();

        if (selectedVocableID >= 0) {
            isVocableSelected = true;
//            // TODO: Async Task???
//            selectedVocable = dictionaryDAO.getVocableById(selectedVocableID);
        } else {
            isVocableSelected = false;
//            selectedVocable = null;
        }
        Toast.makeText(this, "isVocableSelected " + isVocableSelected, Toast.LENGTH_SHORT).show();

        // Send vocable selected to all the listening fragments
        EventBus.getDefault().postSticky(new EventNotifySelectedVocableToObservers(selectedVocable));

        updateViewAndLayout();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_management_view);

        if (savedInstanceState == null) {
            Toast.makeText(this, "savedInstanceState == null", Toast.LENGTH_SHORT).show();

            dictionaryManagementContainer = (FrameLayout) findViewById(R.id.dictionary_management_container);
            dictionaryManipulationContainer = (FrameLayout) findViewById(R.id.dictionary_manipulation_container);

            menuInflater = getMenuInflater();

            dictionaryManagementFragment =
                    (DictionaryManagementFragment) DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANAGEMENT);

            dictionaryManipulationFragment =
                    (DictionaryManipulationFragment) DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANIPULATION);

            dictionaryDAO = new DictionaryDAO(this);
            testDictionaryDAOCRUDOperations();
            exportDatabaseOnSd();

            addFragment(dictionaryManagementContainer, dictionaryManagementFragment);

//            getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//                public void onBackStackChanged() {
////                    loadFragmentsInsideView();
////                    getSupportFragmentManager().beginTransaction().remove(dictionaryManipulationFragment).commit();
////                    getSupportFragmentManager().executePendingTransactions();
//                    dictionaryManipulationContainer.removeAllViews();
//
//                }
//            });
            updateViewAndLayout();

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
                addFragment(dictionaryManipulationContainer, dictionaryManipulationFragment);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************************************/

    private void updateViewAndLayout() {
        loadFragmentsInsideView();
        drawActivityViewLayout();
    }

    private void loadFragmentsInsideView() {
        if (!dictionaryManagementFragment.isAdded())
            addFragment(dictionaryManagementContainer, dictionaryManagementFragment);

        if (isLargeScreenDevice() && isVocableSelected) {
            // LARGE SCREEN
            if (!dictionaryManipulationFragment.isAdded())
                addFragment(dictionaryManipulationContainer, dictionaryManipulationFragment);

        } else {
            // NOT LARGE SCREEN
            if (dictionaryManipulationFragment.isAdded()) {
                removeFragment(dictionaryManipulationFragment);
                setSingleLayout();
            }
        }
    }

    private void drawActivityViewLayout() {
        if (isVocableSelected) {
            if (isLargeScreenDevice()) {
                // LARGE SCREEN
                if (isLandscapeOrientation()) {
                    // LANDSCAPE
                    setLayoutTwoColumnsHorizontal();
                    return;
                } else {
                    // NOT LANDSCAPE
                    setLayoutTwoColumnsVertical();
                    return;
                }
            }
        }
        setSingleLayout();
    }

    private void addFragment(FrameLayout container, Fragment fragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(container.getId(), fragment, getFragmentTag(fragment))
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();
    }

    private String getFragmentTag(Fragment fragment) {
        String fragmentToLoadTAG;
        if (fragment instanceof DictionaryManagementFragment) {
            fragmentToLoadTAG = DictionaryManagementFragment.TAG;
        } else if (fragment instanceof DictionaryManipulationFragment) {
            fragmentToLoadTAG = DictionaryManipulationFragment.TAG;
        } else {
            throw new RuntimeException("Something goes wrong. Fragment to load not recognized");
        }
        return fragmentToLoadTAG;
    }

    private void removeFragment(Fragment fragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .remove(fragment)
                .commit();
        fragmentManager.executePendingTransactions();
    }

    private boolean isLargeScreenDevice() {
        return getResources().getBoolean(R.bool.LARGE_SCREEN);
    }

    private boolean isLandscapeOrientation() {
        return getResources().getBoolean(R.bool.LANDSCAPE);
    }

    private void setLayoutTwoColumnsHorizontal() {
        // Make the dictionaryManagementContainer take 1/2 of the layout's width
        dictionaryManagementContainer.setLayoutParams(
                new LinearLayout.LayoutParams(0, MATCH_PARENT, 1f)
        );

        // Make the dictionaryManipulationContainer take 1/2 of the layout's width
        dictionaryManipulationContainer.setLayoutParams(
                new LinearLayout.LayoutParams(0, MATCH_PARENT, 1f)
        );
    }

    private void setLayoutTwoColumnsVertical() {
        // Make the dictionaryManagementContainer take 1/2 of the layout's height
        dictionaryManagementContainer.setLayoutParams(
                new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1f)
        );

        // Make the dictionaryManipulationContainer take 1/2 of the layout's height
        dictionaryManipulationContainer.setLayoutParams(
                new LinearLayout.LayoutParams(MATCH_PARENT, 0, 1f)
        );
    }

    private void setSingleLayout() {
        dictionaryManagementContainer.setLayoutParams(
                new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
        );

        dictionaryManipulationContainer.setLayoutParams(
                new LinearLayout.LayoutParams(0, 0)
        );
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
