package ru.mnx.gostkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.security.KeyStore;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.security.auth.DestroyFailedException;

import ru.mnx.gostkeeper.data.encryption.GostKeeperKeystore;

/**
 * Activity for password entering
 */
public class PasswordActivity extends AppCompatActivity {

    private static final Logger log = Logger.getLogger(PasswordActivity.class.getName());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (GostKeeperApplication.isKeystoreInitialized()) {
            log.info("Skip login");
            gotoToSecretList();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        Button enterButton = getEnterButton();
        enterButton.setOnClickListener(new EnterButtonClickListener());
    }

    private void gotoToSecretList() {
        Intent newActivity = new Intent(this, SecretsListActivity.class);
        startActivity(newActivity);
    }

    private Button getEnterButton() {
        return (Button) findViewById(R.id.enterButton);
    }

    private EditText getPasswordEditText() {
        return (EditText) findViewById(R.id.passwordEditText);
    }


    /**
     * Enter button click listener
     */
    private static class EnterButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            PasswordActivity parentActivity = (PasswordActivity) v.getContext();
            KeyStore.PasswordProtection pass = getPassword(parentActivity.getPasswordEditText());
            try {
                GostKeeperApplication.initKeystore(v.getContext(), pass);
                parentActivity.gotoToSecretList();
            } catch (GostKeeperKeystore.KeystoreException e) {
                log.log(Level.WARNING, "Password validation error", e);
                Toast.makeText(v.getContext(), R.string.wrong_password_error_message, Toast.LENGTH_LONG).show();
            } finally {
                safeDestroy(pass);
            }
        }

        private KeyStore.PasswordProtection getPassword(EditText passwordEditText) {
            return new KeyStore.PasswordProtection(
                    passwordEditText.getText().toString().toCharArray()
            );
        }

        private void safeDestroy(KeyStore.PasswordProtection pass) {
            try {
                pass.destroy();
            } catch (DestroyFailedException e) {
                //KeyStore.PasswordProtection don't throw DestroyFailedException
                log.log(Level.SEVERE, "Can't destroy password", e);
            }
        }
    }
}
