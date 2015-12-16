package com.blttrs.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.blttrs.R;
import com.blttrs.adapter.CommonListViewAdapter;
import com.blttrs.adapter.ViewHolder;
import com.blttrs.widget.HeaderView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yhw(email:861574834@qq.com)
 * @date 2015-11-26 18:16
 * @package com.blttrs.activity.dialog
 * @description OwnerListActivity  TODO(界面功能描述)
 * @params TODO(进入界面传参描述)
 */
public class OwnerListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private GridView mGvOwnerList;

    private HeaderView mHeaderView;
    private List<String> mOwnerList = new ArrayList<String>();
    private CommonListViewAdapter mOwnerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_list);

        initView();

    }

    private void initView() {
        mGvOwnerList = ((GridView) findViewById(R.id.gv_owner_list));
        mHeaderView = ((HeaderView) findViewById(R.id.hv_ownerlist));
        mHeaderView.setVisibility(HeaderView.HeadCompat.BACK, false);
        mGvOwnerList.setOnItemClickListener(this);
        for(int i = 0; i < 30; i++){
            mOwnerList.add(100+(i+1)+"");
        }
        setAdapter(mOwnerList);

    }

    private void setAdapter(List<String> mOwnerList) {
        if(mOwnerListAdapter == null){
            mOwnerListAdapter = new CommonListViewAdapter(this, R.layout.layout_owner_item, mOwnerList) {
                @Override
                public void convert(ViewHolder viewHolder, Object item, int position) {
                    viewHolder.setText(R.id.tv_id, (String)item);
                }
            };
            mGvOwnerList.setAdapter(mOwnerListAdapter);
        }else {
            mOwnerListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String idStr = mOwnerList.get(position);
        Intent intent = new Intent(this, OwnerInfoActivity.class);
        startActivity(intent);
    }
}
