package com.guru.pglocator.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by shyam on 09-01-2018.
 */

public class ToastHelper {
   static Context context;
  static  String msg;


    public static void toastMsg(Context context,String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
