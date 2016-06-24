package ru.mnx.gostkeeper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class ViewSecretActivity extends AppCompatActivity {

    public static final String SECRET_NAME_EXTRA = "SecretName";
    public static final String SECRET_DATA_EXTRA = "SecretData";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_secret);
        Bundle extras = getIntent().getExtras();
        getSecretNameTextView().setText((String) extras.get(SECRET_NAME_EXTRA));
        getSecretPassTextView().setText((String) extras.get(SECRET_DATA_EXTRA));
    }

    private TextView getSecretNameTextView() {
        return (TextView) findViewById(R.id.secretName);
    }

    private TextView getSecretPassTextView() {
        return (TextView) findViewById(R.id.secretData);
    }
}
