package com.matteoveroni.wordsremember.persistency;

import com.matteoveroni.wordsremember.BuildConfig;
import com.matteoveroni.wordsremember.persistency.dao.UserDAO;
import com.matteoveroni.wordsremember.users.User;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static junit.framework.Assert.assertEquals;

/**
 * @author Matteo Veroni
 */

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class UserDAOTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    private final User user = new User(2, "PersistedUser", "fakemail@gmail.com");

    private UserDAO userDAO;

    @Before
    public void setUp() {
        ShadowApplication app = Shadows.shadowOf(RuntimeEnvironment.application);
        userDAO = new UserDAO(app.getApplicationContext());
    }

    //TODO: fix the test. This is not good for userDAO persistency (i want to save using the id i choose)
    //TODO: try to remove id autoincrement in UserContract schema
    @Test
    public void saveUserWorks() throws Exception {
        long userId = userDAO.saveUser(user);
        assertEquals("Ids are not equal!", user.getId(), userId);
    }

    // This doesnt work i don't know why!

//    @Test(expected = DuplicatedUsernameException.class)
//    public void tryingToSaveAlreadySavedUserThrowsDuplicatedUserException() throws Exception {
//        long userId1 = userDAO.registerUser(notPersistedUser);
//        long userId2 = userDAO.registerUser(notPersistedUser);
//    }

}
