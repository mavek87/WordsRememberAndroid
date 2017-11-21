package com.matteoveroni.wordsremember.scene_userprofile.manager.view.fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.matteoveroni.androidtaggenerator.TagGenerator;
import com.matteoveroni.wordsremember.R;
import com.matteoveroni.wordsremember.persistency.contracts.UserProfilesContract;
import com.matteoveroni.wordsremember.scene_userprofile.manager.events.EventEditUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.UserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.manager.events.EventDeleteUserProfile;
import com.matteoveroni.wordsremember.scene_userprofile.manager.events.EventUserProfileSelected;
import com.matteoveroni.wordsremember.ui.listview.adapters.UserProfilesListViewAdapter;

import org.greenrobot.eventbus.EventBus;

/**
 * @author Matteo Veroni
 */

public class UserProfileListFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = TagGenerator.tag(UserProfileListFragment.class);

    private static final EventBus EVENT_BUS = EventBus.getDefault();
    private static final int ID_CURSOR_LOADER = 1;

    private UserProfilesListViewAdapter profilesListAdapter;
    private LoaderManager fragLoaderManager;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        final String[] columns = new String[]{UserProfilesContract.Schema.COL_ID, UserProfilesContract.Schema.COL_PROFILE_NAME};
        return new CursorLoader(
                getActivity(),
                UserProfilesContract.CONTENT_URI,
                null,
                null,
                null,
                UserProfilesContract.Schema.COL_PROFILE_NAME + " ASC"
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        profilesListAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        profilesListAdapter.swapCursor(null);
    }

    @Override
    public void onResume() {
        fragLoaderManager.restartLoader(ID_CURSOR_LOADER, null, this);
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profiles_list, container, false);

        profilesListAdapter = new UserProfilesListViewAdapter(getContext(), null);
        setListAdapter(profilesListAdapter);

        fragLoaderManager = getLoaderManager();
        fragLoaderManager.initLoader(ID_CURSOR_LOADER, null, this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        Cursor cursor = profilesListAdapter.getCursor();
        EVENT_BUS.postSticky(new EventUserProfileSelected(getSelectedUserProfile(cursor, position)));
        Toast.makeText(getActivity(), cursor.getString(1), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        registerForContextMenu(getListView());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_profiles_list_long_press, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo contextMenuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = contextMenuInfo.position;
        Cursor cursor = profilesListAdapter.getCursor();

        UserProfile selectedUserProfile = getSelectedUserProfile(cursor, position);
        switch (item.getItemId()) {
            case R.id.menu_dictionary_list_long_press_edit:
                EVENT_BUS.postSticky(new EventEditUserProfile(selectedUserProfile));
                // TODO: remove this comment
//                Toast.makeText(getContext(), selectedUserProfile.getName(), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_dictionary_list_long_press_remove:
                EVENT_BUS.postSticky(new EventDeleteUserProfile(selectedUserProfile));
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private UserProfile getSelectedUserProfile(Cursor cursor, int position) {
        cursor.moveToPosition(position);

        return new UserProfile(
                cursor.getLong(cursor.getColumnIndex(UserProfilesContract.Schema.COL_ID)),
                cursor.getString(cursor.getColumnIndex(UserProfilesContract.Schema.COL_PROFILE_NAME))
        );
    }
}








