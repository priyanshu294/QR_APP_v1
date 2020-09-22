package e.akshun.qr_app_v1;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.CalendarContract;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import e.akshun.qr_app_v1.Type_Check.ContactCheckClass;
import e.akshun.qr_app_v1.Type_Check.EmailCheckClass;
import e.akshun.qr_app_v1.Type_Check.EventCheckClass;
import e.akshun.qr_app_v1.Type_Check.PhoneCheckClass;
import e.akshun.qr_app_v1.Type_Check.SmsCheckClass;
import e.akshun.qr_app_v1.Type_Check.TypeCheckClass;
import e.akshun.qr_app_v1.Type_Check.WifiCheckClass;

import static com.google.zxing.client.result.ParsedResultType.ADDRESSBOOK;
import static com.google.zxing.client.result.ParsedResultType.CALENDAR;
import static com.google.zxing.client.result.ParsedResultType.EMAIL_ADDRESS;
import static com.google.zxing.client.result.ParsedResultType.GEO;
import static com.google.zxing.client.result.ParsedResultType.SMS;
import static com.google.zxing.client.result.ParsedResultType.TEL;
import static com.google.zxing.client.result.ParsedResultType.URI;
import static com.google.zxing.client.result.ParsedResultType.WIFI;

public class Suggestion extends AppCompatActivity implements View.OnClickListener, TypeCheckClass.QRTypeListner {

    private static final String TAG = "Suggestion Class";

    private static final int Gallery_REQUEST_CODE = 123;

    ImageView qr_code_img, delete_img, share_img, copy_img;
    TextView content_txt, delete_txt, share_txt, copy_txt;
    Button sugg_button;

    boolean mPermission = false;
    private String mQRType ;
    private String mQRData ;

    private TypeCheckClass mTypeCheckClass ;
    private EmailCheckClass mEmailCheckClass ;
    private WifiCheckClass mWifiCheckClass ;
    private SmsCheckClass mSMSCheckClass ;
    private PhoneCheckClass mPhoneCheckClass ;
    private ContactCheckClass mContactCheckClass ;
    private EventCheckClass mEventCheckClass ;

    private void initialize(){
        qr_code_img = findViewById(R.id.qrcode_result);

        delete_img = findViewById(R.id.delte_img);
        share_img = findViewById(R.id.share_img);
        copy_img = findViewById(R.id.copy_img);

        content_txt = findViewById(R.id.result_text);
        delete_txt = findViewById(R.id.delete_text);
        share_txt = findViewById(R.id.share_text);
        copy_txt = findViewById(R.id.copy_text);
        sugg_button = findViewById(R.id.creare_btn);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        changeStatusBarColor();
        initialize();
        mTypeCheckClass = new TypeCheckClass(this);
        SetUpQRCode();

// Give suggestion for different QR code
        IntentSuggestions();
// Give suggestion for different QR code   btn
        sugg_button.setOnClickListener(this);
        share_img.setOnClickListener(this);
        delete_img.setOnClickListener(this);
        copy_img.setOnClickListener(this);
    }

    private void SetUpQRCode(){
            Bundle mBundle = getIntent().getExtras();
            if(mBundle!= null ){
                String qrData1 = mBundle.getString("qrResult");
                String qrData2 = mBundle.getString("qrImageData");
                Log.d(TAG, "SetUpQRCode() Scanner data ="+qrData1);
                if(qrData1 != null ){
                    Log.d(TAG, "SetUpQRCode() Scanner data ="+qrData1);
                    Log.d(TAG, "SetUpQRCode() Gallery data ="+qrData2);
//                    content_txt.setText(qrData1);
                    mQRData = qrData1 ;
                    QRGEncoder qrgEncoder = new QRGEncoder(qrData1, null, QRGContents.Type.TEXT, 250);
                    try {
                        Bitmap qrBitMap = qrgEncoder.getBitmap();
                        qr_code_img.setImageBitmap(qrBitMap);
                    } catch (Exception e) {
                        Log.d(TAG, "SetUpQRCode() qrData1 EXCEPTION = "+e.getMessage());
                    }
                }
                if(qrData2 != null){
                    Log.d(TAG, "SetUpQRCode() Gallery data ="+qrData2);
                    try {
                        Bitmap qrBitMap = BitmapFactory.decodeStream(this.openFileInput("qrImage"));
                        qr_code_img.setImageBitmap(qrBitMap);
                    } catch (FileNotFoundException e) {
                        Log.d(TAG, "SetUpQRCode() qrData2 EXCEPTION = "+e.getMessage());
                    }
//                    content_txt.setText(qrData2);
                    mQRData = qrData2 ;
                }
                if(hasImage(qr_code_img)){
                    Bitmap qrBitMap = ((BitmapDrawable)qr_code_img.getDrawable()).getBitmap();
                    GetQRResult(qrBitMap);
                }
            }
    }

