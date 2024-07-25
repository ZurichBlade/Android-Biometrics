package com.example.app;

import androidx.biometric.BiometricPrompt;


/**
 * Common interface to listen to Biometric Authentication callbacks
 */
interface BiometricAuthListener {

    void onBiometricAuthenticationSuccess(BiometricPrompt.AuthenticationResult result);

    void onBiometricAuthenticationError(int errorCode, String errorMessage);


}
