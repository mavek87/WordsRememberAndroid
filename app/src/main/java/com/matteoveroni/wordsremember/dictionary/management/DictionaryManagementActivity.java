package com.matteoveroni.wordsremember.dictionary.management;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.matteoveroni.wordsremember.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.management.factories.DictionaryManagementPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.management.interfaces.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.dictionary.management.interfaces.DictionaryManagementView;
import com.matteoveroni.wordsremember.dictionary.fragments.DictionaryManagementFragment;
import com.matteoveroni.wordsremember.dictionary.fragments.factory.DictionaryFragmentFactory;
import com.matteoveroni.wordsremember.dictionary.fragments.DictionaryManipulationFragment;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;
import com.matteoveroni.wordsremember.ui.layout.ViewLayoutType;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.matteoveroni.wordsremember.dictionary.fragments.factory.DictionaryFragmentFactory.DictionaryFragmentType;

/**
 * Activity that handles dictionary management operations
 *
 * @author Matteo Veroni
 */

/**
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.la55rzpm4
 */
public class DictionaryManagementActivity extends AppCompatActivity
        implements DictionaryManagementView, LoaderManager.LoaderCallbacks<DictionaryManagementPresenter> {

    public static final String TAG = "A_DICTIONARY_MANAGE";

    private DictionaryManagementPresenter presenter;
    private static final int PRESENTER_LOADER_ID = 1;

    private ViewLayout viewLayout;

    private FragmentManager fragmentManager;

    private DictionaryManagementFragment managementFragment;
    private DictionaryManipulationFragment manipulationFragment;

    @BindView(R.id.dictionary_management_container)
    FrameLayout managementContainer;

    @BindView(R.id.dictionary_manipulation_container)
    FrameLayout manipulationContainer;

    @BindView(R.id.dictionary_management_floating_action_button)
    FloatingActionButton floatingActionButton;

    public DictionaryManagementActivity() {
    }

    // ANDROID LIFECYCLE METHODS
    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        presenter.onViewAttached(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
//        layoutManager.dispose();
        presenter.onViewDetached();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_management_view);
        ButterKnife.bind(this);

        fragmentManager = getSupportFragmentManager();

        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);

        if (savedInstanceState == null) {

            managementFragment = (DictionaryManagementFragment) DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANAGEMENT);
            manipulationFragment = (DictionaryManipulationFragment) DictionaryFragmentFactory.getInstance(DictionaryFragmentType.MANIPULATION);
