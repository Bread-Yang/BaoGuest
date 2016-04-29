package com.mdground.guest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import com.mdground.guest.MedicalAppliction;
import com.mdground.guest.R;
import com.mdground.guest.activity.settlement.SettlementConfirmActivity;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.ResponseCode;
import com.mdground.guest.api.base.ResponseData;
import com.mdground.guest.api.bean.Device;
import com.mdground.guest.api.server.clinic.GetClinic;
import com.mdground.guest.api.server.global.LoginEmployee;
import com.mdground.guest.api.utils.DeviceIDUtil;
import com.mdground.guest.api.utils.DeviceUtils;
import com.mdground.guest.api.utils.NetworkStatusUtil;
import com.mdground.guest.api.utils.SharedPreferUtils;
import com.mdground.guest.bean.Clinic;
import com.mdground.guest.bean.Employee;
import com.mdground.guest.constant.MemberConstant;
import com.mdground.guest.dialog.LoadingDialog;
import com.mdground.guest.util.MdgConfig;
import com.mdground.guest.util.PreferenceUtils;
import com.mdground.guest.view.ResizeLayout;
import com.socks.library.KLog;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import org.apache.http.Header;

public class LoginActivity extends Activity implements OnClickListener, ResizeLayout.OnResizeListener {

	private ResizeLayout LoginRootLayout;
	private ScrollView scrollView;
	private EditText et_account, et_password;

	private LoadingDialog mLoadIngDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		findViewById();
		setListener();

		mLoadIngDialog = new LoadingDialog(this).initText(getResources().getString(R.string.logining));

		PreferenceUtils.setPrefInt(getApplicationContext(), MemberConstant.LOGIN_STATUS, MemberConstant.LOGIN_OUT);

		autoLogin();
		
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void findViewById() {
		LoginRootLayout = (ResizeLayout) this.findViewById(R.id.login_root_layout);
		scrollView = (ScrollView) this.findViewById(R.id.scrollView);
		et_account = (EditText) findViewById(R.id.et_account);
		et_password = (EditText) findViewById(R.id.et_password);

		String username = PreferenceUtils.getPrefString(this, MemberConstant.USERNAME, "");
		if (username != null) {
			et_account.setText(username);
		}
	}

	private void setListener() {
		LoginRootLayout.setOnResizeListener(this);
	}

	private void autoLogin() {
		// 自动登录
		String username = PreferenceUtils.getPrefString(LoginActivity.this, MemberConstant.USERNAME, null);
		String password = PreferenceUtils.getPrefString(LoginActivity.this, MemberConstant.PASSWORD, null);

		if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {

			login(username, password);
		}
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {

		case R.id.btn_login:

			if (!NetworkStatusUtil.isConnected(this)) {
				Toast.makeText(this, "当前网络不可用，请检查网络设置", Toast.LENGTH_SHORT).show();
				return;
			}

			String acccount = et_account.getText().toString().trim();
			String password = et_password.getText().toString().trim();

			if (TextUtils.isEmpty(acccount)) {
				Toast.makeText(getApplicationContext(), "请输入账号", Toast.LENGTH_SHORT).show();
				return;
			}

			if (TextUtils.isEmpty(password)) {
				Toast.makeText(getApplicationContext(), "请输入密码", Toast.LENGTH_SHORT).show();
				return;
			}

			int deviceID = PreferenceUtils.getPrefInt(getApplicationContext(), MemberConstant.DEVICE_ID, -1);

			login(acccount, password);

			break;

		}
	}

	@Override
	public void OnResize(int w, final int h, int oldw, final int oldh) {
		Handler handler = new Handler(Looper.getMainLooper());
		handler.post(new Runnable() {

			@Override
			public void run() {
				int offset = oldh - h;
				if (offset > 0) {
					scrollView.scrollTo(0, offset);
				}
			}
		});
	}

	private void login(final String userName, final String password) {

		Device device = DeviceUtils.getDeviceInfo(getApplicationContext());

		device.setDeviceID(new DeviceIDUtil().getDeviceID());

		String token = XGPushConfig.getToken(getApplicationContext());
		KLog.e("信鸽token是 :" + token	);

		if (token == null || "".equals(token)) {
			Toast.makeText(this, "信鸽token为空,登录失败", Toast.LENGTH_SHORT).show();
			XGPushManager.registerPush(getApplicationContext());
			return;
		}
		device.setDeviceToken(token);

		new LoginEmployee(this).loginEmployee(userName, password, device, new RequestCallBack() {

			@Override
			public void onStart() {
				mLoadIngDialog.show();
			}

			@Override
			public void onFinish() {

			}

			@Override
			public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				mLoadIngDialog.dismiss();
			}

			@Override
			public void onSuccess(ResponseData response) {

				ResponseCode responseCode = ResponseCode.valueOf(response.getCode());

				switch (responseCode) {
					case Normal: {
						Employee employee = response.getContent(Employee.class);

						if ((employee.getEmployeeRole() & Employee.KE_XIAN_SCREEN) == 0) {
							mLoadIngDialog.dismiss();
							Toast.makeText(getApplicationContext(), "账号异常,请联系客服", Toast.LENGTH_LONG).show();
							return;
						}

						((MedicalAppliction) LoginActivity.this.getApplication()).setLoginEmployee(employee);

						new SharedPreferUtils(getApplicationContext()).put(SharedPreferUtils.ShareKey.DEVICE_ID, employee.getDeviceID());

						PreferenceUtils.setPrefLong(getApplicationContext(), MemberConstant.LOGIN_EMPLOYEE,
								employee.getEmployeeID());
						PreferenceUtils.setPrefInt(getApplicationContext(), MemberConstant.LOGIN_STATUS,
								MemberConstant.LOGIN_IN);
						PreferenceUtils.setPrefString(getApplicationContext(), MemberConstant.USERNAME, employee.getLoginID());
//						PreferenceUtils.setPrefString(getApplicationContext(), MemberConstant.PASSWORD, employee.getLoginPwd());
						PreferenceUtils.setPrefString(getApplicationContext(), MemberConstant.PASSWORD, password);
						PreferenceUtils.setPrefInt(getApplicationContext(), MemberConstant.DEVICE_ID, employee.getDeviceID());
						MdgConfig.setDeviceId(employee.getDeviceID());
						new DeviceIDUtil().saveDeviceIDToSDCard(employee.getDeviceID());

						getClinic();

						break;
					}

					case AppCustom0:
					case AppCustom1:
					case AppCustom2:
					case AppCustom3:
					case AppCustom4:
					case AppCustom5:
					case AppCustom6:
					case AppCustom7:
					case AppCustom8:
					case AppCustom9: {
						mLoadIngDialog.dismiss();
						Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
						break;
					}
				}

			}
		});
	}

	private void getClinic() {
		new GetClinic(getApplicationContext()).getClinic(new RequestCallBack() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
				mLoadIngDialog.dismiss();
            }

            @Override
            public void onFinish() {
				mLoadIngDialog.dismiss();
            }

            @Override
            public void onSuccess(ResponseData response) {
                if (response.getCode() == ResponseCode.Normal.getValue()) {
                    MedicalAppliction application = (MedicalAppliction) getApplication();

                    application.mClinic = response.getContent(Clinic.class);

					Intent intent = new Intent(LoginActivity.this, SettlementConfirmActivity.class);
					startActivity(intent);
					finish();
                }
            }
        });
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
