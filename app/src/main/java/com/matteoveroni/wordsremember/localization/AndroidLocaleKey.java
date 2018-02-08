package com.matteoveroni.wordsremember.localization;

import com.matteoveroni.wordsremember.R;

import lombok.Getter;

/**
 * @author Matteo Veroni
 */

public enum AndroidLocaleKey {
    MSG_ERROR_EMPTY_USER_PROFILE_NAME(R.string.msg_error_empty_user_profile_name),
    CREATE_VOCABLE(R.string.create_vocable),
    EDIT_VOCABLE(R.string.edit_vocable),
    VOCABLE_SAVED(R.string.vocable_saved),
    VOCABLE_REMOVED(R.string.vocable_removed),
    TRANSLATION_ADDED(R.string.translation_added),
    TRANSLATION_SAVED(R.string.translation_saved),
    USER_REGISTERED(R.string.user_registered),
    SIGN_IN_SUCCESSFUL(R.string.sign_in_successful),
    MSG_ERROR_TRYING_TO_STORE_INVALID_VOCABLE(R.string.msg_error_trying_to_store_invalid_vocable),
    MSG_ERROR_TRYING_TO_STORE_DUPLICATE_VOCABLE_NAME(R.string.msg_error_trying_to_store_duplicate_vocable_name),
    MSG_ERROR_TRYING_TO_STORE_INVALID_TRANSLATION(R.string.msg_error_trying_to_store_invalid_translation),
    MSG_ERROR_TRYING_TO_STORE_DUPLICATE_TRANSLATION_NAME(R.string.msg_error_trying_to_store_duplicate_translation_name),
    NAME(R.string.name),
    EMAIL(R.string.email),
    SCORE(R.string.score),
    POINTS(R.string.points),
    MSG_GAME_COMPLETED(R.string.msg_game_completed),
    MSG_ERROR_NO_ANSWER_GIVEN(R.string.msg_error_no_answer_given),
    MSG_ERROR_INSERT_SOME_VOCABLE(R.string.msg_error_insert_some_vocable),
    EASY(R.string.easy),
    MEDIUM(R.string.medium),
    HARD(R.string.hard),
    MSG_GAME_DIFFICULTY_CHANGED(R.string.msg_game_difficulty_changed),
    LAST_GAME_DATE(R.string.last_game_date),
    CORRECT_ANSWER(R.string.correct_answer),
    CORRECT_ANSWERS(R.string.correct_answers),
    WRONG_ANSWER(R.string.wrong_answer),
    MSG_CORRECT_ANSWER(R.string.msg_correct_answer),
    MSG_WRONG_ANSWER(R.string.msg_wrong_answer);

    @Getter
    private final int keyCode;
    @Getter
    private final String keyName;

    AndroidLocaleKey(int androidKeyCode) {
        this.keyCode = androidKeyCode;
        this.keyName = this.name().toLowerCase();
    }

    @Override
    public String toString() {
        return this.keyName;
    }
}
