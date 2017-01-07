package com.matteoveroni.wordsremember.dictionary;

import android.test.mock.MockContext;

import com.matteoveroni.wordsremember.dictionary.model.DictionaryDAO;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import static org.mockito.Mockito.mock;

public class DictionaryDAOTest {

    private MockContext context;
    private DictionaryDAO dao;

    @Before
    public void createMockLayouts() {
//        this.dao = new DictionaryDAO(null);
    }

    @Test
    public void test() {
    }

//    @Test
//    public void testB() {
//        Cursor cursor = mock(Cursor.class);
//        DictionaryDAO.cursorToVocable(cursor);
//        when(cursor.getString(cursor.getColumnIndex(DictionaryContract.Schema.COLUMN_TRANSLATION))).thenReturn()
//
//
//    }
}
