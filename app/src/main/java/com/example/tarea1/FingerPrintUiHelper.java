package com.example.tarea1;

import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import static android.icu.text.UnicodeSet.CASE;


public class FingerPrintUiHelper extends FingerprintManager.AuthenticationCallback {

    private final ImageView ivFinger;
    private final TextView tvError;
    private final Callback callback;
    private CancellationSignal mCancellationSignal;
    private boolean mSelfCancelled;

    private int currentStatus;
    private final int STATUS_INACTIVE =0;
    private final int STATUS_LISTENING =1;
    private final int STATUS_ACCEPTED =2;
    private final int STATUS_ERROR =-1;

    FingerPrintUiHelper(ImageView icon, TextView errorTextView, Callback callback) {
        this.ivFinger = icon;
        this.tvError = errorTextView;
        this.callback = callback;
        setStatusListener(STATUS_INACTIVE);
    }

    public void onResumeListening(){
        if (currentStatus==STATUS_INACTIVE){
            this.startListening(null);
        }

    }

    public void startListening(FingerprintManager.CryptoObject cryptoObject){
        FingerprintManager fingerprintManager= SecurityManager.initSecurityManager(null).getFingerprintManager();
        mCancellationSignal = new CancellationSignal();
        mSelfCancelled = false;
        setStatusListener(STATUS_LISTENING);
        showError("");
        fingerprintManager.authenticate(cryptoObject, mCancellationSignal, 0 /* flags */, this, null);
    }

    public void stopListening() {
        if (mCancellationSignal != null) {
            mSelfCancelled = true;
            mCancellationSignal.cancel();
            mCancellationSignal = null;
        }
    }


    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        if (!mSelfCancelled) {
            setStatusListener(STATUS_ERROR);
            showError(errString.toString());
            ivFinger.postDelayed(new Runnable() {
                @Override
                public void run() {
                    callback.onError();
                }
            }, 3000);
        }else
            setStatusListener(STATUS_INACTIVE);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        setStatusListener(STATUS_ERROR);
        showError(helpString.toString());
    }

    @Override
    public void onAuthenticationFailed() {
        setStatusListener(STATUS_ERROR);
        showError(tvError.getResources().getString(
                R.string.fingerNotRecognized));
    }


    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        setStatusListener(STATUS_ACCEPTED);
        tvError.setText("");
        ivFinger.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onAuthenticated();
            }
        }, 2000);
    }


    private void setStatusListener(int status){
        switch (status){
            case STATUS_INACTIVE:
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivFinger.getDrawable()),
                        ContextCompat.getColor(ivFinger.getContext(), R.color.colorGrey)
                );
                break;
            case STATUS_LISTENING:
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivFinger.getDrawable()),
                        ContextCompat.getColor(ivFinger.getContext(), R.color.colorBlue)
                );
                break;
            case STATUS_ACCEPTED:
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivFinger.getDrawable()),
                        ContextCompat.getColor(ivFinger.getContext(), R.color.colorGreen)
                );
                break;
            case STATUS_ERROR:
                DrawableCompat.setTint(
                        DrawableCompat.wrap(ivFinger.getDrawable()),
                        ContextCompat.getColor(ivFinger.getContext(), R.color.colorRed)
                );
                break;
        }
        currentStatus=status;
    }

    public String showError(String error){
        tvError.setText(error);
        return error;
    }

    public interface Callback {
        void onAuthenticated();
        void onError();
    }

}
