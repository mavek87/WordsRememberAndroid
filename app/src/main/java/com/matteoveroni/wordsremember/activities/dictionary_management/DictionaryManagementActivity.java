package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventCreateVocable;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventManipulateVocable;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventVocableSelected;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventNotifySelectedVocableToObservers;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.factory.DictionaryFragmentFactory;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryManipulationFragment;
import com.matteoveroni.wordsremember.activities.dictionary_management.layout.DictionaryManagementViewLayout;
import com.matteoveroni.wordsremember.activities.dictionary_management.layout.DictionaryManagementActivityLayoutManager;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.provider.dao.DictionaryDAO;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.InvalidParameterException;
import java.util.EmptyStackException;

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

    private DictionaryManagementActivityLayoutManager layoutManager;

    private FrameLayout managementContainer;
    private FrameLayout manipulationContainer;

    /**********************************************************************************************/

    // CONSTRUCTORS

    /**
     * Empty constructor
     */
    public DictionaryManagementActivity() {
    }

    /**********************************************************************************************/

    // ANDROID LIFECYCLE METHODS
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_management_view);
        managementContainer = (FrameLayout) findViewById(R.id.dictionary_management_container);
        manipulationContainer = (FrameLayout) findViewById(R.id.dictionary_manipulation_container);

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            initMemberAttributesInstances();
            populateDatabase();
            exportDatabaseOnSd();
            initViewLayout();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        fragmentManager.putFragment(savedInstanceState, DictionaryManagementFragment.TAG, managementFragment);
        fragmentManager.putFragment(savedInstanceState, DictionaryManipulationFragment.TAG, manipulationFragment);
        savedInstanceState.putSerializable(DictionaryManagementActivityLayoutManager.TAG, layoutManager);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        managementFragment = (DictionaryManagementFragment) fragmentManager.getFragment(savedInstanceState, DictionaryManagementFragment.TAG);
        addFragmentToView(managementContainer, managementFragment, DictionaryManagementFragment.TAG);

        manipulationFragment = (DictionaryManipulationFragment) fragmentManager.getFragment(savedInstanceState, DictionaryManipulationFragment.TAG);
        addFragmentToView(manipulationContainer, manipulationFragment, DictionaryManipulationFragment.TAG);

        layoutManager = (DictionaryManagementActivityLayoutManager) savedInstanceState.getSerializable(DictionaryManagementActivityLayoutManager.TAG);
        layoutManager.setManagementContainer(managementContainer);
        layoutManager.setManipulationContainer(manipulationContainer);

        try {
            DictionaryManagementViewLayout layoutToRestore = layoutManager.readLayoutInUse();

            switch (layoutToRestore.getType()) {
                case SINGLE:
                    layoutManager.useSingleLayoutForFragment(layoutToRestore.getMainFragmentTAG());
                    break;
                case TWO_COLUMNS:
                    layoutManager.useLayoutTwoHorizontalColumns();
                    break;
                case TWO_ROWS:
                    layoutManager.useLayoutTwoVerticalRows();
                    break;
            }
        } catch (EmptyStackException ex) {
            throw new RuntimeException("Error! No previous activity view layout saved to restore!");
        } catch (NullPointerException ex) {
            throw new RuntimeException("Error! Previous activity view layout malformed. No activityLayoutType set");
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
                layoutManager.useSingleLayoutForFragment(DictionaryManipulationFragment.TAG);
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
        layoutManager = new DictionaryManagementActivityLayoutManager(managementContainer, manipulationContainer);
        dictionaryDAO = new DictionaryDAO(this);
    }

    private void initViewLayout() {
        addFragmentToView(managementContainer, managementFragment, DictionaryManagementFragment.TAG);
        addFragmentToView(manipulationContainer, manipulationFragment, DictionaryManipulationFragment.TAG);
        layoutManager.useSingleLayoutForFragment(DictionaryManagementFragment.TAG);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
                DictionaryManagementViewLayout previousLayout = layoutManager.discardCurrentLayoutAndGetPreviousOne();
                switch (previousLayout.getType()) {
                    case SINGLE:
                        layoutManager.useSingleLayoutForFragment(previousLayout.getMainFragmentTAG());
                        break;
                    case TWO_COLUMNS:
                        layoutManager.useLayoutTwoHorizontalColumns();
                        break;
                    case TWO_ROWS:
                        layoutManager.useLayoutTwoVerticalRows();
                        break;
                }
                return true;
            } catch (NullPointerException ex) {
                throw new RuntimeException("Error! Previous activity view layout malformed. No activityLayoutType set");
            }  catch (EmptyStackException ex) {
            }
        }
        return super.onKeyDown(keyCode, event);
    }

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
//                    .addToBackStack(null)
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
//        if (firstSavedVocableId < 0) {
//            Toast.makeText(this, "Vocable " + firstVocableToSave.getName() + " not inserted.", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(this, "Vocable " + firstVocableToSave.getName() + " inserted. His ID in the database is => " + firstSavedVocableId, Toast.LENGTH_SHORT).show();
//        }

        Word secondVocableToSave = new Word("second vocable");
        long secondSavedVocableId = dictionaryDAO.saveVocable(secondVocableToSave);
    }

    private void exportDatabaseOnSd() {
        DatabaseManager.getInstance(getBaseContext()).exportDBOnSD();
    }

    /**********************************************************************************************/
}
