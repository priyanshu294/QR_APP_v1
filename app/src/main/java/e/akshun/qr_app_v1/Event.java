package e.akshun.qr_app_v1;

import android.Manifest;
import android.app.DatePickerDialog;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class Event extends AppCompatActivity implements View.OnTouchListener ,  View.OnClickListener{

    private static final String TAG = "Event Class";
    // variable name changed .
    boolean mPermission = false;
    boolean isQRGenerated = false;

    EditText editText_summary,editText_description,
            editText_location,editText_organizer,
            editText_startdate,editText_enddate;
    ImageView imageView;
    Button button;
    DatePickerDialog datePickerDialog;
    int sYear;
    int sMonth;
    int sDayOfMonth;
    int eYear;
    int eMonth;
    int eDayOfMonth;
    Calendar calendar;

    private ScrollView scrollView;
    private CardView cardView;
    private RelativeLayout root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeStatusBarColor();

        editText_summary = findViewById(R.id.summary_input);
        editText_description = findViewById(R.id.description_input);
        editText_location = findViewById(R.id.location_input);
        editText_organizer = findViewById(R.id.organizer_input);
        editText_startdate = findViewById(R.id.start_date_input);
        editText_enddate = findViewById(R.id.end_date_input);

        imageView = findViewById(R.id.qrcode_image);
        button = findViewById(R.id.creare_btn);

        scrollView = findViewById(R.id.scroll);
        root = findViewById(R.id.root);
        cardView = findViewById(R.id.cv_marker);


        // create qr code btn
        button.setOnClickListener(this);
        // start date btn
        editText_startdate.setOnClickListener(this);
        // end date btn
        editText_enddate.setOnClickListener(this);
        //down button btn
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
            editText_summary.getText().clear();
            editText_description.getText().clear();
            editText_location.getText().clear();
            editText_organizer.getText().clear();
            editText_startdate.getText().clear();
            editText_enddate.getText().clear();
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
                            Toast.makeText(Event.this, "Permissions granted", Toast.LENGTH_SHORT).show();
                        } else {
                            mPermission = false;
                            Toast.makeText(Event.this, "Permissions are Required.", Toast.LENGTH_SHORT).show();
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
        // create qr code btn
        if(v == button) {
            generateQRCode();
        }
            // start date btn
            if(v == editText_startdate){
                calendar = Calendar.getInstance();
                sYear = calendar.get(Calendar.YEAR);
                sMonth = calendar.get(Calendar.MONTH);
                sDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(Event.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                editText_startdate.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, sYear, sMonth, sDayOfMonth);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.show();
            }
            // end date btn
        if(v == editText_enddate){
            calendar = Calendar.getInstance();
            eYear = calendar.get(Calendar.YEAR);
            eMonth = calendar.get(Calendar.MONTH);
            eDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            datePickerDialog = new DatePickerDialog(Event.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                            editText_enddate.setText(day + "/" + (month + 1) + "/" + year);
                        }
                    }, eYear, eMonth, eDayOfMonth);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
        // down btn
        if(v == cardView){
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
            button.isShown();
        }

    }
    private void generateQRCode(){
        String data = "BEGIN:VEVENT" +
                "\nSUMMARY:"+(editText_summary.getText().toString()) +
                "\nLOCATION:" + (editText_location.getText().toString()) +
                "\nDESCRIPTION:" + (editText_description.getText().toString()) +
                "\nDTSTART:" +  +sYear+(sDayOfMonth+1)+sDayOfMonth +
                "\nDTEND:" + +eYear+(eDayOfMonth+1)+eDayOfMonth +
                "\nORGANIZER:" + (editText_organizer.getText().toString()) +
                "\nEND:VEVENT" ;

//        BEGIN:VEVENT
//        SUMMARY:evt name
//        LOCATION:fbd
//        URL:slogfy.com
//        DESCRIPTION:notes
//                DTSTART;TZID=Asia/Calcutta:20200915T090000
//                DTEND;TZID=Asia/Calcutta:20200915T100000
//        END:VEVENT

        String data_summary = editText_summary.getText().toString();
        String data_desc = editText_description.getText().toString();
        String data_loc = editText_location.getText().toString();
        String data_organ = editText_organizer.getText().toString();
        String data_statdate = editText_startdate.getText().toString();
        String data_enddata = editText_enddate.getText().toString();

        if (data_summary.trim().isEmpty()) {
            editText_summary.setError("Value Required.");
        } else if (data_desc.trim().isEmpty()) {
            editText_description.setError("Value Required.");
        } else if (data_loc.trim().isEmpty()) {
            editText_location.setError("Value Required.");
        } else if (data_organ.trim().isEmpty()) {
            editText_organizer.setError("Value Required.");
        } else if (data_statdate.trim().isEmpty()) {
            editText_startdate.setError("Value Required.");
        } else if (data_enddata.trim().isEmpty()) {
            editText_enddate.setError("Value Required.");
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
