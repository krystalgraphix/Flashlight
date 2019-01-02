package com.krystalgraphix.torch;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class HomeActivity extends AppCompatActivity {

    private AdView mAdView;

    Button btnFlashLight;
    private static final int CAMERA_REQUEST =123;
    boolean hasCameraFlash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //this.setContentView(R.layout.activity_home);

        ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);

        MobileAds.initialize(this, "ca-app-pub-8156214218288299~7730958662");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        btnFlashLight = findViewById(R.id.btnFlashLightToggle);

        btnFlashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraFlash) {
                    if (btnFlashLight.getText().toString().contains("ON")) {
                        btnFlashLight.setText("OFF");

                        flashLightOff();
                    } else {
                        btnFlashLight.setText("ON");
                        flashLightOn();
                    }
                } else {
                    android.widget.Toast.makeText(HomeActivity.this, "No flash available on your device", Toast.LENGTH_SHORT).show();
                }
            }
        });
        }

        private void flashLightOn() {
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, true);
            } catch (CameraAccessException e) {
            }
        }

        private void flashLightOff(){
            CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            try {
                String cameraId = cameraManager.getCameraIdList()[0];
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
            switch (requestCode){
                case CAMERA_REQUEST:
                    if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
                    } else {
                        btnFlashLight.setEnabled(false);

                        android.widget.Toast.makeText(HomeActivity.this, "Permission Denied for the Camera", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
}