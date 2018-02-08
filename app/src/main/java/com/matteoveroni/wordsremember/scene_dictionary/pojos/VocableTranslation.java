package com.matteoveroni.wordsremember.scene_dictionary.pojos;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

@AllArgsConstructor
@EqualsAndHashCode
public class VocableTranslation {

    @Getter
    private final Word vocable;

    @Getter
    @Setter
    private Word translation;
}
