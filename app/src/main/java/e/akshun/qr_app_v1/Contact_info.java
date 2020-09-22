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

import com.hbb20.CountryCodePicker;
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

public class Contact_info extends AppCompatActivity implements View.OnTouchListener,View.OnClickListener {

    private static final String TAG = "Contact_info Class";
    // variable name changed .
    boolean mPermission = false;
    boolean isQRGenerated = false;
    EditText editText_first_name,editText_org,editText_email,editText_phone,editText_address,editText_url;
    ImageView imageView;
    Button button;
    private ScrollView scrollView;
    private CardView cardView;
    private RelativeLayout root;
    private CountryCodePicker ccp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeStatusBarColor();

        editText_first_name = findViewById(R.id.name_input);

        editText_org = findViewById(R.id.org_input);
        editText_email = findViewById(R.id.email_input);
        editText_phone = findViewById(R.id.phone_input);
        editText_address = findViewById(R.id.address_input);

        editText_url = findViewById(R.id.url_web_input);

        imageView = findViewById(R.id.qrcode_image);
        button = findViewById(R.id.creare_btn);
        ccp = findViewById(R.id.ccp);

        scrollView = findViewById(R.id.scroll);
        root = findViewById(R.id.root);
        cardView = findViewById(R.id.cv_marker);

        //create qr code btn
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
            editText_first_name.getText().clear();
            editText_org.getText().clear();
            editText_email.getText().clear();
            editText_phone.getText().clear();
            editText_address.getText().clear();
            editText_url.getText().clear();
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
// permission for storage
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
                            Toast.makeText(Contact_info.this, "Permissions granted", Toast.LENGTH_SHORT).show();
                        } else {
                            mPermission = false;
                            Toast.makeText(Contact_info.this, "Permissions are Required.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }


                }).check();
        return mPermission;
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

    @Override
    public void onClick(View v) {
        //create qr code btn
        if(v == button){
            generateQRCode();
        }
        // dwon btn
        if(v == cardView){
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            button.isShown();

        }
    }
    private void generateQRCode(){
        String data = "MECARD:N:"+(editText_first_name.getText().toString())+";ORG:"+ (editText_org.getText().toString())+
                ";EMAIL:"+ (editText_email.getText().toString())+";TEL:"+(editText_phone.getText().toString())+
                ";ADR:"+ (editText_address.getText().toString())+";URL:"+(editText_url.getText().toString())+";";

        String data_first = editText_first_name.getText().toString() ;
        String data_org = editText_org.getText().toString() ;
        String data_email = editText_email.getText().toString() ;
        String data_phone = editText_phone.getText().toString() ;
        String data_address = editText_address.getText().toString() ;
        String data_url = editText_url.getText().toString() ;

        if (data_first.trim().isEmpty()) {
            editText_first_name.setError("Value Required .");
        }else if(data_phone.trim().isEmpty()){
            editText_phone.setError("Value Required.");
        }else if(data_phone.matches("[a-z,A-Z]*")){
            editText_phone.setError("Enter Valid Phone Number.");
        }else if(!data_email.trim().equals("") && !Patterns.EMAIL_ADDRESS.matcher(data_email).matches()){
            editText_email.setError("Please Enter Valid Email.");
        }else if(!data_url.trim().equals("") && !Patterns.WEB_URL.matcher(data_url).matches()){
            editText_url.setError("Please Enter Valid URL.");
        }
        else {
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
