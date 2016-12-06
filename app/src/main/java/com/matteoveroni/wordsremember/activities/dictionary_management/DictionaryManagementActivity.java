package com.matteoveroni.wordsremember.activities.dictionary_management;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventCreateVocable;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventManipulateVocable;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventVocableSelected;
import com.matteoveroni.wordsremember.activities.dictionary_management.events.EventNotifySelectedVocableToObservers;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.factory.DictionaryFragmentFactory;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.activities.dictionary_management.fragments.DictionaryManipulationFragment;
import com.matteoveroni.wordsremember.activities.dictionary_management.layout.DictionaryManagementActivityLayoutManager;
import com.matteoveroni.wordsremember.model.Word;
import com.matteoveroni.wordsremember.provider.DatabaseManager;
import com.matteoveroni.wordsremember.activities.dictionary_management.model.DictionaryDAO;

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

    public static final String TAG = "A_DICTIONARY_MANAGE";

    private DictionaryDAO model;

    private FragmentManager fragmentManager;

    private DictionaryManagementFragment managementFragment;
    private DictionaryManipulationFragment manipulationFragment;

    private DictionaryManagementActivityLayoutManager layoutManager;

    private FrameLayout managementContainer;
    private FrameLayout manipulationContainer;

    private FloatingActionButton floatingActionButton;

    /**********************************************************************************************/

    // CONSTRUCTOR
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
        layoutManager.dispose();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_management_view);
        findViewElementsReferences();

        fragmentManager = getSupportFragmentManager();

        if (savedInstanceState == null) {
            model = new DictionaryDAO(this);

            managementFragment = (DictionaryManagementFragment) DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANAGEMENT);
            manipulationFragment = (DictionaryManipulationFragment) DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANIPULATION);
            layoutManager = new DictionaryManagementActivityLayoutManager(managementContainer, manipulationContainer);

            setupFirstView();

            populateDatabase();
            exportDatabaseOnSd();
        }

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layoutManager.useSingleLayoutForFragment(DictionaryManipulationFragment.TAG);
                EventBus.getDefault().postSticky(new EventCreateVocable());
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        fragmentManager.putFragment(savedInstanceState, DictionaryManagementFragment.TAG, managementFragment);
        fragmentManager.putFragment(savedInstanceState, DictionaryManipulationFragment.TAG, manipulationFragment);
        savedInstanceState.putSerializable(DictionaryManagementActivityLayoutManager.TAG, layoutManager);
        savedInstanceState.putSerializable(DictionaryDAO.TAG, model);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        model = (DictionaryDAO) savedInstanceState.getSerializable(DictionaryDAO.TAG);

        managementFragment = (DictionaryManagementFragment) fragmentManager.getFragment(savedInstanceState, DictionaryManagementFragment.TAG);
        addFragmentToView(managementContainer, managementFragment, DictionaryManagementFragment.TAG);

        manipulationFragment = (DictionaryManipulationFragment) fragmentManager.getFragment(savedInstanceState, DictionaryManipulationFragment.TAG);
        addFragmentToView(manipulationContainer, manipulationFragment, DictionaryManipulationFragment.TAG);

        layoutManager = (DictionaryManagementActivityLayoutManager) savedInstanceState.getSerializable(DictionaryManagementActivityLayoutManager.TAG);
        layoutManager.resyncWithNewViewElements(managementContainer, manipulationContainer);

        try {
            layoutManager.restoreLayout(DictionaryManagementActivityLayoutManager.LayoutChronology.CURRENT);
        } catch (EmptyStackException ex) {
            throw new RuntimeException("Error! No previous activity view layout saved to restore!");
        } catch (NullPointerException ex) {
            throw new RuntimeException("Error! Previous activity view layout malformed. No activityLayoutType set");
        }
    }

    /**********************************************************************************************/

    // EVENTS

    /**
     * Observer method launched when a EventVocableSelected is posted on the app event bus
     *
     * @param event Event that occurs when a vocable is selected
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVocableSelected(EventVocableSelected event) {
        long selectedVocableID = event.getSelectedVocableID();

        Word selectedVocable = null;
        if (selectedVocableID >= 0) {
            selectedVocable = model.getVocableById(selectedVocableID);

            if (isLargeScreenDevice()) {
                if (isLandscapeOrientation()) {
                    layoutManager.useLayoutTwoHorizontalColumns();
                } else {
                    layoutManager.useLayoutTwoVerticalRows();
                }
            } else {
//                manipulationContainer.removeAllViews();
                layoutManager.useSingleLayoutForFragment(DictionaryManipulationFragment.TAG);
            }

            EventBus.getDefault().postSticky(new EventNotifySelectedVocableToObservers(selectedVocable));
        }
    }

    /**
     * Method called when a manipulation operation on a vocable (update or remove) is required
     *
     * @param event The event for manipulating a vocable
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVocableManipulationRequest(EventManipulateVocable event) {
        final long selectedVocableID = event.getVocableIDToManipulate();
        switch (event.getTypeOfManipulation()) {
            case EDIT:
                break;
            case REMOVE:
                if (model.removeVocable(selectedVocableID)) {
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
    private void findViewElementsReferences() {
        managementContainer = (FrameLayout) findViewById(R.id.dictionary_management_container);
        manipulationContainer = (FrameLayout) findViewById(R.id.dictionary_manipulation_container);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.dictionary_management_floating_action_button);
    }


    private void setupFirstView() {
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
                layoutManager.restoreLayout(DictionaryManagementActivityLayoutManager.LayoutChronology.PREVIOUS);
//                EventBus.getDefault().postSticky(new EventResetSelection());
                return true;
            } catch (Exception ex) {
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

    private void populateDatabase() {
        Word firstVocableToSave = new Word("test123");
        model.saveVocable(firstVocableToSave);

        Word secondVocableToSave = new Word("second vocable");
        model.saveVocable(secondVocableToSave);
    }

    private void exportDatabaseOnSd() {
        DatabaseManager.getInstance(getBaseContext()).exportDBOnSD();
    }

    /**********************************************************************************************/
}
