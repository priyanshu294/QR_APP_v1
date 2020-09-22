package e.akshun.qr_app_v1;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

public class DownloadQRCode  {

    private Context mcontext;
    private ImageView mImageview;
    boolean mPermission = false;

    private static final String TAG = " DownloadQRCode Class";


    public DownloadQRCode(Context c, ImageView i) {
        this.mcontext = c;
        this.mImageview = i;
    }

    public  void callDownload(){
        Log.d(TAG, "saveToGallery() generated already now save ");
        BitmapDrawable bitmapDrawable = (BitmapDrawable) mImageview.getDrawable();
        Bitmap bitmap = bitmapDrawable.getBitmap();

        FileOutputStream fileOutputStream = null;
        File file = Environment.getExternalStorageDirectory();
        File dir = new File(file.getAbsolutePath() + "/Qr code");
        dir.mkdir();

        String filename = String.format("%d.png", System.currentTimeMillis());
        File outfile = new File(dir, filename);

        try {
            fileOutputStream = new FileOutputStream(outfile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(mcontext.getApplicationContext(), "QR code saved \nInternal storage/Qr code", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.d(TAG, "saveToGallery() EXCEPTION : " + e.getMessage());
            Toast.makeText(mcontext.getApplicationContext(), "Could not Download.!!!", Toast.LENGTH_SHORT).show();
        }
    }


}

