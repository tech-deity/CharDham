package in.chardhamtour.chardhamyatra.controller.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class InputValidation {
    private Context context;

    /*
     * constructor
     *
     * @param context
     */
    public InputValidation(Context context) {
        this.context = context;
    }

    /*
     * method to check InputEditText filled .
     *
     * @param textInputEditText
     * @param textInputLayout
     * @param message
     * @return
     */
    public boolean isInputEditTextFilled(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputLayout.getEditText().getText().toString().trim();
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
            hideKeyboardFrom(textInputEditText);
        }

        return true;
    }

    public boolean isInputEditTextFilledNumber(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputEditText.getText().toString().trim();
//        int number = Integer.parseInt(value);
        if (value.isEmpty()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        }
        else if (!value.matches("[0-9]+")) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
        }
        else{
            textInputLayout.setErrorEnabled(false);
            hideKeyboardFrom(textInputEditText);
        }

        return true;
    }

    public boolean isETEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }


    public boolean isValidPhoneNumber(TextInputEditText textInputEditTextMobile, TextInputLayout textInputLayout, String message) {
        if (textInputEditTextMobile == null) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditTextMobile);
        } else if (textInputEditTextMobile.length() < 10 || textInputEditTextMobile.length() > 10) {
            hideKeyboardFrom(textInputEditTextMobile);
            textInputLayout.setError(message);
            return false;
        }
        else{
            textInputLayout.setErrorEnabled(false);
            hideKeyboardFrom(textInputEditTextMobile);
//                android.util.Patterns.PHONE.matcher((CharSequence) textInputEditTextMobile).matches();


        }return true;
    }



    public boolean isInputEditTextEmail(TextInputEditText textInputEditText, TextInputLayout textInputLayout, String message) {
        String value = textInputLayout.getEditText().getText().toString().trim();
        if (value.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(value).matches()) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
            hideKeyboardFrom(textInputEditText);
        }
        return true;
    }

    public boolean isInputEditTextMatches(TextInputEditText textInputEditText1, TextInputEditText textInputEditText2, TextInputLayout textInputLayout, String message) {
        String value1 = textInputEditText1.getText().toString().trim();
        String value2 = textInputEditText2.getText().toString().trim();
        if (!value1.contentEquals(value2)) {
            textInputLayout.setError(message);
            hideKeyboardFrom(textInputEditText2);
            return false;
        } else {
            textInputLayout.setErrorEnabled(false);
            hideKeyboardFrom(textInputEditText2);
        }
        return true;
    }



    /*
     * method to Hide keyboard
     *
     * @param view
     */
    private void hideKeyboardFrom(View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    public  boolean isEmail(String email){
       return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public  boolean isPhoneNumber(String phone){
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }
}
