package e.akshun.qr_app_v1;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button generateBtn,scanBtn;
    private ImageView qrImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        changeStatusBarColor();

        //qrvalue = findViewById(R.id.qrInput);
        generateBtn = findViewById(R.id.generateBtn);
        scanBtn = findViewById(R.id.scanBtn);
        qrImage = findViewById(R.id.qrPlaceHolder);

        scanBtn.setOnClickListener(this);
        generateBtn.setOnClickListener(this);

    }

    // Action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.about_us:
                Aboutus();
                break;
            case R.id.feedback :
                FeedbackQR() ;
                break;
            case R.id.privacy_policy :
                PrivacyPolicy();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onClick(View view) {
        if(view == scanBtn){
            startActivity(new Intent(MainActivity.this,Scanner.class));
        }
        if(view == generateBtn){
            startActivity(new Intent(MainActivity.this,Select_QR_Code.class));
        }
    }

    // About us page
    private void Aboutus() {
        startActivity(new Intent(MainActivity.this,AboutUS.class));

    }
    // Privacy Policy
    private void PrivacyPolicy() {
        startActivity(new Intent(MainActivity.this,Privacy_Policy.class));
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
