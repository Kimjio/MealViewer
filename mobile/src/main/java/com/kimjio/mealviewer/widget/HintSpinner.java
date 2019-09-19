package com.kimjio.mealviewer.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;

import com.kimjio.mealviewer.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HintSpinner extends AppCompatSpinner {

    private CharSequence hint;
    private List<CharSequence> entryList;

    public HintSpinner(Context context) {
        this(context, null);
    }

    public HintSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.spinnerStyle);
    }

    public HintSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, -1);
    }

    public HintSpinner(Context context, int mode) {
        this(context, null, R.attr.spinnerStyle, mode);
    }

    public HintSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode) {
        this(context, attrs, defStyleAttr, mode, null);
    }

    public HintSpinner(Context context, AttributeSet attrs, int defStyleAttr, int mode, Resources.Theme popupTheme) {
        super(context, attrs, defStyleAttr, mode, popupTheme);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.HintSpinner, defStyleAttr, 0);

        hint = typedArray.getText(R.styleable.HintSpinner_android_hint);
        entryList = new ArrayList<>(Arrays.asList(typedArray.getTextArray(R.styleable.HintSpinner_android_entries)));

        if (hint != null) {
            entryList.add(hint);
            final ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
                    context, android.R.layout.simple_spinner_item, entryList) {

                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getView(position, convertView, parent);
                    if (position == getCount()) {
                        final TextView textView = view.findViewById(android.R.id.text1);
                        textView.setHint(textView.getText());
                        textView.setText(null);
                    }
                    return view;
                }

                @Override
                public int getCount() {
                    return super.getCount() - 1;
                }
            };
            adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
            setAdapter(adapter);
            setSelection(getCount());
        }

        typedArray.recycle();
    }

    public CharSequence getHint() {
        return hint;
    }

    public void setHint(CharSequence hint) {
        if (hint == null && this.hint != null) {
            entryList.remove(this.hint);
            if (super.getSelectedItemPosition() == getAdapter().getCount() + 1)
                setSelection(INVALID_POSITION);
        } else if (this.hint != null)
            entryList.set(entryList.indexOf(this.hint), hint);
        else {
            entryList.add(hint);
            setSelection(getCount());
        }
        this.hint = hint;
    }

    @Override
    public int getSelectedItemPosition() {
        int selectedPosition = super.getSelectedItemPosition();
        if (hint != null && selectedPosition == getAdapter().getCount())
            return INVALID_POSITION;
        return selectedPosition;
    }
}
