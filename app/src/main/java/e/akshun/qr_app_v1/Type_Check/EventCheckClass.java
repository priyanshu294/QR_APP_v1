package e.akshun.qr_app_v1.Type_Check;

import android.util.Log;

import com.google.zxing.client.result.CalendarParsedResult;

public class EventCheckClass {

    private static final String TAG = "EventCheckClass";

    private CalendarParsedResult mCalendarParsedResult ;

    public EventCheckClass(Object obj){
        this.mCalendarParsedResult = (CalendarParsedResult) obj ;
    }

    public String setEventSummary(){
        Log.d(TAG, "setEventSummary() "+mCalendarParsedResult.getSummary());
        if(mCalendarParsedResult.getSummary() == null) {
            return "";
        }else{
            return mCalendarParsedResult.getSummary() ;
        }
    }
    public String setEventDescp(){
        if(mCalendarParsedResult.getDescription() == null) {
            return "";
        }else{
            return mCalendarParsedResult.getDescription() ;
        }

    }
    public String setEventOrganizer(){

        if(mCalendarParsedResult.getOrganizer() == null) {
            return "";
        }else{
            return mCalendarParsedResult.getOrganizer() ;
        }
    }
    public String setEventloc(){
        if(mCalendarParsedResult.getLocation() == null) {
            return "";
        }else{
            return mCalendarParsedResult.getLocation() ;
        }
    }
    public long setEventStrDate(){
       return mCalendarParsedResult.getStartTimestamp();
    }
    public long setEventEndDate(){
        return  mCalendarParsedResult.getEndTimestamp();
    }
}