    //  Give suggestion for different QR code
    @SuppressLint("SetTextI18n")
    private void IntentSuggestions() {

        String qrData = mQRData ;
        if(!qrData.equals("")){
            if (mQRType.equals(TEL.toString())) {
                sugg_button.setText("Call");
                content_txt.setText("TEL : "+mPhoneCheckClass.SetPhoneNumber());
            } else if (mQRType.equals(URI.toString())) {
                sugg_button.setText("Open in Browser");
                content_txt.setText("URL : "+mQRData);
            } else if (mQRType.equals(SMS.toString())) {
                sugg_button.setText("Send SMS");
                content_txt.setText("TEL : "+mSMSCheckClass.SetSMSNumber()+
                                     "\nMSG : "+mSMSCheckClass.SetSMSBody());
            } else if (mQRType.equals(GEO.toString())){
                sugg_button.setText("Search On GoogleMap");
                content_txt.setText("LAT-LNG : "+mQRData);
            } else if (mQRType.equals(EMAIL_ADDRESS.toString())) {
                sugg_button.setText("Send Email");
                content_txt.setText("EMAIL : "+mEmailCheckClass.SetEmailTo()+
                                    "\nSUB  : "+mEmailCheckClass.SetEmailSubject()+
                                    "\nBODY : "+mEmailCheckClass.SetEmailBody());
            } else if (mQRType.equals(WIFI.toString())){
                sugg_button.setText("Connect Wifi");
                content_txt.setText("SSID : "+mWifiCheckClass.SetWifiSSID()+
                                    "\nPASS : "+mWifiCheckClass.SetWifiPassword());
            }else if (mQRType.equals(ADDRESSBOOK.toString())){
                sugg_button.setText("SAVE CONTACT");
                content_txt.setText("NAME  : "+mContactCheckClass.setContactName()+
                                    "\nPHONE : "+mContactCheckClass.setContactPhone()+
                                    "\nEMAIL : "+mContactCheckClass.setContactEmail()+
                                    "\nORG   : "+mContactCheckClass.setContactOrg()+
                                    "\nWEB   : "+mContactCheckClass.setContactUrl()+
                                    "\nADR   : "+mContactCheckClass.setContactAddress());
            }else if(mQRType.equals(CALENDAR.toString())){
                sugg_button.setText("SET EVENT");
                content_txt.setText("SUMMARY : "+mEventCheckClass.setEventSummary()+
                        "\nORG   : "+mEventCheckClass.setEventOrganizer()+
                        "\nLOC   : "+mEventCheckClass.setEventloc()+
                        "\nDESCP : "+mEventCheckClass.setEventDescp()+
                        "\nBEGIN : "+mEventCheckClass.setEventStrDate()+
                        "\nEND   : "+mEventCheckClass.setEventEndDate());
            }
            else {
                sugg_button.setText("Web Search");
                content_txt.setText("DATA : \n"+mQRData);
            }
        }else{
            sugg_button.setText("-- --");
        }
    }

