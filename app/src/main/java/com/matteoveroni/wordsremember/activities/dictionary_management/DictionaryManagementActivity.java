package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventCreateVocable;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventManipulateVocable;
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

import java.security.InvalidParameterException;

import static com.matteoveroni.wordsremember.activities.dictionary_management.fragments.factory.DictionaryFragmentFactory.DictionaryFragmentType;

/**
 * Activity that handles dictionary management operations
 *
 * @author Matteo Veroni
 */

public class DictionaryManagementActivity extends AppCompatActivity {

    // ATTRIBUTES

    private static final String TAG = "A_DICTIONARY_MANAGE";

    private DictionaryDAO dictionaryDAO;

    private FragmentManager fragmentManager;

    private DictionaryManagementFragment managementFragment;
    private DictionaryManipulationFragment manipulationFragment;

    private FrameLayout managementContainer;
    private FrameLayout manipulationContainer;

    /**
     * Inner class that hosts all the data related to the layout of the view
     */
    private static final class ViewLayout {
        static final String TAG = "ViewLayoutTag";

        static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

        enum Type {
            SINGLE, TWO_COLUMNS, TWO_ROWS
        }
    }

    private ViewLayout.Type viewLayoutType;


    /**********************************************************************************************/

    // CONSTRUCTORS

    /**
     * Empty constructor
     */
    public DictionaryManagementActivity() {
    }

    /**********************************************************************************************/

    // ANDROID LIFECYCLE METHODS

    /**
     * Method called when activity lifecycle starts. This activity is registered to the event bus
     * as a listener.
     */
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    /**
     * Method called when activity lifecycle stops. Before activity is destroyed it is unregistered
     * from the event bus.
     */
    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    /**
     * Method called when the activity creation starts
     *
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_management_view);
        managementContainer = (FrameLayout) findViewById(R.id.dictionary_management_container);
        manipulationContainer = (FrameLayout) findViewById(R.id.dictionary_manipulation_container);

        fragmentManager = getSupportFragmentManager();

        // Initialize If the app is created for the first time
        if (savedInstanceState == null) {
            initMemberAttributesInstances();
            populateDatabase();
            exportDatabaseOnSd();
            initViewLayout();
        }
    }

    /**
     * Method called when the activity is going to be stopped.
     *
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // save inside the savedInstanceStateBundle every value to restore when the activity is recreated
        if (managementFragment != null && managementFragment.isAdded()) {
            fragmentManager.putFragment(savedInstanceState, DictionaryManagementFragment.TAG, managementFragment);
        }
        if (manipulationFragment != null && manipulationFragment.isAdded()) {
            fragmentManager.putFragment(savedInstanceState, DictionaryManipulationFragment.TAG, manipulationFragment);
        }
        savedInstanceState.putSerializable(ViewLayout.TAG, viewLayoutType);
    }

    /**
     * Method called when the activity is restored after device settings modifications (restore data from savedInstanceBundle).
     *
     * @param savedInstanceState Saved instance state bundle
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        boolean wasManagementFragmentSaved = savedInstanceState.containsKey(DictionaryManagementFragment.TAG);
        boolean wasManipulationFragmentSaved = savedInstanceState.containsKey(DictionaryManagementFragment.TAG);

        if (!wasManagementFragmentSaved && !wasManipulationFragmentSaved) {
            throw new RuntimeException("Error! No fragment was saved before the activity was stopped.");
        }

        if (wasManagementFragmentSaved) {
            managementFragment = (DictionaryManagementFragment) fragmentManager.getFragment(savedInstanceState, DictionaryManagementFragment.TAG);
            addFragmentToView(managementContainer, managementFragment, DictionaryManagementFragment.TAG);
        }

        if (wasManipulationFragmentSaved) {
            manipulationFragment = (DictionaryManipulationFragment) fragmentManager.getFragment(savedInstanceState, DictionaryManipulationFragment.TAG);
            addFragmentToView(manipulationContainer, manipulationFragment, DictionaryManipulationFragment.TAG);
        }

        // Restore saved layout instance
        viewLayoutType = (ViewLayout.Type) savedInstanceState.getSerializable(ViewLayout.TAG);

        // Re-apply saved layout to the view
        if (viewLayoutType != null) {
            switch (viewLayoutType) {
                case SINGLE:
                    useSingleLayoutForFragment(managementFragment != null ? DictionaryManagementFragment.TAG : DictionaryManipulationFragment.TAG);
                    break;
                case TWO_COLUMNS:
                    useLayoutTwoHorizontalColumns();
                    break;
                case TWO_ROWS:
                    useLayoutTwoVerticalRows();
                    break;
            }
        } else {
            throw new RuntimeException("Error! Cannot retrieve any viewLayoutType saved into the saveInstanceState bundle");
        }
    }

    // ANDROID LIFECYCLE METHODS - MENU

    /**
     * Method called when the options menu is created
     *
     * @param menu The menu to inflate
     * @return True if the options menu was inflated successfully
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dictionary_management, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_create_vocable:

                removeFragmentFromView(managementFragment);
                addFragmentToView(manipulationContainer, manipulationFragment, DictionaryManipulationFragment.TAG);
                useSingleLayoutForFragment(DictionaryManipulationFragment.TAG);

                EventBus.getDefault().postSticky(new EventCreateVocable());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**********************************************************************************************/

