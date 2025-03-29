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
import com.bandapp.database.entity.Location;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class LocationHistoryAdapter extends ListAdapter<Location, LocationHistoryAdapter.LocationViewHolder> {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault());

    public LocationHistoryAdapter() {
        super(new DiffUtil.ItemCallback<Location>() {
            @Override
            public boolean areItemsTheSame(@NonNull Location oldItem, @NonNull Location newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Location oldItem, @NonNull Location newItem) {
                return oldItem.getLatitude() == newItem.getLatitude() &&
                       oldItem.getLongitude() == newItem.getLongitude() &&
                       oldItem.getTimestamp().equals(newItem.getTimestamp());
            }
        });
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location_history, parent, false);
        return new LocationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        Location location = getItem(position);
        holder.bind(location);
    }

    static class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView coordinatesText;
        private TextView timestampText;
        private TextView accuracyText;

        public LocationViewHolder(@NonNull View itemView) {
            super(itemView);
            coordinatesText = itemView.findViewById(R.id.coordinates);
            timestampText = itemView.findViewById(R.id.timestamp);
            accuracyText = itemView.findViewById(R.id.accuracy);
        }

        public void bind(Location location) {
            coordinatesText.setText(String.format(Locale.getDefault(), "%.6f, %.6f",
                    location.getLatitude(), location.getLongitude()));
            timestampText.setText(dateFormat.format(location.getTimestamp()));
            accuracyText.setText(String.format(Locale.getDefault(), "Accuracy: %.1fm",
                    location.getAccuracy()));
        }
    }
} 