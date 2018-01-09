package com.amaze.mqtt.adapters;

/**
 * Created by arpitkh996 on 08-09-2016.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.amaze.mqtt.POJO.Device;
import com.amaze.mqtt.POJO.DeviceState;

import com.amaze.mqtt.R;

import java.util.ArrayList;

/**
 * Created by arpitkh996 on 07-09-2016.
 */

public class ElectronicsAdapter extends RecyclerView.Adapter<ElectronicsAdapter.CustomHolder> {

    Context mContext;
    LayoutInflater inflater;
    ArrayList<Device> deviceDetailsArrayList;
    View view;
    OnClickListener editClickListener;

    public ElectronicsAdapter(Activity activity, ArrayList<Device> arrayList) {
        this.mContext = activity;
        inflater = LayoutInflater.from(mContext);
        deviceDetailsArrayList = new ArrayList<>();
        deviceDetailsArrayList.addAll(arrayList);
    }


    @Override
    public CustomHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        view = inflater.inflate(R.layout.electronics_row, parent, false);
        return new CustomHolder(view);
    }

    /*
    This method is written to open or close a particular section, by passing the position of the element
    and then calling notifydataSetchanged to make the changes visible
     */

    public void setDeviceDetailsArrayList(ArrayList<Device> deviceDetailsArrayList) {
        this.deviceDetailsArrayList = deviceDetailsArrayList;
    }

    @Override
    public void onBindViewHolder(final CustomHolder holder, final int position) {

        final Device deviceDetails = deviceDetailsArrayList.get(position);
        holder.title.setText(deviceDetails.getDesc2());
        holder.description.setText(deviceDetails.getDesc1());
        holder.containerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editClickListener.onClick(deviceDetails);
            }
        });
        if(deviceDetails.getState()!=null)
            holder.state.setText(deviceDetails.getState()+"");
    }


    @Override
    public int getItemCount() {
        return deviceDetailsArrayList.size();
    }

    public interface OnClickListener {
        void onClick(Device deviceDetails);
    }

    public ArrayList<Device> getDeviceDetailsArrayList() {
        return deviceDetailsArrayList;
    }

    public void setEditClickListener(OnClickListener editClickListener) {
        this.editClickListener = editClickListener;
    }
    public void updateState(ArrayList<DeviceState> deviceStates){
        for(DeviceState deviceState:deviceStates){
            Device electronic=getElectronicById(deviceState.getDeviceId());

            electronic.setState(deviceState.getState());
        }
        notifyDataSetChanged();

    }
    Device getElectronicById(String deviceId){
        for(Device electronic:deviceDetailsArrayList){
            if(electronic.getEdeviceid().equals(deviceId))
                return electronic;
        }
        return null;
    }
    public class CustomHolder extends RecyclerView.ViewHolder {

        TextView title, description,state;

        View containerView;

        public CustomHolder(View itemView) {
            super(itemView);

            containerView = itemView;
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            state=(TextView)itemView.findViewById(R.id.state);
        }

    }
}
