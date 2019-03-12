package com.kupang.androidbpbd.bpbdkupang.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kupang.androidbpbd.bpbdkupang.ui.DetilHistoryActivity;
import com.kupang.androidbpbd.bpbdkupang.R;
import com.kupang.androidbpbd.bpbdkupang.constants.Constants;
import com.kupang.androidbpbd.bpbdkupang.model.Report;

import java.util.List;

/**
 * Created by apple on 3/8/18.
 */

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    //List to store all reports
    List<Report> reports;
    private Context context;

    //Constructor of this class
    public ReportAdapter(List<Report> reports, Context context) {
        super();
        //Getting all reports
        this.reports = reports;
        this.context = context;
    }

    @Override
    public ReportAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.report_list, parent, false);
        ReportAdapter.ViewHolder viewHolder = new ReportAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReportAdapter.ViewHolder holder, final int position) {

        //Getting the particular item from the list
        final Report report = reports.get(position);

        holder.textViewDate.setText(reports.get(position).getDate());
        holder.textViewDisasterType.setText(reports.get(position).getDisasterType());

        Log.e("ReportAdapter", "Tes" + ", date :" + report.getDate());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                if (reports.size() > 0) {

                    openDetailActivity(reports.get(position).getReportId(), reports.get(position).getDate(), reports.get(position).getLatitude(), reports.get(position).getLongitude(),
                            reports.get(position).getAddress(), reports.get(position).getDisasterType(), reports.get(position).getPhoto(), reports.get(position).getInformation(),
                            reports.get(position).getUserId());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return reports.size();
    }

    private void openDetailActivity(int reportId, String date, String latitude, String longitude, String address, String disasterType,
                                    String photo, String information, String userId) {
        Intent i = new Intent(context, DetilHistoryActivity.class);

        //PACK DATA TO SEND
        i.putExtra(Constants.TAG_REPORT_ID, reportId);
        i.putExtra(Constants.TAG_REPORT_DATE, date);
        i.putExtra(Constants.TAG_REPORT_LATITUDE, latitude);
        i.putExtra(Constants.TAG_REPORT_LONGITUDE, longitude);
        i.putExtra(Constants.TAG_REPORT_ADDRESS, address);
        i.putExtra(Constants.TAG_REPORT_DISASTER_TYPE, disasterType);
        i.putExtra(Constants.TAG_REPORT_PHOTO, photo);
        i.putExtra(Constants.TAG_REPORT_INFORMATION, information);
        i.putExtra(Constants.TAG_REPORT_USER_ID, userId);
        //open activity
        context.startActivity(i);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //Views
//        public NetworkImageView imageView;
        public TextView textViewDate;
        public TextView textViewDisasterType;
        ItemClickListener itemClickListener;

        //Initializing Views
        public ViewHolder(View itemView) {
            super(itemView);
            textViewDate = (TextView) itemView.findViewById(R.id.date_text_view);
            textViewDisasterType = (TextView) itemView.findViewById(R.id.disaster_type_text_view);

            itemView.setOnClickListener(this);
        }

        public void setItemClickListener(ItemClickListener itemClickListener) {
            this.itemClickListener = itemClickListener;
        }

        @Override
        public void onClick(View v) {
            this.itemClickListener.onItemClick(this.getLayoutPosition());
        }
    }
}
