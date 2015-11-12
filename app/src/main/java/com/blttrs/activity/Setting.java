package com.blttrs.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.blttrs.R;


/**
 * the setting activity
 */
public class Setting extends PreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 所的的值将会自动保存到SharePreferences
		addPreferencesFromResource(R.layout.setting);
	}
}