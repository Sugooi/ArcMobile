package www.wielabs.com.arc.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.SparseArray;
import android.view.View;
import android.widget.EditText;

import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.PhoneNumber;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.accountkit.ui.SkinManager;
import com.facebook.accountkit.ui.UIManager;

import java.util.UUID;

import www.wielabs.com.arc.R;


public class AccountKitLoginActivity extends AppCompatActivity {

    EditText phone;
    String user_phone;
    ProgressDialog progressDialog;
    //    private static final int FRAMEWORK_REQUEST_CODE = 1;
    private String initialStateParam;
    Bundle b;


    private int nextPermissionsRequestCode = 4000;
    public static final int FRAMEWORK_REQUEST_CODE = 1;
    int flowOption, position;
    String flowType;
    String key;


    private interface OnCompleteListener {
        void onComplete();
    }

    private final SparseArray<OnCompleteListener> permissionsListeners = new SparseArray<>();

    private Bundle bundle;
    UIManager uiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_kit_login);

        AppCompatButton login = (AppCompatButton) findViewById(R.id.phone);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin(LoginType.PHONE);

            }
        });
    }
    private void onLogin(final LoginType loginType) {
        showProgressDialog();
        final Intent intent = new Intent(AccountKitLoginActivity.this, AccountKitActivity.class);
        final AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder
                = new AccountKitConfiguration.AccountKitConfigurationBuilder(loginType, AccountKitActivity.ResponseType.CODE);

        uiManager = new SkinManager(SkinManager.Skin.CLASSIC, getResources().getColor(R.color.colorAccent));
        configurationBuilder.setUIManager(uiManager);

        configurationBuilder.setFacebookNotificationsEnabled(true);
        configurationBuilder.setReadPhoneStateEnabled(true);
        configurationBuilder.setReceiveSMS(true);
        configurationBuilder.setDefaultCountryCode("33");
        PhoneNumber phoneNumber = new PhoneNumber("33", user_phone, "FR");
        configurationBuilder.setInitialPhoneNumber(phoneNumber);


        initialStateParam = UUID.randomUUID().toString();
        configurationBuilder.setInitialAuthState(initialStateParam);
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        OnCompleteListener completeListener = () -> startActivityForResult(intent, FRAMEWORK_REQUEST_CODE);
        final OnCompleteListener receiveSMSCompleteListener = completeListener;
        completeListener = () -> requestPermissions(
                Manifest.permission.RECEIVE_SMS,
                R.string.permissions_receive_sms_title,
                R.string.permissions_receive_sms_message,
                receiveSMSCompleteListener);

        final OnCompleteListener readPhoneStateCompleteListener = completeListener;
        completeListener = () -> requestPermissions(
                Manifest.permission.READ_PHONE_STATE,
                R.string.permissions_read_phone_state_title,
                R.string.permissions_read_phone_state_message,
                readPhoneStateCompleteListener);
        final OnCompleteListener readStorageCompleteListener = completeListener;
        completeListener = () -> requestPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                R.string.permissions_read_phone_state_title,
                R.string.permissions_read_phone_state_message,
                readStorageCompleteListener);
        completeListener.onComplete();

        final OnCompleteListener readContactsListener = completeListener;
        completeListener = () -> requestPermissions(
                Manifest.permission.READ_CONTACTS,
                R.string.permissions_read_contacts_title,
                R.string.permissions_read_contacts_message,
                readContactsListener);
        completeListener.onComplete();

//        startActivityForResult(intent, FRAMEWORK_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FRAMEWORK_REQUEST_CODE) {
            final AccountKitLoginResult loginResult =
                    data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            final String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                final String authorizationCode = loginResult.getAuthorizationCode();
                final long tokenRefreshIntervalInSeconds = loginResult.getTokenRefreshIntervalInSeconds();
                if (authorizationCode != null) {
                    toastMessage = String.format(
                            "Success:%s...",
                            authorizationCode.substring(0, 10));
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                    Intent intent = new Intent(AccountKitLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    // getUserDetails(authorizationCode);
                } else {
                    toastMessage = "Unknown response type";
                }
            }
        }
    }
    private void requestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        checkRequestPermissions(
                permission,
                rationaleTitleResourceId,
                rationaleMessageResourceId,
                listener);
    }

    @TargetApi(23)
    private void checkRequestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (getApplicationContext().checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        final int requestCode = nextPermissionsRequestCode++;
        permissionsListeners.put(requestCode, listener);

        if (shouldShowRequestPermissionRationale(permission)) {
            new AlertDialog.Builder(this)
                    .setTitle(rationaleTitleResourceId)
                    .setMessage(rationaleMessageResourceId)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> requestPermissions(new String[]{permission}, requestCode))
                    .setNegativeButton(android.R.string.no, (dialog, which) -> {
                        // ignore and clean up the listener
                        permissionsListeners.remove(requestCode);
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(
            final int requestCode,
            final @NonNull String[] permissions,
            final @NonNull int[] grantResults) {
        final OnCompleteListener permissionsListener = permissionsListeners.get(requestCode);
        permissionsListeners.remove(requestCode);
        if (permissionsListener != null
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsListener.onComplete();
        }
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(AccountKitLoginActivity.this);
        progressDialog.setMessage("Signing In..."); // Setting Message
        progressDialog.setTitle("Please Wait"); // Setting Title
//        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Horizontal
        progressDialog.show(); // Display Progress Dialog
    }
}
