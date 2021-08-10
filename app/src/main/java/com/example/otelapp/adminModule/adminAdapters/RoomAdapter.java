package com.example.otelapp.adminModule.adminAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.otelapp.R;
import com.example.otelapp.models.Hotel;
import com.example.otelapp.models.Room;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.RoomViewHolder> {

    DatabaseReference dbRef;
    ArrayList<Room> rooms = new ArrayList<>();
    Context context;
    String sHotel;

    public RoomAdapter(ArrayList<Room> rooms, Context context, String sHotel) {
        this.rooms = rooms;
        this.context = context;
        this.sHotel = sHotel;

    }

    @Override
    public RoomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.room_item, parent, false);
        //return inflated view
        return new RoomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RoomViewHolder holder, int position) {

        holder.roomNo.setText(rooms.get(position).roomNumber + "");
        holder.roomStatus.setText(rooms.get(position).currentStatus + "");
        if (rooms.get(position).currentStatus.equals("Booked")
                ||
                rooms.get(position).currentStatus.equals("booked")) {
            holder.roomCardContainer.setCardBackgroundColor(context.getResources().getColor(R.color.secondaryLightColor));
        }
        if (rooms.get(position).currentStatus.equals("Pending")
                ||
                rooms.get(position).currentStatus.equals("pending")) {
            holder.roomCardContainer.setCardBackgroundColor(context.getResources().getColor(R.color.pendingRoomColor));
        }

        //now if the room is selected than do something
        holder.roomCardContainer.setOnClickListener(v -> {
            if (rooms.get(position).currentStatus.equals("Pending")
                    ||
                    rooms.get(position).currentStatus.equals("pending")) {

                //now approve the pending room
                TextView title = new TextView(context);
                title.setText("Confirm Room for Booking ?");
                title.setPadding(10, 10, 10, 10);
                title.setGravity(Gravity.CENTER);
                title.setTextColor(Color.WHITE);
                title.setTextSize(20);

                AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.MyAlertDialogTheme).create();
                alertDialog.setCustomTitle(title);
                this.dbRef = FirebaseDatabase.getInstance().getReference("main");
                alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                int roomNum = Integer.parseInt(rooms.get(position).roomNumber) ;

                                dbRef.child("hotelsList").child(sHotel).child("rooms").child(String.valueOf(roomNum)).child("currentStatus").setValue("Booked");
                                dialog.dismiss();

                            }
                        });

                alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                Button btnPositive = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
                Button btnNegative = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnPositive.getLayoutParams();
                layoutParams.weight = 10;
                btnPositive.setLayoutParams(layoutParams);
                btnNegative.setLayoutParams(layoutParams);
            }
        });

    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }

    //view holder for rooms
    public class RoomViewHolder extends RecyclerView.ViewHolder {
        TextView roomNo;
        CardView roomCardContainer;
        TextView roomStatus;

        public RoomViewHolder(@NonNull View itemView) {
            super(itemView);
            roomNo = itemView.findViewById(R.id.roomNo);
            roomStatus = itemView.findViewById(R.id.roomStatus);
            roomCardContainer = itemView.findViewById(R.id.roomCardContainer);
        }
    }
}
