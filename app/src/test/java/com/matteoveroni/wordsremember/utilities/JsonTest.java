package com.matteoveroni.wordsremember.utilities;

import com.google.gson.Gson;
import org.junit.Test;
import static junit.framework.Assert.assertSame;

/**
 * @author Matteo Veroni
 */

public class JsonTest {

    @Test
    public void ONLY_SAME_GSON_INSTANCE_IS_RETURNED_FROM_JSON_GET_INSTANCE() {
        Gson firstGsonObject = Json.getInstance();
        Gson secondGsonObject = Json.getInstance();

        assertSame("The two gson instances should be the same one", firstGsonObject, secondGsonObject);
    }
}
