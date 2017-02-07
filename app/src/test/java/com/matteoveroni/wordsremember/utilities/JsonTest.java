package com.matteoveroni.wordsremember.utilities;

import com.google.gson.Gson;
import org.junit.Test;
import static junit.framework.Assert.assertSame;

/**
 * @author Matteo Veroni
 */

public class JsonTest {

    @Test
    public void onlySameGsonInstanceIsReturnedFromJsonGetInstance() {
        Gson gson1 = Json.getInstance();
        Gson gson2 = Json.getInstance();

        assertSame("The two gson instances should be the same one", gson1, gson2);
    }
}
