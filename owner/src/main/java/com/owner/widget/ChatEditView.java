package com.owner.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.owner.R;

/**
 * @author yhw(email:861574834@qq.com)
 * @date 2015-12-07 20:04
 * @package com.owner.widget
 * @description ChatEditView  TODO(消息发送控件)
 * @params TODO(进入界面传参描述)
 */
public class ChatEditView extends LinearLayout implements View.OnClickListener {

    private LayoutInflater mInflater;
    private Button mBtnSend;
    private EditText mEdtContent;
    private ImageView mImgContacts, mImgCall, mImgMessage;

    private OnClickActionListener mSendListener;

    public OnClickActionListener getmSendListener() {
        return mSendListener;
    }

    public void setmSendListener(OnClickActionListener mSendListener) {
        this.mSendListener = mSendListener;
    }

    public ChatEditView(Context context) {
        this(context, null);
    }

    public ChatEditView(Context context, AttributeSet attrs) {
        super(context, attrs);

        View root = mInflater.from(context).inflate(R.layout.layout_chat_edit, this);
        mImgContacts = ((ImageView) root.findViewById(R.id.img_phone));
        mImgCall = ((ImageView) root.findViewById(R.id.img_call));
        mImgMessage = ((ImageView) root.findViewById(R.id.img_message));
        mEdtContent = ((EditText) root.findViewById(R.id.edt_content));
        mBtnSend = ((Button) root.findViewById(R.id.btn_send));

        mEdtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = mEdtContent.getText().toString().trim();
                if(!TextUtils.isEmpty(str)){
                    mBtnSend.setClickable(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = mEdtContent.getText().toString().trim();
                if(!TextUtils.isEmpty(str)){
                    mBtnSend.setClickable(true);
                }
            }
        });
        mBtnSend.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_send:
                String content = mEdtContent.getText().toString().trim();
                mSendListener.onMessageSend(content);
                mEdtContent.setText("");
                break;
            default:
                break;
        }
    }

    public interface OnClickActionListener{
        void onMessageSend(String content);

        //联系人 按钮
        void onContactsActionClick();

        //拨号按钮
        void onCallActionClick();

        //信息按钮
        void onMessageActionClick();
    }
}
