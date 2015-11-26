package com.blttrs.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.blttrs.R;
import com.blttrs.adapter.CommonListAdapter;
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
    private CommonListAdapter mOwnerListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_list);

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
            mOwnerListAdapter = new CommonListAdapter(this, R.layout.layout_owner_item, mOwnerList) {

                OwnerHolder viewHolder;
                String  id;
                @Override
                public Holder initView(View container) {
                    viewHolder = new OwnerHolder();
                    viewHolder.mId = ((TextView) container.findViewById(R.id.tv_id));
                    return viewHolder;
                }

                @Override
                public void initData(int position, Holder holder, Object obj) {
                    id = (String) obj;
                    viewHolder = (OwnerHolder) holder;
                    viewHolder.mId.setText(id);
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

    static class OwnerHolder extends CommonListAdapter.Holder{
        TextView mId;
    }
}
