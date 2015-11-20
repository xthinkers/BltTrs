package com.blttrs.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blttrs.R;
import com.blttrs.utils.ToastUtils;

import java.io.File;

public class OwnerInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = "OwnerInfoActivity";

    private static final int CAPTURE_IMAGE_REQUEST_CODE = 100;

    private TextView mTvPhoneRecode1;
    private TextView mTvPhoneRecode2;
    private TextView mTvPhoneRecode3;

    private ImageView mImgContactsOne;
    private ImageView mImgContactsTwo;
    private ImageView mImgContactsThree;

    private TextView mTvMsg;//信息
    private TextView mTvRecord;//通话记录
    private TextView mTvPhoto;//照片

    private ImageView mImgAvatar;
    private TextView mTvSearch;//搜索
    private TextView mTvStatus;//连接状态

    private PhoneStateReceiver phoneStateReceiver;

    class PhoneStateReceiver extends BroadcastReceiver {

        public PhoneStateReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO: This method is called when the BroadcastReceiver is receiving
            // an Intent broadcast.
            String action = intent.getAction();
            // 拨打电话
            if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
                String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
                Log.i(TAG, "call out:" + phoneNumber);
                Intent phoneIntent = new Intent(OwnerInfoActivity.this, BTTrsActivity.class);
                phoneIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(phoneIntent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_owner_info);

        initView();

        phoneStateReceiver = new PhoneStateReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
        intentFilter.setPriority(1000);
        registerReceiver(phoneStateReceiver, intentFilter);
    }

    private void initView() {
        mImgAvatar = ((ImageView) findViewById(R.id.img_avatar));

        mTvPhoneRecode1 = ((TextView) findViewById(R.id.tv_phone1));
        mTvPhoneRecode2 = ((TextView) findViewById(R.id.tv_phone2));
        mTvPhoneRecode3 = ((TextView) findViewById(R.id.tv_phone3));

        mImgContactsOne = ((ImageView) findViewById(R.id.img_contacts_one));
        mImgContactsTwo = ((ImageView) findViewById(R.id.img_contacts_two));
        mImgContactsThree = ((ImageView) findViewById(R.id.img_contacts_three));

        mTvMsg = ((TextView) findViewById(R.id.tv_msg));
        mTvRecord = ((TextView) findViewById(R.id.tv_record));
        mTvPhoto = ((TextView) findViewById(R.id.tv_camera));

        mTvSearch = ((TextView) findViewById(R.id.tv_search));
        mTvStatus = ((TextView) findViewById(R.id.tv_status));

        mTvMsg.setOnClickListener(this);
        mTvRecord.setOnClickListener(this);
        mTvPhoto.setOnClickListener(this);

        mImgContactsOne.setOnClickListener(this);
        mImgContactsTwo.setOnClickListener(this);
        mImgContactsThree.setOnClickListener(this);

        mTvSearch.setOnClickListener(this);

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
            case R.id.tv_msg://信息记录
                Uri uri = Uri.parse("smsto://"+phoneNumber);
                Intent msgIntent = new Intent(Intent.ACTION_SENDTO, uri);
                startActivity(msgIntent);
                break;
            case R.id.tv_record://通话记录
                Intent recordIntent = new Intent();
                recordIntent.setAction(Intent.ACTION_CALL_BUTTON);
                startActivity(recordIntent);
                break;
            case R.id.tv_camera://照片
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                startActivity(cameraIntent);
                startActivityForResult(cameraIntent, CAPTURE_IMAGE_REQUEST_CODE);
                break;
            case R.id.tv_search:
                // TODO: 15/11/20 搜索蓝牙或者Wifi 
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 如果是拍照
        if (CAPTURE_IMAGE_REQUEST_CODE == requestCode && RESULT_OK == resultCode)
        {
            Log.i(TAG, "CAPTURE_IMAGE");
            // Check if the result includes a thumbnail Bitmap
            if (data != null)
            {
                // 没有指定特定存储路径的时候
                Log.i(TAG, " data is NOT null, file on default position.");
                // 指定了存储路径的时候（intent.putExtra(MediaStore.EXTRA_OUTPUT,fileUri);）
                // Image captured and saved to fileUri specified in the
                // Intent
                ToastUtils.showShort(this, "Image saved to:\n" + data.getData());
                if (data.hasExtra("data")) {
                    Bitmap thumbnail = data.getParcelableExtra("data");
                    mImgAvatar.setScaleType(ImageView.ScaleType.FIT_XY);
                    mImgAvatar.setImageBitmap(thumbnail);
                }
            } else {
                ToastUtils.showShort(this, " data is null ");
                Log.i(TAG, "data IS null, file saved on target position.");
                // If there is no thumbnail image data, the image
                // will have been stored in the target output URI.
                // Resize the full image to fit in out image view.
                int width = mImgAvatar.getWidth();
                int height = mImgAvatar.getHeight();

                BitmapFactory.Options factoryOptions = new BitmapFactory.Options();

                factoryOptions.inJustDecodeBounds = true;

                String fileUri = getExternalFilesDir("image").getPath()+File.separator + System.currentTimeMillis()
                        + ".png";

                File file = new File(fileUri);

                BitmapFactory.decodeFile(file.getAbsolutePath(), factoryOptions);

                int imageWidth = factoryOptions.outWidth;
                int imageHeight = factoryOptions.outHeight;

                // Determine how much to scale down the image
                int scaleFactor = Math.min(imageWidth / width, imageHeight
                        / height);
                // Decode the image file into a Bitmap sized to fill the
                // View
                factoryOptions.inJustDecodeBounds = false;
                factoryOptions.inSampleSize = scaleFactor;
                factoryOptions.inPurgeable = true;
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(),
                        factoryOptions);
                mImgAvatar.setImageBitmap(bitmap);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(phoneStateReceiver != null){
            unregisterReceiver(phoneStateReceiver);
        }
    }
}
