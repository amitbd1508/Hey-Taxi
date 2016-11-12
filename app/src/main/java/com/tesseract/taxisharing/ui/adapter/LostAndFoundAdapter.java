package com.tesseract.taxisharing.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.tesseract.taxisharing.R;
import com.tesseract.taxisharing.model.LOF;

import java.util.List;

/**
 * Created by hhson on 8/12/2016.
 */
public class LostAndFoundAdapter extends RecyclerView.Adapter<LostAndFoundAdapter.ViewHolder> {

    List<LOF>lofs;
    Context context;


    public LostAndFoundAdapter(List<LOF> lofs, Context context) {
        this.lofs = lofs;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lost_and_found, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(lofs.get(position).getType().equals("lost"))
        {
            holder.container.setBackgroundColor(Color.parseColor("#EF5351"));
        }
        holder.tvTitle.setText(lofs.get(position).getTitle());
        holder.tvStatus.setText(lofs.get(position).getType().toUpperCase());
        holder.tvPhone.setText(lofs.get(position).getPhone());
        holder.tvDescription.setText(lofs.get(position).getDescription());
        holder.tvLocation.setText(lofs.get(position).getLocation());
        holder.TVtime.setText(lofs.get(position).getTime());


    }

    @Override
    public int getItemCount() {
        return lofs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvLocation;
        TextView tvDescription;
        TextView tvPhone;
        TextView tvStatus;
        TextView TVtime;



        View container;


        public ViewHolder(View itemView) {
            super(itemView);
            container=itemView.findViewById(R.id.layout_laf_container);
            tvDescription= (TextView) itemView.findViewById(R.id.tv_lof_details);
            tvLocation= (TextView) itemView.findViewById(R.id.tv_lof_location);
            tvPhone= (TextView) itemView.findViewById(R.id.tv_lof_contract);
            tvStatus= (TextView) itemView.findViewById(R.id.tv_lof_category);
            tvTitle= (TextView) itemView.findViewById(R.id.tv_lof_title);
            TVtime= (TextView) itemView.findViewById(R.id.tv_lof_time);




        }
    }


}
