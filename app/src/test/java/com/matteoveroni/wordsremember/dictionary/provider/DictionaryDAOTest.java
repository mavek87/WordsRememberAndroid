package com.matteoveroni.wordsremember.dictionary.provider;

import android.content.ContentValues;
import android.net.Uri;
import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;
import android.util.Log;

import com.matteoveroni.wordsremember.provider.DictionaryProvider;
import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;
import com.matteoveroni.wordsremember.utilities.TagGenerator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

/**
 * http://jameskbride.com/2016/02/28/android-tdd-series-test-driving-data-part-2-contentproviders.html
 */

@RunWith(MockitoJUnitRunner.class)
public class DictionaryDAOTest extends ProviderTestCase2<DictionaryProvider> {

    public static final String TAG = TagGenerator.getInstance().getTag(DictionaryDAOTest.class);

    //    private String DICTIONARY_URI = "content://com.matteoveroni.wordsremember.provider/dictionary";
    private String DICTIONARY_URI = DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + DictionaryContract.NAME;
    private static final String NAME = DictionaryContract.NAME;

    @Mock
    private MockContentResolver mockResolver;
    @Mock
    private Uri dictionaryUri;

    private static final String VALID_VOCABLE_NAME = "Prova";

    public DictionaryDAOTest() {
        super(DictionaryProvider.class, DictionaryProvider.CONTENT_AUTHORITY);
    }

    @Before
    public void initialize() {
        when(dictionaryUri.toString()).thenReturn(DICTIONARY_URI);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        Log.d(TAG, "setUp");
        mockResolver = getMockContentResolver();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        Log.d(TAG, "tearDown");
    }

    @Test
    public void testInsert() {
        ContentValues values = new ContentValues();
        values.put(DictionaryContract.Schema.COLUMN_ID, 1);
        values.put(DictionaryContract.Schema.COLUMN_NAME, VALID_VOCABLE_NAME);

        Uri uri = getMockContentResolver().insert(DictionaryContract.CONTENT_URI, values);
        assertNotNull(uri);
    }
//
//    @Test
//    public void testActiveUserInsert__inserts_a_valid_record() {
//        Uri uri = mockResolver.insert(DictionaryContract.CONTENT_URI, getFullActiveUserContentValues());
//        assertEquals(1L, ContentUris.parseId(uri));
//    }

//    public void testActiveUserInsert__cursor_contains_valid_data() {
//        mMockResolver.insert(ActiveUserContract.CONTENT_URI, getFullActiveUserContentValues());
//        Cursor cursor = mMockResolver.query(ActiveUserContract.CONTENT_URI, null, null, new String[]{}, null);
//        assertNotNull(cursor);
//        assertEquals(1, cursor.getCount());
//        assertTrue(cursor.moveToFirst());
//        assertEquals(VALID_USERNAME, cursor.getString(cursor.getColumnIndex(ActiveUserContract.Columns.USERNAME)));
//        assertEquals(VALID_COMPANY_CODE, cursor.getString(cursor.getColumnIndex(ActiveUserContract.Columns.COMPANY_CODE)));
//        assertEquals(VALID_COMPANY_LETTER, cursor.getString(cursor.getColumnIndex(ActiveUserContract.Columns.COMPANY_LETTER)));
//        assertEquals(VALID_DRIVER_CODE, cursor.getString(cursor.getColumnIndex(ActiveUserContract.Columns.DRIVER_CODE)));
//        assertEquals(VALID_SITE_NUMBER, cursor.getString(cursor.getColumnIndex(ActiveUserContract.Columns.SITE_NUMBER)));
//        assertEquals(VALID_FIRST_NAME, cursor.getString(cursor.getColumnIndex(ActiveUserContract.Columns.FIRST_NAME)));
//        assertEquals(VALID_SURNAME, cursor.getString(cursor.getColumnIndex(ActiveUserContract.Columns.SURNAME)));
//    }
//
//    public void testActiveUserInsert__throws_SQLiteConstraintException_when_NOT_NULL_constraint_not_met() {
//        try {
//            mMockResolver.insert(ActiveUserContract.CONTENT_URI, getActiveUserContentValuesWithNullCompanyCode());
//            fail("SQLiteConstraintException should have been thrown!");
//        } catch (SQLiteConstraintException e) {
//            assertEquals("active_user.comp_code may not be NULL (code 19)", e.getMessage());
//        }
//    }

    /**
     * @return a ContentValues object with a value set for each ActiveUser column
     */
    public static ContentValues getFullActiveUserContentValues() {
        ContentValues v = new ContentValues(1);
        v.put(DictionaryContract.Schema.COLUMN_NAME, VALID_VOCABLE_NAME);
        return v;
    }

//    public static ContentValues getActiveUserContentValuesWithNullCompanyCode() {
//        ContentValues v = new ContentValues(7);
//        v.put(ActiveUserContract.Columns.USERNAME, VALID_USERNAME);
//        v.putNull(ActiveUserContract.Columns.COMPANY_CODE);
//        v.put(ActiveUserContract.Columns.COMPANY_LETTER, VALID_COMPANY_LETTER);
//        v.put(ActiveUserContract.Columns.DRIVER_CODE, VALID_DRIVER_CODE);
//        v.put(ActiveUserContract.Columns.SITE_NUMBER, VALID_SITE_NUMBER);
//        v.put(ActiveUserContract.Columns.FIRST_NAME, VALID_FIRST_NAME);
//        v.put(ActiveUserContract.Columns.SURNAME, VALID_SURNAME);
//        return v;
//    }
}


