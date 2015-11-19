package com.blttrs.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blttrs.R;

public class OwnerInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTvPhoneRecode1;
    private TextView mTvPhoneRecode2;
    private TextView mTvPhoneRecode3;
    private ImageView mImgContactsOne;
    private ImageView mImgContactsTwo;
    private ImageView mImgContactsThree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_owner_info);

        initView();
    }

    private void initView() {
        mTvPhoneRecode1 = ((TextView) findViewById(R.id.tv_phone1));
        mTvPhoneRecode2 = ((TextView) findViewById(R.id.tv_phone2));
        mTvPhoneRecode3 = ((TextView) findViewById(R.id.tv_phone3));
        mImgContactsOne = ((ImageView) findViewById(R.id.img_contacts_one));
        mImgContactsTwo = ((ImageView) findViewById(R.id.img_contacts_two));
        mImgContactsThree = ((ImageView) findViewById(R.id.img_contacts_three));

        mImgContactsOne.setOnClickListener(this);
        mImgContactsTwo.setOnClickListener(this);
        mImgContactsThree.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String phoneNumber = "";
        switch (id) {
            case R.id.img_contacts_one:
                //拨打电话
                phoneNumber = "17095656131";
                Intent intentPhone = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
                startActivity(intentPhone);
                break;
            case R.id.img_contacts_two:
                //跳转到联系人界面
                phoneNumber = "18254863495";
                break;
            case R.id.img_contacts_three:
                //跳转到联系人界面
                phoneNumber = "18353123125";
                Intent contactsIntent = new Intent();
                contactsIntent.setAction(Intent.ACTION_PICK);
                contactsIntent.setData(Contacts.People.CONTENT_URI);
                startActivity(contactsIntent);
                break;
        }
    }
}