    @Override
    public void QRType(String Type, Object typeObject) {
        this.mQRType = Type ;
        Log.d(TAG, "QRType() TYPE = "+mQRType);
        if(typeObject != null){
            switch(mQRType){
                case "EMAIL_ADDRESS" : mEmailCheckClass = new EmailCheckClass(typeObject);
                    break ;
                case "WIFI" : mWifiCheckClass = new WifiCheckClass(typeObject);
                    break ;
                case "SMS" : mSMSCheckClass = new SmsCheckClass(typeObject);
                    break ;
                case "TEL" : mPhoneCheckClass = new PhoneCheckClass(typeObject);
                    break ;
                case "ADDRESSBOOK"  : mContactCheckClass = new ContactCheckClass(typeObject);
                    break ;
                case "CALENDAR"  : mEventCheckClass = new EventCheckClass(typeObject);
                    break ;
                default:
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 123) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                MakeCall();
            } else {
                Toast.makeText(this, "Permission Required.", Toast.LENGTH_LONG).show();
            }
        }
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == Gallery_REQUEST_CODE) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                qr_code_img.setImageBitmap(selectedImage);
                if(hasImage(qr_code_img)){
                    Bitmap qrBitMap = ((BitmapDrawable)qr_code_img.getDrawable()).getBitmap();
                    GetQRResult(qrBitMap);
                    IntentSuggestions();
                }

            } catch (FileNotFoundException e) {

                Log.d(TAG, "onActivityResult() ERROR " + e.getMessage());
                Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
    }

    // calling suggestion or call Intent
    private void MakeCall() {
        String number = content_txt.getText().toString();
        if (number.trim().length() > 0) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);

            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        123);
            } else {
                if(number.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(number));
                    startActivity(intent);
                } else{
                    String dial = "tel:" + number;
                    Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(dial));
                    startActivity(intent);
                }
            }
        }
    }

    // Intent for browser
    private void BrowserIntent() {
        String url = mQRData ;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (url.startsWith("https://") || url.startsWith("http://")){
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }else{
            String query = url ;
            query = URLEncoder.encode(query);
            String google = "https://www.google.com/search?q="+query ;
            intent.setData(Uri.parse(google));
            startActivity(intent);
        }
        }

    // Intent for search on web
    private void SearchIntent() {
        String web = mQRData;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String query = web ;
        query = URLEncoder.encode(query);
        String google = "https://www.google.com/search?q="+query ;
        intent.setData(Uri.parse(google));
        startActivity(intent);
    }

    // Intent for wifi
    private void WifiIntent() {
        String wifi = mQRData;
        Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        wifiIntent.putExtra("ssid", wifi);
        startActivity(wifiIntent);
    }

    // Intent for search on GoogleMap
    private void MapIntent() {
        String location = mQRData;
        Uri gmIntentUri = Uri.parse(location);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);
    }

    // Intent for sms
    private void SentMessage() {
        String number = mSMSCheckClass.SetSMSNumber();
        String message = mSMSCheckClass.SetSMSBody();
        Log.d(TAG, "SentMessage() num = "+number +"\n message = "+message);
        Uri sms_uri = Uri.parse("sms:"+number);
        Intent sms_intent = new Intent(Intent.ACTION_VIEW, sms_uri);
//        sms_intent.putExtra("address", number);
        sms_intent.putExtra("sms_body", message);
        try {
            Intent chooser = Intent.createChooser(sms_intent, "Send SMS with :");
            startActivity(chooser);
        }catch(Exception e){
            Log.e(TAG, "SentMessage() EXCEPTION "+e.getMessage() );
        }
    }

    //Intent for email
    private void EmailIntent() {

            String emailTo = mEmailCheckClass.SetEmailTo();
            String subject = mEmailCheckClass.SetEmailSubject();
            String body = mEmailCheckClass.SetEmailBody() ;
            Intent intent = new Intent(Intent.ACTION_SENDTO)
                    .setData(new Uri.Builder().scheme("mailto").build())
                    .putExtra(Intent.EXTRA_EMAIL, new String[]{ "<"+emailTo+">" })
                    .putExtra(Intent.EXTRA_SUBJECT, subject)
                    .putExtra(Intent.EXTRA_TEXT, body);
            Log.d(TAG, "EmailIntent() intent data = "+intent.getData());
            try {
                Intent chooser = Intent.createChooser(intent, "Send email with :");
                startActivity(chooser);
            } catch (Exception e) {
                Log.d(TAG, "EmailIntent() NO EMAIL APP AVAILABLE ... !");
            }
    }

    private void AddContact(){
        String cName = mContactCheckClass.setContactName() ;
        String cPhone = mContactCheckClass.setContactPhone() ;
        String cEmail = mContactCheckClass.setContactEmail() ;
        String cOrg = mContactCheckClass.setContactOrg() ;
        String cUrl = mContactCheckClass.setContactUrl() ;
        String cAddress = mContactCheckClass.setContactAddress() ;

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.NAME, cName);
        intent.putExtra(ContactsContract.Intents.Insert.PHONE, cPhone);
        intent.putExtra(ContactsContract.Intents.Insert.EMAIL, cEmail);
        intent.putExtra(ContactsContract.Intents.Insert.COMPANY,cOrg);
        intent.putExtra(ContactsContract.Intents.Insert.DATA,cUrl);
        intent.putExtra(ContactsContract.Intents.Insert.POSTAL,cAddress);

        startActivity(intent);
    }

    private void AddEvent(){
        String eSummary = mEventCheckClass.setEventSummary() ;
        String eDescp = mEventCheckClass.setEventDescp() ;
        String eLoc = mEventCheckClass.setEventloc() ;
        String eOrg = mEventCheckClass.setEventOrganizer();
        String eStrDate = Long.toString(mEventCheckClass.setEventStrDate()) ;
        String eEndDate = Long.toString(mEventCheckClass.setEventEndDate()) ;

        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setData(CalendarContract.Events.CONTENT_URI)
          .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,eStrDate) // Only date part is considered when ALL_DAY is true; Same as DTSTART
          .putExtra(CalendarContract.EXTRA_EVENT_END_TIME,eEndDate) // Only date part is considered when ALL_DAY is true
          .putExtra(CalendarContract.Events.EVENT_LOCATION, eLoc)
          .putExtra(CalendarContract.Events.DESCRIPTION, eDescp)
          .putExtra(CalendarContract.Events.ORGANIZER,eOrg)
          .putExtra(CalendarContract.Events.TITLE,eSummary);

        startActivity(intent);
    }
