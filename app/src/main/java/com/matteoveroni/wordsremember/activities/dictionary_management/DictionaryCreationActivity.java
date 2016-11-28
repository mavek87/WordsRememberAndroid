package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventNotifySelectedVocableToObservers;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventVocableSelected;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryManipulationFragment;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.factory.DictionaryFragmentFactory;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.provider.dao.DictionaryDAO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.matteoveroni.wordsremember.activities.dictionary_management.fragments.factory.DictionaryFragmentFactory.DictionaryFragmentType;

/**
 * Activity for handling creation of new vocables in the dictionary
 *
 * @author Matteo Veroni
 */

public class DictionaryCreationActivity extends AppCompatActivity {

    // ATTRIBUTES

    private static final String TAG = "A_DICTIONARY_CREATE";

    private DictionaryDAO dictionaryDAO;

    private MenuInflater menuInflater;

    private DictionaryManagementFragment dictionaryManagementFragment;
    private DictionaryManipulationFragment dictionaryManipulationFragment;

    private FrameLayout dictionaryManagementContainer;
    private FrameLayout dictionaryManipulationContainer;

    private boolean isVocableSelected = false;
    private boolean is

    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    /**********************************************************************************************/

    // CONSTRUCTORS

    /**
     * Empty constructor
     */
    public DictionaryCreationActivity() {
    }

    /**********************************************************************************************/

    // ANDROID LIFECYCLE METHODS

    /**
     * Method called when activity lifecycle starts
     * This activity is registered to the event bus as a listener
     */
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    /**
     * Method called when activity lifecycle stops
     * Before activity is destroyed it is unregistered from the event bus
     */
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Method called when the activity creation starts
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_management_view);

        dictionaryManagementContainer = (FrameLayout) findViewById(R.id.dictionary_management_container);
        dictionaryManipulationContainer = (FrameLayout) findViewById(R.id.dictionary_manipulation_container);

        if (savedInstanceState == null) {
            Toast.makeText(this, "savedInstanceState == null", Toast.LENGTH_SHORT).show();

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

    // ANDROID LIFECYCLE METHODS - MENU

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_management, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_vocable:

                if (isLargeScreenDevice()) {
                    loadFragmentsInsideView();
                } else {
                    dictionaryManagementContainer.removeAllViews();
                    removeFragment(dictionaryManagementFragment);
                    setLayout(0, 0, MATCH_PARENT, MATCH_PARENT);
                    addFragment(dictionaryManipulationContainer, dictionaryManipulationFragment);
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************************************/

    // EVENTS

    /**
     * @param event
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDictionaryItemSelected(EventVocableSelected event) {
        Word selectedVocable;
        long selectedVocableID = event.getSelectedVocableID();

        if (selectedVocableID >= 0) {
            // A vocable is selected
            isVocableSelected = true;

            // TODO: Async Task???
            selectedVocable = dictionaryDAO.getVocableById(selectedVocableID);
        } else {
            // No vocable selected
            isVocableSelected = false;
            selectedVocable = null;
        }

        // Send selected vocable to all the listeners (fragments)
        EventBus.getDefault().postSticky(new EventNotifySelectedVocableToObservers(selectedVocable));

        updateViewAndLayout();
    }

    /**********************************************************************************************/

    // HELPER METHODS

    /**
     *
     */
    private void updateViewAndLayout() {
        loadFragmentsInsideView();
        setViewLayout();
    }

    /**
     * Load required fragments into the view depending on the size of the display and his orientation
     */
    private void loadFragmentsInsideView() {
        // Add management fragment if it's not added yet in any case
        if (!dictionaryManagementFragment.isAdded())
            addFragment(dictionaryManagementContainer, dictionaryManagementFragment);

        if (isLargeScreenDevice() && isVocableSelected) {
            // LARGE SCREEN and A VOCABLE SELECTED
            // so load the manipulation fragment inside the view together with the management fragment
            if (!dictionaryManipulationFragment.isAdded())
                addFragment(dictionaryManipulationContainer, dictionaryManipulationFragment);

        } else {
            // NOT LARGE SCREEN or NO VOCABLE SELECTED
            // so if it's present the manipulation fragment in the view remove it
            if (dictionaryManipulationFragment.isAdded())
                removeFragment(dictionaryManipulationFragment);
//                useSingleLayout();
        }
    }

    /**
     * Set the layout of the view
     */
    private void setViewLayout() {
        if (isVocableSelected) {
            // VOCABLE SELECTED
            if (isLargeScreenDevice()) {
                // LARGE SCREEN
                if (isLandscapeOrientation()) {
                    // LANDSCAPE MODE
                    useLayoutTwoHorizontalColumns();
                    return;
                } else {
                    // NOT IN LANDSCAPE MODE
                    useLayoutTwoVerticalRows();
                    return;
                }
            }
        }
        useSingleLayout();
    }

    /**
     * Add a fragment inside a frame layout container
     *
     * @param container
     * @param fragment
     */
    private void addFragment(FrameLayout container, Fragment fragment) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .add(container.getId(), fragment, getFragmentTag(fragment))
                .addToBackStack(null)
                .commit();
        fragmentManager.executePendingTransactions();
    }

    /**
     * Retrieve fragment TAG from fragment
     *
     * @param fragment
     * @return retrieved fragment TAG string
     */
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

    /**
     * Remove a fragment from the view
     *
     * @param fragment
     */
    private void removeFragment(Fragment fragment) {
        if (fragment.isAdded()) {
            final FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
            fragmentManager.executePendingTransactions();
        }
    }

    /**
     * Use a layout with two horizontal columns that hosts managment and manipulation fragments
     */
    private void useLayoutTwoHorizontalColumns() {
        setLayout(0, MATCH_PARENT, 1f, 0, MATCH_PARENT, 1f);
    }

    /**
     * Use a layout with two vertical rows that hosts managment and manipulation fragments
     */
    private void useLayoutTwoVerticalRows() {
        setLayout(MATCH_PARENT, 0, 1f, MATCH_PARENT, 0, 1f);
    }

    /**
     * Use a single layout with only the management fragment visible
     */
    private void useSingleLayout() {
        setLayout(MATCH_PARENT, MATCH_PARENT, 0, 0);
    }

    private void setLayout(int managementContainerWidth, int managementContainerHeight, int manipulationContainerWidth, int manipulationContainerHeight) {
        dictionaryManagementContainer.setLayoutParams(
                new LinearLayout.LayoutParams(managementContainerWidth, managementContainerHeight)
        );

        dictionaryManipulationContainer.setLayoutParams(
                new LinearLayout.LayoutParams(manipulationContainerWidth, manipulationContainerHeight)
        );
    }

    private void setLayout(
            int managementContainerWidth,
            int managementContainerHeight,
            float managementContainerWeight,
            int manipulationContainerWidth,
            int manipulationContainerHeight,
            float manipulationContainerWeight) {

        dictionaryManagementContainer.setLayoutParams(
                new LinearLayout.LayoutParams(managementContainerWidth, managementContainerHeight, managementContainerWeight)
        );

        dictionaryManipulationContainer.setLayoutParams(
                new LinearLayout.LayoutParams(manipulationContainerWidth, manipulationContainerHeight, manipulationContainerWeight)
        );
    }


    private boolean isLargeScreenDevice() {
        return getResources().getBoolean(R.bool.LARGE_SCREEN);
    }

    private boolean isLandscapeOrientation() {
        return getResources().getBoolean(R.bool.LANDSCAPE);
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

    /**********************************************************************************************/
}
