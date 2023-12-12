package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.REGISTER_ACTIVITY_RETURN_DATA_KEY;
import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_KEY;
import static com.example.finalproject.Classes.InitiateFunctions.initUserSharedPreferences;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.finalproject.Adapters.UserAdapter;
import com.example.finalproject.Classes.MyPair;
import com.example.finalproject.Classes.User;
import com.example.finalproject.Classes.UserValidations;
import com.example.finalproject.Classes.ValidationData;
import com.example.finalproject.DatabaseClasses.MyDatabase;
import com.example.finalproject.R;

import java.util.ArrayList;
import java.util.List;

public class UsersListViewActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private Button btnSortByFirstName, btnSortByLastName,btnSearchUser;
    private EditText etSearchUser;
    private LinearLayout llSearchUser, llSortUser;
    private ListView lvUsers;
    private List<User> usersList;
    private UserAdapter userAdapter;
    private SharedPreferences sharedPreferences;
    private boolean isUserSignedIn;
    private SharedPreferences.Editor editor;
    private User curUser;
    private MyDatabase myDatabase;
    private int registerActivityResult;

    private boolean isSortedByFirstName, isSortedByLastName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list_view);

        btnSortByFirstName = findViewById(R.id.btnSortByFirstName);
        btnSortByLastName = findViewById(R.id.btnSortByLastName);
        btnSearchUser = findViewById(R.id.btnSearchUser);
        etSearchUser = findViewById(R.id.etSearchUser);
        llSearchUser = findViewById(R.id.llSearchUser);
        llSortUser = findViewById(R.id.llSortUser);
        lvUsers = findViewById(R.id.lvUsers);

        btnSortByFirstName.setOnClickListener(this);
        btnSortByLastName.setOnClickListener(this);
        btnSearchUser.setOnClickListener(this);

        btnSortByFirstName.setBackgroundColor(Color.MAGENTA);
        btnSortByLastName.setBackgroundColor(Color.MAGENTA);

        isSortedByFirstName = isSortedByLastName = false;


        myDatabase = MyDatabase.getInstance(this);
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        editor = sharedPreferences.edit();

        //checking if the user is saved in the SP and initializing vars if it is.
        MyPair<ValidationData, User> validationPair = initUserSharedPreferences(sharedPreferences, myDatabase);
        isUserSignedIn = validationPair.getFirst().isValid();
        if(!isUserSignedIn) Log.v("SignIn", validationPair.getFirst().getError());
        else curUser = validationPair.getSecond();

        userListSorter();
        userAdapter = new UserAdapter(this, usersList);
        lvUsers.setAdapter(userAdapter);
        lvUsers.setOnItemClickListener(this);
        lvUsers.setOnItemLongClickListener(this);

        if(!curUser.isAdmin()){
            llSearchUser.setVisibility(View.GONE);
            llSortUser.setVisibility(View.GONE);
        }
        else{
            llSearchUser.setVisibility(View.VISIBLE);
            llSortUser.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onBackPressed() {
        finishActivity(registerActivityResult);
        super.onBackPressed();
    }

    public void finishActivity(int result){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(REGISTER_ACTIVITY_RETURN_DATA_KEY, result);
        if(result == Activity.RESULT_OK) setResult(Activity.RESULT_OK, returnIntent);
        else setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void userListSorter(){
        if(curUser.isAdmin()) {
            ValidationData validateFullName = UserValidations.validateFullName(etSearchUser.getText().toString());
            if (!validateFullName.isValid()) etSearchUser.setError(validateFullName.getError());
            else {
                usersList = myDatabase.userDAO().getUsersContainingAndSorted(etSearchUser.getText().toString(), isSortedByFirstName ? 1 : 0, isSortedByLastName ? 1 : 0);
                lvUsers.invalidateViews();
            }
        }
        else {
            usersList = new ArrayList<>();
            usersList.add(curUser);
        }
    }


    @Override
    public void onClick(View v) {
        if(v==btnSortByFirstName){
            isSortedByFirstName = !isSortedByFirstName;
            if (isSortedByFirstName)
                btnSortByFirstName.setBackgroundColor(Color.rgb(128, 128, 128));
            else btnSortByFirstName.setBackgroundColor(Color.MAGENTA);

        } else if (v==btnSortByLastName) {
            isSortedByLastName = !isSortedByLastName;
            if (isSortedByLastName)
                btnSortByLastName.setBackgroundColor(Color.rgb(128, 128, 128));
            else btnSortByLastName.setBackgroundColor(Color.MAGENTA);
            userListSorter();

        } else if (v==btnSearchUser) {
            userListSorter();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if((!usersList.isEmpty()) && usersList.get(position).getId() == curUser.getId()){
            Intent intent = new Intent(this, RegisterActivity.class);
            activityResultLauncher.launch(intent);
        }
        else Toast.makeText(this, "You can only edit yourself!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return false;
    }

    private ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    registerActivityResult = result.getResultCode();
                }
            }
    );
}