package in.iosense.weightx;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;


public class StartStreamActivity extends AppCompatActivity {

    private WiFiStateChangeReceiver wiFiStateChangeReceiver;
    private ViewPager viewPager;
    private final static int MY_PERMISSIONS_REQUEST_CAMERA = 69;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_start_stream);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initViewPager();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        requestPermission();

        putIpAddressToSharedPref(getIpAddress());

        wiFiStateChangeReceiver = new WiFiStateChangeReceiver();
        registerReceiver(wiFiStateChangeReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        registerReceiver(wiFiStateChangeReceiver, new IntentFilter(WifiManager.WIFI_STATE_CHANGED_ACTION));
    }

    private void buildSession(SharedPreferences sharedPreferences) {
        boolean isAudioStream = sharedPreferences.getBoolean("is_audio_stream", false);
        int videoEncoder = sharedPreferences.getInt("video_encoder", 0);
        int resolution = sharedPreferences.getInt("resolution", 2);
        int width[] = getResources().getIntArray(R.array.resolution_width);
        int height[] = getResources().getIntArray(R.array.resolution_height);
    }

    private void putIpAddressToSharedPref(String ipAddress) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(this).edit();
        editor.putString("ip_local", ipAddress);
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    void initViewPager(){
        viewPager = (ViewPager)findViewById(R.id.about_connect_pager);
        StartStreamPagerAdapter startStreamPagerAdapter = new StartStreamPagerAdapter(getSupportFragmentManager());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                PagerCirclesManager.dotStatusManage(position, getActivity());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        viewPager.setAdapter(startStreamPagerAdapter);
    }

    private Activity getActivity(){
        return this;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wiFiStateChangeReceiver);
    }

    public void requestPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_CAMERA);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
                    buildSession(sharedPreferences);
                } else {
                    startActivity(new Intent(getActivity(), MainMenuActivity.class));
                    finish();
                }
                return;
            }
        }
    }

    public void coverAboutConnectionLayout(){
        LinearLayout aboutConnectionLayout = (LinearLayout) findViewById(R.id.about_connection);
        if(aboutConnectionLayout != null) aboutConnectionLayout.setVisibility(View.GONE);
    }

    public void showAboutConnectionLayout(){
        View aboutConnectionLayout =  findViewById(R.id.about_connection);
        if(aboutConnectionLayout != null) aboutConnectionLayout.setVisibility(View.VISIBLE);
    }

    public void SlideToInputPage(View view){
        viewPager.setCurrentItem(0);
    }

    public void SlideToQrCodePage(View view){
        viewPager.setCurrentItem(1);
    }

    public void SlideToNfcPage(View view){
        viewPager.setCurrentItem(2);
    }

    private String getIpAddress(){
        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        String ipAddressFormatted = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
        return ipAddressFormatted;
    }

    private void logError(final String msg) {
        final String error = (msg == null) ? "Error unknown" : msg;
        AlertDialog.Builder builder = new AlertDialog.Builder(StartStreamActivity.this);
        builder.setTitle("Error");
        builder.setMessage(error).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(StartStreamActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }
        });
        builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_BACK) {
                    Intent intent = new Intent(StartStreamActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
