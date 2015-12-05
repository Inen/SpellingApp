package com.apps.inen.spellingapp;

import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by dima on 04.12.15.
 */
public class GameFragment extends Fragment {

    final int BUTTONS_COUNT = 4;

    TextView mWordTextView;
    LinearLayout mCellPanel;
    LinearLayout mButtonPanel;

    Drawable buttonBackground;
    Drawable cellDefaultBackground;
    Drawable cellFocusedBackground;

    Resources resources;
    String word;
    Random random;
    int wordsCount;
    String[] wordsEN;
    String[] wordsRU;
    String[] alphabet;

    int iteration;
    int screenWidth;
    int screenHeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game, container, false);

        mWordTextView = (TextView) view.findViewById(R.id.wordTextView);
        mCellPanel = (LinearLayout) view.findViewById(R.id.cellPanel);
        mButtonPanel = (LinearLayout) view.findViewById(R.id.buttonPanel);

        resources = getResources();

        buttonBackground = ContextCompat.getDrawable(getActivity(), R.drawable.button_background);
        cellDefaultBackground = ContextCompat.getDrawable(getActivity(),
                R.drawable.cell_default_background);
        cellFocusedBackground = ContextCompat.getDrawable(getActivity(),
                R.drawable.cell_focused_background);

        random = new Random();
        wordsEN = resources.getStringArray(R.array.words_en);
        wordsRU = resources.getStringArray(R.array.words_ru);
        alphabet = resources.getStringArray(R.array.alphabet_en);

        wordsCount = resources.getTextArray(R.array.words_en).length;

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        initScreen();

        return view;
    }

    private void initScreen() {
        iteration = 0;
        int num = random.nextInt(wordsCount);
        mWordTextView.setText(wordsRU[num]);
        word = wordsEN[num];
        initCells(word.length());
        initButtons(String.valueOf(word.charAt(iteration)));
    }


    private void resume() {
        iteration++;
        initButtons(String.valueOf(word.charAt(iteration)));
    }

    private void initButtons(final String currentLetter) {
        mButtonPanel.removeAllViews();
        boolean isExist = false;
        for (int i = 0; i < BUTTONS_COUNT; ++i) {
            Button button = new Button(getActivity());

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenHeight/10, screenHeight/10);
            //lp.weight = 1;
            lp.setMargins(10, 10, 10, 10);

            button.setLayoutParams(lp);
            button.setBackground(buttonBackground);

            String letter = alphabet[random.nextInt(alphabet.length)];
            button.setText(letter);
            if (letter.equals(currentLetter))
                isExist = true;

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Button btn = (Button) view;
                    if (btn.getText().equals(currentLetter)) {
                        TextView tv = (TextView) mCellPanel.getChildAt(iteration);
                        tv.setGravity(Gravity.CENTER);
                        tv.setText(currentLetter);
                        if (tv.getHeight() > tv.getWidth())
                            tv.setTextSize(tv.getWidth() / 3);
                        else
                            tv.setTextSize(tv.getHeight() / 3);

                        tv.setBackground(cellDefaultBackground);

                        if (iteration < word.length()-1) {
                            mCellPanel.getChildAt(iteration + 1).setBackground(cellFocusedBackground);
                            resume();
                        } else
                            initScreen();
                    }

                }
            });
            mButtonPanel.addView(button);
        }
        if (!isExist) {
            int num = random.nextInt(mButtonPanel.getChildCount());
            Button button = (Button) mButtonPanel.getChildAt(num);
            button.setText(currentLetter);
        }
    }

    private void initCells(int count) {
        mCellPanel.removeAllViews();
        for (int i = 0; i < count; ++i) {
            TextView textView = new TextView(getActivity());

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(screenHeight/10, screenHeight/10);
            //lp.weight = 1;
            lp.setMargins(10, 10, 10, 10);
            textView.setLayoutParams(lp);

            if (i != iteration)
                textView.setBackground(cellDefaultBackground);
            else textView.setBackground(cellFocusedBackground);
            mCellPanel.addView(textView);
        }
    }

}
