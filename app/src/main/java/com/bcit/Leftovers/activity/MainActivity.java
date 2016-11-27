package com.bcit.Leftovers.activity;

import android.Manifest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bcit.Leftovers.fragment.LogIn_Dialog;
import com.bcit.Leftovers.fragment.SignUp_Dialog;
import com.bcit.Leftovers.other.ImageUploader;
import com.bcit.Leftovers.other.Login;
import com.bcit.Leftovers.other.Logout;
import com.bcit.Leftovers.other.MongoDB;
import com.bcit.Leftovers.other.SaveSharedPreference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bcit.Leftovers.R;
import com.bcit.Leftovers.fragment.FindAMeal_Fragment;
import com.bcit.Leftovers.fragment.Home_Fragment;
import com.bcit.Leftovers.fragment.Ingredients_Fragment;
import com.bcit.Leftovers.fragment.Nearby_Fragment;
import com.bcit.Leftovers.fragment.History_Fragment;
import com.bcit.Leftovers.other.CircleTransform;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.concurrent.ExecutionException;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import com.beardedhen.androidbootstrap.TypefaceProvider;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg;
    public static ImageView imgProfile;
    public static TextView txtName, txtWebsite;
    private Toolbar toolbar;

    // urls to load navigation header background image
    // and profile image
    public static String urlProfileImg = "https://static.mengniang.org/common/thumb/a/a2/59205988_p0.jpg/250px-59205988_p0.jpg";
    //userName
    public static String userName = "King";
    //email
    public static String email = "leftover@bcit.ca";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "Home";
    private static final String TAG_INGREDIENTS = "Ingredients";
    private static final String TAG_FIND_A_MEAL = "Find-a-meal";
    private static final String TAG_NEARBY = "Nearby";
    private static final String TAG_HISTORY = "History";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    private static Uri mCropImageUri;

    public FindAMeal_Fragment fF;
    public Ingredients_Fragment iF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TypefaceProvider.registerDefaultIconSets();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        if (SaveSharedPreference.getUser(this, "email") != null) {
            if (!(new Login(SaveSharedPreference.getUser(this, "email"), this).login())) {
                Log.e(MainActivity.class.getName(), SaveSharedPreference.getUser(this, "email"));
            }
        }
        if (Login.loginStatus == 1 && Login.loginStatus != 0) {
            avatarClickListener();
        }

        fF = new FindAMeal_Fragment();
        iF = new Ingredients_Fragment();
    }

    /**
     * For avatar
     */

    public void avatarClickListener() {
        imgProfile.setClickable(true);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CropImage.isExplicitCameraPermissionRequired(MainActivity.this)) {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
                } else {
                    CropImage.startPickImageActivity(MainActivity.this);
                }
                //CropImage.startPickImageActivity(MainActivity.this);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);
            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            Uri imageUri = result.getUri();
            ImageUploader uploader = new ImageUploader(userName, email);
            String upload;
            try {
                upload = uploader.execute(imageUri.getPath()).get();
                if (upload != null) {
                    urlProfileImg = "https://wayneking.me/mongoDB/leftover_images/user_avatar/avatars/" + email + "_" + userName + "." + upload.replace("\"", "");
                    if (updateAvatar()) {
                        loadNavHeader();
                    } else {
                        Toast.makeText(this, "Sorry cannot upload your icon", Toast.LENGTH_LONG).show();
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_OK){
            if (CropImage.isExplicitCameraPermissionRequired(this)){
                requestPermissions(new String[]{Manifest.permission.CAMERA}, CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE);
            }else {
                Uri imageUri = CropImage.getCaptureImageOutputUri(this);
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                startCropImageActivity(mCropImageUri);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.CAMERA_CAPTURE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
                // required permissions granted, start crop image activity
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri)
                .setAllowRotation(true)
                .setCropShape(CropImageView.CropShape.OVAL)
                .start(this);
    }

    private boolean updateAvatar() throws ExecutionException, InterruptedException {
        String json = "email=" + email + "&collection=usersInfo" + "&action=updateOne"
                + "&which=avatar" + "&to=" + urlProfileImg;
        MongoDB mongoDB = new MongoDB(this);
        String result = mongoDB.execute(json).get();
        if (result != null) {
            return true;
        } else {
            return false;
        }
    }


    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        txtName.setText(userName);
        txtWebsite.setText(email);

        // loading header background image
        Glide.with(this).load(R.drawable.drawer_header_bg)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imgProfile);

        // showing dot next to notifications label
        //navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                Home_Fragment homeFragment = new Home_Fragment();
                return homeFragment;
            case 1:
                // photos
                Ingredients_Fragment ingredientsFragment = new Ingredients_Fragment();
                return ingredientsFragment;

            case 2:
                // movies fragment
                FindAMeal_Fragment find_a_meal_Fragment = new FindAMeal_Fragment();
                return find_a_meal_Fragment;

            case 3:
                // notifications fragment
                Nearby_Fragment nearbyFragment = new Nearby_Fragment();
                return nearbyFragment;

            case 4:
                // settings fragment
                History_Fragment historyFragment = new History_Fragment();
                return historyFragment;
            default:
                return new Home_Fragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_ingredients:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_INGREDIENTS;
                        break;
                    case R.id.nav_find_a_meal:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_FIND_A_MEAL;
                        break;
                    case R.id.nav_nearby:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NEARBY;
                        break;
                    case R.id.nav_history:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_HISTORY;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, PrivacyPolicyActivity.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shrink_to_original);
                imgProfile.startAnimation(anim);
                if (Login.loginStatus == 1 && Login.loginStatus != 0) {
                    avatarClickListener();
                }
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (navItemIndex != 3) {
            if (Login.loginStatus == 1) {
                menu.findItem(R.id.action_logout).setVisible(true);
                menu.findItem(R.id.action_login).setVisible(false);
                menu.findItem(R.id.action_signup).setVisible(false);
            } else if (Login.loginStatus == 0) {
                menu.findItem(R.id.action_logout).setVisible(false);
                menu.findItem(R.id.action_login).setVisible(true);
                menu.findItem(R.id.action_signup).setVisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0 || navItemIndex == 1 || navItemIndex == 2 || navItemIndex == 4) {
            getMenuInflater().inflate(R.menu.main, menu);

        }

        // when fragment is notifications, load the menu created for notifications
        else if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        if (id == R.id.action_signup) {
            SignUp_Dialog signupDialog = new SignUp_Dialog();
            signupDialog.show(getFragmentManager(), "Signup");
            drawer.closeDrawers();
        }
        if (id == R.id.action_login) {
            LogIn_Dialog loginDialog = new LogIn_Dialog();
            synchronized (this) {
                loginDialog.show(getFragmentManager(), "Login");
            }
            drawer.closeDrawers();
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            if (!(new Logout(Login.email, MainActivity.this).logout())) {
                return false;
            } else {
                Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
                Log.d("status", Login.loginStatus + "");
                imgProfile.setClickable(false);
                return true;
            }
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);
    }

    public void chooseIngredient(View v) {
        iF.chooseIngredient(v);
    }

    public void nextStep(View v) {
        iF.nextStep(v);
    }
}
