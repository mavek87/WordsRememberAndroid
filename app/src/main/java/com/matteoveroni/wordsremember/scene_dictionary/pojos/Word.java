package com.matteoveroni.wordsremember.scene_dictionary.pojos;

import com.matteoveroni.myutils.Json;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

@EqualsAndHashCode
public class Word {

    @Getter
    @Setter
    private long id = -1;

    @Getter
    @Setter
    private String name = "";

    public Word() {
    }

    public Word(String name) {
        this.name = name;
    }

    public Word(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public String toJson() {
        return Json.getInstance().toJson(this);
    }

    public static Word fromJson(String json) {
        return Json.getInstance().fromJson(json, Word.class);
    }

    public static boolean isNullOrEmpty(Word word) {
        return word == null || word.getName().trim().isEmpty();
    }

    public static boolean isNotNullNorEmpty(Word word) {
        return !isNullOrEmpty(word);
    }

    public static boolean isPersisted(Word vocable) {
        return Word.isNotNullNorEmpty(vocable) && vocable.getId() > 0;
    }

    public static boolean isNotPersisted(Word vocable) {
        return Word.isNullOrEmpty(vocable) || vocable.getId() <= 0;
    }
}
