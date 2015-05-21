package com.awong.micpower;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Perference {
    
    private SharedPreferences sp;
    public Boolean logToast;
    Context mContext;
    
    public Perference(Context mContext) {
        this.mContext = mContext;
        sp = mContext.getSharedPreferences("mic", Context.MODE_PRIVATE);
        logToast = sp.getBoolean("logToast", true);
    }
    
    public void setLogToast(Boolean value) {
        logToast = value;
        Editor editor = sp.edit();
        editor.putBoolean("logToast", value);
        editor.commit();
    }
    
    public Boolean getLogToast() {
        return logToast;
    }
}
