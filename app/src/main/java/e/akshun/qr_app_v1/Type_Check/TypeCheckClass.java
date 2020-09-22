package e.akshun.qr_app_v1.Type_Check;

import android.util.Log;

import com.google.zxing.Result;
import com.google.zxing.client.result.AddressBookAUResultParser;
import com.google.zxing.client.result.AddressBookParsedResult;
import com.google.zxing.client.result.CalendarParsedResult;
import com.google.zxing.client.result.EmailAddressParsedResult;
import com.google.zxing.client.result.GeoParsedResult;
import com.google.zxing.client.result.ParsedResult;
import com.google.zxing.client.result.ResultParser;
import com.google.zxing.client.result.SMSParsedResult;
import com.google.zxing.client.result.TelParsedResult;
import com.google.zxing.client.result.URIParsedResult;
import com.google.zxing.client.result.WifiParsedResult;

public class TypeCheckClass {

    private static final String TAG = "TypeCheckClass";

    private Result mResult ;
    private QRTypeListner mQRTypeListner ;

    public TypeCheckClass(QRTypeListner qrTypeListner){
        this.mQRTypeListner = qrTypeListner ;
    }

    public interface QRTypeListner{
        void QRType(String Type, Object typeObject);
    }

    public void GetTypeOfQR(Result result){
        this.mResult = result ;
        ParsedResult parserdResult = ResultParser.parseResult(mResult);
        Log.d(TAG, "TYPE: " + parserdResult.getType().toString());
        String type = parserdResult.getType().toString();

        switch (parserdResult.getType()) {

            case EMAIL_ADDRESS:
                Log.d(TAG, "EMAIL_ADDRESS: " + parserdResult.getDisplayResult());
                EmailAddressParsedResult email = (EmailAddressParsedResult) parserdResult;
                mQRTypeListner.QRType(type,email);

                break;
            case URI:
                URIParsedResult uri = (URIParsedResult) parserdResult;
                Log.d(TAG, "URI: " + parserdResult.getDisplayResult());
                mQRTypeListner.QRType(type,uri);
                break;
            case GEO:
                GeoParsedResult geo = (GeoParsedResult) parserdResult;
                Log.d(TAG, "GEO: " + parserdResult.getDisplayResult());
                mQRTypeListner.QRType(type,geo);
                break;
            case TEL:
                TelParsedResult tel = (TelParsedResult) parserdResult;
                Log.d(TAG, "TEL: " + parserdResult.getDisplayResult());
                mQRTypeListner.QRType(type,tel);
                break;
            case SMS:
                Log.d(TAG, "SMS: " + parserdResult.getDisplayResult());
                SMSParsedResult sms = (SMSParsedResult) parserdResult;
                mQRTypeListner.QRType(type,sms);
                break;
            case WIFI:
                Log.d(TAG, "WIFI: "+parserdResult.getDisplayResult());
                WifiParsedResult wifi = (WifiParsedResult)parserdResult ;
                mQRTypeListner.QRType(type,wifi);
                break ;
            case ADDRESSBOOK:
                Log.d(TAG, "ADDRESSBOOK : "+parserdResult.getDisplayResult());
                AddressBookParsedResult addressBook = (AddressBookParsedResult) parserdResult ;
                mQRTypeListner.QRType(type,addressBook);
                break ;
            case CALENDAR:
                Log.d(TAG, "CALENDER EVENT : "+parserdResult.getDisplayResult());
                CalendarParsedResult calendarParsedResult = (CalendarParsedResult) parserdResult ;
                mQRTypeListner.QRType(type,calendarParsedResult);
                break ;
            default:
                mQRTypeListner.QRType(type,null);

        }
    }

}
