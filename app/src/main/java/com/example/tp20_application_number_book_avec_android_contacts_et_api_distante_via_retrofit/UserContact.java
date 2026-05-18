package com.example.tp20_application_number_book_avec_android_contacts_et_api_distante_via_retrofit;

import com.google.gson.annotations.SerializedName;

public class UserContact {
    private int id;
    @SerializedName("name")
    private String fullName;
    @SerializedName("phone")
    private String phoneNumber;
    private String source;
    private String created_at;

    public UserContact() {}

    public UserContact(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getCreated_at() { return created_at; }
    public void setCreated_at(String created_at) { this.created_at = created_at; }
}
