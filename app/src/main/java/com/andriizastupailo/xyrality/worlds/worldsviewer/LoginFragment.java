package com.andriizastupailo.xyrality.worlds.worldsviewer;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

/**
 * Handle input parameters
 */

public class LoginFragment extends Fragment implements View.OnClickListener {

    private static final int MIN_PASSWORD_LENGTH = 4;

    private View mRootView;
    private TextInputLayout mTextInputLayoutEmail;
    private TextInputLayout mTextInputLayoutPassword;
    private TextView mTextViewEmail;
    private TextView mTextViewPassword;
    private Button mSignInButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_login, container, false);
        mTextInputLayoutEmail = (TextInputLayout) mRootView.findViewById(R.id.input_email);
        mTextInputLayoutPassword = (TextInputLayout) mRootView.findViewById(R.id.input_password);
        mTextViewEmail = (TextView) mRootView.findViewById(R.id.email);
        mTextViewPassword = (TextView) mRootView.findViewById(R.id.password);
        mSignInButton = (Button) mRootView.findViewById(R.id.signin_button);
        mSignInButton.setOnClickListener(this);
        return mRootView;
    }

    @Override
    public void onClick(View v) {
        boolean hasError = false;

        if(Patterns.EMAIL_ADDRESS.matcher(mTextViewEmail.getText()).matches()){
            mTextInputLayoutEmail.setErrorEnabled(false);
        } else {
            mTextInputLayoutEmail.setError(getString(R.string.email_error));
            mTextViewEmail.requestFocus();
            hasError = true;
        }

        int passwordLength =
                mTextViewPassword.getText() == null ? 0 : mTextViewPassword.getText().length();
        if(passwordLength >= MIN_PASSWORD_LENGTH){
            mTextInputLayoutPassword.setErrorEnabled(false);
        } else {
            String errorString = String.format(getString(R.string.password_error_format), MIN_PASSWORD_LENGTH);
            mTextInputLayoutPassword.setError(errorString);
            if(!hasError)
                mTextViewPassword.requestFocus();
            hasError = true;
        }

        if(!hasError){
            hideKeyboard(getActivity(), getActivity().getCurrentFocus());
            boolean isNetEnable = isNetworkConnected(getActivity());
            if(!isNetEnable){
                hasError = true;
                Snackbar.make(mRootView, "Missing internet connection", Snackbar.LENGTH_SHORT)
                        .show();
            }
        }

        if(!hasError){
            PreferenceStore.setLogin(getActivity(), mTextViewEmail.getText().toString());
            PreferenceStore.setPassword(getActivity(), mTextViewPassword.getText().toString());
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left)
                    .replace(R.id.fragment_container, new WorldsFragment())
                    .commit();
        }
    }

    private static boolean isNetworkConnected(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNework = cm.getActiveNetworkInfo();
        if(activeNework != null){
            return activeNework.isConnectedOrConnecting();
        }
        return false;
    }

    private static void hideKeyboard(Context context, View view){
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
