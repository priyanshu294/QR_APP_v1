package e.akshun.qr_app_v1.Type_Check;

import com.google.zxing.client.result.EmailAddressParsedResult;

public class EmailCheckClass {
    private static final String TAG ="EmailCheckClass";

    private EmailAddressParsedResult mEmailAddressParsedResult ;

    public EmailCheckClass(Object object){
        this.mEmailAddressParsedResult = (EmailAddressParsedResult)object;
    }

    public String SetEmailTo(){
        if(mEmailAddressParsedResult.getTos() != null){
            return mEmailAddressParsedResult.getTos()[0] ;
        }else{
            return "";
        }
    }
    public String SetEmailSubject(){
        return mEmailAddressParsedResult.getSubject();
    }
    public String SetEmailBody(){
        return mEmailAddressParsedResult.getBody();
    }
}

