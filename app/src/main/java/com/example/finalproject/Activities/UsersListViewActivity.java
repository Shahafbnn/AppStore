package com.example.finalproject.Activities;

import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_USER_KEY;
import static com.example.finalproject.Classes.Constants.REGISTER_ACTIVITY_RETURN_DATA_KEY;
import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_KEY;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
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
import com.example.finalproject.Classes.User;
import com.example.finalproject.Classes.UserValidations;
import com.example.finalproject.Classes.ValidationData;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private FirebaseFirestore db;
    private ProgressDialog waitProgressDialog;
    private CollectionReference collectionReference;
    private Query query;

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


        db = FirebaseFirestore.getInstance();
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        editor = sharedPreferences.edit();
        collectionReference = db.collection("users");

        //getting the user from the intent
        curUser = (User) getIntent().getSerializableExtra(INTENT_CURRENT_USER_KEY);
        isUserSignedIn = curUser != null;


        usersList = new ArrayList<>();
        userAdapter = new UserAdapter(this, usersList);

        if(curUser.isUserIsAdmin()){
        }else{
            usersList.add(curUser);
            db.collection("users").document(curUser.getUserId());
        }
        createLoadingScreen();
        class ProgressThread extends Thread{
            private boolean isRunning;
            private boolean isListReceived;
            private ProgressDialog waitProgressDialog;

            public ProgressThread(ProgressDialog waitProgressDialog) {
                this.isRunning = true;
                isListReceived = false;
                this.waitProgressDialog = waitProgressDialog;
            }

            @Override
            public void run() {
                super.run();
                long sleepTime = 1000;
                while(isRunning && waitProgressDialog.getProgress() >= waitProgressDialog.getMax()){
                    if(isListReceived){
                        sleepTime = 10;
                    }

                    if(waitProgressDialog.getProgress() < 90 || isListReceived){
                        waitProgressDialog.incrementProgressBy(1);
                    }

                    try {
                        sleep(sleepTime);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                waitProgressDialog.dismiss();
                interrupt();

            }

            public boolean isRunning() {
                return isRunning;
            }

            public void setRunning(boolean running) {
                isRunning = running;
            }

            public boolean isListReceived() {
                return isListReceived;
            }

            public void setListReceived(boolean listReceived) {
                isListReceived = listReceived;
            }
        }


        Handler progressThreadHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {
                return false;
            }
        });

        ProgressThread progressThread = new ProgressThread(waitProgressDialog);
        progressThread.start();


        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    User temp;
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        temp = documentSnapshot.toObject(User.class);
                        temp.setUserId(documentSnapshot.getId());
                        usersList.add(temp);
                    }
                    progressThread.setListReceived(true);
                } else {
                    Log.d("debug", "Error getting documents: ", task.getException());
                }
            }
        });



        userListSorter();
        lvUsers.setAdapter(userAdapter);
        lvUsers.setOnItemClickListener(this);
        lvUsers.setOnItemLongClickListener(this);

        if(!curUser.isUserIsAdmin()){
            llSearchUser.setVisibility(View.GONE);
            llSortUser.setVisibility(View.GONE);
        }
        else{
            llSearchUser.setVisibility(View.VISIBLE);
            llSortUser.setVisibility(View.VISIBLE);
        }

        // The callback can be enabled or disabled here or in handleOnBackPressed()
    }

    private void createLoadingScreen(){
        waitProgressDialog = new ProgressDialog(this);
        waitProgressDialog.setMax(100);
        waitProgressDialog.setMessage("Its loading....");
        waitProgressDialog.setTitle("ProgressDialog bar example");
        waitProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        waitProgressDialog.show();
    }

    private void updateList(){

    }

    public void finishActivity(){
        //setReturnIntents(result);
        finish();
    }
    public void setReturnIntents(int result){
        Intent returnIntent = new Intent();
        returnIntent.putExtra(REGISTER_ACTIVITY_RETURN_DATA_KEY, result);
        if(result == Activity.RESULT_OK) setResult(Activity.RESULT_OK, returnIntent);
        else setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private List<User> getUsersContainingAndSorted(String search, boolean isSortedByFirstName, boolean isSortedByLastName){
        List<User> usersList = new ArrayList<>();

        Query query = db.collection("users");

        if (isSortedByFirstName) {
            query = query.orderBy("userFirstName", Query.Direction.ASCENDING);
        }

        // If isSortedByLastName is true, sort by last name
        if (isSortedByLastName) {
            query = query.orderBy("userLastName", Query.Direction.ASCENDING);
        }
        boolean[] finished = new boolean[]{false};
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = document.toObject(User.class);
                                if((search == null || search.isEmpty())) usersList.add(user);
                                else if(user.getFullNameAdmin().contains(search)) usersList.add(user);
                            }
                        } else {
                            Log.w("user", "Error getting documents.", task.getException());
                        }
                        finished[0] = true;
                    }
                });
        while(!finished[0]) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return usersList;
    }
    private void userListSorter(){
        if(curUser.isUserIsAdmin()) {
            ValidationData validateFullName = UserValidations.validateFullName(etSearchUser.getText().toString());
            if (!validateFullName.isValid()) etSearchUser.setError(validateFullName.getError());
            else {
                usersList.clear();
                usersList.addAll(getUsersContainingAndSorted(etSearchUser.getText().toString(), isSortedByFirstName, isSortedByLastName));
                userAdapter.notifyDataSetChanged();
            }
        }
        else {
            usersList.clear();
            db.collection("users")
                    .document(curUser.getUserId())  // replace 'curUser.getId()' with the ID of the user you want to retrieve
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            if (documentSnapshot.exists()) {
                                User user = documentSnapshot.toObject(User.class);
                                usersList.add(user);
                                // Now the 'user' object has been added to 'usersList'
                            } else {
                                Log.d("user", "No such document");
                            }
                        }
                    });
            userAdapter.notifyDataSetChanged();
        }
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
        if((!usersList.isEmpty()) && usersList.get(position).getUserId() == curUser.getUserId()){
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra(Constants.INTENT_CURRENT_USER_KEY, curUser);
            startActivity(intent);
        }
        else Toast.makeText(this, "You can only edit yourself!", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        createDeleteAlertDialog(usersList.get(position));
        return true;
    }

    public void deleteUser(User delUser){
        if(delUser.isUserIsAdmin()){
            Toast.makeText(this, "You can't delete an admin!", Toast.LENGTH_LONG).show();
            return;
        }
        if(delUser.getUserId().equals(curUser.getUserId())){
            editor.clear();
            editor.commit();
            curUser = null;
            isUserSignedIn = false;
            InitiateFunctions.deleteUserFromFirestore(delUser);
            Toast.makeText(this, "You deleted yourself!", Toast.LENGTH_LONG).show();
            finishActivity();
        }
        InitiateFunctions.deleteUserFromFirestore(delUser);
        lvUsers.invalidateViews();


    }
    public void createDeleteAlertDialog(User delUser){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete User");
        builder.setMessage("Are you sure?");
        builder.setCancelable(true);
        builder.setPositiveButton("Delete", new AlertDialogClick(delUser, this));
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
                deleteUser(delUser);
            }

            if (which == dialog.BUTTON_NEGATIVE) {
                Toast.makeText(context, "Canceled", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        }
    }
}