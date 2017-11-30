package com.matteoveroni.wordsremember.provider;


import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Matteo Veroni
 */

/**
 * Useful resources:
 * <p>
 * https://github.com/jameskbride/grocery-reminder/blob/master/app/src/test/java/com/groceryreminder/data/ReminderDBHelperTest.java
 * jameskbride.com/2016/02/13/android-tdd-series-test-driving-data-part-1-sqliteopenhelper.html
 * </p>
 */

// TODO: tests are broken must be fixed!

//@RunWith(RobolectricTestRunner.class)
//@Config(constants = BuildConfig.class)
//public class DatabaseHelperTest {
//
//    private ShadowApplication app = Shadows.shadowOf(RuntimeEnvironment.application);
//    private DatabaseHelper dbHelper;
//    private Cursor queryResults;
//    private ContentValues values = new ContentValues();
//
//    private final Table TABLE_VOCABLES = new Table(VocablesContract.Schema.TABLE_NAME, VocablesContract.Schema.ALL_COLUMNS);
//    private final Table TABLE_TRANSLATIONS = new Table(TranslationsContract.Schema.TABLE_NAME, TranslationsContract.Schema.ALL_COLUMNS);
//    private final Table TABLE_VOCABLESTRANSLATIONS = new Table(VocablesTranslationsContract.Schema.TABLE_NAME, VocablesTranslationsContract.Schema.ALL_COLUMNS);
//
//    private static final String INVALID_TABLE = "invalid_table";
//    private static final String[] INVALID_COLUMN = {"invalid_column"};
//
//    private static final String VALID_VOCABLE_NAME = "test_vocable_1";
//    private static final String VALID_TRANSLATION_NAME = "test_translation_1";
//
//    @Before
//    public void setUp() {
//        dbHelper = DatabaseManager.getInstance(app.getApplicationContext()).getCurrentProfileDBHelper();
//        assertNotNull("dbHelper should be created before each test", dbHelper);
//
//        final SQLiteDatabase database = dbHelper.getReadableDBForCurrentProfile();
//        assertNotNull("Database should be created before each test", database);
//        database.closeCurrentProfileDB();
//
//        queryResults = null;
//    }
//
//    @After
//    public void tearDown() {
//        if (dbHelper != null) {
//            dbHelper.closeCurrentProfileDB();
//        }
//        if (queryResults != null) {
//            queryResults.closeCurrentProfileDB();
//        }
//        values.clear();
//    }
//
//    @Test
//    public void test_AfterDbHelperIsCreated_DbNameShouldBeSet() {
//        assertEquals("DbHelper name should be equal to expected name", DATABASE_NAME, dbHelper.getCurrentProfileDBName());
//    }
//
//    @Test
//    public void test_AfterDbHelperCreation_TableVocables_ShouldBeCreatedAndEmpty() {
//        checkIfTableIsCreatedAndEmpty(TABLE_VOCABLES);
//    }
//
//    @Test
//    public void test_AfterDbHelperIsCreated_TableTranslations_ShouldBeCreatedAndEmpty() {
//        checkIfTableIsCreatedAndEmpty(TABLE_TRANSLATIONS);
//    }
//
//    @Test
//    public void test_AfterDbHelperIsCreated_TableVocablesTranslations_ShouldBeCreatedAndEmpty() {
//        checkIfTableIsCreatedAndEmpty(TABLE_VOCABLESTRANSLATIONS);
//    }
//
//    private void checkIfTableIsCreatedAndEmpty(Table table) {
//        queryResults = getQueryResultsFromTable(table);
//        assertTrue(table.getName() + " QueryResults shouldn't contain any query result", queryResults.getCount() == 0);
//    }
//
//    @Test(expected = SQLiteException.class)
//    public void test_WhenQueryExecutedUsingInvalidTable_SQliteExceptionShouldBeThrown() {
//        queryResults = dbHelper.getReadableDBForCurrentProfile().query(
//                INVALID_TABLE,
//                VocablesContract.Schema.ALL_COLUMNS,
//                null, null, null, null, null, null
//        );
//    }
//
//    @Test(expected = SQLiteException.class)
//    public void test_WhenQueryExecutedUsingInvalidColumn_SQliteExceptionShouldBeThrown() {
//        queryResults = dbHelper.getReadableDBForCurrentProfile().query(
//                VocablesContract.Schema.TABLE_NAME,
//                INVALID_COLUMN,
//                null, null, null, null, null, null
//        );
//    }
//
//    @Test
//    public void test_InsertValidVocableSucceed() {
//        final long idOfDataInserted = insertValidRecordInTable(TABLE_VOCABLES);
//        queryResults = getQueryResultsFromTable(TABLE_VOCABLES);
//        assertTrue("After inserting valid vocable table should contains one value", queryResults.getCount() == 1);
//        assertTrue("Should move to the first retrieved vocable position", queryResults.moveToFirst());
//        assertEquals("The id of the only record existing should be equal to the id of data inserted",
//                idOfDataInserted,
//                queryResults.getLong(queryResults.getColumnIndex(VocablesContract.Schema.COL_ID))
//        );
//        assertEquals("The inserted vocable name should match with the expected value",
//                VALID_VOCABLE_NAME,
//                queryResults.getString(queryResults.getColumnIndex(VocablesContract.Schema.COL_VOCABLE))
//        );
//    }
//
//    @Test
//    public void test_DeleteExistingVocableSucceed() {
//        final long idOfDataInserted = insertValidRecordInTable(TABLE_VOCABLES);
//
//        final int rowDeleted = dbHelper.getWritableDBForCurrentProfile().delete(
//                VocablesContract.Schema.TABLE_NAME,
//                VocablesContract.Schema.COL_ID + "=" + idOfDataInserted,
//                null
//        );
//        assertEquals("Should delete only one row", 1, rowDeleted);
//
//        checkIfTableIsCreatedAndEmpty(TABLE_VOCABLES);
//    }
//
//    @Test
//    public void test_UpdateExistingVocableSucceed() {
//        final long idOfDataInserted = insertValidRecordInTable(TABLE_VOCABLES);
//
//        final String UPDATED_VOCABLE_NAME = "UpdatedVocableName";
//        values.put(VocablesContract.Schema.COL_VOCABLE, UPDATED_VOCABLE_NAME);
//
//        final int rowsUpdated = dbHelper.getWritableDBForCurrentProfile().update(
//                VocablesContract.Schema.TABLE_NAME,
//                values,
//                VocablesContract.Schema.COL_ID + "=" + idOfDataInserted, null);
//        assertEquals("Rows updated should be only one", 1, rowsUpdated);
//
//        queryResults = getQueryResultsFromTable(TABLE_VOCABLES);
//        assertTrue("QueryResults must return just one vocable", queryResults.getCount() == 1);
//
//        queryResults.moveToFirst();
//        assertEquals("The only existing vocable should be updated like expected",
//                UPDATED_VOCABLE_NAME,
//                queryResults.getString(queryResults.getColumnIndex(VocablesContract.Schema.COL_VOCABLE))
//        );
//    }
//
//    @Test
//    public void test_resetDatabase_ShouldClearAllTables() {
//        insertValidRecordInTable(TABLE_VOCABLES);
//        insertValidRecordInTable(TABLE_TRANSLATIONS);
//        insertValidRecordInTable(TABLE_VOCABLESTRANSLATIONS);
//
//        dbHelper.resetCurrentProfileDB();
//
//        checkIfTableIsCreatedAndEmpty(TABLE_VOCABLES);
//        checkIfTableIsCreatedAndEmpty(TABLE_TRANSLATIONS);
//        checkIfTableIsCreatedAndEmpty(TABLE_VOCABLESTRANSLATIONS);
//    }
//
//    private Cursor getQueryResultsFromTable(Table table) {
//        return dbHelper.getReadableDBForCurrentProfile().query(table.getName(), table.getColumns(), null, null, null, null, null, null);
//    }
//
//    private long insertValidRecordInTable(Table table) {
//        switch (table.getName()) {
//            case VocablesContract.Schema.TABLE_NAME:
//                values.put(VocablesContract.Schema.COL_VOCABLE, VALID_VOCABLE_NAME);
//                break;
//            case TranslationsContract.Schema.TABLE_NAME:
//                values.put(TranslationsContract.Schema.COL_TRANSLATION, VALID_TRANSLATION_NAME);
//                break;
//            case VocablesTranslationsContract.Schema.TABLE_NAME:
//                values.put(VocablesTranslationsContract.Schema.COL_VOCABLE_ID, 1);
//                values.put(VocablesTranslationsContract.Schema.COL_TRANSLATION_ID, 1);
//                break;
//            default:
//                throw new RuntimeException("Error trying to insert new record in table. Table " + table.getName() + " unknown");
//        }
//        long idOfDataInserted = dbHelper.getWritableDBForCurrentProfile().insert(table.getName(), null, values);
//        dbHelper.closeCurrentProfileDB();
//        return idOfDataInserted;
//    }
//
//    private class Table {
//        private final String name;
//        private final String[] columns;
//
//        Table(String name, String[] columns) {
//            this.name = name;
//            this.columns = columns;
//        }
//
//        String getName() {
//            return name;
//        }
//
//        String[] getColumns() {
//            return columns;
//        }
//    }
//}
