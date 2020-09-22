package e.akshun.qr_app_v1.Type_Check;

import android.net.Uri;

import com.google.zxing.client.result.TelParsedResult;

public class PhoneCheckClass {

    private static final String TAG ="PhoneCheckClass";

    private TelParsedResult mTelParsedResult ;

    public PhoneCheckClass(Object object){
        this.mTelParsedResult = (TelParsedResult) object;
    }

    public String SetPhoneNumber(){
        return mTelParsedResult.getNumber() ;
    }
    public Uri SetTelUri(){
        return Uri.parse(mTelParsedResult.getTelURI());
    }

}
