package com.matteoveroni.wordsremember.dictionary;

import android.test.ProviderTestCase2;
import android.test.mock.MockContentResolver;

import com.matteoveroni.wordsremember.provider.DictionaryProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import static org.mockito.Mockito.mock;

/**
 * @author Matteo Veroni
 */

/**
 * Reference Material:
 *
 * http://stackoverflow.com/questions/5286876/android-code-example-for-testing-with-the-providertestcase2
 */

@RunWith(MockitoJUnitRunner.class)
public class DictionaryDAOTest extends ProviderTestCase2<DictionaryProvider> {

    @Mock
    private MockContentResolver mockResolver;

    public DictionaryDAOTest() {
        super(DictionaryProvider.class, DictionaryProvider.CONTENT_AUTHORITY);
    }

    @Before
    public void createMockResolver() {
        mockResolver = getMockContentResolver();
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
//
//class MockContentProviderTest extends AndroidTestCase {
//    public void testMockPhoneNumbersFromContacts(){
//        //Step 1: Create data you want to return and put it into a matrix cursor
//        //In this case I am mocking getting phone numbers from Contacts Provider
//        String[] exampleData = {"(979) 267-8509"};
//        String[] examleProjection = new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER};
//        MatrixCursor matrixCursor = new MatrixCursor(examleProjection);
//        matrixCursor.addRow(exampleData);
//
//        //Step 2: Create a stub content provider and add the matrix cursor as the expected result of the query
//        HashMapMockContentProvider mockProvider = new HashMapMockContentProvider();
//        mockProvider.addQueryResult(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, matrixCursor);
//
//        //Step 3: Create a mock resolver and add the content provider.
//        MockContentResolver mockResolver = new MockContentResolver();
//        mockResolver.addProvider(ContactsContract.AUTHORITY /*Needs to be the same as the authority of the provider you are mocking */, mockProvider);
//
//        //Step 4: Add the mock resolver to the mock context
//        ContextWithMockContentResolver mockContext = new ContextWithMockContentResolver(super.getContext());
//        mockContext.setContentResolver(mockResolver);
//
//        //Example Test
//        ExampleClassUnderTest underTest = new ExampleClassUnderTest();
//        String result = underTest.getPhoneNumbers(mockContext);
//        assertEquals("(979) 267-8509",result);
//    }
//
//    //Specialized Mock Content provider for step 2.  Uses a hashmap to return data dependent on the uri in the query
//    public class HashMapMockContentProvider extends MockContentProvider {
//        private HashMap<Uri, Cursor> expectedResults = new HashMap<Uri, Cursor>();
//        public void addQueryResult(Uri uriIn, Cursor expectedResult){
//            expectedResults.put(uriIn, expectedResult);
//        }
//        @Override
//        public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder){
//            return expectedResults.get(uri);
//        }
//    }
//
//    public class ContextWithMockContentResolver extends RenamingDelegatingContext {
//        private ContentResolver contentResolver;
//        public void setContentResolver(ContentResolver contentResolver){ this.contentResolver = contentResolver;}
//        public ContextWithMockContentResolver(Context targetContext) { super(targetContext, "test");}
//        @Override public ContentResolver getContentResolver() { return contentResolver; }
//        @Override public Context getApplicationContext(){ return this; } //Added in-case my class called getApplicationContext()
//    }
//
//    //An example class under test which queries the populated cursor to get the expected phone number
//    public class ExampleClassUnderTest{
//        public  String getPhoneNumbers(Context context){//Query for  phone numbers from contacts
//            String[] projection = new String[]{ ContactsContract.CommonDataKinds.Phone.NUMBER};
//            Cursor cursor= context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, projection, null, null, null);
//            cursor.moveToNext();
//            return cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//        }
//    }
//}