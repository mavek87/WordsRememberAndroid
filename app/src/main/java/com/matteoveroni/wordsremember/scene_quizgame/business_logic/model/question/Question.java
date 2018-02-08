package com.matteoveroni.wordsremember.scene_quizgame.business_logic.model.question;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Matteo Veroni
 */

@AllArgsConstructor
public class Question {

    @Getter
    private final String questionMsg;
    @Getter
    private final Set<String> trueAnswers;

    public void addTrueAnswer(String answer) {
        trueAnswers.add(answer);
    }
}
