package com.example.finalproject.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.Classes.InitiateFunctions;
import com.example.finalproject.Classes.User;
import com.example.finalproject.DatabaseClasses.MyDatabase;
import com.example.finalproject.R;

public class UserAdapter extends BaseAdapter {

    private Context context;
    private User[] usersArray;
    private MyDatabase myDatabase;

    public UserAdapter(Context context) {
        this.context = context;
        this.myDatabase = MyDatabase.getInstance(context);;
        this.usersArray = myDatabase.userDAO().getAllUsers().toArray(new User[0]);
    }

    @Override
    public int getCount() {
        return usersArray.length;
    }

    @Override
    public Object getItem(int position) {
        return usersArray[position];
    }

    @Override
    public long getItemId(int position) {
        return usersArray[position].getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View userView = inflater.inflate(R.layout.user_adapter, parent, false);
        User user = usersArray[position];

        ImageView ivUserAdapterPfp = userView.findViewById(R.id.ivUserAdapterPfp);
        TextView tvUserAdapterFullName = userView.findViewById(R.id.tvUserAdapterFullName);
        TextView tvUserAdapterAge = userView.findViewById(R.id.tvUserAdapterAge);
        TextView tvUserAdapterEmail = userView.findViewById(R.id.tvUserAdapterEmail);
        TextView tvUserAdapterCity = userView.findViewById(R.id.tvUserAdapterCity);
        TextView tvUserAdapterWeight = userView.findViewById(R.id.tvUserAdapterWeight);
        TextView tvUserAdapterPhoneNumber = userView.findViewById(R.id.tvUserAdapterPhoneNumber);

        ivUserAdapterPfp.setImageBitmap(user.getImgBitmap());
        tvUserAdapterFullName.setText(user.getFullNameAdmin());
        tvUserAdapterAge.setText(user.getAge().toString());
        tvUserAdapterEmail.setText(user.getEmail());
        tvUserAdapterCity.setText(myDatabase.cityDAO().getCityById(user.getHomeCityId()).getCityName());
        tvUserAdapterWeight.setText(Double.toString(user.getWeight()));
        tvUserAdapterPhoneNumber.setText(user.getPhoneNumber());

        return userView;
    }
}
