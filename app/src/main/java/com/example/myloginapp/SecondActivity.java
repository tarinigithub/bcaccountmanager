package com.example.myloginapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.IOException;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondaryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Log.w(TAG, "getTokenFromAccountManager ->" + getTokenFromAccountManager());

        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(this);
        if (acct != null){

            String personName = acct.getDisplayName();
            Log.w(TAG, "Last signed-in account, personName->" + personName);
            Log.w(TAG, "Last signed-in account, getIdToken->" + acct.getIdToken());

            String idToken = acct.getIdToken();

            String androidID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            System.out.println("Android Id " + androidID);

            Log.w(TAG, "androidID ->" + androidID);


            MyAsyncTasks myAsyncTasks = new MyAsyncTasks(idToken, androidID);
            myAsyncTasks.execute();

        } else {
            Log.w(TAG, "NO ONE Has logged in ");
        }
    }

    //TARINI999
    String getTokenFromAccountManager() {
        String token = "";
        String username = "admin@ivantibcetest.com";
        String password = "Mi4man112233";
        String your_account_type = "com.google";

        AccountManager accountManager = AccountManager.get(this);
        final Account account = new Account(username, your_account_type);
        accountManager.addAccountExplicitly(account, password, null);

        Bundle options = new Bundle();

        String lookupScope = "https://www.googleapis.com/auth/cloud-identity.devices.lookup";

        accountManager.getAuthToken(
                account,                     // Account retrieved using getAccountsByType()
                lookupScope,                    // Auth scope
                options,                        // Authenticator-specific options
                this,                           // Your activity
                new OnTokenAcquired(),          // Callback called when a token is successfully acquired
                new Handler(new OnError()));    // Callback called if an error occurs

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