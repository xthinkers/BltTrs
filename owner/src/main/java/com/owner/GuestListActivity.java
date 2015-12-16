package com.owner;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.owner.adapter.CommonListViewAdapter;
import com.owner.adapter.ViewHolder;
import com.owner.widget.HeaderView;

import java.util.ArrayList;
import java.util.List;

public class GuestListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, HeaderView.OnClickCallBack {

    private ListView mLvGuest;

    private HeaderView mHeaderView;
    private List<String> mGuestList = new ArrayList<String>();
    private CommonListViewAdapter mGuestListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_guest_list);

        initView();
    }

    private void initView() {
        mLvGuest = ((ListView) findViewById(R.id.gv_list));
        mHeaderView = ((HeaderView) findViewById(R.id.hv_guestlist));
        mHeaderView.setVisibility(HeaderView.HeadCompat.BACK, false);

        mLvGuest.setOnItemClickListener(this);
        mHeaderView.setOnClickCallBackLisenter(this);
        mHeaderView.setClilckListener(HeaderView.HeadCompat.RIGHT);

        for(int i = 0; i < 6; i++){
            mGuestList.add("访客"+(i+1));
        }
        setAdapter(mGuestList);
    }

    private void setAdapter(List<String> mOwnerList) {
        if(mGuestListAdapter == null){
            mGuestListAdapter = new CommonListViewAdapter(this, R.layout.layout_guest_item, mOwnerList) {
                @Override
                public void convert(ViewHolder viewHolder, Object item) {
                    viewHolder.setText(R.id.tv_id, (String)item);
                }
            };
            mLvGuest.setAdapter(mGuestListAdapter);
        }else {
            mGuestListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idStr = mGuestList.get(position);
        Intent intent = new Intent(this, ChatMessageActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClickCallBack(HeaderView.HeadCompat headCompat) {
        switch(headCompat){
            case RIGHT:
                // TODO: 15/12/12 跳转到设置界面
                Intent intent = new Intent(this, OwnerSettingActivity.class);
                startActivity(intent);
                break;
        }
    }
}
