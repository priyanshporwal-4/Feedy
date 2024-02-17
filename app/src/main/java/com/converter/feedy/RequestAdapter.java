package com.converter.feedy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class RequestAdapter extends FirestoreRecyclerAdapter<Request, RequestAdapter.RequestHolder> {

    private SelectListener listener;

    public RequestAdapter(@NonNull FirestoreRecyclerOptions<Request> options, SelectListener listener) {
        super(options);
        this.listener = listener;

    }

    @Override
    protected void onBindViewHolder(@NonNull RequestHolder requestHolder, int i, @NonNull Request request) {
        requestHolder.name.setText(request.getName());
        requestHolder.phone.setText(request.getPhone());
        requestHolder.expiry.setText(request.getExpiry());
        requestHolder.quantity.setText(request.getQuantity());
        requestHolder.foodItems.setText(request.getFood_items());

        requestHolder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(request.getLatitude(), request.getLongitude());
            }
        });

        String stringLatitude = request.getLatitude();
        String stringLongitude = request.getLatitude();
    }

    @NonNull
    @Override
    public RequestHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_layout, parent, false);
        return new RequestHolder(view);
    }

    class RequestHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView phone;
        TextView quantity;
        TextView expiry;
        TextView foodItems;
        CardView cardView;

        public RequestHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            quantity = itemView.findViewById(R.id.quantity);
            expiry = itemView.findViewById(R.id.expiry);
            foodItems = itemView.findViewById(R.id.food_items);
            cardView = itemView.findViewById(R.id.cardview);
        }
    }
}
