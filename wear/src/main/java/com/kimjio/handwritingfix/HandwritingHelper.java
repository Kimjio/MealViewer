package com.kimjio.handwritingfix;

import android.content.Context;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import java.lang.ref.WeakReference;

/**
 * Prevent text disappear by Google Handwriting Input
 * It is trick; sometimes may not working...
 *
 * @author kimji
 */
public class HandwritingHelper {
    private static final String IME_MODE_HANDWRITING = "handwriting";

    private TextView.OnEditorActionListener actionListener;

    private ActionListener listener;

    private WeakReference<TextView> textViewRef;

    public HandwritingHelper() {
        this(null);
    }

    public HandwritingHelper(TextView.OnEditorActionListener actionListener) {
        this.actionListener = actionListener;
        this.listener = new ActionListener();
    }

    /**
     * Attaches the {@link HandwritingHelper} to the provided TextView, by calling
     * {@link TextView#setOnEditorActionListener(TextView.OnEditorActionListener)}.
     * You can call this method with {@code null} to detach it from the current TextView.
     *
     * @param textView The TextView instance to which you want to add this helper or
     *                 {@code null} if you want to remove HandwritingHelper from the current
     *                 TextView.
     */
    public void attachToTextView(TextView textView) {
        if (textView == null) {
            if (textViewRef.get() != null) {
                textViewRef.get().setOnEditorActionListener(null);
                textViewRef = null;
            }
        }
        textViewRef = new WeakReference<>(textView);
        textViewRef.get().setOnEditorActionListener(listener);
        textViewRef.get().addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewAttachedToWindow(View v) {
                TextView view = (TextView) v;
                view.setOnEditorActionListener(listener);
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                TextView view = (TextView) v;
                view.setOnEditorActionListener(null);
            }
        });
    }

    private class ActionListener implements TextView.OnEditorActionListener {

        @Override
        public boolean onEditorAction(final TextView v, int actionId, KeyEvent event) {
            InputMethodManager manager = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (manager != null && manager.getCurrentInputMethodSubtype().getMode().equals(IME_MODE_HANDWRITING) && actionId != EditorInfo.IME_NULL) {
                CharSequence text = Editable.Factory.getInstance().newEditable(v.getText());

                // Disable TextView to blocking text disappearing.
                v.setEnabled(false);
                // Reset
                v.setText(text);
                // Activate after 150ms delay to ignore the operation of IME
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.setEnabled(true);
                    }
                }, 150);
            }
            if (actionListener != null)
                return actionListener.onEditorAction(v, actionId, event);
            return false;
        }
    }
}
