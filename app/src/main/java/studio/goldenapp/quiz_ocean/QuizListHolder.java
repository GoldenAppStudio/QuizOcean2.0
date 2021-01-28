package studio.goldenapp.quiz_ocean;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class QuizListHolder extends AppCompatActivity {

    public static String QUIZ_GRID_ID;
    public static String QUIZ_GRID_NAME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_list_holder);

        QUIZ_GRID_ID = getIntent().getStringExtra("QUIZ_GRID_ID");
        QUIZ_GRID_NAME = getIntent().getStringExtra("QUIZ_GRID_NAME");
    }
}