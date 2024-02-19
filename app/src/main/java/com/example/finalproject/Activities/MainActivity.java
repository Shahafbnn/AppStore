package com.example.finalproject.Activities;

import static android.view.Gravity.CENTER;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_APP_KEY;
import static com.example.finalproject.Classes.Constants.INTENT_CURRENT_USER_KEY;
import static com.example.finalproject.Classes.Constants.SHARED_PREFERENCES_KEY;
import static com.example.finalproject.Classes.InitiateFunctions.*;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.finalproject.Adapters.AppAdapter;
import com.example.finalproject.Classes.*;
import com.example.finalproject.Classes.App.App;
import com.example.finalproject.Classes.App.AppView;
import com.example.finalproject.Classes.Category.Categories;
import com.example.finalproject.Classes.User.User;
import com.example.finalproject.Classes.User.Validations;
import com.example.finalproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.*;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MenuItem itemLogIn,itemRegister, itemLogOut, itemDataUpdate, itemAboutMe, itemAppSettings;
    private TextView tvWelcome;
    private ImageView ivProfilePic;
    private RadioButton rbMainActivity, rbCategoryActivity;
    private SharedPreferences sharedPreferences;
    private Boolean isUserSignedIn;
    private SharedPreferences.Editor editor;
    private User curUser;
    private FirebaseFirestore db;
    private DocumentReference userDocRef;
    //private EventListener<DocumentSnapshot> snapshotListener;
    private boolean reloadActivityInMenuOptionsPrepare;
    private Boolean isMenuPrepared;
    private boolean reloadActivity;
    private ArrayList<App> createdAppsArrayList;
    private OnCompleteListener<DocumentSnapshot> userGetOnCompleteListener;

    // This ActivityResultLauncher is used to handle the result from the RegisterActivity.
    // It retrieves the User object from the returned Intent and updates the current user and sign-in status.
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode()== Activity.RESULT_OK){
                        if(result.getData() != null){
                            //get the returned app from UploadAppActivity
                            App app = (App)result.getData().getSerializableExtra(INTENT_CURRENT_APP_KEY);
                            if(app!=null){
                                boolean found = false;
                                for(int i = 0; i < createdAppsArrayList.size() && !found; i++){
                                    if(createdAppsArrayList.get(i).getAppId().equals(app.getAppId())) {
                                        createdAppsArrayList.set(i, app);
                                        found = true;
                                    }
                                }
                                if(!found){
                                    createdAppsArrayList.add(app);
                                }
                                //if we updated the app then we won't need to update the user (since we don't change the user there)
                            }else{
                                User user = (User)result.getData().getSerializableExtra(INTENT_CURRENT_USER_KEY);
                                if(user != null){
                                    curUser = user;
                                    isUserSignedIn = true;
                                    InitiateFunctions.setSharedPreferencesData(editor, user.getUserId());
                                }
                                else{
                                    //if the returned user is null (meaning you deleted yourself MainActivity->UsersListViewActivity->RegisterActivity).
                                    isUserSignedIn = false;
                                    curUser = null;
                                }
                                //this is if we update an app in UploadAppActivity
                            }





                            //reload activity if isMenuPrepared is true;
                            // needs to be conditional
                            // because what if there are delays on the onCreate get fetch?
                            conditionalReloadActivity();
                        }
                    }
                }
            }
    );

    private Categories categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //wipeDataOffFireStore();

        // Initialize views and Firestore instance
        rbMainActivity = findViewById(R.id.rbMainActivity);
        rbMainActivity.setOnClickListener(this);
        rbCategoryActivity = findViewById(R.id.rbCategoryActivity);
        rbCategoryActivity.setOnClickListener(this);
        
        tvWelcome = findViewById(R.id.tvWelcome);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        db = FirebaseFirestore.getInstance();

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREFERENCES_KEY, 0);
        editor = sharedPreferences.edit();
        //Log.v("debug", "sharedPreferences data at onCreate: " + sharedPreferences.getAll());

        // Check if the user is already signed in
        String id = sharedPreferences.getString(Constants.USER_ID_KEY, "-1");
        if(id.equals("-1")){
            isUserSignedIn = false;
            setConditionalReloadActivityData();
        }
        else {
            // Fetch the user data from Firestore
            db.collection("users").document(id).get().addOnCompleteListener((@NonNull Task<DocumentSnapshot> task) -> {
                //Log.v("debug", "user get at onCreate: " + task.getResult().toString() + ", is successful: " + task.isSuccessful());
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        User user = documentSnapshot.toObject(User.class);
                        if (user != null) {
                            //set the user data
                            curUser = user;
                            curUser.setUserId(id);
                            isUserSignedIn = true;
                            InitiateFunctions.setSharedPreferencesData(editor ,id);
                            //Log.v("debug", "user get at onCreate after 2nd check: " + curUser.toString());

                        } else {
                            isUserSignedIn = false;
                        }
                    } else {
                        isUserSignedIn = false;
                        Log.d("debug", "initUserSharedPreferences get failed with ", task.getException());
                    }
                    // Reload the activity based on the new data
                    conditionalReloadActivity();
                });
        }

        // Reload the activity if the menu is already prepared
        if(isMenuPrepared != null){
            if(reloadActivity) reloadActivity();
        }
        else reloadActivityInMenuOptionsPrepare = true;
