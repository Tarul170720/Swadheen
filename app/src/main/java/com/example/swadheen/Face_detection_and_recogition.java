package com.example.swadheen;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.MultiProcessor;
import com.google.android.gms.vision.Tracker;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;

public class Face_detection_and_recogition extends AppCompatActivity {

    private static final String TAG = "FaceTrackerDemo";

    private CameraSource mCameraSource = null;

    private CameraSurfacePreview mPreview;

    private CameraOverlay cameraOverlay;

    private static final int RC_HANDLE_GMS = 9001;

    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private static CountDownTimer countDownTimer;
    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_face_detection_and_recogition);

        mPreview = (CameraSurfacePreview) findViewById(R.id.preview);

        cameraOverlay = (CameraOverlay) findViewById(R.id.faceOverlay);

        int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);

        if (rc == PackageManager.PERMISSION_GRANTED) {

            createCameraSource();

        } else {

            requestCameraPermission();

        }
        Start_time();

    }

    private void requestCameraPermission() {

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,

                Manifest.permission.CAMERA)) {

            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);

            return;

        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                ActivityCompat.requestPermissions(thisActivity, permissions,

                        RC_HANDLE_CAMERA_PERM);

            }

        };

        Snackbar.make(cameraOverlay, "Camera permission is required",

                Snackbar.LENGTH_INDEFINITE)

                .setAction("OK", listener)

                .show();

    }

    private void createCameraSource() {

        Context context = getApplicationContext();

        FaceDetector detector = new FaceDetector.Builder(context)

                .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)

                .build();



        detector.setProcessor(

                new MultiProcessor.Builder<>(new Face_detection_and_recogition.GraphicFaceTrackerFactory())

                        .build());

        if (!detector.isOperational()) {

            Log.e(TAG, "Face detector dependencies are not yet available.");

        }

        mCameraSource = new CameraSource.Builder(context, detector)

                .setRequestedPreviewSize(640, 480)

                .setFacing(CameraSource.CAMERA_FACING_FRONT)

                .setRequestedFps(30.0f)

                .build();

    }

    @Override

    protected void onResume() {

        super.onResume();

        startCameraSource();

    }

    @Override

    protected void onPause() {

        super.onPause();

        mPreview.stop();

    }

    @Override

    protected void onDestroy() {

        super.onDestroy();

        if (mCameraSource != null) {


            mCameraSource.release();

        }

    }
    @Override

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode != RC_HANDLE_CAMERA_PERM) {

            Log.d(TAG, "Got unexpected permission result: " + requestCode);

            super.onRequestPermissionsResult(requestCode, permissions, grantResults);

            return;

        }

        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "Camera permission granted - initialize the camera source");

            createCameraSource();

            return;

        }

        Log.e(TAG, "Permission not granted: results len = " + grantResults.length +

                " Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                finish();

            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("FaceTrackerDemo")

                .setMessage("Need Camera access permission!")

                .setPositiveButton("OK", listener)

                .show();

    }

    private void startCameraSource() {

        int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(

                getApplicationContext());

        if (code != ConnectionResult.SUCCESS) {

            Dialog dlg =

                    GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);

            dlg.show();

        }

        if (mCameraSource != null) {

            try {

                mPreview.start(mCameraSource, cameraOverlay);

            } catch (IOException e) {

                Log.e(TAG, "Unable to start camera source.", e);

                mCameraSource.release();

                mCameraSource = null;

            }

        }

    }

    public void Start_time(){
        countDownTimer=new CountDownTimer(5000,1000){

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                Log.i("Finished","Timer all done");

                Intent intent=new Intent(Face_detection_and_recogition.this,QuestionAnswer.class);

                startActivity(intent);




                finish();
                return;

            }
        }.start();
    }

    private class GraphicFaceTrackerFactory implements MultiProcessor.Factory<Face> {

        @Override

        public Tracker<Face> create(Face face) {

            return new Face_detection_and_recogition.GraphicFaceTracker(cameraOverlay);

        }

    }

    private class GraphicFaceTracker extends Tracker<Face> {

        private CameraOverlay mOverlay;

        private FaceOverlayGraphics faceOverlayGraphics;

        GraphicFaceTracker(CameraOverlay overlay) {

            mOverlay = overlay;

            faceOverlayGraphics = new FaceOverlayGraphics(overlay);

        }

        @Override

        public void onNewItem(int faceId, Face item) {

            faceOverlayGraphics.setId(faceId);

        }

        @Override

        public void onUpdate(FaceDetector.Detections<Face> detectionResults, Face face) {

            mOverlay.add(faceOverlayGraphics);

            faceOverlayGraphics.updateFace(face);

        }

        @Override

        public void onMissing(FaceDetector.Detections<Face> detectionResults) {

            mOverlay.remove(faceOverlayGraphics);

        }

        @Override

        public void onDone() {

            mOverlay.remove(faceOverlayGraphics);

        }

    }


}