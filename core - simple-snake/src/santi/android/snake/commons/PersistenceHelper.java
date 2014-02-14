package santi.android.snake.commons;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;


public class PersistenceHelper {

	public static boolean persist(Context context, String key, boolean value, String prefsName) {
		
		if(key == null || key.equals(""))
			return false;
		
		Editor editor = getSharedPreferences(context, prefsName).edit();
		editor.putBoolean(key, value);
		editor.commit();
		return true;
	}
	
	public static boolean persist(Context context, String key, float value, String prefsName) {
		
		if(key == null || key.equals(""))
			return false;
		
		Editor editor = getSharedPreferences(context, prefsName).edit();
		editor.putFloat(key, value);
		editor.commit();
		return true;
	}
	
	public static boolean persist(Context context, String key, int value, String prefsName) {
		
		if(key == null || key.equals(""))
			return false;
		
		Editor editor = getSharedPreferences(context, prefsName).edit();
		editor.putInt(key, value);
		editor.commit();
		return true;
	}
	
	public static boolean persist(Context context, String key, long value, String prefsName) {
		
		if(key == null || key.equals(""))
			return false;
		
		Editor editor = getSharedPreferences(context, prefsName).edit();
		editor.putLong(key, value);
		editor.commit();
		return true;
	}
	
	public static boolean persist(Context context, String key, String value, String prefsName) {
		
		if(key == null || key.equals(""))
			return false;
		
		Editor editor = getSharedPreferences(context, prefsName).edit();
		editor.putString(key, value);
		editor.commit();
		return true;
	}
	
	//- ####################################################################################################
	//- PRIVATE METHODS
	//- ####################################################################################################
	
	private static SharedPreferences getSharedPreferences(Context context, String name) {
		
		if(name != null && !name.equals(""))
			return context.getSharedPreferences(name, Context.MODE_PRIVATE);
		else
			return PreferenceManager.getDefaultSharedPreferences(context);
	}
}