// to fix the data

    } //onCreate's end

    private void wipeDataOffFireStore(){
        deleteAllExceptCategories("apps");
        deleteAllUserCreatedApps();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_sign_in_menu, menu);

        itemRegister = menu.findItem(R.id.itemRegister);
        itemLogIn = menu.findItem(R.id.itemLogIn);
        itemLogOut = menu.findItem(R.id.itemLogOut);
        itemDataUpdate = menu.findItem(R.id.itemDataUpdate);
        itemAboutMe = menu.findItem(R.id.itemAboutMe);
        itemAppSettings = menu.findItem(R.id.itemAppSettings);

        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        //if the user is logged in the Register and Log In options disappear and a Log Out option appears.
        if(isMenuPrepared == null){
            itemRegister = menu.findItem(R.id.itemRegister);
            itemLogIn = menu.findItem(R.id.itemLogIn);
            itemLogOut = menu.findItem(R.id.itemLogOut);
            itemDataUpdate = menu.findItem(R.id.itemDataUpdate);
            itemAppSettings = menu.findItem(R.id.itemAppSettings);

            itemRegister.setVisible(false);
            itemLogIn.setVisible(false);
            itemLogOut.setVisible(false);
            itemDataUpdate.setVisible(false);
            if(reloadActivityInMenuOptionsPrepare) {
                reloadActivity();
                reloadActivityInMenuOptionsPrepare = false;
            }
            isMenuPrepared = true;
        }
        changeOptionMenuItemsVisibility(isUserSignedIn);
        return true;
    }

    private void changeOptionMenuItemsVisibility(Boolean isUserSignedIn){
        if(isUserSignedIn == null){
            itemRegister.setVisible(false);
            itemLogIn.setVisible(false);
            itemLogOut.setVisible(false);
            itemDataUpdate.setVisible(false);
            itemAppSettings.setVisible(false);

        }
        else if(isUserSignedIn)
        {
            itemRegister.setVisible(false);
            itemLogIn.setVisible(false);
            itemLogOut.setVisible(true);
            itemDataUpdate.setVisible(true);
            itemAppSettings.setVisible(true);
        }
        else
        {
            itemRegister.setVisible(true);
            itemLogIn.setVisible(true);
            itemLogOut.setVisible(false);
            itemDataUpdate.setVisible(false);
            itemAppSettings.setVisible(false);

        }
        invalidateOptionsMenu();
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item==itemLogIn){
            createCustomDialogLogIn();
            return true;
        }
        else if(item==itemRegister){
            Intent intent = new Intent(this, RegisterActivity.class);
            intent.putExtra(Constants.INTENT_CURRENT_USER_KEY, curUser);
            activityResultLauncher.launch(intent);
            return true;
        }
        else if(item==itemLogOut){
            editor.clear();
            editor.commit();
            isUserSignedIn = false;
            curUser = null;
            reloadActivity();
            invalidateViews(tvWelcome, ivProfilePic);
            invalidateOptionsMenu();
            return true;
        }
        else if(item==itemDataUpdate){
            Intent intent = new Intent(this, UsersListViewActivity.class);
            intent.putExtra(Constants.INTENT_CURRENT_USER_KEY, curUser);
            activityResultLauncher.launch(intent);
            return true;
        }
        else if(item==itemAboutMe){
            createAlertDialog();
            return true;
        }
        else if(item==itemAppSettings){
            createAppSettingsDialog();
            return true;
        }
        else return super.onOptionsItemSelected(item);
    }
    /**
     * This method is used to reload the activity.
     * It calls the changeOptionMenuItemsVisibility method to update the visibility of menu items, and the initViewsFromUser method to update the views with the user data.
     */
    private void reloadActivity(){
        changeOptionMenuItemsVisibility(isUserSignedIn);
        initViewsFromUser(curUser, isUserSignedIn, this, db, tvWelcome, ivProfilePic);
    }
    /**
     * This method is used to conditionally reload the activity based on the user's sign-in status.
     * It checks if the menu is prepared and if a reload is required, and then calls the reloadActivity method.
     */
    private void conditionalReloadActivity(){
        if(isMenuPrepared != null) reloadActivity();
        else reloadActivityInMenuOptionsPrepare = true;
    }
    private void setConditionalReloadActivityData(){
        if(isMenuPrepared != null) reloadActivity = true;
        else reloadActivityInMenuOptionsPrepare = true;
    }
    private void createAlertDialog(){
        class AlertDialogClick implements DialogInterface.OnClickListener {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == dialog.BUTTON_POSITIVE) {
                    dialog.dismiss();
                }
            }
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("About: ");
        String userName = "Guest";
        String aboutTheApp = "About my app store -\n" +
                "This application will be a digital app store for downloading apps on the phone.\n" +
                "Options:\n" +
                "1. The Log In options which are: Log in, Register, Log out, Update data, user deletion, and the about page! \n" +
                "2. Search apps by name\n" +
                "3. Search for apps using voice search\n" +
                "4. Search history\n" +
                "5. A category page that includes: news, games, social networks, entertainment, work and more.\n" +
                "6. Uploading apps to the store.\n" +
                "7. Installing apps from the store.\n" +
                "8. Sharing apps to another app such as sharing a message to WhatsApp.\n" +
                "9. Displaying application information to users including: application name, application creator, amount of downloads, application size, application phone permissions such as camera permissions.\n" +
                "10. The app creator's page where all the apps they uploaded are displayed.\n" +
                "11. Average rating of a creator's apps.\n" +
                "12 Public app rating shown to all users on the app's download page.\n" +
                "13. Public comments about the app from users who have installed it.\n" +
                "14. The rating of the responses by the users.\n" +
                "15. A page of \"more apps\" in the same category as the app you selected.\n" +
                "16. App settings which include: changing the color of the background and buttons and more.\n" +
                "17. Recently added apps page.\n" +
                "18. Popular apps page with the highest rating and number of installation apps.\n" +
                "19. Suggested apps page based on your search history and app rating.";
        if(isUserSignedIn) userName = curUser.getFullNameAdmin();
        builder.setMessage("Hello " + userName + "!\nI am a computer science student, I live in Israel, and for this project I chose to make an app store!\n" +
                aboutTheApp);
        builder.setCancelable(true);
        builder.setPositiveButton("Cancel", new AlertDialogClick());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void createAppSettingsDialog(){
        if(createdAppsArrayList == null){
            db.collection("users").document(curUser.getUserId()).collection(Constants.FIRESTORE_USER_CREATED_APPS_KEY).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        createdAppsArrayList = new ArrayList<>();

                        App app;
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            app = documentSnapshot.toObject(App.class);
                            app.setAppId(documentSnapshot.getId());
                            createdAppsArrayList.add(app);
                        }

                        setAdapterAndShowDialog();
                    }
                }
            });
        }
        else setAdapterAndShowDialog();
    }
    private void setAdapterAndShowDialog(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.app_settings_dialog);


        Button btnCreateNewApp = dialog.findViewById(R.id.btnCreateNewApp);
        // what happens when you click the create new app button:
        btnCreateNewApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadAppActivity.class);
                dialog.dismiss();
                conditionalLaunchUploadActivity(intent);

            }
        });
        View.OnClickListener listener = new View.OnClickListener() {
            // what happens when you click an app in the dialog:
            @Override
            public void onClick(View v) {
                App app = ((AppView)v).getApp();

                Intent intent = new Intent(getApplicationContext(), UploadAppActivity.class);
                intent.putExtra(Constants.INTENT_CURRENT_APP_KEY, app);
                dialog.dismiss();
                conditionalLaunchUploadActivity(intent);
            }
        };

        RecyclerView rv = dialog.findViewById(R.id.rvCreatedApps);
        AppAdapter adapter = new AppAdapter(getApplicationContext(), createdAppsArrayList, listener, curUser);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