// Delete QR code
    private  void DeleteQR(){

        if(hasImage(qr_code_img)){
            AlertDialog.Builder builder = new AlertDialog.Builder(Suggestion.this);
            builder.setTitle("Delete QR :");
            builder.setMessage("Are you sure you want to delete QR data ?");
            builder.setIcon(R.drawable.icon_image);
            builder.setCancelable(false);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Suggestion.this, "QR deleted ...",Toast.LENGTH_SHORT).show();
                    qr_code_img.setImageDrawable(null);
                    qr_code_img.setBackground(getDrawable(R.color.color_grey));
                    content_txt.setText("");
                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(Suggestion.this, "Cancelled Deletion ...",Toast.LENGTH_SHORT).show();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            Toast.makeText(this, "No QR code to delete ...", Toast.LENGTH_SHORT).show();
        }

    }

    // Share code data
    private void shareQR() {
        // share using File Provider
        Log.d(TAG, "shareQR() is called ");
        if(hasImage(qr_code_img)) {
            Log.d(TAG, "shareQR() ImageView is not empty .");
            ShareQRCode sr = new ShareQRCode(this,qr_code_img);
            sr.callshare();
        }else{
            Toast.makeText(this, "No QR code to share ...", Toast.LENGTH_SHORT).show();
        }
    }

    // save qr code
    private void saveToGallery() {
        // checking for imageview is empty or not.
        if (hasImage(qr_code_img)) {
            DownloadQRCode dw = new DownloadQRCode(this,qr_code_img);
            dw.callDownload();
        }else{
            Toast.makeText(this, "No QR Code to Save ...", Toast.LENGTH_SHORT).show();
        }
    }

    // Copy to clipboard
    private void Copycode(){
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("text", content_txt.getText().toString());
        clipboardManager.setPrimaryClip(clipData);
        Toast.makeText(Suggestion.this, "Copied Successfully",Toast.LENGTH_SHORT).show();
    }

    // permission for storage
    private boolean checkpermission() {
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
                            Toast.makeText(Suggestion.this, "Permissions granted", Toast.LENGTH_SHORT).show();
                        } else {
                            mPermission = false;
                            Toast.makeText(Suggestion.this, "Permissions are Required.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }

                }).check();
        return mPermission;
    }

    // For calling
    @Override
    public void onClick(View v) {
        if (v == sugg_button) {
            String qrData = content_txt.getText().toString() ;
            if(!qrData.equals("")){
                if (mQRType.equals(TEL.toString())) {
                    MakeCall();
                } else if (mQRType.equals(URI.toString())) {
                    BrowserIntent();
                } else if (mQRType.equals(SMS.toString())) {
                    SentMessage();
                } else if (mQRType.equals(GEO.toString())) {
                    MapIntent();
                } else if (mQRType.equals(EMAIL_ADDRESS.toString())) {
                    EmailIntent();
                } else if (mQRType.equals(WIFI.toString())) {
                    WifiIntent();
                } else if (mQRType.equals(ADDRESSBOOK.toString())){
                    AddContact();
                }else if (mQRType.equals(CALENDAR.toString())){
                    AddEvent();
                }
                else {
                    SearchIntent();
                }
            }else{
                Toast.makeText(this, "No data found ...", Toast.LENGTH_SHORT).show();
            }
        }
        if (v == share_img) {
           shareQR();
        }
        if(v == delete_img){
            DeleteQR();
        }
        if(v == copy_img){
            Copycode();
        }
    }
    // Action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scan_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.download :
                if (!checkpermission()) {
                    checkpermission();
                    if(checkpermission()){
                        saveToGallery();
                    }
                } else {
                    saveToGallery();
                }break;
            case R.id.gallery :
                chooseImage();
                break;
            case R.id.about_us:
                Aboutus();
                break;
            case R.id.feedback :
                FeedbackQR() ;
                break;
            case R.id.privacy_policy :
                PrivacyPolicy() ;
                break;
            default:
        }
        return super.onOptionsItemSelected(item);

    }

    // gallery take qr code image
    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select QR code"), Gallery_REQUEST_CODE);
    }

    private boolean hasImage (@NonNull ImageView view){
        Drawable drawable = view.getDrawable();
        boolean hasImage = (drawable != null);

        if (hasImage && (drawable instanceof BitmapDrawable)) {
            hasImage = ((BitmapDrawable) drawable).getBitmap() != null;
        }

        return hasImage;
    }

    private void GetQRResult(Bitmap bMap){
        if(bMap != null){
            int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
            bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());
            LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Reader reader = new MultiFormatReader();
            try {
                Result result = reader.decode(bitmap);
                mTypeCheckClass.GetTypeOfQR(result);
            } catch (Exception e) {
                Log.d(TAG, "GetQRResult() EXCEPTION "+e.getMessage());
            }
        }
    }
    // About us page
    private void Aboutus() {
        startActivity(new Intent(Suggestion.this,AboutUS.class));

    }

    // Privacy Policy
    private void PrivacyPolicy() {
        startActivity(new Intent(Suggestion.this,Privacy_Policy.class));
    }

    // feedback to QR code
    private void FeedbackQR(){
        Intent intent = new Intent(Intent.ACTION_SENDTO)
                .setData(new Uri.Builder().scheme("mailto").build())
                .putExtra(Intent.EXTRA_EMAIL, new String[]{ "narendok@gmail.com" });
        startActivity(intent);

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