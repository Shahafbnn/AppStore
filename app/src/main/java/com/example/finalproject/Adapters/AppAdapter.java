package com.example.finalproject.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.Activities.ChosenAppActivity;
import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.App.AppView;
import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.User.User;

import java.util.ArrayList;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {
    private Context context;
    private ArrayList<App> appArrayList;
    private View.OnClickListener onClickListener;
    private User curUser;

    public AppAdapter(Context context, ArrayList<App> appArrayList, View.OnClickListener onClickListener, User curUser) {
        this.context = context;
        this.appArrayList = appArrayList;
        this.onClickListener = onClickListener;
        this.curUser = curUser;
    }

    @NonNull
    @Override
    public AppAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(new AppView(context));
    }

    @Override
    public void onBindViewHolder(@NonNull AppAdapter.ViewHolder holder, int position) {
        AppView appView = (AppView) holder.itemView;
        appView.setData(context, appArrayList.get(position));

        appView.setOnClickListener(onClickListener);
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
