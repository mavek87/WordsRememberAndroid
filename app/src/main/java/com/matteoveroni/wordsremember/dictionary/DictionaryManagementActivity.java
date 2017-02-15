package com.matteoveroni.wordsremember.dictionary;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.matteoveroni.wordsremember.PresenterLoader;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.dictionary.presenter.DictionaryManagementPresenter;
import com.matteoveroni.wordsremember.dictionary.factories.DictionaryManagementPresenterFactory;
import com.matteoveroni.wordsremember.pojo.Word;
import com.matteoveroni.wordsremember.utilities.Json;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Dictionary Management Activity
 * <p>
 * https://medium.com/@czyrux/presenter-surviving-orientation-changes-with-loaders-6da6d86ffbbf#.la55rzpm4
 *
 * @author Matteo Veroni
 */

public class DictionaryManagementActivity
        extends AppCompatActivity
        implements DictionaryManagementView, LoaderManager.LoaderCallbacks<DictionaryManagementPresenter> {

    public static final String TAG = TagGenerator.tag(DictionaryManagementActivity.class);

    private DictionaryManagementPresenter presenter;
    private final int PRESENTER_LOADER_ID = 1;

    public DictionaryManagementActivity() {
    }

    @Override
    @OnClick(R.id.dictionary_management_floating_action_button)
    @SuppressWarnings("unused")
    public void createVocableAction() {
        presenter.onCreateVocableRequest();
    }

    @Override
    public void goToManipulationView(Word vocableToManipulate) {
        Intent intent_startManipulationActivity = new Intent(getApplicationContext(), DictionaryManipulationActivity.class);
        intent_startManipulationActivity.putExtra(
                Extras.VOCABLE_TO_MANIPULATE,
                Json.getInstance().toJson(vocableToManipulate)
        );
        startActivity(intent_startManipulationActivity);
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dictionary_management);
        ButterKnife.bind(this);

        getSupportLoaderManager().initLoader(PRESENTER_LOADER_ID, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.onViewAttached(this);
        presenter.onViewCreated(getApplicationContext());
    }

    @Override
    protected void onStop() {
        presenter.onViewDetached();
        super.onStop();
    }

    /**********************************************************************************************/

    // Presenter Loader methods

    /**********************************************************************************************/

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
}