//
//import android.content.ContentValues;
//import android.net.Uri;
//import android.test.ProviderTestCase2;
//import android.test.mock.MockContentResolver;
//import android.util.Log;
//
//import com.matteoveroni.wordsremember.pojo.Word;
//import com.matteoveroni.wordsremember.provider.DictionaryProvider;
//import com.matteoveroni.wordsremember.provider.contracts.DictionaryContract;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import static org.mockito.Matchers.eq;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
///**
// * @author Matteo Veroni
// */
//
///**
// * Reference Material:
// * <p>
// * http://stackoverflow.com/questions/5286876/android-code-example-for-testing-with-the-providertestcase2
// * https://github.com/Miserlou/Android-SDK-Samples/blob/master/NotePad/tests/src/com/example/android/notepad/NotePadProviderTest.java
// */
//
//@RunWith(MockitoJUnitRunner.class)
//public class DictionaryDAOTest extends ProviderTestCase2<DictionaryProvider> {
//
//    public static final String TAG_PREFIX = "DictDAOTest";
//
//    //    private String DICTIONARY_URI = "content://com.matteoveroni.wordsremember.provider/dictionary";
//    private String DICTIONARY_URI = DictionaryProvider.SCHEME + DictionaryProvider.CONTENT_AUTHORITY + "/" + DictionaryContract.NAME;
//    private static final String NAME = DictionaryContract.NAME;
//
//    @Mock
//    private MockContentResolver mockResolver;
//    @Mock
//    private Uri dictionaryUri;
//
//    public DictionaryDAOTest() {
//        super(DictionaryProvider.class, DictionaryProvider.CONTENT_AUTHORITY);
//    }
//
//    @Before
//    public void initialize() {
//        when(dictionaryUri.toString()).thenReturn(DICTIONARY_URI);
//    }
//
//    @Override
//    protected void setUp() throws Exception {
//        super.setUp();
//        Log.d(TAG_PREFIX, "setUp");
//        mockResolver = getMockContentResolver();
//
//
//    }
//
//    @Override
//    protected void tearDown() throws Exception {
//        super.tearDown();
//        Log.d(TAG_PREFIX, "tearDown");
//    }
//
//    @Test
//    public void test() {
//        assertEquals("dictionaryUri is wrong", dictionaryUri.toString(), "content://com.matteoveroni.wordsremember.provider/dictionary");
//
//        String[] data = {"prova"};
//        String[] projection = new String[]{DictionaryContract.Schema.COLUMN_NAME};
//
//        ContentValues cv = new ContentValues();
//        cv.put(DictionaryContract.Schema.COLUMN_NAME, "prova");
//        mockResolver.insert(dictionaryUri, cv);
//    }
//
////    @Test
////    public void testB() {
////        Cursor cursor = mock(Cursor.class);
////        DictionaryDAO.cursorToVocable(cursor);
////        when(cursor.getString(cursor.getColumnIndex(DictionaryContract.Schema.COLUMN_TRANSLATION))).thenReturn()
////
////
////    }
//
//}
////
////class MockContentProviderTest extends AndroidTestCase {
////    public void testMockPhoneNumbersFromContacts(){
////        //Step 1: Create data you want to return and put it into a matrix cursor
////        //In this case I am mocking getting phone numbers from Contacts Provider
////        String[] exampleData = {"(979) 267-8509"};
////        String[] examleProjection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
////        MatrixCursor matrixCursor = new MatrixCursor(examleProjection);
////        matrixCursor.addRow(exampleData);
////
////        //Step 2: Create a stub content provider and add the matrix cursor as the expected result of the query
////        HashMapMockContentProvider mockProvider = new HashMapMockContentProvider();
////        mockProvider.addQueryResult(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, matrixCursor);
////
////        //Step 3: Create a mock resolver and add the content provider.
////        MockContentResolver mockResolver = new MockContentResolver();
////        mockResolver.addProvider(ContactsContract.AUTHORITY /*Needs to be the same as the authority of the provider you are mocking */, mockProvider);
////
////        //Step 4: Add the mock resolver to the mock context
////        ContextWithMockContentResolver mockContext = new ContextWithMockContentResolver(super.getContext());
////        mockContext.setContentResolver(mockResolver);
////
////        //Example Test
////        ExampleClassUnderTest underTest = new ExampleClassUnderTest();
////        String result = underTest.getPhoneNumbers(mockContext);
////        assertEquals("(979) 267-8509",result);
////    }
////
////    //Specialized Mock Content provider for step 2.  Uses a hashmap to return data dependent on the dictionaryUri in the query
////    public class HashMapMockContentProvider extends MockContentProvider {
////        private HashMap<Uri, Cursor> expectedResults = new HashMap<Uri, Cursor>();
////        public void addQueryResult(Uri uriIn, Cursor expectedResult){
////            expectedResults.put(uriIn, expectedResult);
////        }
////        @Override
////        public Cursor query(Uri dictionaryUri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
////            return expectedResults.instance(dictionaryUri);
////        }
////    }
////
////    public class ContextWithMockContentResolver extends RenamingDelegatingContext {
////        private ContentResolver contentResolver;
////        public void setContentResolver(ContentResolver contentResolver){ this.contentResolver = contentResolver;}
////        public ContextWithMockContentResolver(Context targetContext) { super(targetContext, "test");}
////        @Override public ContentResolver getContentResolver() { return contentResolver; }
////        @Override public Context getApplicationContext(){ return this; } //Added in-case my class called getApplicationContext()
////    }
////
////    //An example class under test which queries the populated cursor to instance the expected phone number
////    public class ExampleClassUnderTest{
////        public  String getPhoneNumbers(Context context){//Query for  phone numbers from contacts
////            String[] projection = new String[]{ ContactsContract.CommonDataKinds.Phone.NUMBER};
////            Cursor cursor= context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
////            cursor.moveToNext();
////            return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
////        }
////    }
////}