    // EVENTS

    /**
     * Observer method launched when a EventVocableSelected is posted on the app event bus
     *
     * @param event Event that occurs when a vocable is selected
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDictionaryItemSelected(EventVocableSelected event) {
        Word selectedVocable;
        long selectedVocableID = event.getSelectedVocableID();

        selectedVocable = (selectedVocableID >= 0) ? dictionaryDAO.getVocableById(selectedVocableID) : null;

        // Send selected vocable to all the listeners (fragments)
        EventBus.getDefault().postSticky(new EventNotifySelectedVocableToObservers(selectedVocable));

//        loadFragmentsInsideView(true, true);
    }

    /**
     * Method called when a manipulation operation on a vocable (update or remove) is required
     *
     * @param event The event for manipulating a vocable
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDictionaryItemManipulationRequested(EventManipulateVocable event) {
        final long selectedVocableID = event.getVocableIDToManipulate();
        switch (event.getTypeOfManipulation()) {
            case EDIT:
                break;
            case REMOVE:
                if (dictionaryDAO.removeVocable(selectedVocableID)) {
                    // Send selected vocable to all the listeners (fragments)
                    EventBus.getDefault().postSticky(new EventNotifySelectedVocableToObservers(null));

//                    loadFragmentsInsideView(true, false);

                }
                break;
            default:
                throw new InvalidParameterException("Internal error, something goes wrong! - Invalid vocable manipulation operation");
        }
    }

    /**********************************************************************************************/

    // HELPER METHODS

    /**
     * Private method called by onCreate (only the first time) that init all the member attributes instances.
     */
    private void initMemberAttributesInstances() {
        managementFragment = (DictionaryManagementFragment) DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANAGEMENT);
        manipulationFragment = (DictionaryManipulationFragment) DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANIPULATION);

        dictionaryDAO = new DictionaryDAO(this);
    }

    private void initViewLayout() {
        addFragmentToView(managementContainer, managementFragment, DictionaryManagementFragment.TAG);
        useSingleLayoutForFragment(DictionaryManagementFragment.TAG);
        viewLayoutType = ViewLayout.Type.SINGLE;
    }

