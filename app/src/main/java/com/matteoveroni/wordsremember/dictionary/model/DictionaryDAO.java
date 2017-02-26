package com.matteoveroni.wordsremember.dictionary.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.wordsremember.dictionary.model.commands.AsyncDeleteCommand;
import com.matteoveroni.wordsremember.dictionary.model.commands.AsyncInsertCommand;
import com.matteoveroni.wordsremember.dictionary.model.commands.AsyncQueryCommand;
import com.matteoveroni.wordsremember.dictionary.model.commands.AsyncUpdateCommand;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

/**
 * Class that allows CRUD operations on dictionary data using a content resolver to communicate with
 * the dictionary content provider.
 *
 * @author Matteo Veroni
 */

public class DictionaryDAO {

    private final ContentResolver contentResolver;

    public DictionaryDAO(Context context) {
        this.contentResolver = context.getContentResolver();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Async methods - Vocable
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void asyncSaveVocable(Word vocable) {
        if (isWordValid(vocable) && vocable.getId() < 0) {
            ContentValues values = new ContentValues();
            values.put(VocablesContract.Schema.COLUMN_VOCABLE, vocable.getName());

            new AsyncInsertCommand(
                    contentResolver,
                    VocablesContract.CONTENT_URI,
                    values
            ).execute();
        }
    }

    public void asyncGetVocableById(long id) {
        if (id > 0) {
            String str_id = String.valueOf(id);
            String[] projection = {VocablesContract.Schema.COLUMN_VOCABLE};
            String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
            String[] selectionArgs = {str_id};

            new AsyncQueryCommand(
                    contentResolver,
                    VocablesContract.CONTENT_URI,
                    projection,
                    selection,
                    selectionArgs,
                    ""
            ).execute();
        }
    }

    public void asyncUpdateVocable(long id, Word updatedVocable) {
        if (id > 0) {
            final String str_id = String.valueOf(id);
            final ContentValues values = vocableToContentValues(updatedVocable);
            final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_id};

            new AsyncUpdateCommand(
                    contentResolver,
                    VocablesContract.CONTENT_URI,
                    values,
                    selection,
                    selectionArgs
            ).execute();
        }
    }

    public void asyncDeleteVocable(long id) {
        if (id > 0) {
            final String str_id = String.valueOf(id);
            final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_id};

            new AsyncDeleteCommand(
                    contentResolver,
                    VocablesContract.CONTENT_URI,
                    selection,
                    selectionArgs
            ).execute();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Async methods - Translations
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void asyncSaveTranslationForVocable(Word translation, Word vocable) {
        if (isWordValid(translation) && translation.getId() < 0 && isWordValid(vocable) && vocable.getId() > 0) {

            ContentValues translationValue = new ContentValues();
            translationValue.put(TranslationsContract.Schema.COLUMN_TRANSLATION, translation.getName());

            ContentValues vocablesTranslationValue = new ContentValues();
            vocablesTranslationValue.put(VocablesTranslationsContract.Schema.COLUMN_VOCABLE_ID, vocable.getId());
            vocablesTranslationValue.put(VocablesTranslationsContract.Schema.COLUMN_TRANSLATION_ID, translation.getId());

            new AsyncInsertCommand(contentResolver, TranslationsContract.CONTENT_URI, translationValue,
                    new AsyncInsertCommand(contentResolver, VocablesContract.CONTENT_URI, vocablesTranslationValue)
            );

//            new AsyncTranslationsHandler(contentResolver).startInsert(
//                    1,
//                    null,
//                    TranslationsContract.CONTENT_URI,
//                    translationToContentValues(translation)
//            );
        }
    }

    public void asyncGetTranslationById(long id) {
//        if (id > 0) {
//            final String str_idColumn = String.valueOf(id);
//
//            final String[] projection = {TranslationsContract.Schema.COLUMN_TRANSLATION};
//            final String selection = TranslationsContract.Schema.COLUMN_ID + " = ?";
//            final String[] selectionArgs = {str_idColumn};
//
//            final Uri uri = Uri.withAppendedPath(TranslationsContract.CONTENT_URI, str_idColumn).buildUpon().build();
//
//            asyncVocablesHandler.startQuery(
//                    1,
//                    null,
//                    uri,
//                    projection,
//                    selection,
//                    selectionArgs,
//                    null
//            );
//        }
    }

    public void asyncUpdateTranslation(long id, Word updatedTranslation) {
//        if (id > 0) {
//            final String str_id = String.valueOf(id);
//
//            final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
//            final String[] selectionArgs = {str_id};
//
//            final Uri uri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_id).buildUpon().build();
//
//            asyncVocablesHandler.startUpdate(
//                    1,
//                    null,
//                    uri,
//                    vocableToContentValues(updatedTranslation),
//                    selection,
//                    selectionArgs
//            );
//        }
    }

    public void asyncDeleteTranslation(long id) {
//        if (id > 0) {
//            final String str_idColumn = String.valueOf(id);
//
//            final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
//            final String[] selectionArgs = {str_idColumn};
//
//            final Uri uri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_idColumn).buildUpon().build();
//
//            asyncVocablesHandler.startDelete(
//                    1,
//                    null,
//                    uri,
//                    selection,
//                    selectionArgs
//            );
//        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Sync methods - Vocables
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Word getVocableById(long id) {
        final String str_id = String.valueOf(id);

        final String[] projection = {VocablesContract.Schema.COLUMN_VOCABLE};
        final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_id};

        final Uri uri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_id).buildUpon().build();

        Cursor cursor = contentResolver.query(
                uri,
                projection,
                selection,
                selectionArgs,
                null
        );

        if (cursor != null) {
            cursor.moveToFirst();
            return cursorToVocable(cursor);
        } else {
            throw new RuntimeException("duplicated ids for different vocables");
        }
    }

