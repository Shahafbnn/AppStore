package com.example.finalproject.Adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.finalproject.Classes.User;

public class UserAdapter extends ArrayAdapter<User> {
    public UserAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
