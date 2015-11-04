package com.andela.helpmebuy.views;


import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.andela.helpmebuy.R;

public class TogglePasswordVisibilityButton extends ImageButton {

    private int passwordVisibleImageResource;

    private int passwordInvisibleImageResource;

    private int passwordEditTextId;

    public TogglePasswordVisibilityButton(Context context, AttributeSet set) {
        super(context, set);

        TypedArray attrs = context.getTheme().obtainStyledAttributes(set, R.styleable.TogglePasswordVisibilityButton, 0, 0);

        try {
            passwordVisibleImageResource = attrs.getResourceId(R.styleable.TogglePasswordVisibilityButton_passwordVisibleImageResource, 0);

            passwordInvisibleImageResource = attrs.getResourceId(R.styleable.TogglePasswordVisibilityButton_passwordInvisibleImageResource, 0);

            passwordEditTextId = attrs.getResourceId(R.styleable.TogglePasswordVisibilityButton_passwordEditText, 0);

            setImageResource(passwordInvisibleImageResource);

        } finally {
            attrs.recycle();
        }

        attachOnToggleEventHandler();
    }

    public void attachOnToggleEventHandler() {
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = (Activity) getContext();

                if (activity != null) {
                    EditText passwordEditText = (EditText) activity.findViewById(passwordEditTextId);

                    if (passwordEditText.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT);
                        setImageResource(passwordVisibleImageResource);

                    } else {
                        passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        setImageResource(passwordInvisibleImageResource);
                    }

                    passwordEditText.setSelection(passwordEditText.getText().length());
                }
            }
        });

    }
}
