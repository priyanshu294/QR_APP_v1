package e.akshun.qr_app_v1;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

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

public class Text extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Text Class";
    // variable name changed .
    boolean mPermission = false;
    boolean isQRGenerated = false;


    EditText editText;
    ImageView imageView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeStatusBarColor();
        editText = findViewById(R.id.text_input);
        imageView = findViewById(R.id.qrcode_image);
        button = findViewById(R.id.creare_btn);

        // create qr code btn
        button.setOnClickListener(this);


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
                    if (mPermission) {
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
                if (!isQRGenerated) {
                    Toast.makeText(this, "Please Create QR Code.!", Toast.LENGTH_SHORT).show();
                } else {
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
            editText.getText().clear();
            Toast.makeText(this, "Delete QR Code", Toast.LENGTH_SHORT).show();
            imageView.setBackgroundColor(Color.rgb(128, 128, 128));
            isQRGenerated = false;
        }

    }


    private void shareImage() {
      ShareQRCode sr = new ShareQRCode(this,imageView);
      sr.callshare();

    }


    private void saveToGallery() {
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


    // permission for storage
    public boolean checkpermission() {
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
                            Toast.makeText(Text.this, "Permissions granted", Toast.LENGTH_SHORT).show();
                        } else {
                            mPermission = false;
                            Toast.makeText(Text.this, "Permissions are Required.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).check();
        return mPermission;
    }



    @Override
    public void onClick(View v) {
        // create qr code btn
        if (v == button) {
            generateQRCode();
        }
    }

    private void generateQRCode() {
        String data = editText.getText().toString();
        if (data.isEmpty()) {
            editText.setError("Value Required.");

        } else {
            Toast.makeText(this, "Generating QR Code ... ", Toast.LENGTH_SHORT).show();
            QRGEncoder qrgEncoder = new QRGEncoder(data, null, QRGContents.Type.TEXT, 250);

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
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.gradient_background_login));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
            window.setStatusBarColor(getResources().getColor(R.color.notification));
        }
    }
}
