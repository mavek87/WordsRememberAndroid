package com.matteoveroni.wordsremember.scene_report;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Matteo Veroni
 */

@AllArgsConstructor
public class QuizViewAdapter {

    @Getter
    @Setter
    private String gameDifficulty;
    @Getter
    @Setter
    private String numberOfQuestions;
    @Getter
    @Setter
    private String numberOfCorrectAnswers;
    @Getter
    @Setter
    private String numberOfWrongAnswers;
    @Getter
    @Setter
    private String correctnessPercentage;
    @Getter
    @Setter
    private String avgResponseTime;
}
