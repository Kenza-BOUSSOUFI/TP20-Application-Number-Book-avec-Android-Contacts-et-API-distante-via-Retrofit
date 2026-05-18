package com.example.tp20_application_number_book_avec_android_contacts_et_api_distante_via_retrofit;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ContactDisplayAdapter extends RecyclerView.Adapter<ContactDisplayAdapter.ContactHolder> {

    private List<UserContact> dataList;

    public ContactDisplayAdapter(List<UserContact> dataList) {
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public ContactHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_2, parent, false);
        return new ContactHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactHolder holder, int position) {
        UserContact item = dataList.get(position);
        holder.primaryText.setText(item.getFullName());
        holder.secondaryText.setText(item.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void refreshData(List<UserContact> newList) {
        this.dataList = newList;
        notifyDataSetChanged();
    }

    static class ContactHolder extends RecyclerView.ViewHolder {
        TextView primaryText, secondaryText;

        public ContactHolder(@NonNull View itemView) {
            super(itemView);
            primaryText = itemView.findViewById(android.R.id.text1);
            secondaryText = itemView.findViewById(android.R.id.text2);
        }
    }
}
