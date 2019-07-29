package com.example.mytaxi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mytaxi.model.Car;


import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    private List<Car> mCars;
    private Context context;
    private final ClickListener mClickListener;

    public RecyclerViewAdapter(Context context , List<Car> cars ,ClickListener clickListener) {

        this.mCars = cars;
        this.context = context;
        this.mClickListener = clickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_item, parent,false);
        ViewHolder mViewHolder = new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        int id = mCars.get(position).getId();
        String type = mCars.get(position).getType();


        holder.carID.setText(id+"");
        holder.carType.setText(type);
        if(type.equals("TAXI")){
            holder.carImage.setImageResource(R.drawable.taxi);
        }
        else if(type.equals("POOLING")){
            holder.carImage.setImageResource(R.drawable.car_pooling);
        }

    }

    @Override
    public int getItemCount() {
        return mCars.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder  {

        TextView carID;
        TextView carType;
        ImageView carImage;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);

            carID = itemView.findViewById(R.id.car_id);
            carType = itemView.findViewById(R.id.car_type);
            carImage = itemView.findViewById(R.id.car_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.clickListen(mCars.get(getAdapterPosition()));
                }
            });

        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}