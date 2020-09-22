package e.akshun.qr_app_v1;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
//import android.provider.CalendarContract;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;


import android.os.Bundle;

public class AboutUS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_u_s);
       getSupportActionBar().setDisplayHomeAsUpEnabled(false);
       changeStatusBarColor();

        Element adsElement = new Element();
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setDescription("Easy QR Code App is a very simple and useful application " +
                        "which helps you to create your own custom QR-code image. You can use this QR code image " +
                        "for advertisement, to share information, to become a part of modern world... This application " +
                        "has both features to generate and scan the QR code .")
                .addItem(new Element().setTitle("Version 1.0"))
                .addGroup("CONNECT WITH US :")
                .addEmail("narendok@gmail.com")
                .addWebsite("https://slogfy.com/")
                .addYoutube("")
                .addPlayStore("")
                .addInstagram("RoboSlog")
                .addItem(createCopyright())
                .create();
        setContentView(aboutPage);
    }
    private Element createCopyright()
    {
        Element copyright = new Element();
        @SuppressLint("DefaultLocale") final String copyrightString = String.format("Developed by Slogfy Co. %d",
                Calendar.getInstance().get(Calendar.YEAR));
        copyright.setTitle(copyrightString);
        // copyright.setIconDrawable(R.drawable.icon_image);
        copyright.setGravity(Gravity.CENTER);
        copyright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUS.this,copyrightString,Toast.LENGTH_SHORT).show();
            }
        });
        return copyright;
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