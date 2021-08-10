package com.example.otelapp.adminModule.adminAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otelapp.R;
import com.example.otelapp.activities.SpecificHotel;
import com.example.otelapp.adapters.HotelMainAdapter;
import com.example.otelapp.adminModule.AdminRooms;
import com.example.otelapp.models.Hotel;
import com.example.otelapp.utils.SharedPrefs;
import com.google.gson.Gson;

import java.util.ArrayList;

public class HotelAdapter extends RecyclerView.Adapter<HotelAdapter.HotelViewHolder> {
    Context context;
    ArrayList<Hotel> hotels = new ArrayList<>();
    SharedPrefs prefs;

    //constructor
    //adapter constructor
    public HotelAdapter(Context context, ArrayList<Hotel> hotels) {
        //Log.i("TAG-Recycler", "HotelMainAdapter: ---> "+hotels.toString());
        this.context = context;
        this.hotels = hotels;
        this.prefs = new SharedPrefs(context);
    }

    @NonNull
    @Override
    public HotelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.hotel_item, parent, false);
        //return inflated view
        return new HotelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HotelViewHolder holder, int position) {
        String hotelName, hotelAddress;
        int totalRooms;
        hotelAddress = hotels.get(position).hotelAddress;
        hotelName = hotels.get(position).hotelName;
        totalRooms = hotels.get(position).totalRooms;
        holder.totalRooms.setText("" + totalRooms);
        holder.hotelAddress.setText("" + hotelAddress);
        holder.hotelName.setText("" + hotelName);
        holder.cardImage.setImageResource(R.mipmap.ic_launcher);
        holder.hotelCard.setOnClickListener(v -> {
            //if hotel is clicked
            Intent intent = new Intent(context, AdminRooms.class);

            prefs.removeKeyPair("selectedHotelAdmin");
            prefs.setString("selectedHotelAdmin" ,new Gson().toJson(hotels.get(position)));
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return hotels.size();
    }


    //View Holder Class

    public class HotelViewHolder extends RecyclerView.ViewHolder{
        TextView hotelName, totalRooms, hotelAddress;
        CardView hotelCard;
        ImageView cardImage;

        public HotelViewHolder(View itemView) {
            super(itemView);
            hotelName = itemView.findViewById(R.id.hotelName_HotelItem);
            hotelAddress = itemView.findViewById(R.id.hotelAddress_HotelItem);
            totalRooms = itemView.findViewById(R.id.totalRooms_HotelItem);
            hotelCard = itemView.findViewById(R.id.hotelContainerMain);
            cardImage = itemView.findViewById(R.id.hotelImageMain);
        }
    }
}

