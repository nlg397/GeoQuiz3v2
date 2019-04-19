package com.book.geoquiz3v2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class QuizActivity extends AppCompatActivity {

    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "index";

    private static final String KEY_TRUE_FALSE_BUTTONS_ENABLED = "true_and_false_buttons_are_enabled";
    private static final String KEY_NEXT_BUTTON_ENABLED = "next_button_is_enabled";
    private static final String KEY_CORRECT_ANSWERS_COUNT = "correct_answers_count";
    private static final String KEY_INCORRECT_ANSWERS_COUNT = "incorrect_answers_count";

    private Button mTrueButton;
    private Button mFalseButton;
    private Button mNextButton;
    private TextView mQuestionTextView;

    private Question[] mQuestionBank = new Question[] {
            new Question(R.string.question_london, true),
            new Question(R.string.question_ocean, true),
            new Question(R.string.question_africa, true),
            new Question(R.string.question_spb, false),
            new Question(R.string.question_india, false),
            new Question(R.string.question_moscow, false),
    };

    private int mCurrentIndex = 0;

    private boolean mTrueFalseButtonsIsEnabled = true;
    private boolean mNextButtonIsEnabled = false;
    private int mCorrectAnswersCount = 0;
    private int mIncorrectAnswersCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate(Bungle) called");

        setContentView(R.layout.activity_quiz);

        if (savedInstanceState != null) {
            mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);

            mTrueFalseButtonsIsEnabled = savedInstanceState.getBoolean(KEY_TRUE_FALSE_BUTTONS_ENABLED, true);
            mNextButtonIsEnabled = savedInstanceState.getBoolean(KEY_NEXT_BUTTON_ENABLED, false);

            mCorrectAnswersCount = savedInstanceState.getInt(KEY_CORRECT_ANSWERS_COUNT, 0);
            mIncorrectAnswersCount = savedInstanceState.getInt(KEY_INCORRECT_ANSWERS_COUNT, 0);
        }

        mQuestionTextView = (TextView) findViewById(R.id.question_text_view);

        mTrueButton = (Button) findViewById(R.id.true_button);
        mTrueButton.setEnabled(mTrueFalseButtonsIsEnabled);
        mTrueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);

                setEnabledOfButtons(false);

                checkEnd();
            }
        });

        mFalseButton = (Button) findViewById(R.id.false_button);
        mFalseButton.setEnabled(mTrueFalseButtonsIsEnabled);
        mFalseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);

                setEnabledOfButtons(false);

                checkEnd();
            }
        });

        mNextButton = (Button) findViewById(R.id.next_button);
        mNextButton.setEnabled(mNextButtonIsEnabled);
        mNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
                updateQuestion();

                setEnabledOfButtons(true);
            }
        });

        updateQuestion();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart() called");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume() called");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause() called");
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.i(TAG, "onSaveInstanceState");
        savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);

        savedInstanceState.putBoolean(KEY_TRUE_FALSE_BUTTONS_ENABLED, mTrueFalseButtonsIsEnabled);
        savedInstanceState.putBoolean(KEY_NEXT_BUTTON_ENABLED, mNextButtonIsEnabled);

        savedInstanceState.putInt(KEY_CORRECT_ANSWERS_COUNT, mCorrectAnswersCount);
        savedInstanceState.putInt(KEY_INCORRECT_ANSWERS_COUNT, mIncorrectAnswersCount);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop() called");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy() called");
    }

    private void updateQuestion() {
        int question = mQuestionBank[mCurrentIndex].getTextResId();
        mQuestionTextView.setText(question);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = mQuestionBank[mCurrentIndex].isAnswerTrue();

        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            messageResId = R.string.correct_toast;
            mCorrectAnswersCount ++;
        } else {
            messageResId = R.string.incorrect_toast;
            mIncorrectAnswersCount ++;
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT)
                .show();
    }

    private void setEnabledOfButtons(boolean nextButtonIsPressed) {
        if (nextButtonIsPressed) {
            mTrueFalseButtonsIsEnabled = true;
            mNextButtonIsEnabled = false;
        } else {
            mTrueFalseButtonsIsEnabled = false;
            mNextButtonIsEnabled = true;
        }
        mTrueButton.setEnabled(mTrueFalseButtonsIsEnabled);
        mFalseButton.setEnabled(mTrueFalseButtonsIsEnabled);
        mNextButton.setEnabled(mNextButtonIsEnabled);
    }

    private void checkEnd() {
        if (mCurrentIndex == mQuestionBank.length - 1) {
            double percent = (double)mCorrectAnswersCount /
                    (mCorrectAnswersCount + mIncorrectAnswersCount) * 100;
            String message = String.format(getResources().getString(R.string.end_toast),
                    mCorrectAnswersCount, mIncorrectAnswersCount, percent);
            Toast toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.TOP, 0, 0);
            toast.show();

            mCorrectAnswersCount = 0;
            mIncorrectAnswersCount = 0;
        }
    }

}














