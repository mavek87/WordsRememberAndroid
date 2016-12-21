package com.matteoveroni.wordsremember.dictionary;

import android.content.Intent;
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
import android.widget.Toast;

import com.matteoveroni.wordsremember.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.factories.DictionaryFragmentFactory;
import com.matteoveroni.wordsremember.dictionary.factories.DictionaryManagementPresenterFactory;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.dictionary.interfaces.DictionaryManagementView;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.ui.layout.ViewLayout;

import static com.matteoveroni.wordsremember.ui.layout.ViewLayout.ViewLayoutBuilder;

import com.matteoveroni.wordsremember.ui.layout.ViewLayoutType;
import com.matteoveroni.wordsremember.utilities.Json;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.matteoveroni.wordsremember.dictionary.factories.DictionaryFragmentFactory.DictionaryFragmentType;

/**
 * Dictionary Management Activity
 *
 * @author Matteo Veroni
 */

/**
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.la55rzpm4
 */
public class DictionaryManagementActivity extends AppCompatActivity
        implements DictionaryManagementView, LoaderManager.LoaderCallbacks<DictionaryManagementPresenter> {

    public static final String TAG = "A_DICTIONARY_MANAGE";

    private ViewLayout viewLayout;

    private DictionaryManagementPresenter presenter;
    private static final int PRESENTER_ID = 1;

    private FragmentManager fragmentManager;
    private DictionaryManagementFragment managementFragment;
    private DictionaryManipulationFragment manipulationFragment;

    private boolean isActivityCreatedForTheFirstTime;

    @BindView(R.id.dictionary_management_container)
    FrameLayout managementContainer;

    @BindView(R.id.dictionary_manipulation_container)
    FrameLayout manipulationContainer;

    @BindView(R.id.dictionary_management_floating_action_button)
    FloatingActionButton floatingActionButton;

    public DictionaryManagementActivity() {
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

    // ANDROID LIFECYCLE METHOD

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
        if (isActivityCreatedForTheFirstTime) {
            presenter.onViewCreatedForTheFirstTime();
        }
    }

    @Override
    protected void onStop() {
        presenter.onViewDetached();
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_management_view);

        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(PRESENTER_ID, null, this);

        fragmentManager = getSupportFragmentManager();

        if (isActivityCreatedForTheFirstTime = (savedInstanceState == null)) {
            managementFragment = (DictionaryManagementFragment) DictionaryFragmentFactory.create(DictionaryFragmentType.MANAGEMENT);
            manipulationFragment = (DictionaryManipulationFragment) DictionaryFragmentFactory.create(DictionaryFragmentType.MANIPULATION);

            addFragmentToView(managementContainer, managementFragment, DictionaryManagementFragment.TAG);
            addFragmentToView(manipulationContainer, manipulationFragment, DictionaryManipulationFragment.TAG);
        }
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
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (presenter.onKeyBackPressedRestorePreviousState()) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
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
        viewLayout = ViewLayoutBuilder
                .viewLayoutType(ViewLayoutType.SINGLE_LAYOUT)
                .mainFragmentTag(fragmentTAG)
                .build();
    }

    @Override
    public void useTwoHorizontalColumnsLayout() {
        setLayout(0, ViewLayout.MATCH_PARENT, 1f, 0, ViewLayout.MATCH_PARENT, 1f);
        viewLayout = ViewLayoutBuilder
                .viewLayoutType(ViewLayoutType.TWO_COLUMNS_LAYOUT)
                .build();
    }

    @Override
    public void useTwoVerticalRowsLayout() {
        setLayout(ViewLayout.MATCH_PARENT, 0, 1f, ViewLayout.MATCH_PARENT, 0, 1f);
        viewLayout = ViewLayoutBuilder
                .viewLayoutType(ViewLayoutType.TWO_ROWS_LAYOUT)
                .build();
    }

    @Override
    public void switchToManipulationView(Word vocableToManipulate) {
        Intent intent_startManipulationActivity = new Intent(getApplicationContext(), DictionaryManipulationActivity.class);
        intent_startManipulationActivity.putExtra(
                Extras.VOCABLE_TO_MANIPULATE,
                (vocableToManipulate != null) ? Json.getInstance().toJson(vocableToManipulate) : ""
        );
        startActivity(intent_startManipulationActivity);
    }

    @Override
    public boolean isViewLarge() {
        return getResources().getBoolean(R.bool.LARGE_SCREEN);
    }

    @Override
    public boolean isViewLandscape() {
        return getResources().getBoolean(R.bool.LANDSCAPE_MODE);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    /**********************************************************************************************/

//    // EVENTS - ACTIVITY VIEW EVENTS
    @OnClick(R.id.dictionary_management_floating_action_button)
    @SuppressWarnings("unused")
    public void onFloatingActionButtonClicked() {
//        injectedLayoutManager.useSingleLayoutWithFragment(DictionaryManipulationFragment.TAG);
//        EventBus.getDefault().postSticky(new EventStartVocableCreation());
        Word vocableToCreate = new Word("provaCreazione");
        presenter.onCreateVocableRequest(vocableToCreate);
    }
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
//    public void onEventNotifiedVocableToVisualize(EventVocableSelected event) {
//        long selectedVocableID = event.getSelectedVocableID();
//
//        Word selectedVocable = null;
//        if (selectedVocableID >= 0) {
//            selectedVocable = model.asyncGetVocableById(selectedVocableID);
//
//            if (isViewLarge()) {
//                if (isViewLandscape()) {
//                    injectedLayoutManager.useTwoHorizontalColumnsLayout();
//                } else {
//                    injectedLayoutManager.useTwoVerticalRowsLayout();
//                }
//            } else {
////                manipulationContainer.removeAllViews();
//                injectedLayoutManager.useSingleLayoutWithFragment(DictionaryManipulationFragment.TAG);
//            }
//
//            EventBus.getDefault().postSticky(new EventVisualizeVocable(selectedVocable));
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
