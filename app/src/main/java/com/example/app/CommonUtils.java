package com.example.app;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class CommonUtils {


    /**
     * Checks if the device has Biometric support
     */

    public static String isBiometricSupported(Activity context) {

        String result = "Opps!! Something went Wrong";

        BiometricManager biometricManager = BiometricManager.from(context);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | BIOMETRIC_WEAK)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d("MY_APP_TAG", "App can authenticate using biometrics.");
                result = "App can authenticate using biometrics.";
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e("MY_APP_TAG", "No biometric features available on this device.");
                result = "No biometric features available on this device.";
                break;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e("MY_APP_TAG", "Biometric features are currently unavailable.");
                result = "Biometric features are currently unavailable.";
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.e("MY_APP_TAG", "No biometric credential are enrolled.");
                result = "No biometric credential are enrolled.";

                // Prompts the user to create credentials that your app accepts.
              /*  final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED, BIOMETRIC_STRONG | DEVICE_CREDENTIAL);
                context.startActivityForResult(enrollIntent, 101);*/
                break;
        }

        return result;
    }


    /**
     * Checks if the device has Biometric support
     */
    private static int hasBiometricCapability(Context context) {
        BiometricManager biometricManager = BiometricManager.from(context);
        return biometricManager.canAuthenticate(BIOMETRIC_WEAK | BIOMETRIC_STRONG);
    }

    /**
     * Checks if Biometric Authentication (example: Fingerprint) is set in the device
     */
    public static boolean isBiometricReady(Context context) {
        return hasBiometricCapability(context) == BiometricManager.BIOMETRIC_SUCCESS;
    }


    /**
     * Prepares PromptInfo dialog with provided configuration
     *
     * @return
     */
    private static BiometricPrompt.PromptInfo setBiometricPromptInfo(String title, String subtitle, String description, Boolean allowDeviceCredential) {

        BiometricPrompt.PromptInfo.Builder builder = new BiometricPrompt.PromptInfo.Builder().setTitle(title).setSubtitle(subtitle).setDescription(description).setNegativeButtonText("Cancel");

//                // Use Device Credentials if allowed, otherwise show Cancel Button
//                builder.apply {
//            if (allowDeviceCredential) setDeviceCredentialAllowed(true)
//            else setNegativeButtonText("Cancel")
//        }


        return builder.build();

    }


    /**
     * Initializes BiometricPrompt with the caller and callback handlers
     */

    private static BiometricPrompt initBiometricPrompt(AppCompatActivity activity, BiometricAuthListener listener) {

        // Attach calling Activity
        Executor executor = ContextCompat.getMainExecutor(activity);

        // Attach callback handlers
        BiometricPrompt.AuthenticationCallback callback = new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                listener.onBiometricAuthenticationError(errorCode, errString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                listener.onBiometricAuthenticationSuccess(result);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Log.w(this.getClass().getName(), "Authentication failed for an unknown reason");
            }
        };


        return new BiometricPrompt(activity, executor, callback);
    }


    /**
     * Displays a BiometricPrompt with provided configurations
     */

    public static void showBiometricPrompt(String title, String subtitle, String description, AppCompatActivity activity, BiometricAuthListener listener, BiometricPrompt.CryptoObject cryptoObject, Boolean allowDeviceCredential) {

        // Prepare BiometricPrompt Dialog
        BiometricPrompt.PromptInfo promptInfo = setBiometricPromptInfo(title, subtitle, description, allowDeviceCredential);

        // Attach with caller and callback handler
        BiometricPrompt biometricPrompt = initBiometricPrompt(activity, listener);

        // Authenticate with a CryptoObject if provided, otherwise default authentication
        if (cryptoObject == null) {
            biometricPrompt.authenticate(promptInfo);
        } else {
            biometricPrompt.authenticate(promptInfo, cryptoObject);
        }

    }


    /**
     * Navigates to Device's Settings screen Biometric Setup
     */
   /* fun lunchBiometricSettings(context: Context) {
        ActivityCompat.startActivity(
                context,
                Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS),
                null
        )
    }*/

}
