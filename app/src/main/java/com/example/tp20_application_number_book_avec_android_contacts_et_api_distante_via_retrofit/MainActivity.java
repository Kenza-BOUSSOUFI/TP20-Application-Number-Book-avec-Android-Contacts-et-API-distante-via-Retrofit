package com.example.tp20_application_number_book_avec_android_contacts_et_api_distante_via_retrofit;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnImport, btnSync, btnSearch;
    private EditText searchField;
    private RecyclerView recyclerView;
    private ContactDisplayAdapter contactAdapter;
    private List<UserContact> localContacts = new ArrayList<>();
    private RemoteService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupNetwork();
        setupListeners();
    }

    private void initViews() {
        btnImport = findViewById(R.id.btnFetchLocal);
        btnSync = findViewById(R.id.btnPushToServer);
        btnSearch = findViewById(R.id.btnTriggerSearch);
        searchField = findViewById(R.id.inputSearchQuery);
        recyclerView = findViewById(R.id.listDisplayArea);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactAdapter = new ContactDisplayAdapter(localContacts);
        recyclerView.setAdapter(contactAdapter);
    }

    private void setupNetwork() {
        apiService = ServiceGenerator.getRetrofitInstance().create(RemoteService.class);
    }

    private void setupListeners() {
        btnImport.setOnClickListener(v -> handlePermissionRequest());
        btnSync.setOnClickListener(v -> uploadContactsToServer());
        btnSearch.setOnClickListener(v -> performRemoteSearch());
    }

    private void handlePermissionRequest() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED) {
            fetchDeviceContacts();
        } else {
            contactPermissionLauncher.launch(Manifest.permission.READ_CONTACTS);
        }
    }

    private final ActivityResultLauncher<String> contactPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    fetchDeviceContacts();
                } else {
                    Toast.makeText(this, "Accès aux contacts refusé", Toast.LENGTH_SHORT).show();
                }
            });

    private void fetchDeviceContacts() {
        localContacts.clear();

        Cursor cursor = getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        );

        if (cursor != null) {
            int nameIndex = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            int phoneIndex = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIndex);
                String phone = cursor.getString(phoneIndex);
                localContacts.add(new UserContact(name, phone));
            }
            cursor.close();
        }

        contactAdapter.refreshData(localContacts);
        Toast.makeText(this, localContacts.size() + " contacts importés", Toast.LENGTH_SHORT).show();
    }

    private void uploadContactsToServer() {
        if (localContacts.isEmpty()) {
            Toast.makeText(this, "Aucun contact à synchroniser", Toast.LENGTH_SHORT).show();
            return;
        }

        for (UserContact contact : localContacts) {
            apiService.pushContact(contact).enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(@NonNull Call<ServerResponse> call, @NonNull Response<ServerResponse> response) {
                    // Success handling could be improved here (e.g., progress bar)
                }

                @Override
                public void onFailure(@NonNull Call<ServerResponse> call, @NonNull Throwable t) {
                    Toast.makeText(MainActivity.this, "Échec de connexion : " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

        Toast.makeText(this, "Synchronisation en cours...", Toast.LENGTH_SHORT).show();
    }

    private void performRemoteSearch() {
        String query = searchField.getText().toString().trim();

        if (query.isEmpty()) {
            Toast.makeText(this, "Veuillez saisir un critère", Toast.LENGTH_SHORT).show();
            return;
        }

        apiService.findByKeyword(query).enqueue(new Callback<List<UserContact>>() {
            @Override
            public void onResponse(@NonNull Call<List<UserContact>> call, @NonNull Response<List<UserContact>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    contactAdapter.refreshData(response.body());
                    Toast.makeText(MainActivity.this, response.body().size() + " résultats trouvés", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<UserContact>> call, @NonNull Throwable t) {
                Toast.makeText(MainActivity.this, "Erreur de recherche", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
