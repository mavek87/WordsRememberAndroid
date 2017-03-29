package com.matteoveroni.wordsremember.dictionary.model;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.dictionary.commands.AsyncSearchTranslationsByNameCommand;
import com.matteoveroni.wordsremember.dictionary.commands.AsyncSearchVocablesByNameCommand;
import com.matteoveroni.wordsremember.dictionary.commands.AsyncDeleteCommand;
import com.matteoveroni.wordsremember.dictionary.commands.AsyncInsertCommand;
import com.matteoveroni.wordsremember.dictionary.commands.AsyncUpdateCommand;
import com.matteoveroni.wordsremember.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.pojos.Word;
import com.matteoveroni.wordsremember.provider.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesContract;
import com.matteoveroni.wordsremember.provider.contracts.VocablesTranslationsContract;

import java.util.ArrayList;
import java.util.List;

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

    /***********************************************************************************************
     * Async methods - VOCABLES
     **********************************************************************************************/

    public void asyncSaveVocable(Word vocable) {
        if (!Word.isValid(vocable) || vocable.getId() > 0) {
            throw new IllegalArgumentException("AsyncSaveVocable invalid argument");
        }
        final ContentValues values = new ContentValues();
        values.put(VocablesContract.Schema.COL_VOCABLE, vocable.getName());

        new AsyncInsertCommand(
                contentResolver,
                VocablesContract.CONTENT_URI,
                values
        ).execute();
    }

    public void asyncUpdateVocable(long id, Word updatedVocable) {
        if (!Word.isValid(updatedVocable) || id < 1) {
            throw new IllegalArgumentException("AsyncUpdateVocable invalid argument");
        }
        final String str_id = String.valueOf(id);
        final String selection = VocablesContract.Schema.COL_ID + "=?";
        final String[] selectionArgs = {str_id};

        new AsyncUpdateCommand(
                contentResolver,
                VocablesContract.CONTENT_URI,
                vocableToContentValues(updatedVocable),
                selection,
                selectionArgs
        ).execute();
    }

    public void asyncDeleteVocable(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("AsyncDeleteVocable invalid argument");
        }
        asyncDeleteVocableTranslationsByVocableId(id);
        new AsyncDeleteCommand(
                contentResolver,
                VocablesContract.CONTENT_URI,
                VocablesContract.Schema.COL_ID + "=?",
                new String[]{String.valueOf(id)}
        ).execute();
    }

    public void asyncSearchVocableByName(String vocableName) throws IllegalArgumentException {
        if (Str.isNullOrEmpty(vocableName)) {
            throw new IllegalArgumentException("AsyncSearchVocableByName invalid argument");
        }
        new AsyncSearchVocablesByNameCommand(contentResolver, vocableName, "").execute();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Async methods - TRANSLATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void asyncSaveTranslation(Word translation) {
        if (!Word.isValid(translation) || translation.getId() > 0) {
            throw new RuntimeException("AsyncSaveTranslation invalid argument");
        }
        new AsyncInsertCommand(
                contentResolver,
                TranslationsContract.CONTENT_URI,
                translationToContentValues(translation)
        ).execute();
    }

    public void asyncDeleteTranslation(long id) {
        if (id < 1) {
            throw new IllegalArgumentException("AsyncDeleteTranslation invalid argument");
        }
        asyncDeleteVocableTranslationsByTranslationId(id);
        new AsyncDeleteCommand(
                contentResolver,
                TranslationsContract.CONTENT_URI,
                TranslationsContract.Schema.COL_ID + "=?",
                new String[]{String.valueOf(id)}
        ).execute();
    }

    public void asyncSearchTranslationByName(String translationName) throws IllegalArgumentException {
        if (Str.isNullOrEmpty(translationName)) {
            throw new IllegalArgumentException("AsyncSearchTranslationByName invalid argument");
        }
        new AsyncSearchTranslationsByNameCommand(contentResolver, translationName, "").execute();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Async methods - VOCABLES_TRANSLATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void asyncDeleteVocableTranslationsByVocableId(long vocableId) {
        if (vocableId < 1) {
            throw new IllegalArgumentException("AsyncDeleteVocableTranslationsByTranslationId invalid argument");
        }
        new AsyncDeleteCommand(
                contentResolver,
                VocablesTranslationsContract.CONTENT_URI,
                VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID + "=?",
                new String[]{String.valueOf(vocableId)}
        ).execute();
    }

    public void asyncDeleteVocableTranslationsByTranslationId(long translationId) {
        if (translationId < 1) {
            throw new IllegalArgumentException("AsyncDeleteVocableTranslationsByTranslationId invalid argument");
        }
        new AsyncDeleteCommand(
                contentResolver,
                VocablesTranslationsContract.CONTENT_URI,
                VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID + "=?",
                new String[]{String.valueOf(translationId)}
        ).execute();
    }

    public void asyncDeleteVocableTranslationsByVocableAndTranslationIds(long vocableId, long translationId) {
        if (vocableId < 1 || translationId < 1) {
            throw new IllegalArgumentException("AsyncDeleteVocableTranslationsByVocableAndTranslationIds invalid argument");
        }
        new AsyncDeleteCommand(
                contentResolver,
                VocablesTranslationsContract.CONTENT_URI,
                VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID + "=? AND " + VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID + "=?",
                new String[]{String.valueOf(vocableId), String.valueOf(translationId)}
        ).execute();
    }

    public void asyncSaveVocableTranslation(VocableTranslation vocableTranslation) {
        final Word translation = vocableTranslation.getTranslation();
        final Word vocable = vocableTranslation.getVocable();
        if (!Word.isValid(translation) || translation.getId() < 1 || !Word.isValid(vocable) && vocable.getId() < 1) {
            throw new IllegalArgumentException("AsyncSaveVocableTranslation invalid argument");
        }
        final ContentValues vocablesTranslationValue = new ContentValues();
        vocablesTranslationValue.put(VocablesTranslationsContract.Schema.COL_VOCABLE_ID, vocable.getId());
        vocablesTranslationValue.put(VocablesTranslationsContract.Schema.COL_TRANSLATION_ID, translation.getId());

        new AsyncInsertCommand(contentResolver, VocablesTranslationsContract.CONTENT_URI, vocablesTranslationValue).execute();

    }

    /***********************************************************************************************
     * Sync methods - Vocables
     **********************************************************************************************/

    public Word getVocableById(long id) {
        final String str_id = String.valueOf(id);

        final String[] projection = {VocablesContract.Schema.COL_VOCABLE};
        final String selection = VocablesContract.Schema.COL_ID + " = ?";
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
        if (Word.isValid(vocable) && vocable.getId() < 0) {
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

        final String selection = VocablesContract.Schema.COL_ID + " = ?";
        final String[] selectionArgs = {str_id};

        final Uri uri = Uri.withAppendedPath(VocablesContract.CONTENT_URI, str_id).buildUpon().build();

        int updatedRecords = contentResolver.update(uri, vocableToContentValues(newVocable), selection, selectionArgs);

        return updatedRecords > 0;
    }

    public boolean removeVocable(long vocableID) {
        int recordDeleted = 0;
        if (vocableID > 0) {
            final String str_id = String.valueOf(vocableID);

            final String selection = VocablesContract.Schema.COL_ID + " = ?";
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

    /***********************************************************************************************
     * Helper methods
     **********************************************************************************************/

    public static Word cursorToVocable(Cursor cursor) {
        final Word vocable = new Word(cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COL_VOCABLE)));
        vocable.setId(cursor.getLong(cursor.getColumnIndex(VocablesContract.Schema.COL_ID)));
        return vocable;
    }

    public static List<Word> cursorToListOfVocables(Cursor cursor) {
        List<Word> vocablesWithSameName = new ArrayList<>();
        if (cursor == null) {
            return vocablesWithSameName;
        }
        while (cursor.moveToNext()) {
            Word vocable = new Word("");
            vocable.setId(cursor.getLong(cursor.getColumnIndex(VocablesContract.Schema.COL_ID)));
            vocable.setName(cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COL_VOCABLE)));
            vocablesWithSameName.add(vocable);
        }
        return vocablesWithSameName;
    }

    public static Word cursorToTranslation(Cursor cursor) {
        final Word translation = new Word(cursor.getString(cursor.getColumnIndex(TranslationsContract.Schema.COL_TRANSLATION)));
        translation.setId(cursor.getLong(cursor.getColumnIndex(TranslationsContract.Schema.COL_ID)));
        return translation;
    }

    public static List<Word> cursorToListOfTranslations(Cursor cursor) {
        List<Word> translationsWithSameName = new ArrayList<>();
        if (cursor == null) {
            return translationsWithSameName;
        }
        while (cursor.moveToNext()) {
            Word translation = new Word("");
            translation.setId(cursor.getLong(cursor.getColumnIndex(TranslationsContract.Schema.COL_ID)));
            translation.setName(cursor.getString(cursor.getColumnIndex(TranslationsContract.Schema.COL_TRANSLATION)));
            translationsWithSameName.add(translation);
        }
        return translationsWithSameName;
    }

    ContentValues vocableToContentValues(Word vocable) {
        final ContentValues values = new ContentValues();
        values.put(VocablesContract.Schema.COL_VOCABLE, vocable.getName());
        return values;
    }

    ContentValues translationToContentValues(Word translation) {
        final ContentValues values = new ContentValues();
        values.put(TranslationsContract.Schema.COL_TRANSLATION, translation.getName());
        return values;
    }
}


