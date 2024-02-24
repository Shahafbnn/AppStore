package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_USER_KEY;
import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_KEY;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.example.finalproject.Classes.Constants;
import com.example.finalproject.Classes.InitiateFunctions;
import com.example.finalproject.Classes.User.User;
import com.example.finalproject.R;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class UsersListViewActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    private Button btnSortByFirstName, btnSortByLastName,btnSearchUser;
    private EditText etSearchUser;
    private LinearLayout llSearchUser, llSortUser;
    private ListView lvUsers;
    private List<User> originalUsersList;
    private List<User> usersList;
    private UserAdapter userAdapter;
    private SharedPreferences sharedPreferences;
    private boolean isUserSignedIn;
    private SharedPreferences.Editor editor;
    private User curUser;
    private FirebaseFirestore db;
    private ProgressDialog waitProgressDialog;
    private Boolean isCurUserAdmin;
    private FirebaseStorage storage;
    private Boolean isDataChanged;
    private boolean isSortedByFirstName, isSortedByLastName;
    // This ActivityResultLauncher is used to handle the result from the RegisterActivity.
    // It retrieves the User object from the returned Intent and updates the current user and sign-in status.
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        if(result.getData() != null){
                            User user = (User) result.getData().getSerializableExtra(INTENT_CURRENT_USER_KEY);
                            if(user != null){ //can never be null, checking anyways.

                                int position = getListIndexOf(usersList, curUser);
                                //Log.v("debug", "the pos: " + position);
                                curUser = user;
                                isUserSignedIn = true;
                                //if a user was sent back we can assume it was changed
                                //but since we passed it through the intent the curUser pointer changed.
                                //isCurUserAdmin shouldn't be null.
                                //if one of them is true and the other is false, meaning he wasn't admin and now he is.
                                if(user.isUserIsAdmin() && !isCurUserAdmin){
                                    fetchUsersListDataFromFirestore();
                                    isCurUserAdmin = true;
                                }
                                // was admin and is no longed admin.
                                else if(!user.isUserIsAdmin() && isCurUserAdmin){
                                    usersList.clear();
                                    usersList.add(curUser);
                                    isCurUserAdmin = false;
                                    originalUsersList = new ArrayList<>(usersList);
                                }
                                // if he was changed no matter if he was an admin or not.
                                else {
                                    usersList.set(position, curUser);
                                    originalUsersList.set(position, curUser);
                                }
                                userAdapter.notifyDataSetChanged();

                                isDataChanged = true;
                            }
                        }
                    }
                }
            }
    );
    private int getListIndexOf(List<User> list, User user){
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if(user.getUserId().equals(list.get(i).getUserId())) return i;
        }
        return -1;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list_view);

        storage = FirebaseStorage.getInstance();

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


        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        editor = sharedPreferences.edit();

        //getting the user from the intent
        curUser = (User) getIntent().getSerializableExtra(INTENT_CURRENT_USER_KEY);
        isUserSignedIn = curUser != null;
        if(isUserSignedIn) isCurUserAdmin = curUser.isUserIsAdmin();


        usersList = new ArrayList<>();
        userAdapter = new UserAdapter(this, usersList);

        lvUsers.setAdapter(userAdapter);
        lvUsers.setOnItemClickListener(this);
        lvUsers.setOnItemLongClickListener(this);

        if(curUser.isUserIsAdmin()){
            llSearchUser.setVisibility(View.VISIBLE);
            llSortUser.setVisibility(View.VISIBLE);
        }
        else{
            llSearchUser.setVisibility(View.GONE);
            llSortUser.setVisibility(View.GONE);
        }


        isDataChanged = false;


        //getting the list data
        if(curUser.isUserIsAdmin()){
            fetchUsersListDataFromFirestore();
        }
        else{
            usersList.add(curUser);
            userAdapter.notifyDataSetChanged();
            originalUsersList = new ArrayList<>(usersList);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Back is pressed... Finishing the activity
                finishActivity(isDataChanged != null && isDataChanged, curUser);
            }
        });
    }

    private void createLoadingScreen(){
        waitProgressDialog = new ProgressDialog(this);
        waitProgressDialog.setMax(100);
        waitProgressDialog.setMessage("Its loading....");
        waitProgressDialog.setTitle("ProgressDialog bar example");
        waitProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        waitProgressDialog.show();
    }
    private void finishActivity(boolean result, User user){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(Constants.INTENT_CURRENT_USER_KEY, user);
        if(result) setResult(Activity.RESULT_OK, returnIntent);
        else setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }
    private void userListSorter(){
        usersList.clear();
        usersList.addAll(getListContainingAndSorted(originalUsersList, etSearchUser.getText().toString(), isSortedByFirstName, isSortedByLastName));
        userAdapter.notifyDataSetChanged();
    }
    private void fetchUsersListDataFromFirestore(){
        Toast.makeText(this, "Getting data, please wait!", Toast.LENGTH_LONG).show();
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if(!usersList.isEmpty()) usersList.clear();
                User user;
                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                    user = documentSnapshot.toObject(User.class);
                    user.setUserId(documentSnapshot.getId());
                    usersList.add(user);
                }
                userAdapter.notifyDataSetChanged();
                originalUsersList = new ArrayList<>(usersList);
            } else {
                Log.d("debug", "Error getting documents: ", task.getException());
            }
        });
    }
    @Override
    public void onClick(View v) {
        if(v==btnSortByFirstName){
            isSortedByFirstName = !isSortedByFirstName;
            if (isSortedByFirstName)
                btnSortByFirstName.setBackgroundColor(Color.rgb(128, 128, 128));
            else btnSortByFirstName.setBackgroundColor(Color.MAGENTA);
            userListSorter();

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
        if((!usersList.isEmpty()) && usersList.get(position).getUserId().equals(curUser.getUserId())){
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra(Constants.INTENT_CURRENT_USER_KEY, curUser);
            activityResultLauncher.launch(intent);
        }
        else Toast.makeText(this, "You can only edit yourself!", Toast.LENGTH_LONG).show();
    }
    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        createDeleteAlertDialog(usersList.get(position));
        return true;
    }
    public void disableUser(User delUser){
        if(delUser.isUserIsAdmin()){
            Toast.makeText(this, "You can't disable an admin!", Toast.LENGTH_LONG).show();
            return;
        }
        if(delUser.getUserId().equals(curUser.getUserId())){
            editor.clear();
            editor.commit();
            curUser = null;
            isUserSignedIn = false;
            InitiateFunctions.disableUserFromFirestore(delUser);
            Toast.makeText(this, "You disabled yourself!", Toast.LENGTH_LONG).show();
            finishActivity(true, null);
        }
        InitiateFunctions.disableUserFromFirestore(delUser);
        removeUserFromUsersList(delUser);
        lvUsers.invalidateViews();
        isDataChanged = true;
    }

    private List<User> getListContainingAndSorted(List<User> list, String containing, boolean isSortedByFirstName, boolean isSortedByLastName){
        ArrayList<User> sorted;
        if(containing.isEmpty()){
            sorted = new ArrayList<>(list);
        }
        else{
            sorted = new ArrayList<>();
            User user;
            int size = list.size();
            for (int i = 0; i < size; i++) {
                user = list.get(i);
                if (user.getFullNameAdmin().contains(containing)) sorted.add(user);
            }
        }

        if(isSortedByLastName && isSortedByFirstName){
            sorted.sort(Comparator.comparing(User::getUserFirstName).thenComparing(User::getUserLastName));
        }
        else if(isSortedByFirstName){
            sorted.sort(Comparator.comparing(User::getUserFirstName));
        }
        else if(isSortedByLastName){
            sorted.sort(Comparator.comparing(User::getUserLastName));
        }

        return sorted;
    }

    private void removeUserFromUsersList(User delUser){
        usersList.remove(delUser);
        originalUsersList.remove(delUser);
    }
    public void createDeleteAlertDialog(User delUser){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Disable User?");
        builder.setMessage("Are you sure?");
        builder.setCancelable(true);
        builder.setPositiveButton("Disable", new AlertDialogClick(delUser, this));
        builder.setNegativeButton("Cancel", new AlertDialogClick(delUser, this));
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GREEN);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
    }
    private class AlertDialogClick implements DialogInterface.OnClickListener {
        private User delUser;
        private Context context;
        public AlertDialogClick(User delUser, Context context){
            this.delUser = delUser;
            this.context = context;
        }
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (which == dialog.BUTTON_POSITIVE) {
                dialog.dismiss();
                disableUser(delUser);
            }

            if (which == dialog.BUTTON_NEGATIVE) {
                Toast.makeText(context, "Canceled", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }
    }
}