//    /**
//     * @param useManagementFragment
//     * @param useManipulationFragment
//     */
//    private void loadFragmentsInsideView(boolean useManagementFragment, boolean useManipulationFragment) {
//        if (useManagementFragment && useManipulationFragment) {
//            // Add management fragment if it's not added yet in any case
//            addFragmentToView(managementContainer, managementFragment);
//
//            if (isLargeScreenDevice() && (managementFragment != null && managementFragment.isItemSelected())) {
//                // LARGE SCREEN and A VOCABLE SELECTED
//                // so load the manipulation fragment inside the view together with the management fragment
//                addFragmentToView(manipulationContainer, manipulationFragment);
//
//                if (isLandscapeOrientation()) {
//                    // LANDSCAPE MODE
//                    useLayoutTwoHorizontalColumns();
//                } else {
//                    // NOT IN LANDSCAPE MODE
//                    useLayoutTwoVerticalRows();
//                }
//            } else {
//                // NOT LARGE SCREEN or NO VOCABLE SELECTED
//                // so if it's present the manipulation fragment in the view remove it
//                manipulationContainer.removeAllViews();
//                removeFragmentFromView(manipulationFragment);
//                useSingleLayoutForFragment(managementFragment);
//            }
//        } else if (useManagementFragment) {
//            manipulationContainer.removeAllViews();
//            removeFragmentFromView(manipulationFragment);
//            addFragmentToView(managementContainer, managementFragment);
//            useSingleLayoutForFragment(managementFragment);
//        } else if (useManipulationFragment) {
//            managementContainer.removeAllViews();
//            removeFragmentFromView(managementFragment);
//            addFragmentToView(manipulationContainer, manipulationFragment);
//            useSingleLayoutForFragment(manipulationFragment);
//        }
//    }

    /**
     * Add a fragment to the view
     *
     * @param container
     * @param fragment
     * @param fragmentTAG
     * @return true if the fragment was successfully added to the view, false otherwise
     */
    private boolean addFragmentToView(FrameLayout container, Fragment fragment, String fragmentTAG) {
        if (fragment != null && !fragment.isAdded()) {
            fragmentManager
                    .beginTransaction()
                    .add(container.getId(), fragment, fragmentTAG)
                    .addToBackStack(null)
                    .commit();
            fragmentManager.executePendingTransactions();
            return true;
        }
        return false;
    }

    /**
     * Remove a fragment from the view if it's present
     *
     * @param fragment
     * @return true if the fragment was successfully removed from the view, false otherwise
     */
    private boolean removeFragmentFromView(Fragment fragment) {
        if (fragment != null && fragment.isAdded()) {
            fragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit();
            fragmentManager.executePendingTransactions();
            return true;
        }
        return false;
    }

    /**
     * Use a layout with two horizontal columns that hosts managment and manipulation fragments
     */
    private void useLayoutTwoHorizontalColumns() {
        setLayout(0, ViewLayout.MATCH_PARENT, 1f, 0, ViewLayout.MATCH_PARENT, 1f);
    }

    /**
     * Use a layout with two vertical rows that hosts managment and manipulation fragments
     */
    private void useLayoutTwoVerticalRows() {
        setLayout(ViewLayout.MATCH_PARENT, 0, 1f, ViewLayout.MATCH_PARENT, 0, 1f);
    }

    /**
     * Use a single layout with only the management fragment visible
     */
    private void useSingleLayoutForFragment(String fragmentTAG) {
        switch (fragmentTAG) {
            case DictionaryManagementFragment.TAG:
                setLayout(ViewLayout.MATCH_PARENT, ViewLayout.MATCH_PARENT, 0, 0);
                break;
            case DictionaryManipulationFragment.TAG:
                setLayout(0, 0, ViewLayout.MATCH_PARENT, ViewLayout.MATCH_PARENT);
                break;
        }
    }

    private void setLayout(int managementContainerWidth, int managementContainerHeight, int manipulationContainerWidth, int manipulationContainerHeight) {
        managementContainer.setLayoutParams(
                new LinearLayout.LayoutParams(managementContainerWidth, managementContainerHeight)
        );

        manipulationContainer.setLayoutParams(
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

        managementContainer.setLayoutParams(
                new LinearLayout.LayoutParams(managementContainerWidth, managementContainerHeight, managementContainerWeight)
        );

        manipulationContainer.setLayoutParams(
                new LinearLayout.LayoutParams(manipulationContainerWidth, manipulationContainerHeight, manipulationContainerWeight)
        );
    }


    private boolean isLargeScreenDevice() {
        return getResources().getBoolean(R.bool.LARGE_SCREEN);
    }

    private boolean isLandscapeOrientation() {
        return getResources().getBoolean(R.bool.LANDSCAPE);
    }

    // TODO: Remove this test method that populates vocables into the database
    private void populateDatabase() {
        Word firstVocableToSave = new Word("test123");
        long firstSavedVocableId = dictionaryDAO.saveVocable(firstVocableToSave);
        if (firstSavedVocableId < 0) {
            Toast.makeText(this, "Vocable " + firstVocableToSave.getName() + " not inserted.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vocable " + firstVocableToSave.getName() + " inserted. His ID in the database is => " + firstSavedVocableId, Toast.LENGTH_SHORT).show();
        }

        Word secondVocableToSave = new Word("second vocable");
        long secondSavedVocableId = dictionaryDAO.saveVocable(secondVocableToSave);
        if (secondSavedVocableId < 0) {
            Toast.makeText(this, "Vocable " + secondVocableToSave.getName() + " not inserted.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Vocable " + secondVocableToSave.getName() + " inserted. His ID in the database is => " + secondSavedVocableId, Toast.LENGTH_SHORT).show();
        }
    }

    private void exportDatabaseOnSd() {
        DatabaseManager.getInstance(getBaseContext()).exportDBOnSD();
    }

    /**********************************************************************************************/
}
