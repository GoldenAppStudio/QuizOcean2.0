package studio.goldenapp.quiz_ocean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.Objects;

public class PrefManager {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;

    // shared pref mode
    int PRIVATE_MODE = 0;

    // Shared preferences file name
    private static final String PREF_NAME = "studio.goldenapp.quiz_ocean";

    private static final String IS_DARK_MODE_ON = "IsDarkModeOn";
    private static final String IS_QUESTION_SOUND_ON = "IsQuestionSoundOn";
    private static final String IS_ANSWER_SOUND_ON = "IsAnswerSoundOn";
    private static final String IS_QUIZ_SOUND_ON = "IsQuizSoundOn";

    @SuppressLint("CommitPrefEdits")
    public PrefManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setDarkMode(boolean isDarkModeOn) {
        editor.putBoolean(IS_DARK_MODE_ON, isDarkModeOn);
        editor.commit();
    }

    public boolean isDarkModeOn() {
        return pref.getBoolean(IS_DARK_MODE_ON, false);
    }

    public void setQuestionSound(boolean isQuestionSoundOn) {
        editor.putBoolean(IS_QUESTION_SOUND_ON, isQuestionSoundOn);
        editor.commit();
    }

    public boolean isQuestionSoundOn() {
        return pref.getBoolean(IS_QUESTION_SOUND_ON, false);
    }

    public void setAnswerSound(boolean isAnswerSoundOn) {
        editor.putBoolean(IS_ANSWER_SOUND_ON, isAnswerSoundOn);
        editor.commit();
    }

    public boolean isAnswerSoundOn() {
        return pref.getBoolean(IS_ANSWER_SOUND_ON, false);
    }

    public void setQuizSound(boolean isQuizSoundOn) {
        editor.putBoolean(IS_QUIZ_SOUND_ON, isQuizSoundOn);
        editor.commit();
    }

    public boolean isQuizSoundOn() {
        return pref.getBoolean(IS_QUIZ_SOUND_ON, false);
    }
}
