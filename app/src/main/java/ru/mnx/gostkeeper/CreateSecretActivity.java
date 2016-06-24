package ru.mnx.gostkeeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class CreateSecretActivity extends AppCompatActivity {

    public static final String SECRET_NAME_EXTRA = "SecretName";
    public static final String SECRET_DATA_EXTRA = "SecretData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_secret);

        Button commitButton = getCommitSecretButton();
        commitButton.setOnClickListener(new CommitSecretButtonClickListener());
    }

    private Button getCommitSecretButton() {
        return (Button) findViewById(R.id.commitSecretButton);
    }

    private EditText getSecretNameEditText() {
        return (EditText) findViewById(R.id.secretNameEdit);
    }

    private EditText getSecretDataEditText() {
        return (EditText) findViewById(R.id.secretDataEdit);
    }


    /**
     * Слушатель нажатия на кнопку "Сохранить"
     */
    private static class CommitSecretButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            CreateSecretActivity parentActivity = (CreateSecretActivity) v.getContext();
            EditText secretNameEdit = parentActivity.getSecretNameEditText();
            EditText secretDataEdit = parentActivity.getSecretDataEditText();
            Intent intent = new Intent();
            intent.putExtra(SECRET_NAME_EXTRA, secretNameEdit.getText());
            intent.putExtra(SECRET_DATA_EXTRA, secretDataEdit.getText());
            parentActivity.setResult(RESULT_OK, intent);
            parentActivity.finish();
        }
    }
}
