package com.amaze.mqtt.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amaze.mqtt.POJO.Rasp;
import com.amaze.mqtt.R;

import java.util.ArrayList;

/**
 * Created by arpitkh996 on 07-09-2016.
 */

public class ChooseDeviceAdapter extends RecyclerView.Adapter<ChooseDeviceAdapter.CustomHolder> {

    Context mContext;
    LayoutInflater inflater;
    ArrayList<Rasp> deviceDetailsArrayList;
    View view;
    OnClickListener editClickListener;

    public ChooseDeviceAdapter(Activity activity, ArrayList<Rasp> arrayList) {
        this.mContext = activity;
        inflater = LayoutInflater.from(mContext);
        deviceDetailsArrayList = new ArrayList<>();
        deviceDetailsArrayList.addAll(arrayList);
    }

    public void setDeviceDetailsArrayList(ArrayList<Rasp> deviceDetailsArrayList) {
        this.deviceDetailsArrayList = deviceDetailsArrayList;
    }

    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.choose_device_row, parent, false);
        return new CustomHolder(view);
    }

    /*
    This method is written to open or close a particular section, by passing the position of the element
    and then calling notifydataSetchanged to make the changes visible
     */


    @Override
    public void onBindViewHolder(final CustomHolder holder, final int position) {

        final Rasp deviceDetails = deviceDetailsArrayList.get(position);
        holder.title.setText(deviceDetails.getName());
        holder.description.setText(deviceDetails.getDesc());
        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            editClickListener.onClick(deviceDetails);
            }
        });
    }


    @Override
    public int getItemCount() {
        return deviceDetailsArrayList.size();
    }

    public interface OnClickListener {
        void onClick(Rasp deviceDetails);
    }

    public void setEditClickListener(OnClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }

    public class CustomHolder extends RecyclerView.ViewHolder {

        TextView title, description;

        View containerView;

        public CustomHolder(View itemView) {
            super(itemView);

            containerView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);

        }

    }
}
