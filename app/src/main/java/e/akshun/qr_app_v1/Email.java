package e.akshun.qr_app_v1;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;
import androidx.core.view.MotionEventCompat;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Email extends AppCompatActivity implements View.OnClickListener,View.OnTouchListener {

    private static final String TAG = "Email Class";
    // variable name changed .
    boolean mPermission = false;
    boolean isQRGenerated = false;
    Button button;
    ImageView imageView;
    EditText editText_add, editText_sub, editText_mes;
   private ScrollView scrollView;
   private CardView cardView;
    private RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeStatusBarColor();

        editText_add = findViewById(R.id.address_input);
        editText_sub = findViewById(R.id.subject_input);
        editText_mes = findViewById(R.id.message_input);
        imageView = findViewById(R.id.qrcode_image);
        button = findViewById(R.id.creare_btn);

        scrollView = findViewById(R.id.scroll);
        root = findViewById(R.id.root);
        cardView = findViewById(R.id.cv_marker);



        // onCLick() is called at last to make code look cleaner , please use this way from future.
        // create qr code btn
        button.setOnClickListener(this);

        //down button
        cardView.setOnClickListener(this);

        root.setOnTouchListener(this);

    }


    // Action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mymenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.download:
                if (!checkpermission()) {
                    checkpermission();
                    if(mPermission){
                        saveToGallery();
                    }
                } else {
                    saveToGallery();
                }
                break;

            case R.id.delete:
                deleteImage();
                break;

            case R.id.share:
                if(!isQRGenerated){
                    Toast.makeText(this, "Please Create QR Code.!", Toast.LENGTH_SHORT).show();
                }else {
                    shareImage();
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);

    }

    private void deleteImage() {
        if (!isQRGenerated) {
            Toast.makeText(this, "QR code not generated.!", Toast.LENGTH_SHORT).show();
        } else {
            imageView.setImageDrawable(null);
            editText_add.getText().clear();
            editText_mes.getText().clear();
            editText_sub.getText().clear();
            Toast.makeText(this, "Delete QR Code", Toast.LENGTH_SHORT).show();
            imageView.setBackgroundColor(Color.rgb(128,128,128));
            isQRGenerated = false;
        }
    }


    private void shareImage() {
        ShareQRCode sr = new ShareQRCode(this,imageView);
        sr.callshare();
     }

    private void saveToGallery () {
        // checking for imageview is empty or not.

        if (!isQRGenerated) {
            Log.d(TAG, "saveToGallery() generate qr");
            generateQRCode();
        }
        if (isQRGenerated) {
            DownloadQRCode dw = new DownloadQRCode(this,imageView);
            dw.callDownload();
        }
    }
    //permission for location
    private boolean checkpermission () {
        // checkpermission returns boolean value.

        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
        Dexter.withActivity(this)
                .withPermissions(permissions)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        Log.d(TAG, "onPermissionsChecked() Report = " + report);
                        if (report.areAllPermissionsGranted()) {
                            mPermission = true;
                            Toast.makeText(Email.this, "Permissions granted", Toast.LENGTH_SHORT).show();
                        } else {
                            mPermission = false;
                            Toast.makeText(Email.this, "Permissions are Required.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).check();
        return mPermission;
    }
        //btn code
        @Override
        public void onClick (View view){
            if (view == button) {
                generateQRCode();
            }
            // down btn
            if(view == cardView){
                scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                button.isShown();
            }
        }

        // scroll down code
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_DOWN) :
                isInBound();
                return true;
            case (MotionEvent.ACTION_MOVE) :
                isInBound();
                return true;
            case (MotionEvent.ACTION_UP) :
                isInBound();
                return true;
            case (MotionEvent.ACTION_CANCEL) :
                isInBound();
                return true;
            case (MotionEvent.ACTION_OUTSIDE) :
                return true;
            default :
                return super.onTouchEvent(event);
        }
    }
    private void isInBound(){
        Rect scrollBounds = new Rect();
        scrollView.getHitRect(scrollBounds);
        if (button.getLocalVisibleRect(scrollBounds)) {

            cardView.setVisibility(View.GONE);
        } else {

            cardView.setVisibility(View.VISIBLE);
        }
    }

    private void generateQRCode(){
        String data1 =  (editText_add.getText().toString()) + "?&subject=" +
                (editText_sub.getText().toString()) + "&body=" + (editText_mes.getText().toString());

        Log.d(TAG, "onClick() EMAIL QR Uri = "+data1);

        String data_email = editText_add.getText().toString();
        String data_sub = editText_sub.getText().toString();
        String data_mes = editText_mes.getText().toString();

        if (data_email.trim().isEmpty()) {
            editText_add.setError("Value Required.");
        } else if (data_sub.trim().isEmpty()) {
            editText_sub.setError("Value Required.");
        } else if (data_mes.trim().isEmpty()) {
            editText_mes.setError("Value Required.");
        } else if(!Patterns.EMAIL_ADDRESS.matcher(data_email).matches()){
            editText_add.setError("Please Enter Valid Email.");
        }
        else {
            Toast.makeText(this, "Generating QR Code ... ", Toast.LENGTH_SHORT).show();
            QRGEncoder qrgEncoder = new QRGEncoder(data1, null, QRGContents.Type.EMAIL, 250);

            try {
                Bitmap qrBits = qrgEncoder.getBitmap();

                imageView.setImageBitmap(qrBits);
                isQRGenerated = true;

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void changeStatusBarColor() {
        ActionBar actionBar=getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_background_login));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.notification));
        }
    }
}
