package com.example.finalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.Classes.User;
import com.example.finalproject.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private List<User> users;
    private FirebaseFirestore db;

    public UserAdapter(Context context, List<User> usersArray) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
        this.users = usersArray;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View userView = inflater.inflate(R.layout.user_adapter, parent, false);
        User user = users.get(position);

        ImageView ivUserAdapterPfp = userView.findViewById(R.id.ivUserAdapterPfp);
        TextView tvUserAdapterFullName = userView.findViewById(R.id.tvUserAdapterFullName);
        TextView tvUserAdapterAge = userView.findViewById(R.id.tvUserAdapterAge);
        TextView tvUserAdapterEmail = userView.findViewById(R.id.tvUserAdapterEmail);
        TextView tvUserAdapterCity = userView.findViewById(R.id.tvUserAdapterCity);
        TextView tvUserAdapterWeight = userView.findViewById(R.id.tvUserAdapterWeight);
        TextView tvUserAdapterPhoneNumber = userView.findViewById(R.id.tvUserAdapterPhoneNumber);

        ivUserAdapterPfp.setImageURI(user.getImgUri(context));
        tvUserAdapterFullName.setText(user.getFullNameAdmin());
        tvUserAdapterAge.setText(user.getAge().toString());
        tvUserAdapterEmail.setText(user.getUserEmail());
        tvUserAdapterCity.setText(user.getHomeCityName());
        tvUserAdapterWeight.setText(Double.toString(user.getUserWeight()));
        tvUserAdapterPhoneNumber.setText(user.getUserPhoneNumber());

        return userView;
    }
}
