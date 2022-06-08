package com.example.myloginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    ImageView googleBtn;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        googleBtn = findViewById(R.id.google_btn);

        Log.w(TAG, "TARINI9992 before GoogleSignInOptions.Builder");
       // getTokenFromAccountManager();
        navigateToAuthActivity();

        /*
        String lookupScope = "https://www.googleapis.com/auth/cloud-identity.devices.lookup";

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestScopes(new Scope(lookupScope))
                .requestIdToken("83485897810-adbdgqvf03l0bruhm6u3i2o75c0tvn6r.apps.googleusercontent.com")
                .requestEmail()
                .build();


        //original code gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        //gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        gsc = GoogleSignIn.getClient(this, gso);

        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

         */
    }

    void signIn() {
        Intent signInIntent = gsc.getSignInIntent();
        startActivityForResult(signInIntent, 1000);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                Log.w(TAG, "onActivityResult Before getting task getResult");
                task.getResult(ApiException.class);

                navigateToSecondActivity();
            } catch (ApiException e) {
                e.printStackTrace();
                Log.w(TAG, "onActivityResult exception");
            }
        }
    }

    void navigateToSecondActivity() {
        finish();
        //Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        //startActivity(intent);

    }

    void navigateToAuthActivity() {
        finish();
        Intent intent = new Intent(MainActivity.this, AuthActivity.class);
        startActivity(intent);

    }

    String getTokenFromAccountManager() {
        String token = "";
        String username = "admin@ivantibcetest.com";
        String password = "Mi4man112233";
        String your_account_type = "ivantibcetest.com";

        AccountManager accountManager = AccountManager.get(this);
        final Account account = new Account(username, your_account_type);
        //accountManager.addAccountExplicitly(account, password, null);

        Bundle options = new Bundle();

        String lookupScope = "https://www.googleapis.com/auth/cloud-identity.devices.lookup";

        accountManager.getAuthToken(
                account,                     // Account retrieved using getAccountsByType()
                lookupScope,                    // Auth scope
                options,                        // Authenticator-specific options
                this,                           // Your activity
                new MainActivity.OnTokenAcquired(),          // Callback called when a token is successfully acquired
                new Handler(new MainActivity.OnError()));    // Callback called if an error occurs

        return token;
    }

    private class OnTokenAcquired implements AccountManagerCallback<Bundle> {
        @Override
        public void run(AccountManagerFuture<Bundle> result) {
            // Get the result of the operation from the AccountManagerFuture.
            Bundle bundle = null;
            try {
                bundle = result.getResult();
                // The token is a named value in the bundle. The name of the value
                // is stored in the constant AccountManager.KEY_AUTHTOKEN.
                String token = bundle.getString(AccountManager.KEY_AUTHTOKEN);

                Log.w(TAG, "TARINI999token OnTokenAcquired " + token);

            } catch (AuthenticatorException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (OperationCanceledException e) {
                e.printStackTrace();
            }

            Log.w(TAG, "TARINI999token NO TOKEN " );

        }
    }

    private class OnError implements Handler.Callback {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            Log.w(TAG, "TARINI999tokenerror error occured " + message);
            return false;
        }
    }
}