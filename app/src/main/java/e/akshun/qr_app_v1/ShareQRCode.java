package e.akshun.qr_app_v1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;

public class ShareQRCode  {

    private Context mcontext;
    private ImageView mImageview;


    public ShareQRCode(Context c, ImageView i) {
        this.mcontext = c;
        this.mImageview = i;
    }



    public void callshare(){

        // share using File Provider
        Drawable drawable = mImageview.getDrawable();
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

        try {
            File file = new File(mcontext.getApplicationContext().getExternalCacheDir(), File.separator + "image.png");
            FileOutputStream fOut = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            Uri photoURI = FileProvider.getUriForFile(mcontext.getApplicationContext(), BuildConfig.APPLICATION_ID + ".provider", file);

            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setType("image/png");

            mcontext.startActivity(Intent.createChooser(intent, "Share QR Code via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
