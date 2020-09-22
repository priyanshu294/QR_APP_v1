package e.akshun.qr_app_v1.Type_Check;

import com.google.zxing.client.result.AddressBookParsedResult;

public class ContactCheckClass {

    private static final String TAG = "ContactCheckClass";

    private AddressBookParsedResult mAddressBookParsedResult ;

    public ContactCheckClass(Object obj){
        this.mAddressBookParsedResult = (AddressBookParsedResult) obj ;
    }

    public String setContactName(){
        if(mAddressBookParsedResult.getNames() == null){
            return "" ;
        }else{
            return  mAddressBookParsedResult.getNames()[0] ;
        }
    }

    public String setContactPhone(){
        if(mAddressBookParsedResult.getPhoneNumbers() ==null){
            return "" ;
        }else{
            return  mAddressBookParsedResult.getPhoneNumbers()[0] ;
        }
    }
    public String setContactEmail(){
        if(mAddressBookParsedResult.getEmails() == null){
            return "" ;
        }else{
            return  mAddressBookParsedResult.getEmails()[0] ;
        }
    }
    public String setContactOrg(){
        if(mAddressBookParsedResult.getOrg() == null){
            return "" ;
        }else{
            return  mAddressBookParsedResult.getOrg() ;
        }
    }
    public String setContactUrl(){
        if(mAddressBookParsedResult.getURLs() == null){
            return "" ;
        }else{
            return  mAddressBookParsedResult.getURLs()[0] ;
        }
    }
    public String setContactAddress(){
        if(mAddressBookParsedResult.getAddresses() == null){
            return "" ;
        }else{
            return  mAddressBookParsedResult.getAddresses()[0] ;
        }
    }
}
