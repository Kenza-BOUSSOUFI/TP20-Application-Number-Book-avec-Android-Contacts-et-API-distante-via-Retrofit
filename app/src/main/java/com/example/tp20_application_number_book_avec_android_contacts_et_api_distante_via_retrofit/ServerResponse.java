package com.example.tp20_application_number_book_avec_android_contacts_et_api_distante_via_retrofit;

public class ServerResponse {
    private boolean success;
    private String message;

    public boolean isSuccessful() {
        return success;
    }

    public String getStatusMessage() {
        return message;
    }
}
