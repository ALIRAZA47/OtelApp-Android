package com.example.otelapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otelapp.BookRoomPopup;
import com.example.otelapp.R;
import com.example.otelapp.models.Room;

import java.util.ArrayList;

public class RoomsAdapter extends RecyclerView.Adapter<RoomsAdapter.RoomsViewHolder> {

    ArrayList<Room> rooms = new ArrayList<>();
    Context context;

    public RoomsAdapter(ArrayList<Room> rooms, Context context) {
        this.rooms = rooms;
        this.context = context;
    }

    @Override
    public RoomsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.room_item, parent, false);
        //return inflated view
        return new RoomsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RoomsViewHolder holder, int position) {

        holder.roomNo.setText(rooms.get(position).roomNumber + "");
        holder.roomStatus.setText(rooms.get(position).currentStatus + "");
        if (rooms.get(position).currentStatus.equals("Booked")
                ||
                rooms.get(position).currentStatus.equals("booked")) {
            holder.roomCardContainer.setCardBackgroundColor(context.getResources().getColor(R.color.secondaryLightColor));
        }

        // now card click functionality
        holder.roomCardContainer.setOnClickListener(v -> {
            if ((rooms.get(position).currentStatus.equals("Booked"))) {
                // proceed on if room is not booked
                Toast.makeText(context, "Already Booked ^_^", Toast.LENGTH_SHORT).show();
                return;
            } else {
               // room is not Booked so user can proceed to book the room
                Intent intent = new Intent(context, BookRoomPopup.class);
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    //view holder class
    public class RoomsViewHolder extends RecyclerView.ViewHolder {
        TextView roomNo;
        CardView roomCardContainer;
        TextView roomStatus;

        public RoomsViewHolder(View itemView) {
            super(itemView);
            roomNo = itemView.findViewById(R.id.roomNo);
            roomStatus = itemView.findViewById(R.id.roomStatus);
            roomCardContainer = itemView.findViewById(R.id.roomCardContainer);
        }
    }
}