    public long saveVocable(Word vocable) {
        long id = -1;
        if (isWordValid(vocable) && vocable.getId() < 0) {
            final Uri uri = contentResolver.insert(
                    VocablesContract.CONTENT_URI,
                    vocableToContentValues(vocable)
            );

            if (uri != null) {
                final String createdRowId = uri.getLastPathSegment();

                if (!createdRowId.isEmpty()) {
                    id = Long.valueOf(createdRowId);
                }
            }
        }
        return id;
    }

    public boolean updateVocable(long vocableID, Word newVocable) {
        final String str_id = String.valueOf(vocableID);

        final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
        final String[] selectionArgs = {str_id};

        final Uri uri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_id).buildUpon().build();

        int updatedRecords = contentResolver.update(uri, vocableToContentValues(newVocable), selection, selectionArgs);

        return updatedRecords > 0;
    }

    public boolean removeVocable(long vocableID) {
        int recordDeleted = 0;
        if (vocableID > 0) {
            final String str_id = String.valueOf(vocableID);

            final String selection = VocablesContract.Schema.COLUMN_ID + " = ?";
            final String[] selectionArgs = {str_id};

            final Uri vocableUri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_id).buildUpon().build();

            recordDeleted = contentResolver.delete(
                    vocableUri,
                    selection,
                    selectionArgs
            );
        }
        return recordDeleted > 0;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Helper methods - Translations
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public static Word cursorToVocable(Cursor cursor) {
        final Word vocable = new Word(cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_VOCABLE)));
        vocable.setId(cursor.getLong(cursor.getColumnIndex(VocablesContract.Schema.COLUMN_ID)));
        return vocable;
    }

    public static Word cursorToTranslation(Cursor cursor) {
        final Word translation = new Word(cursor.getString(cursor.getColumnIndex(TranslationsContract.Schema.COLUMN_TRANSLATION)));
        translation.setId(cursor.getLong(cursor.getColumnIndex(TranslationsContract.Schema.COLUMN_ID)));
        return translation;
    }

    ContentValues vocableToContentValues(Word vocable) {
        final ContentValues values = new ContentValues();
        if (isWordValid(vocable)) {
            values.put(VocablesContract.Schema.COLUMN_VOCABLE, vocable.getName());
        }
        return values;
    }

    ContentValues translationToContentValues(Word translation) {
        final ContentValues values = new ContentValues();
        if (isWordValid(translation)) {
            values.put(TranslationsContract.Schema.COLUMN_TRANSLATION, translation.getName());
        }
        return values;
    }

    boolean isWordValid(Word word) {
        return word != null && word.getName() != null;
    }
}


