package com.example.finalproject.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.App.AppView;

import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
    private Context context;
    private ArrayList<App> appArrayList;

    public AppAdapter(Context context, ArrayList<App> appArrayList) {
        this.context = context;
        this.appArrayList = appArrayList;
    }

    @NonNull
    @Override
    public AppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new AppView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull AppAdapter.ViewHolder holder, int position) {
        ((AppView)holder.itemView).setData(context, appArrayList.get(position));
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return appArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

}
