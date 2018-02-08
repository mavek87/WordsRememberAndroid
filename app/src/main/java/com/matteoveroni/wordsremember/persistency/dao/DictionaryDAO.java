package com.matteoveroni.wordsremember.persistency.dao;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.matteoveroni.myutils.Str;
import com.matteoveroni.wordsremember.persistency.commands.AsyncCountUniqueVocablesWithTranslationCommand;
import com.matteoveroni.wordsremember.persistency.commands.AsyncDeleteCommand;
import com.matteoveroni.wordsremember.persistency.commands.AsyncInsertCommand;
import com.matteoveroni.wordsremember.persistency.commands.AsyncSearchDistinctVocableWithTranslationByOffsetCommand;
import com.matteoveroni.wordsremember.persistency.commands.AsyncSearchTranslationsByNameCommand;
import com.matteoveroni.wordsremember.persistency.commands.AsyncSearchVocableTranslationsCommand;
import com.matteoveroni.wordsremember.persistency.commands.AsyncSearchVocablesByIdCommand;
import com.matteoveroni.wordsremember.persistency.commands.AsyncSearchVocablesByNameCommand;
import com.matteoveroni.wordsremember.persistency.commands.AsyncUpdateCommand;
import com.matteoveroni.wordsremember.persistency.contracts.TranslationsContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesContract;
import com.matteoveroni.wordsremember.persistency.contracts.VocablesTranslationsContract;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.VocableTranslation;
import com.matteoveroni.wordsremember.scene_dictionary.pojos.Word;

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
        if (Word.isNullOrEmpty(vocable) || vocable.getId() > 0)
            throw new IllegalArgumentException("Invalid vocable");

        ContentValues values = new ContentValues();
        values.put(VocablesContract.Schema.COL_VOCABLE, vocable.getName());

        new AsyncInsertCommand(
                contentResolver,
                VocablesContract.CONTENT_URI,
                values
        ).execute();
    }

    public void asyncSearchVocableById(long id) {
        if (id < 1) throw new IllegalArgumentException("Negative id");

        new AsyncSearchVocablesByIdCommand(contentResolver, id, "").execute();
    }

    public void asyncSearchVocableByName(String vocableName) throws IllegalArgumentException {
        if (Str.isNullOrEmpty(vocableName)) throw new IllegalArgumentException("Invalid name");

        new AsyncSearchVocablesByNameCommand(contentResolver, vocableName, "").execute();
    }

    public void asyncUpdateVocable(long id, Word updatedVocable) {
        if (Word.isNullOrEmpty(updatedVocable) || id < 1)
            throw new IllegalArgumentException("Invalid vocable or id");

        String str_id = String.valueOf(id);
        String selection = VocablesContract.Schema.COL_ID + "=?";
        String[] selectionArgs = {str_id};

        new AsyncUpdateCommand(
                contentResolver,
                VocablesContract.CONTENT_URI,
                vocableToContentValues(updatedVocable),
                selection,
                selectionArgs
        ).execute();
    }

    public void asyncDeleteVocable(long id) {
        if (id < 1) throw new IllegalArgumentException("Negative id");

        asyncDeleteVocableTranslationsByVocableId(id);

        new AsyncDeleteCommand(
                contentResolver,
                VocablesContract.CONTENT_URI,
                VocablesContract.Schema.COL_ID + "=?",
                new String[]{String.valueOf(id)}
        ).execute();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Async methods - TRANSLATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void asyncSaveTranslation(Word translation) {
        if (Word.isNullOrEmpty(translation) || translation.getId() > 0)
            throw new IllegalArgumentException("Invalid translation");

        new AsyncInsertCommand(
                contentResolver,
                TranslationsContract.CONTENT_URI,
                translationToContentValues(translation)
        ).execute();
    }

    public void asyncDeleteTranslation(long id) {
        if (id < 1) throw new IllegalArgumentException("Negative id");

        asyncDeleteVocableTranslationsByTranslationId(id);

        new AsyncDeleteCommand(
                contentResolver,
                TranslationsContract.CONTENT_URI,
                TranslationsContract.Schema.COL_ID + "=?",
                new String[]{String.valueOf(id)}
        ).execute();
    }

    public void asyncSearchTranslationByName(String translationName) throws IllegalArgumentException {
        if (Str.isNullOrEmpty(translationName)) throw new IllegalArgumentException("Invalid name");

        new AsyncSearchTranslationsByNameCommand(contentResolver, translationName, "").execute();
    }

    public void asyncSearchVocableTranslations(Word vocable) {
        if (vocable == null || vocable.getId() < 1)
            throw new IllegalArgumentException("Invalid vocable or vocable id minor than one");

        new AsyncSearchVocableTranslationsCommand(contentResolver, vocable, null).execute();
    }

    public void asyncSearchDistinctVocableWithTranslationByOffset(int offset) throws IllegalArgumentException {
        if (offset < 0) throw new IllegalArgumentException("Negative offset");

        new AsyncSearchDistinctVocableWithTranslationByOffsetCommand(contentResolver, String.valueOf(offset)).execute();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Async methods - VOCABLES_TRANSLATIONS
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public void countDistinctVocablesWithTranslations() {
        new AsyncCountUniqueVocablesWithTranslationCommand(contentResolver).execute();
    }

    public void asyncDeleteVocableTranslationsByVocableId(long id) {
        if (id < 1) throw new IllegalArgumentException("Negative id");

        new AsyncDeleteCommand(
                contentResolver,
                VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI,
                VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID + "=?",
                new String[]{String.valueOf(id)}
        ).execute();
    }

    public void asyncDeleteVocableTranslationsByTranslationId(long id) {
        if (id < 1) throw new IllegalArgumentException("Negative id");

        new AsyncDeleteCommand(
                contentResolver,
                VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI,
                VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID + "=?",
                new String[]{String.valueOf(id)}
        ).execute();
    }

    public void asyncDeleteVocableTranslationsByVocableAndTranslationIds(long vocableId, long translationId) {
        if (vocableId < 1 || translationId < 1) throw new IllegalArgumentException("Negative id");

        new AsyncDeleteCommand(
                contentResolver,
                VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI,
                VocablesTranslationsContract.Schema.TABLE_DOT_COL_VOCABLE_ID + "=? AND " + VocablesTranslationsContract.Schema.TABLE_DOT_COL_TRANSLATION_ID + "=?",
                new String[]{String.valueOf(vocableId), String.valueOf(translationId)}
        ).execute();
    }

    public void asyncSaveVocableTranslation(VocableTranslation vocableTranslation) {
        Word translation = vocableTranslation.getTranslation();
        Word vocable = vocableTranslation.getVocable();

        if (Word.isNullOrEmpty(vocable) || Word.isNotPersisted(translation))
            throw new IllegalArgumentException("AsyncSaveVocableTranslation invalid argument");

        ContentValues vocablesTranslationValue = new ContentValues();
        vocablesTranslationValue.put(VocablesTranslationsContract.Schema.COL_VOCABLE_ID, vocable.getId());
        vocablesTranslationValue.put(VocablesTranslationsContract.Schema.COL_TRANSLATION_ID, translation.getId());

        new AsyncInsertCommand(contentResolver, VocablesTranslationsContract.VOCABLES_TRANSLATIONS_CONTENT_URI, vocablesTranslationValue).execute();

    }

    /***********************************************************************************************
     * Sync methods - Vocables
     **********************************************************************************************/

    public long saveVocable(Word vocable) {
        long id = vocable.getId();

        if (Word.isNullOrEmpty(vocable) || id < 0) return -1;

        Uri uri = contentResolver.insert(
                VocablesContract.CONTENT_URI,
                vocableToContentValues(vocable)
        );

        if (uri != null) {
            String createdRowId = uri.getLastPathSegment();

            if (!createdRowId.isEmpty()) {
                id = Long.valueOf(createdRowId);
            }
        }

        return id;
    }

    /***********************************************************************************************
     * Helper methods
     **********************************************************************************************/

    public static Word cursorToVocable(Cursor cursor) {
        Word vocable = new Word(cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COL_VOCABLE)));
        vocable.setId(cursor.getLong(cursor.getColumnIndex(VocablesContract.Schema.COL_ID)));
        return vocable;
    }

    public static List<Word> cursorToListOfVocables(Cursor cursor) {
        List<Word> vocables = new ArrayList<>();
        if (cursor == null) {
            return vocables;
        }
        while (cursor.moveToNext()) {
            Word vocable = new Word("");
            vocable.setId(cursor.getLong(cursor.getColumnIndex(VocablesContract.Schema.COL_ID)));
            vocable.setName(cursor.getString(cursor.getColumnIndex(VocablesContract.Schema.COL_VOCABLE)));
            vocables.add(vocable);
        }
        return vocables;
    }

    public static Word cursorToTranslation(Cursor cursor) {
        Word translation = new Word(cursor.getString(cursor.getColumnIndex(TranslationsContract.Schema.COL_TRANSLATION)));
        translation.setId(cursor.getLong(cursor.getColumnIndex(TranslationsContract.Schema.COL_ID)));

        return translation;
    }

    public static List<Word> cursorToListOfTranslations(Cursor cursor) {
        List<Word> translations = new ArrayList<>();
        if (cursor == null) {
            return translations;
        }
        while (cursor.moveToNext()) {
            Word translation = new Word("");
            translation.setId(cursor.getLong(cursor.getColumnIndex(TranslationsContract.Schema.COL_ID)));
            translation.setName(cursor.getString(cursor.getColumnIndex(TranslationsContract.Schema.COL_TRANSLATION)));
            translations.add(translation);
        }
        return translations;
    }

    ContentValues vocableToContentValues(Word vocable) {
        ContentValues values = new ContentValues();

        values.put(VocablesContract.Schema.COL_VOCABLE, vocable.getName());

        return values;
    }

    ContentValues translationToContentValues(Word translation) {
        ContentValues values = new ContentValues();

        values.put(TranslationsContract.Schema.COL_TRANSLATION, translation.getName());

        return values;
    }
}


