package com.bandapp.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.bandapp.R;
import com.bandapp.database.entity.EmergencyContact;

public class EmergencyContactsAdapter extends ListAdapter<EmergencyContact, EmergencyContactsAdapter.ContactViewHolder> {
    private OnContactClickListener listener;

    public interface OnContactClickListener {
        void onContactClick(EmergencyContact contact);
    }

    public EmergencyContactsAdapter(OnContactClickListener listener) {
        super(new DiffUtil.ItemCallback<EmergencyContact>() {
            @Override
            public boolean areItemsTheSame(@NonNull EmergencyContact oldItem, @NonNull EmergencyContact newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull EmergencyContact oldItem, @NonNull EmergencyContact newItem) {
                return oldItem.getName().equals(newItem.getName()) &&
                       oldItem.getPhoneNumber().equals(newItem.getPhoneNumber()) &&
                       oldItem.getRelationship().equals(newItem.getRelationship());
            }
        });
        this.listener = listener;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_emergency_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        EmergencyContact contact = getItem(position);
        holder.bind(contact);
    }

    class ContactViewHolder extends RecyclerView.ViewHolder {
        private TextView nameText;
        private TextView phoneText;
        private TextView relationshipText;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.contact_name);
            phoneText = itemView.findViewById(R.id.contact_phone);
            relationshipText = itemView.findViewById(R.id.contact_relationship);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onContactClick(getItem(position));
                }
            });
        }

        public void bind(EmergencyContact contact) {
            nameText.setText(contact.getName());
            phoneText.setText(contact.getPhoneNumber());
            relationshipText.setText(contact.getRelationship());
        }
    }
} 