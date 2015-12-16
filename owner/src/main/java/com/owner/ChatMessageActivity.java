package com.owner;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.owner.adapter.CommonListViewAdapter;
import com.owner.adapter.ViewHolder;
import com.owner.domain.ChatMessage;
import com.owner.widget.ChatEditView;
import com.owner.widget.HeaderView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yhw(email:861574834@qq.com)
 * @date 2015-12-07 19:49
 * @package com.owner
 * @description ChatMessageActivity  TODO(信息发送接收界面)
 * @params TODO(进入界面传参描述)
 */
public class ChatMessageActivity extends Activity implements ChatEditView.OnClickActionListener {

    private HeaderView mHeaderView;

    private ListView mLvChat;
    private List<ChatMessage> mData;
    private CommonListViewAdapter mAdapter;

    private ChatEditView mChatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mData = new ArrayList<ChatMessage>();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.content_chat_message);

        initView();
        setAdapter();
    }

    private void initView() {
        mHeaderView = ((HeaderView) findViewById(R.id.hv_chat_message));
        mHeaderView.setActivity(this);
        mHeaderView.setVisibility(HeaderView.HeadCompat.RIGHT, false);
        mLvChat = ((ListView) findViewById(R.id.lv_chat));

        mChatView = ((ChatEditView) findViewById(R.id.cet_chat));
        mChatView.setmSendListener(this);
    }

    @Override
    public void onMessageSend(String content) {
        // TODO: 15/12/7 向对方发送消息
        if(TextUtils.isEmpty(content)){
            return;
        }
        ChatMessage message = new ChatMessage();
        message.setId(0);
        message.setFromId(1);
        message.setToId(2);
        message.setContent(content);
        message.setType(2);
        mData.add(message);
        setAdapter();
    }

    @Override
    public void onContactsActionClick() {
        //联系人按钮被点击
    }

    @Override
    public void onCallActionClick() {
        //拨号键盘按钮被点击
    }

    @Override
    public void onMessageActionClick() {
        //我的消息按钮被点击
    }

    private void setAdapter(){
        if(mAdapter == null){
            mAdapter = new CommonListViewAdapter(this, R.layout.layout_item_message, mData) {

                @Override
                public void convert(ViewHolder viewHolder, Object item) {
                    ChatMessage chatMessage = (ChatMessage) item;
                    ImageView leftAvatar = viewHolder.getView(R.id.avatar_left);
                    ImageView rightAvatar = viewHolder.getView(R.id.avatar_right);
                    TextView leftMessage = viewHolder.getView(R.id.tv_left_message);
                    TextView rightMessage = viewHolder.getView(R.id.tv_right_message);
                    switch (chatMessage.getType()) {
                        case 1://表示接收到的消息
                            rightAvatar.setVisibility(View.GONE);
                            rightMessage.setVisibility(View.GONE);

                            leftMessage.setText(chatMessage.getContent());
                            break;
                        case 2://表示发送的消息
                            leftAvatar.setVisibility(View.GONE);
                            leftMessage.setVisibility(View.GONE);

                            rightMessage.setText(chatMessage.getContent());
                            break;
                    }
                }
            };
            mLvChat.setAdapter(mAdapter);
        }
        mAdapter.notifyDataSetChanged();
        mLvChat.setSelection(mData.size() - 1);
    }

}
