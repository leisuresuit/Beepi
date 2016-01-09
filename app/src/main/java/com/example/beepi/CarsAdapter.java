package com.example.beepi;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.beepi.model.Car;
import com.example.beepi.model.CarShotUrls;
import com.example.beepi.util.ViewUtil;

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
        ImageLoader imageLoader = AppHandles.getInstance().getImageLoader();
        holder.pager.setAdapter(new CarImageAdapter(holder.itemView.getContext(), imageLoader));
        return holder;
    }

    @Override
    public void onBindViewHolder(CarViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        final Car car = mCars.get(position);
        CarImageAdapter pagerAdapter = ((CarImageAdapter) holder.pager.getAdapter());
        pagerAdapter.setUrls(car.carShotUrls);
        holder.pager.setCurrentItem(0);
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

    static class CarImageAdapter extends PagerAdapter {
        private final LayoutInflater mLayoutInflater;
        private final ImageLoader mImageLoader;
        private CarShotUrls mUrls;

        public CarImageAdapter(Context context, ImageLoader imageLoader) {
            mLayoutInflater = LayoutInflater.from(context);
            mImageLoader = imageLoader;
        }

        public void setUrls(CarShotUrls urls) {
            mUrls = urls;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return (mUrls != null) ? 3 : 0;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            NetworkImageView iv = ViewUtil.findViewById(v, R.id.image);
            String url = null;
            switch (position) {
                case 0:
                    url = mUrls.heroShotUrl;
                    break;
                case 1:
                    url = mUrls.leftSideShotUrl;
                    break;
                case 2:
                    url = mUrls.rightSideShotUrl;
                    break;
            }
            if (url != null) {
                iv.setImageUrl("https:" + url, mImageLoader);
            }
            container.addView(iv);
            return iv;
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
