package com.matteoveroni.wordsremember.activities.dictionary_management;

import com.matteoveroni.wordsremember.model.Word;

public interface DictionaryModel {
    long saveVocable(Word vocable) throws NullPointerException;
    boolean updateVocable(long vocableID, Word newVocable);
    Word getVocableById(long id);
    boolean removeVocable(long vocableID);
}
