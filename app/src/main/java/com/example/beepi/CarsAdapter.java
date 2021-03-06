package com.example.beepi;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beepi.model.Car;
import com.example.beepi.util.ViewUtil;
import com.squareup.picasso.Picasso;

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
        CarViewHolder holder = new CarViewHolder(v);
        CarImageAdapter adapter = new CarImageAdapter(holder.itemView.getContext());
        adapter.setListener(mListener);
        holder.pager.setAdapter(adapter);
        return holder;
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        final Car car = mCars.get(position);
        CarImageAdapter pagerAdapter = ((CarImageAdapter) holder.pager.getAdapter());
        pagerAdapter.setCar(car);
        holder.pager.setCurrentItem(0);
        holder.name.setText(context.getString(R.string.car_item_label, car.title, car.trim));
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

    public void addCars(Car[] cars) {
        if (cars != null) {
            mCars.addAll(Arrays.asList(cars));
            notifyItemRangeInserted(mCars.size() - cars.length, cars.length);
        }
    }

    static class CarImageAdapter extends PagerAdapter {
        private final LayoutInflater mLayoutInflater;
        private Car mCar;
        private CarListener mListener;

        public CarImageAdapter(Context context) {
            mLayoutInflater = LayoutInflater.from(context);
        }

        public void setListener(CarListener listener) {
            mListener = listener;
        }

        public void setCar(Car car) {
            mCar = car;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return (mCar != null && mCar.carShotUrls != null) ? 3 : 0;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = mLayoutInflater.inflate(R.layout.car_item_image, container, false);
            if (mListener != null){
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListener.onCarClick(mCar);
                    }
                });
            }
            ImageView iv = ViewUtil.findViewById(v, R.id.image);
            String url = null;
            switch (position) {
                case 0:
                    url = mCar.carShotUrls.heroShotUrl;
                    break;
                case 1:
                    url = mCar.carShotUrls.leftSideShotUrl;
                    break;
                case 2:
                    url = mCar.carShotUrls.rightSideShotUrl;
                    break;
            }
            if (url != null) {
                Picasso.with(v.getContext())
                        .load("https:" + url)
                        .into(iv);
            }
            container.addView(v);
            return v;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return (view == object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    static class CarViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.pager) ViewPager pager;
        @Bind(R.id.name) TextView name;
        @Bind(R.id.miles) TextView miles;
        @Bind(R.id.price) TextView price;

        CarViewHolder(View view) {
            super(view);

            ButterKnife.bind(this, view);
        }
    }

}
