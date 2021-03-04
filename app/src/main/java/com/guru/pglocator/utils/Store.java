package com.guru.pglocator.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by shyam on 10-01-2018.
 */

public class Store {
   static SharedPreferences sharedPreferences;
   static SharedPreferences.Editor editor;
    public static  void userDetails(Context context, String username,String name,String type){
        sharedPreferences=context.getSharedPreferences("pgloc",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
      editor.putString("username",username);
      editor.putString("name",name);
      editor.putString("type",type);
        editor.commit();
    }






    public static HashMap<String,String> getUserDetails(Context context){
        sharedPreferences=context.getSharedPreferences("pgloc",Context.MODE_PRIVATE);

        HashMap map=new HashMap();
        map.put("username",sharedPreferences.getString("username",null));
        map.put("name",sharedPreferences.getString("name",null));
        map.put("type",sharedPreferences.getString("type",null));

        return map;

    }
public static  void logout(Context context){
    sharedPreferences=context.getSharedPreferences("pgloc",Context.MODE_PRIVATE);
    editor=sharedPreferences.edit();
    editor.clear();
    editor.commit();


}

}
