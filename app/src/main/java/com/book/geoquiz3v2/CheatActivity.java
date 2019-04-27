package com.book.geoquiz3v2;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class CheatActivity extends AppCompatActivity {

    private static final String EXTRA_ANSWER_IS_TRUE =
            "com.book.geoquiz3v2.answer_is_true";
    private static final String EXTRA_ANSWER_SHOWN =
            "com.book.geoquiz3v2.answer_shown";

    private static final String KEY_ANSWER_SHOWN = "answer_was_shown";

    private boolean mAnswerIsTrue;

    private TextView mAnswerTextView;
    private Button mShowAnswerButton;
    private TextView mApiLevelTextView;

    private boolean mAnswerWasShown = false;

    public static Intent newIntent (Context packageContext, boolean answerIsTrue) {
        Intent intent = new Intent(packageContext, CheatActivity.class);
        intent.putExtra(EXTRA_ANSWER_IS_TRUE, answerIsTrue);
        return intent;
    }

    public static boolean wasAnswerShown(Intent result) {
        return result.getBooleanExtra(EXTRA_ANSWER_SHOWN, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cheat);

        if (savedInstanceState != null) {
            mAnswerWasShown = savedInstanceState.getBoolean(KEY_ANSWER_SHOWN, false);
        }

        mAnswerIsTrue = getIntent().getBooleanExtra(EXTRA_ANSWER_IS_TRUE, false);

        mAnswerTextView = findViewById(R.id.answer_text_view);
        mShowAnswerButton = findViewById(R.id.show_answer_button);

        if (mAnswerWasShown) {
            showAnswer();
            // Ломаеца, logcat -> fresh issue on issuetracker.google.com
            //hideButton(mShowAnswerButton);
            mShowAnswerButton.setVisibility(View.INVISIBLE);

        } else {
            mShowAnswerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAnswer();
                    hideButton(mShowAnswerButton);
                }
            });
        }


        mApiLevelTextView = findViewById(R.id.api_level_text_view);
        int apiLevel = Build.VERSION.SDK_INT;
        String apiLevelText = String.format(getResources().getString(R.string.api_level_text), apiLevel);
        mApiLevelTextView.setText(apiLevelText);

    }

    private void setAnswerShownResult(boolean isAnswerShown) {
        Intent data = new Intent();
        data.putExtra(EXTRA_ANSWER_SHOWN, isAnswerShown);
        setResult(RESULT_OK, data);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean(KEY_ANSWER_SHOWN, mAnswerWasShown);
    }

    private void showAnswer() {
        if (mAnswerIsTrue) {
            mAnswerTextView.setText(R.string.true_button);
        } else {
            mAnswerTextView.setText(R.string.false_button);
        }
        mAnswerWasShown = true;
        setAnswerShownResult(mAnswerWasShown);
    }

    private void hideButton(final Button button) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = button.getWidth() / 2;
            int cy = button.getHeight() / 2;
            float radius = button.getWidth();
            Animator anim = ViewAnimationUtils
                    .createCircularReveal(button, cx, cy, radius, 0);
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    button.setVisibility(View.INVISIBLE);
                }
            });
            anim.start();
        } else {
            button.setVisibility(View.INVISIBLE);
        }
    }


}