//            layoutManager = new DictionaryManagementViewLayoutManager(managementContainer, manipulationContainer);

            addFragmentToView(managementContainer, managementFragment, DictionaryManagementFragment.TAG);
            addFragmentToView(manipulationContainer, manipulationFragment, DictionaryManipulationFragment.TAG);

            presenter.onViewCreatedForTheFirstTime();
        }
    }

    @Override
    public Context getContext() {
        return getApplicationContext();
    }

    @Override
    public ViewLayout getViewLayout() {
        return this.viewLayout;
    }

    @Override
    public void useSingleLayoutWithFragment(String fragmentTAG) {
        switch (fragmentTAG) {
            case DictionaryManagementFragment.TAG:
                setLayout(ViewLayout.MATCH_PARENT, ViewLayout.MATCH_PARENT, 0, 0);
                break;
            case DictionaryManipulationFragment.TAG:
                setLayout(0, 0, ViewLayout.MATCH_PARENT, ViewLayout.MATCH_PARENT);
                break;
        }
        ViewLayout viewLayoutToUse = new ViewLayout(ViewLayoutType.SINGLE_LAYOUT);
        viewLayoutToUse.setMainFragmentTAG(fragmentTAG);
        viewLayout = viewLayoutToUse;
    }

    @Override
    public void useTwoHorizontalColumnsLayout() {
        setLayout(0, ViewLayout.MATCH_PARENT, 1f, 0, ViewLayout.MATCH_PARENT, 1f);
        viewLayout = new ViewLayout(ViewLayoutType.TWO_COLUMNS_LAYOUT);
    }

    @Override
    public void useTwoVerticalRowsLayout() {
        setLayout(ViewLayout.MATCH_PARENT, 0, 1f, ViewLayout.MATCH_PARENT, 0, 1f);
        viewLayout = new ViewLayout(ViewLayoutType.TWO_ROWS_LAYOUT);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        fragmentManager.putFragment(savedInstanceState, DictionaryManagementFragment.TAG, managementFragment);
        fragmentManager.putFragment(savedInstanceState, DictionaryManipulationFragment.TAG, manipulationFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        managementFragment = (DictionaryManagementFragment) fragmentManager.getFragment(savedInstanceState, DictionaryManagementFragment.TAG);
        addFragmentToView(managementContainer, managementFragment, DictionaryManagementFragment.TAG);

        manipulationFragment = (DictionaryManipulationFragment) fragmentManager.getFragment(savedInstanceState, DictionaryManipulationFragment.TAG);
        addFragmentToView(manipulationContainer, manipulationFragment, DictionaryManipulationFragment.TAG);

        presenter.onViewRestored();

//        layoutManager = (DictionaryManagementViewLayoutManager) savedInstanceState.getSerializable(DictionaryManagementViewLayoutManager.TAG);
//        layoutManager.resyncWithNewViewElements(managementContainer, manipulationContainer);
//
//        try {
//            layoutManager.getViewLayout(DictionaryManagementViewLayoutManager.LayoutChronology.CURRENT);
//        } catch (EmptyStackException ex) {
//            throw new RuntimeException("Error! No previous activity view layout saved to restore!");
//        } catch (NullPointerException ex) {
//            throw new RuntimeException("Error! Previous activity view layout malformed. No activityLayoutType set");
//        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            try {
//                layoutManager.getViewLayout(DictionaryManagementViewLayoutManager.LayoutChronology.PREVIOUS);
//                EventBus.getDefault().postSticky(new EventResetSelection());
                return true;
            } catch (Exception ex) {
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public Loader<DictionaryManagementPresenter> onCreateLoader(int id, Bundle arg) {
        return new PresenterLoader<>(this, new DictionaryManagementPresenterFactory());
    }

    @Override
    public void onLoadFinished(Loader<DictionaryManagementPresenter> loader, DictionaryManagementPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onLoaderReset(Loader<DictionaryManagementPresenter> loader) {
        presenter = null;
    }


    /**********************************************************************************************/

//    // EVENTS - ACTIVITY VIEW EVENTS
//    @SuppressWarnings("unused")
//    @OnClick(R.id.dictionary_management_floating_action_button)
//    public void onFloatingActionButtonClicked() {
//        layoutManager.useSingleLayoutWithFragment(DictionaryManipulationFragment.TAG);
//        EventBus.getDefault().postSticky(new EventCreateVocable());
//    }
//
//    // EVENTS - EXTERNAL EVENTS
//
//    /**
//     * Observer method launched when a EventVocableSelected is posted on the app event bus
//     *
//     * @param event Event that occurs when a vocable is selected
//     */
//    @SuppressWarnings("unused")
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onVocableSelected(EventVocableSelected event) {
//        long selectedVocableID = event.getSelectedVocableID();
//
//        Word selectedVocable = null;
//        if (selectedVocableID >= 0) {
//            selectedVocable = model.getVocableById(selectedVocableID);
//
//            if (isLargeScreenDevice()) {
//                if (isLandscapeOrientation()) {
//                    layoutManager.useTwoHorizontalColumnsLayout();
//                } else {
//                    layoutManager.useTwoVerticalRowsLayout();
//                }
//            } else {
////                manipulationContainer.removeAllViews();
//                layoutManager.useSingleLayoutWithFragment(DictionaryManipulationFragment.TAG);
//            }
//
//            EventBus.getDefault().postSticky(new EventNotifySelectedVocableToObservers(selectedVocable));
//        }
//    }
//
//    /**
//     * Method called when a manipulation operation on a vocable (update or remove) is required
//     *
//     * @param event The event for manipulating a vocable
//     */
//    @SuppressWarnings("unused")
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onVocableManipulationRequest(EventManipulateVocable event) {
//        final long selectedVocableID = event.getVocableIDToManipulate();
//        switch (event.getTypeOfManipulation()) {
//            case EDIT:
//                break;
//            case REMOVE:
//                if (model.removeVocable(selectedVocableID)) {
//                    // Send selected vocable to all the listeners (fragments)
//                    EventBus.getDefault().postSticky(new EventNotifySelectedVocableToObservers(null));
////                    loadFragmentsInsideView(true, false);
//                }
//                break;
//            default:
//                throw new InvalidParameterException("Internal error, something goes wrong! - Invalid vocable manipulation operation");
//        }
//    }

    /**********************************************************************************************/

    // HELPER METHODS

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
//                    useTwoHorizontalColumnsLayout();
//                } else {
//                    // NOT IN LANDSCAPE MODE
//                    useTwoVerticalRowsLayout();
//                }
//            } else {
//                // NOT LARGE SCREEN or NO VOCABLE SELECTED
//                // so if it's present the manipulation fragment in the view remove it
//                manipulationContainer.removeAllViews();
//                removeFragmentFromView(manipulationFragment);
//                useSingleLayoutWithFragment(managementFragment);
//            }
//        } else if (useManagementFragment) {
//            manipulationContainer.removeAllViews();
//            removeFragmentFromView(manipulationFragment);
//            addFragmentToView(managementContainer, managementFragment);
//            useSingleLayoutWithFragment(managementFragment);
//        } else if (useManipulationFragment) {
//            managementContainer.removeAllViews();
//            removeFragmentFromView(managementFragment);
//            addFragmentToView(manipulationContainer, manipulationFragment);
//            useSingleLayoutWithFragment(manipulationFragment);
//        }
//    }
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
//
//    private boolean isLargeScreenDevice() {
//        return getResources().getBoolean(R.bool.LARGE_SCREEN);
//    }
//
//    private boolean isLandscapeOrientation() {
//        return getResources().getBoolean(R.bool.LANDSCAPE);
//    }

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

}
