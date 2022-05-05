package com.gesall.mat_och_sov_klocka.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.gesall.mat_och_sov_klocka.R;
import com.gesall.mat_och_sov_klocka.fragments.CreateFragment;
import com.gesall.mat_och_sov_klocka.fragments.EditFragment;
import com.gesall.mat_och_sov_klocka.models.ListModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.RECEIVE_BOOT_COMPLETED;
import static android.Manifest.permission.SET_ALARM;
import static android.Manifest.permission.VIBRATE;
import static com.gesall.mat_och_sov_klocka.R.drawable.ic_baseline_add_24;
import static com.gesall.mat_och_sov_klocka.fragments.CreateFragment.CREATE;
import static com.gesall.mat_och_sov_klocka.fragments.EditFragment.EDIT;
import static com.gesall.mat_och_sov_klocka.fragments.HomeFragment.HOME;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MAIN ACTIVITY DEBUG";


    FloatingActionButton fab;

    private ListModel model;

    // request code when asking for permissions
    private static final int ALL_PERMISSIONS_RESULT = 2000;
    // arrays dealing with permissions
    private ArrayList<String> permissionsToRequest;
    private ArrayList<String> permissionsRejected = new ArrayList<>();
    private ArrayList<String> permissions = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fab = findViewById(R.id.fab);

        // deprecated
        //model = ViewModelProviders.of(this).get(ListModel.class);

        model = new ViewModelProvider(this).get(ListModel.class);

        // add all the permissions we are interested in to our permissions array
        permissions.add(VIBRATE);
        permissions.add(RECEIVE_BOOT_COMPLETED);
        permissions.add(SET_ALARM);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            permissions.add(FOREGROUND_SERVICE);
        }

        // find all denied permissions
        permissionsToRequest = findUnAskedPermissions(permissions);

        // if we are running above marshmallow
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // if we have permission, if permission is granted, if green means go etc
            if (permissionsToRequest.size() > 0) {

                findUnAskedPermissions(permissionsToRequest);

            } else {
                // else we don't have permission, so lets ask the user for it
                requestPermissions(permissionsToRequest.toArray(new String[0]), ALL_PERMISSIONS_RESULT);
            }
        }

        fab.setOnClickListener(view -> {
            int whichFragment = model.getCurrentFragment();

            switch (whichFragment) {
                case HOME:
                    // Toast.makeText(this, "We're in home fragment", Toast.LENGTH_LONG).show();
                    navigateFromHomeToCreate();

                    break;
                case CREATE:
                    Toast.makeText(this, "Timer created!", Toast.LENGTH_LONG).show();
                    saveNewItem();
                    break;
                case EDIT:
                    Toast.makeText(this, "Timer edited!", Toast.LENGTH_LONG).show();
                    saveEditedItem();
                    break;
            }

        });
    }


    private void saveEditedItem() {

        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);


        if (navHostFragment instanceof NavHostFragment) {
            Fragment visibleFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

            if (visibleFragment instanceof EditFragment) {

                ((EditFragment) visibleFragment).saveEditedItem();
            }
        }

    }

    private void saveNewItem() {


        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);


        if (navHostFragment instanceof NavHostFragment) {
            Fragment visibleFragment = navHostFragment.getChildFragmentManager().getFragments().get(0);

            if (visibleFragment instanceof CreateFragment) {
                // doThing();
                ((CreateFragment) visibleFragment).saveInputFromTextFields();
            }
        }

    }


    private void navigateFromHomeToCreate() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.action_HomeFragment_to_CreateFragment);
    }

    private void navigateFromCreateToHome() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        navController.navigate(R.id.action_CreateFragment_to_HomeFragment);
    }


    /**
     * called by the fragments, changes the icon of the fab depending on the @param icon
     */
    public void changeIconFab(String icon) {

        try {
            switch (icon) {
                case "checkmark":
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_baseline_check_24));
                    break;
                case "plus":
                    fab.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), ic_baseline_add_24));
                    break;

                default:
                    throw new IllegalStateException("Unexpected value: " + icon);
            }
        } catch (Exception error) {
            Log.e(TAG, error.getMessage());
        }
    }


    /**
     * not needed i think
     */
    public Fragment getForegroundFragment() {
        Fragment navHostFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        return navHostFragment == null ? null : navHostFragment.getChildFragmentManager().getFragments().get(0);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * find any permissions that haven't been granted, for whatever reason.
     * we don't respect permanently disabled ones >:)
     * first of three "cascading?" methods for asking permissions
     */
    private ArrayList<String> findUnAskedPermissions(ArrayList<String> wantedPermissions) {

        ArrayList<String> result = new ArrayList<>();
        for (String perm : wantedPermissions) {
            if (!hasPermission(perm)) {
                result.add(perm);
            }
        }
        return result;
    }


    /**
     * check if the specified permission is granted
     * second of the "cascading" methods asking for permissions
     */
    private boolean hasPermission(String permission) {

        if (isVersionAboveLollipop()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    /**
     * make sure we are running above lollipop
     * third and final of the "cascading" methods asking for permissions
     */
    private boolean isVersionAboveLollipop() {

        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }


    /**
     * this method is called after requestPermissions()
     * we invoke this method to handle if the user gave us the permissions we required or not
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        // check if it was us that asked permissions
        if (requestCode == ALL_PERMISSIONS_RESULT) {
            // check if we have all required permissions
            for (String perms : permissionsToRequest) {
                if (!hasPermission(perms)) {
                    permissionsRejected.add(perms);
                }
            }
            // if any permission is rejected
            if (permissionsRejected.size() > 0) {

                // if we are running on marshmallow or later we need to ask permissions at runtime
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    // if there is an element in the rejected permissions array, i.e. if one permission has been rejected
                    if (shouldShowRequestPermissionRationale(permissionsRejected.get(0))) {
                        showMessageOKCancel("These permissions are mandatory for the application. Please allow access.",
                                (dialog, which) -> {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                        requestPermissions(permissionsRejected.toArray(new String[permissionsRejected.size()]), ALL_PERMISSIONS_RESULT);
                                    }
                                });
                        return;
                    }
                }
            }
        }

    }

    /**
     * alert message to beg the user for permissions
     */
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}

