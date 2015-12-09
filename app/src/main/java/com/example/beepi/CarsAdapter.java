package com.example.beepi;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.example.beepi.model.Car;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by larwang on 10/23/15.
 */
public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.CarViewHolder> {
    public interface CarListener {
        void onCarClick(Car car);
    }

    private List<Car> mCars = new ArrayList<>();
    private CarListener mListener;

    public void setListener(CarListener listener) {
        mListener = listener;
    }

    @Override
    public int getItemCount() {
        return mCars.size();
    }

    @Override
    public CarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_item, parent, false);
        return new CarViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        final Car car = mCars.get(position);
        holder.background.setImageDrawable(null);
        holder.background.setImageUrl("https:" + car.carShotUrls.heroShotUrl, AppHandles.getInstance().getImageLoader());
        holder.name.setText(context.getString(R.string.car_item_label, car.makeName, car.shortStyleName));
        holder.miles.setText(context.getString(R.string.car_item_miles, car.formattedMileage));
        holder.price.setText(car.formattedSalePrice);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onCarClick(car);
                }
            }
        });
    }

    public void setCars(Car[] cars) {
        mCars.clear();
        if (cars != null) {
            mCars = Arrays.asList(cars);
        }
    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.background) NetworkImageView background;
        @Bind(R.id.name) TextView name;
        @Bind(R.id.miles) TextView miles;
        @Bind(R.id.price) TextView price;

        CarViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

}
