package com.example.app;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;

public class MainActivity extends AppCompatActivity implements BiometricAuthListener {

    Button buttonBiometricsLogin;
    TextView tvNotSupported;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonBiometricsLogin = findViewById(R.id.buttonBiometricsLogin);
        tvNotSupported = findViewById(R.id.tvNotSupported);


        //button visibility
        showBiometricLoginOption();

        buttonBiometricsLogin.setOnClickListener(v -> CommonUtils.showBiometricPrompt(
                "Biometric Authentication",
                "Enter biometric credentials to proceed.",
                "Input your Fingerprint or FaceID to ensure it's you!",
                MainActivity.this,
                MainActivity.this,
                null,
                false));

    }


    public void onBiometricAuthenticationSuccess(BiometricPrompt.AuthenticationResult result) {
        Toast.makeText(this, "Biometric success", Toast.LENGTH_SHORT).show();
    }

    public void onBiometricAuthenticationError(int errorCode, String errorMessage) {
        Toast.makeText(this, "Biometric login. Error: " + errorMessage, Toast.LENGTH_SHORT).show();
    }


    @SuppressLint("SetTextI18n")
    private void showBiometricLoginOption() {

        if (CommonUtils.isBiometricReady(this)) {
            buttonBiometricsLogin.setVisibility(View.VISIBLE);
        } else {
            buttonBiometricsLogin.setVisibility(View.GONE);
            tvNotSupported.setText(CommonUtils.isBiometricSupported(this));
        }

    }


}