/**
 * Copyright (C) 2013-2014 EaseMob Technologies. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *     http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.easemob.chatuidemo.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.applib.controller.HXSDKHelper;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.easemob.chatuidemo.Constant;
import com.easemob.chatuidemo.DemoApplication;
import com.easemob.chatuidemo.DemoHXSDKHelper;
import com.easemob.R;
import com.easemob.chatuidemo.db.UserDao;
import com.easemob.chatuidemo.domain.User;
import com.easemob.chatuidemo.utils.CommonUtils;
import com.easemob.exceptions.EaseMobException;
import com.tencent.connect.auth.QQAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.exception.SocializeException;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * 登陆页面
 * 
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
	private static final String TAG = "LoginActivity";
	public static final int REQUEST_CODE_SETNICK = 1;
	private EditText usernameEditText;
	private EditText passwordEditText;

	private boolean progressShow;
	private boolean autoLogin = false;

	private String currentUsername;
	private String currentPassword;

	private ImageView mImgQQ, mImgWeichat;

	private String WEIXIN_ID = "wx18951bbedc95f9fa";
	private String WEIXIN_SECRET = "9b699f2cc3119a3aa79e7c8c684442c9";

	// QQ登录
	private Tencent mTencent;
	private com.tencent.connect.UserInfo userInfo = null;
	private QQAuth mQQAuth;
	private String action;
	// 微信登录
	private IWXAPI api;
	private boolean isLoginCancel = false;

	private SharedPreferences sp;

	// 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
	private UMSocialService mController = UMServiceFactory
			.getUMSocialService(Constant.DESCRIPTOR);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "1104948033", "QmwhqXG3NAdpJEuv");
		qqSsoHandler.addToSocialSDK();

		// 如果用户名密码都有，直接进入主页面
		if (DemoHXSDKHelper.getInstance().isLogined()) {
			autoLogin = true;
			startActivity(new Intent(LoginActivity.this, MainActivity.class));

			return;
		}
		setContentView(R.layout.activity_login);

		usernameEditText = (EditText) findViewById(R.id.username);
		passwordEditText = (EditText) findViewById(R.id.password);
		mImgQQ = ((ImageView) findViewById(R.id.img_qq));
		mImgWeichat = ((ImageView) findViewById(R.id.img_weichat));
		mImgQQ.setOnClickListener(this);
		mImgWeichat.setOnClickListener(this);

		// 如果用户名改变，清空密码
		usernameEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				passwordEditText.setText(null);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		if (DemoApplication.getInstance().getUserName() != null) {
			usernameEditText.setText(DemoApplication.getInstance().getUserName());
		}
	}

	/**
	 * 登录
	 * 
	 * @param view
	 */
	public void login(View view) {
		if (!CommonUtils.isNetWorkConnected(this)) {
			Toast.makeText(this, R.string.network_isnot_available, Toast.LENGTH_SHORT).show();
			return;
		}
		currentUsername = usernameEditText.getText().toString().trim();
		currentPassword = passwordEditText.getText().toString().trim();

		if (TextUtils.isEmpty(currentUsername)) {
			Toast.makeText(this, R.string.User_name_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(currentPassword)) {
			Toast.makeText(this, R.string.Password_cannot_be_empty, Toast.LENGTH_SHORT).show();
			return;
		}

		progressShow = true;
		final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				progressShow = false;
			}
		});
		pd.setMessage(getString(R.string.Is_landing));
		pd.show();

		final long start = System.currentTimeMillis();
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(currentUsername, currentPassword, new EMCallBack() {

			@Override
			public void onSuccess() {
				if (!progressShow) {
					return;
				}
				// 登陆成功，保存用户名密码
				DemoApplication.getInstance().setUserName(currentUsername);
				DemoApplication.getInstance().setPassword(currentPassword);

				try {
					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					// 处理好友和群组
					initializeContacts();
				} catch (Exception e) {
					e.printStackTrace();
					// 取好友或者群聊失败，不让进入主页面
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							DemoHXSDKHelper.getInstance().logout(true, null);
							Toast.makeText(getApplicationContext(), R.string.login_failure_failed,
									Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
						DemoApplication.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}
				if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
					pd.dismiss();
				}
				// 进入主页面
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);

				finish();
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				if (!progressShow) {
					return;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						pd.dismiss();
						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	public void thirdLogin(String screenName, String passwrod){
		progressShow = true;
		final ProgressDialog pd = new ProgressDialog(LoginActivity.this);
		pd.setCanceledOnTouchOutside(false);
		pd.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				progressShow = false;
			}
		});
		pd.setMessage(getString(R.string.Is_landing));
		pd.show();

		final long start = System.currentTimeMillis();
		// 调用sdk登陆方法登陆聊天服务器
		EMChatManager.getInstance().login(screenName, passwrod, new EMCallBack() {

			@Override
			public void onSuccess() {
				if (!progressShow) {
					return;
				}
				// 登陆成功，保存用户名密码
				DemoApplication.getInstance().setUserName(currentUsername);
				DemoApplication.getInstance().setPassword(currentPassword);

				try {
					// ** 第一次登录或者之前logout后再登录，加载所有本地群和回话
					// ** manually load all local groups and
					EMGroupManager.getInstance().loadAllGroups();
					EMChatManager.getInstance().loadAllConversations();
					// 处理好友和群组
					initializeContacts();
				} catch (Exception e) {
					e.printStackTrace();
					// 取好友或者群聊失败，不让进入主页面
					runOnUiThread(new Runnable() {
						public void run() {
							pd.dismiss();
							DemoHXSDKHelper.getInstance().logout(true, null);
							Toast.makeText(getApplicationContext(), R.string.login_failure_failed,
									Toast.LENGTH_SHORT).show();
						}
					});
					return;
				}
				// 更新当前用户的nickname 此方法的作用是在ios离线推送时能够显示用户nick
				boolean updatenick = EMChatManager.getInstance().updateCurrentUserNick(
						DemoApplication.currentUserNick.trim());
				if (!updatenick) {
					Log.e("LoginActivity", "update current user nick fail");
				}
				if (!LoginActivity.this.isFinishing() && pd.isShowing()) {
					pd.dismiss();
				}
				// 进入主页面
				Intent intent = new Intent(LoginActivity.this,
						MainActivity.class);
				startActivity(intent);

				finish();
			}

			@Override
			public void onProgress(int progress, String status) {
			}

			@Override
			public void onError(final int code, final String message) {
				if (!progressShow) {
					return;
				}
				runOnUiThread(new Runnable() {
					public void run() {
						pd.dismiss();
						Toast.makeText(getApplicationContext(), getString(R.string.Login_failed) + message,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

	private void initializeContacts() {
		Map<String, User> userlist = new HashMap<String, User>();
		// 添加user"申请与通知"
		User newFriends = new User();
		newFriends.setUsername(Constant.NEW_FRIENDS_USERNAME);
		String strChat = getResources().getString(
				R.string.Application_and_notify);
		newFriends.setNick(strChat);

		userlist.put(Constant.NEW_FRIENDS_USERNAME, newFriends);
		// 添加"群聊"
		User groupUser = new User();
		String strGroup = getResources().getString(R.string.group_chat);
		groupUser.setUsername(Constant.GROUP_USERNAME);
		groupUser.setNick(strGroup);
		groupUser.setHeader("");
		userlist.put(Constant.GROUP_USERNAME, groupUser);
		
		// 添加"Robot"
//		User robotUser = new User();
//		String strRobot = getResources().getString(R.string.robot_chat);
//		robotUser.setUsername(Constant.CHAT_ROBOT);
//		robotUser.setNick(strRobot);
//		robotUser.setHeader("");
//		userlist.put(Constant.CHAT_ROBOT, robotUser);
		
		// 存入内存
		((DemoHXSDKHelper)HXSDKHelper.getInstance()).setContactList(userlist);
		// 存入db
		UserDao dao = new UserDao(LoginActivity.this);
		List<User> users = new ArrayList<User>(userlist.values());
		dao.saveContactList(users);
	}
	
	/**
	 * 注册
	 * 
	 * @param view
	 */
	public void register(View view) {
		startActivityForResult(new Intent(this, RegisterActivity.class), 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (autoLogin) {
			return;
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch(id){
			case R.id.img_qq:
				qqLogin(SHARE_MEDIA.QQ);
				break;
			case R.id.img_weichat:
				break;
			default:
				break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 根据requestCode获取对应的SsoHandler
		UMSsoHandler ssoHandler = mController.getConfig().getSsoHandler(
				requestCode);
		if (ssoHandler != null) {
			Tencent.handleResultData(data, new LoginIUiListener());
			ssoHandler.authorizeCallBack(requestCode, resultCode, data);
			Log.i("TAG", "#### ssoHandler.authorizeCallBack");
		}
	}

	private void qqLogin(final SHARE_MEDIA platform) {
		mController.doOauthVerify(this, platform, new SocializeListeners.UMAuthListener() {

			@Override
			public void onStart(SHARE_MEDIA platform) {
//				Toast.makeText(LoginActivity.this, "start", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onError(SocializeException e, SHARE_MEDIA platform) {
			}

			@Override
			public void onComplete(Bundle value, SHARE_MEDIA platform) {
//				Toast.makeText(LoginActivity.this, "onComplete", Toast.LENGTH_SHORT).show();
				String uid = value.getString("uid");
				if (!TextUtils.isEmpty(uid)) {
					getUserInfo(platform);
				} else {
					Toast.makeText(LoginActivity.this, "授权失败...", Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onCancel(SHARE_MEDIA platform) {
				isLoginCancel = true;//取消
			}
		});
	}

	/**
	 * 获取授权平台的用户信息</br>
	 */
	private void getUserInfo(SHARE_MEDIA platform) {
		mController.getPlatformInfo(this, platform, new SocializeListeners.UMDataListener() {
			@Override
			public void onStart() {

			}
			@Override
			public void onComplete(int status, Map<String, Object> info) {
				// String showText = "";
				// if (status == StatusCode.ST_CODE_SUCCESSED) {
				// showText = "用户名：" + info.get("screen_name").toString();
				// Log.d("#########", "##########" + info.toString());
				// } else {
				// showText = "获取用户信息失败";
				// }

				String nickName = "861574834";
				String password = "yu2008271400";
				if(info.containsKey("screen_name")){
					nickName = (String) info.get("screen_name");
				}

				Log.i("TAG", info.toString());

				//登陆

				thirdLogin(nickName, password);
				if (info != null) {
					Toast.makeText(LoginActivity.this, info.toString(), Toast.LENGTH_LONG).show();
				}
			}
		});
	}


	class LoginIUiListener implements IUiListener {

		@Override
		public void onError(UiError arg0) {
			// TODO Auto-generated method stub

		}

		/**
		 * 返回json数据样例
		 *
		 * {"ret":0,"pay_token":"D3D678728DC580FBCDE15722B72E7365",
		 * "pf":"desktop_m_qq-10000144-android-2002-",
		 * "query_authority_cost":448,
		 * "authority_cost":-136792089,
		 * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
		 * "expires_in":7776000,
		 * "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
		 * "msg":"",
		 * "access_token":"A2455F491478233529D0106D2CE6EB45",
		 * "login_cost":499}
		 */
		@Override
		public void onComplete(Object value) {
			// TODO Auto-generated method stub

//			Toast.makeText(LoginActivity.this, value.toString(), Toast.LENGTH_SHORT).show();
			Log.i("TAG", value.toString());
		}

		@Override
		public void onCancel() {
			// TODO Auto-generated method stub

		}
	}
}
