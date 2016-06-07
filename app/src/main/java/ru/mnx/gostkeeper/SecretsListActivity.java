package ru.mnx.gostkeeper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.mnx.gostkeeper.data.entity.Secret;
import ru.mnx.gostkeeper.data.entity.SecretWithData;

public class SecretsListActivity extends AppCompatActivity {

    private static final int ACTION_CREATE = 123;
    private static final String LIST_ITEM_ID = "row_id";
    private static final String LIST_ITEM_NAME = "row_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secrets_list);

        ListView secretsList = getSecretsListView();
        secretsList.setAdapter(getSecretsAdapter());
        secretsList.setOnItemClickListener(new SecretClickListener());

        Button createSecretButton = getCreateSecretButton();
        createSecretButton.setOnClickListener(new CreateSecretButtonClickListener());
    }

    private ListView getSecretsListView() {
        return (ListView) findViewById(R.id.listView);
    }

    private Button getCreateSecretButton() {
        return (Button) findViewById(R.id.createSecretButton);
    }

    private ListAdapter getSecretsAdapter() {
        List<Map<String, Object>> fillMaps = new ArrayList<>();
        for (Secret secret : getSecrets()) {
            Map<String, Object> map = new HashMap<>();
            map.put(LIST_ITEM_ID, secret.getId());
            map.put(LIST_ITEM_NAME, secret.getName());
            fillMaps.add(map);
        }
        final String[] from = new String[] {LIST_ITEM_NAME};
        final int[] to = new int[] {R.id.secret_list_item_name};
        return new SimpleAdapter(this, fillMaps, R.layout.secrets_list_item, from, to);
    }

    private List<Secret> getSecrets() {
        GostKeeperApplication app = (GostKeeperApplication) getApplication();
        return app.getSecretDbHelper().getSecretList();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ACTION_CREATE == requestCode && RESULT_OK == resultCode) {
            createSecret(data.getExtras());
            recreate();
        }
    }

    private void createSecret(Bundle extras) {
        final CharSequence name = (CharSequence) extras.get(CreateSecretActivity.SECRET_NAME_EXTRA);
        final CharSequence data = (CharSequence) extras.get(CreateSecretActivity.SECRET_DATA_EXTRA);
        if (name != null && data != null) {
            GostKeeperApplication app = (GostKeeperApplication) getApplication();
            app.getSecretDbHelper().createSecret(name.toString().getBytes(), data.toString().getBytes());
        }
    }


    /**
     * Слушатель простого клика по секрету из списка
     */
    private static class SecretClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SecretWithData secret = getSecretWithData(parent, position);
            show(parent, secret);
        }

        private void show(AdapterView<?> parent, SecretWithData secret) {
            Intent newActivity = new Intent(parent.getContext(), ViewSecretActivity.class);
            newActivity.putExtra(ViewSecretActivity.SECRET_NAME_EXTRA, secret.getName());
            newActivity.putExtra(ViewSecretActivity.SECRET_DATA_EXTRA, secret.getData());
            newActivity.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            parent.getContext().startActivity(newActivity);
        }

        private SecretWithData getSecretWithData(AdapterView<?> parent, int position) {
            @SuppressWarnings("unchecked")
            Map<String, Object> item = (Map<String, Object>) parent.getItemAtPosition(position);
            int id = (int) item.get(LIST_ITEM_ID);
            return getApplication(parent).getSecretDbHelper().getSecretWithData(id);
        }

        private GostKeeperApplication getApplication(AdapterView<?> parent) {
            Activity activity = (Activity) parent.getContext();
            return (GostKeeperApplication) activity.getApplication();
        }
    }


    /**
     * Слушатель нажатия на кнопку "Создать"
     */
    private static class CreateSecretButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent newActivity = new Intent(v.getContext(), CreateSecretActivity.class);
            Activity parentActivity = (Activity) v.getContext();
            parentActivity.startActivityForResult(newActivity, ACTION_CREATE);
        }
    }
}
