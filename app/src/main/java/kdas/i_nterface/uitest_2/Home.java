package kdas.i_nterface.uitest_2;

import android.app.Notification;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import br.com.goncalves.pugnotification.notification.PugNotification;
import xyz.hanks.library.SmallBang;
import xyz.hanks.library.SmallBangListener;

public class Home extends AppCompatActivity implements LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener{

    CallbackManager callbackManager;

    Thread ref;
    Firebase notif, pinged, pinger;
    String user_num, pinger_num;

    boolean thread = false;
    boolean bnotif = false;
    boolean init = false;
    boolean chk_ping_once = false;

    CardView fab2, fab3, fab4, fab_route_ping;

    Animation slide_up;
    SmallBang mSmallBang;

    private GoogleApiClient googleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_home);

        getSupportActionBar().hide();
        mSmallBang = SmallBang.attach2Window(this);
        Firebase.setAndroidContext(this);

        callbackManager = CallbackManager.Factory.create();
        final LoginButton loginButton = (LoginButton)findViewById(R.id.fb);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("log", loginResult + "");
                Toast.makeText(getApplicationContext(), loginResult + "", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });




//        if (toolbar != null)
//            toolbar.setTitle("SOLO");
//        toolbar.setTitleTextColor(getResources().getColor(R.color.some_white));
//        setSupportActionBar(toolbar);
        slide_up = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.popup);

        fab2 = (CardView) findViewById(R.id.view10);
        fab2.startAnimation(slide_up);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSmallBang.bang(view,300,new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationEnd() {
                        Intent i = new Intent(Home.this, share_location.class);
                        startActivity(i);
                    }
                });


            }
        });

        fab3 = (CardView) findViewById(R.id.view11);
        fab3.startAnimation(slide_up);
        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSmallBang.bang(view,300,new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationEnd() {
                        Intent i = new Intent(Home.this, Main3Activity.class);
                        startActivity(i);
                    }
                });


            }
        });

        fab4 = (CardView) findViewById(R.id.view12);
        fab4.startAnimation(slide_up);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSmallBang.bang(view,300,new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationEnd() {
                        Intent i = new Intent(Home.this, Ping.class);
                        startActivity(i);
                    }
                });

            }
        });

        fab_route_ping = (CardView) findViewById(R.id.view13);
        fab_route_ping.startAnimation(slide_up);
        fab_route_ping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSmallBang.bang(view,300,new SmallBangListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationEnd() {
                        check_pinged();
                    }
                });
            }
        });

        new do_stuff().execute("");


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {}

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onLocationChanged(Location location) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}

    private class do_stuff extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... strings) {
            pref_and_init();
            gps();
            return null;
        }
    }

    public void check_notif(){

        String furl = "https://wifiap-1361.firebaseio.com/" + user_num + "/notif";
        Log.d("ff", furl);
        notif = new Firebase(furl);
        notif.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                bnotif = dataSnapshot.getValue(boolean.class);
                if (bnotif) {
                    thread = true;
                    run_pug();
                    Log.d("Stop", "running");
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void check_pinged(){
        String furl = "https://wifiap-1361.firebaseio.com/" + user_num + "/pinged";
        pinged = new Firebase(furl);
        Log.d("pinged", pinged.toString());
        if (!chk_ping_once){
            pinged.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    boolean pinged = dataSnapshot.getValue(boolean.class);
                    if (pinged){
                        route_pinger();
                    }else{
                        Toast.makeText(getApplicationContext(), "Nobody has pinged you", Toast.LENGTH_LONG).show();
                        chk_ping_once = true;

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }

    }

    public void route_pinger(){
        String furl_route = "https://wifiap-1361.firebaseio.com/" + user_num + "/pinged_by/";
        pinger = new Firebase(furl_route);
        Log.d("pinger", pinger.toString());
        pinger.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                pinger_num = dataSnapshot.getValue(String.class);

                if (!chk_ping_once){
                    Intent i = new Intent(Home.this, MapsActivity.class);
                    i.putExtra("pinger_num", pinger_num);
                    startActivity(i);
                }else {
                    chk_ping_once = false;
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {}
        });
    }

    public void run_pug(){
        PugNotification.with(getApplicationContext())
                .load()
                .title("Hey !")
                .message("You just met someone, Click!")
                .bigTextStyle("You just met someone, spare a moment buddy!")
                .smallIcon(R.drawable.nmsmall)
                .largeIcon(R.drawable.nmlarge)
                .flags(Notification.DEFAULT_ALL)
                .click(Note.class, null)
                .dismiss(Note.class, null)
                .simple()
                .build();
    }

    public void pref_and_init(){
        SharedPreferences pref = getSharedPreferences("prefs", MODE_PRIVATE);
        user_num = pref.getString("Number","");
        init = pref.getBoolean("Init", init);
        Log.d("####", user_num);
        Log.d("usr", user_num);
        Log.d("init", init + "");




        if (!init){
            Intent i = new Intent(Home.this, Init.class);
            startActivity(i);
        }

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                if (!thread){
                    check_notif();
                    Log.d("Keep", "running");
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    public void gps(){
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        builder.setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates locationSettingsStates = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(Home.this,1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;

                    case  LocationSettingsStatusCodes.CANCELED:
                        Toast.makeText(getApplicationContext(), "We don't have a Warp Drive, so turn the GPS ON", Toast.LENGTH_LONG).show();
                }
            }
        });

        googleApiClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }
}
