package com.dujiajun.schoolsecretary;

import android.content.Intent;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

public class SettingsFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener{


    private EditTextPreference edit_username;
    private LocalBroadcastManager localBroadcastManager;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.preferences);
        edit_username = (EditTextPreference) findPreference("username");
        edit_username.setOnPreferenceChangeListener(this);

        localBroadcastManager= LocalBroadcastManager.getInstance(getActivity());
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        switch (preference.getKey()){
            case "username":
                if(newValue.toString().equals("")){
                    Toast.makeText(getActivity(), "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return false;
                }
                Intent intent = new Intent("com.dujiajun.dbstools.CHANGE_USERNAME_BROADCAST");
                intent.putExtra("username",newValue.toString());
                localBroadcastManager.sendBroadcast(intent);
                break;

        }
        return true;
    }
}
