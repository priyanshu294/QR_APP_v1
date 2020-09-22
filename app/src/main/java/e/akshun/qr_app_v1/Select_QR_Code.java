package e.akshun.qr_app_v1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import e.akshun.qr_app_v1.RecyclerAdapter.QRcode;
import e.akshun.qr_app_v1.RecyclerAdapter.RecyclerAdapter;

public class Select_QR_Code extends AppCompatActivity implements RecyclerAdapter.OnqrcodeListener {

    private static final String TAG = "Select_QR_Code";

//    private  String[] mNames = {"Create QR Code for Email","Create QR Code for Wifi","Create QR Code for Event","Create QR Code for Contact",
//
//            "Create QR Code for Text","Create QR Code for Website","Create QR Code for Location", "Create QR Code for Phone",
//            "Create QR Code for Message"};
//
//    private  int [] mImage = {R.drawable.email,R.drawable.wifi,R.drawable.event,R.drawable.contact,R.drawable.text,R.drawable.url,
//            R.drawable.location,R.drawable.phone,R.drawable.sms};

    private ArrayList<QRcode> qRcodeList = new ArrayList<>();
    private ArrayList<String> mArrayNames = new ArrayList<>();
    private ArrayList<Integer> mArrayImages = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter ;
    private QRcode mQRcode ;


    private void InitRecyclerView() {
        Log.d(TAG, "InitRecyclerView() initialized");

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView = findViewById(R.id.home_recycler);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerAdapter = new RecyclerAdapter(qRcodeList,this);
        mRecyclerView.setAdapter(mRecyclerAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select__qr__code);
        changeStatusBarColor();
        ArrayPut();
        preparedTheList();

    }

    private void preparedTheList(){

        int count = 0;
        for(; count < mArrayNames.size() ; count ++ ){
            mQRcode = new QRcode(mArrayNames.get(count) ,mArrayImages.get(count));
            qRcodeList.add(mQRcode);
        }
        InitRecyclerView();
    }

    //Intent pass to all activity...............

    @Override
    public void onqrClick(int position) {


        final Intent intent;
        switch (position){

            case 0:
                 intent = new Intent(getApplicationContext(),Email.class);
                intent.putExtra("qrcodename",qRcodeList.get(position).getQrcodename());
                startActivity(intent);
                break;
            case 1:
                 intent = new Intent(getApplicationContext(),Wifi.class);
                intent.putExtra("qrcodename",qRcodeList.get(position).getQrcodename());
                startActivity(intent);
                break;

            case 2:
                intent = new Intent(getApplicationContext(),Event.class);
                intent.putExtra("qrcodename",qRcodeList.get(position).getQrcodename());
                startActivity(intent);
                break;
            case 3:
                intent = new Intent(getApplicationContext(),Contact_info.class);
                intent.putExtra("qrcodename",qRcodeList.get(position).getQrcodename());
                startActivity(intent);
                break;

            case 4:
                intent = new Intent(getApplicationContext(),Text.class);
                intent.putExtra("qrcodename",qRcodeList.get(position).getQrcodename());
                startActivity(intent);
                break;
            case 5:
                intent = new Intent(getApplicationContext(),URL.class);
                intent.putExtra("qrcodename",qRcodeList.get(position).getQrcodename());
                startActivity(intent);
                break;

            case 6:
                intent = new Intent(getApplicationContext(),GeoLocation.class);
                intent.putExtra("qrcodename",qRcodeList.get(position).getQrcodename());
                startActivity(intent);
                break;
            case 7:
                intent = new Intent(getApplicationContext(),Phone.class);
                intent.putExtra("qrcodename",qRcodeList.get(position).getQrcodename());
                startActivity(intent);
                break;

            case 8:
                intent = new Intent(getApplicationContext(),SMS.class);
                intent.putExtra("qrcodename",qRcodeList.get(position).getQrcodename());
                startActivity(intent);
                break;
            case 9:
                intent = new Intent(getApplicationContext(),Product.class);
                intent.putExtra("qrcodename",qRcodeList.get(position).getQrcodename());
                startActivity(intent);
                break;

                default:
                    break;

        }

    }
    private void ArrayPut(){

        mArrayNames.add("Create QR Code for Email");
        mArrayNames.add("Create QR Code for Wifi");
        mArrayNames.add("Create QR Code for Event");
        mArrayNames.add("Create QR Code for Contact");
        mArrayNames.add("Create QR Code for Text");
        mArrayNames.add("Create QR Code for Website");
        mArrayNames.add("Create QR Code for Location");
        mArrayNames.add("Create QR Code for Phone");
        mArrayNames.add("Create QR Code for Message");
        mArrayNames.add("Create QR Code for Products");

        mArrayImages.add(R.drawable.ic_email);
        mArrayImages.add(R.drawable.ic_wifi);
        mArrayImages.add(R.drawable.ic_event);
        mArrayImages.add(R.drawable.ic_contact);
        mArrayImages.add(R.drawable.ic_text);
        mArrayImages.add(R.drawable.ic_url);
        mArrayImages.add(R.drawable.ic_location);
        mArrayImages.add(R.drawable.ic_phone);
        mArrayImages.add(R.drawable.ic_sms);
        mArrayImages.add(R.drawable.ic_product);
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