//        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.WRAP_CONTENT);
//
//        linearLayoutParams.gravity = CENTER;
//        rv.setLayoutParams(linearLayoutParams);

        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        dialog.show();
    }
    private void conditionalLaunchUploadActivity(Intent intent){
        if(categories==null){
            fetchCategoriesAndLaunchActivity("UploadAppActivity", intent);
        }
        else {
            launchUploadAppActivity(intent);
        }
    }
    private void createCustomDialogLogIn(){
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.log_in_dialog);

        Button btnLogInSubmit = dialog.findViewById(R.id.btnLogInSubmit);
        EditText etEmailAddress = dialog.findViewById(R.id.etEmailAddress);
        EditText etTextPassword = dialog.findViewById(R.id.etTextPassword);

        btnLogInSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnLogInSubmit.setClickable(false);
                if(validateCustomDialogLogInData(dialog)) {

                    db.collection("users")
                            .whereEqualTo("userEmail", etEmailAddress.getText().toString())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    if (task.isSuccessful()) {
                                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                            if(documentSnapshot.exists()){
                                                User user = documentSnapshot.toObject(User.class);
                                                if(user.getUserPassword().equals(etTextPassword.getText().toString())){
                                                    isUserSignedIn = true;
                                                    curUser = user;
                                                    curUser.setUserId(documentSnapshot.getId());

                                                    InitiateFunctions.setSharedPreferencesData(editor ,documentSnapshot.getId());

                                                    reloadActivity();
                                                    //Toast.makeText(getApplicationContext(), "Logging in!", Toast.LENGTH_LONG).show();
                                                    dialog.cancel();
                                                }
                                                else {
                                                    etTextPassword.setError("Incorrect password!");
                                                    btnLogInSubmit.setClickable(true);
                                                }

                                            }
                                            else{
                                                etEmailAddress.setError("User does not exist!");
                                                btnLogInSubmit.setClickable(true);
                                            }
                                        }
                                    } else {
                                        Log.w("user", "Task unsuccessful", task.getException());
                                        btnLogInSubmit.setClickable(true);
                                    }
                                }
                            });
                }
            }
        });
        dialog.show();
    }
    private static boolean validateCustomDialogLogInData(Dialog dialog){
        EditText etEmailAddress = dialog.findViewById(R.id.etEmailAddress);
        EditText etTextPassword = dialog.findViewById(R.id.etTextPassword);
        //Button btnLogInSubmit = dialog.findViewById(R.id.btnLogInSubmit);
        String emailAddress = etEmailAddress.getText().toString();
        String textPassword = etTextPassword.getText().toString();

        //validates if the fields correspond to the rules.
        ValidationData emailValidation = Validations.validateEmail(emailAddress);
        ValidationData passwordValidation = Validations.validatePassword(textPassword);

        //changes the ET error based on the validation.
        if(!emailValidation.isValid()) etEmailAddress.setError(emailValidation.getError());
        if(!passwordValidation.isValid()) etTextPassword.setError(passwordValidation.getError());

        return emailValidation.isValid() && passwordValidation.isValid();
    }
    private void fetchCategoriesAndLaunchActivity(String activity, Intent intent){
        db.collection("apps").document("categories").get().addOnCompleteListener(MainActivity.this, new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    //In Firestore, when you try to get a document that doesn't exist, no specific exception is thrown.
                    // Instead, the DocumentSnapshot object returned by the get() method will have its exists property set to false
                    if(!documentSnapshot.exists()){
                        ArrayList<String> arr = new ArrayList<>();
                        arr.add("Fitness");
                        arr.add("Gaming");
                        arr.add("Work");
                        arr.add("Food");
                        Categories cat = new Categories(arr);
                        db.collection("apps").document("categories").set(cat);
                        categories = cat;
                    }
                    else categories = documentSnapshot.toObject(Categories.class);

                    launchActivity(activity, intent);
                }
            }
        });
    }

    private void deleteAllUserCreatedApps(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String userId = document.getId();
                    db.collection("users").document(userId).collection("createdApps")
                            .get()
                            .addOnCompleteListener(subTask -> {
                                if (subTask.isSuccessful()) {
                                    WriteBatch batch = db.batch();
                                    for (QueryDocumentSnapshot subDocument : subTask.getResult()) {
                                        batch.delete(subDocument.getReference());
                                    }
                                    batch.commit();
                                }
                            });
                }
            }
        });
    }

    private void deleteAllDocuments(String collectionPath) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionPath)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        WriteBatch batch = db.batch();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            batch.delete(documentSnapshot.getReference());
                        }
                        batch.commit();
                    }
                });
    }
    private void deleteAllExceptCategories(String collectionPath) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionPath)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        WriteBatch batch = db.batch();
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            if (!documentSnapshot.getId().equals("categories")) {
                                batch.delete(documentSnapshot.getReference());
                            }
                        }
                        batch.commit();
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v==rbCategoryActivity) {
            Intent intent = new Intent(getApplicationContext(), CategoryActivity.class);
            if(categories==null){
                fetchCategoriesAndLaunchActivity("CategoryActivity", intent);
            }
            else launchCategoryActivity(intent);

        }
    }
    private void launchCategoryActivity(Intent intent){
        intent.putExtra(Constants.INTENT_CATEGORIES_KEY, categories);
        intent.putExtra(INTENT_CURRENT_USER_KEY, curUser);
        startActivity(intent);
    }
    private void launchUploadAppActivity(Intent intent){
        intent.putExtra(Constants.INTENT_CATEGORIES_KEY, categories);
        intent.putExtra(Constants.INTENT_CURRENT_USER_KEY, curUser);
        activityResultLauncher.launch(intent);
    }
    private void launchActivity(String activity, Intent intent){
        if(activity.equals("CategoryActivity")) launchCategoryActivity(intent);
        else if(activity.equals("UploadAppActivity")) launchUploadAppActivity(intent);
    }



}