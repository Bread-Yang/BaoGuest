package com.mdground.guest.activity.settlement;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mdground.guest.R;
import com.mdground.guest.activity.LoginActivity;
import com.mdground.guest.activity.base.BaseActivity;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.ResponseCode;
import com.mdground.guest.api.base.ResponseData;
import com.mdground.guest.api.server.clinic.PayBilling;
import com.mdground.guest.api.server.global.LogoutEmployee;
import com.mdground.guest.constant.MemberConstant;
import com.mdground.guest.util.PreferenceUtils;
import com.mdground.guest.util.StringUtils;
import com.socks.library.KLog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class SettlementConfirmActivity extends BaseActivity implements OnClickListener {

    private LinearLayout llt_already_pay, llt_coupon, llt_cash_charge, llt_change;
    private TextView tv_name, tv_receivable_fee, tv_already_pay, tv_coupon, tv_real_fee_title, tv_change, tv_real_fee_value, tv_cash_charge;
    private Button btn_confirm;

    private Dialog dialog_logout;
    private EditText et_password;

    private CustomXGPushReceiver mXGReceiver;

    private String mPatientName;
    private int mTradePlatfrom, mTotalFeeAdjust, mTotalFeeReal, mTotalFeeRealCash, mBillingID;
    private int mBillingType = 3;

    private boolean mIsRefund, mIsTextChangeManual;

    class CustomXGPushReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MemberConstant.XG_INTENT_TAG.equals(intent.getAction())) {
//                Toast.makeText(SettlementConfirmActivity.this, "收到推送", Toast.LENGTH_SHORT).show();

//                KLog.e("收到推送");

                String title = intent.getStringExtra("title");
                String content = intent.getStringExtra("content");
                String customContent = intent.getStringExtra("customContent");

                KLog.e("拿到的title : " + title);
                KLog.e("拿到的content : " + content);
                KLog.e("拿到的customContent : " +
                        customContent);

                if (!StringUtils.isEmpty(customContent)) {
                    try {
                        JSONObject json = new JSONObject(customContent);
                        String functionName = json.getString("FunctionName");

                        if ("OfficeVisitBillingMessage".equals(functionName)) {
                            JSONObject data = new JSONObject(json.getString("Data"));

                            // 病人姓名
                            mPatientName = data.getString("PatientName");
                            mTotalFeeAdjust = data.getInt("TotalFeeAdjust");
                            mTotalFeeReal = data.getInt("TotalFeeReal");
                            mTotalFeeRealCash = data.getInt("TotalFeeRealCash");

                            refreshFee();
                        } else if ("OfficeVisitBillingFinished".equals(functionName)) {
                            clearLayout();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_confirm);
        findView();
        initMemberData();
        initView();
        setListener();

        mXGReceiver = new CustomXGPushReceiver();
        registerReceiver(mXGReceiver, new IntentFilter(MemberConstant.XG_INTENT_TAG));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mXGReceiver);
    }

    @Override
    public void findView() {
        llt_already_pay = (LinearLayout) findViewById(R.id.llt_already_pay);
        llt_coupon = (LinearLayout) findViewById(R.id.llt_coupon);
        llt_cash_charge = (LinearLayout) findViewById(R.id.llt_cash_charge);
        llt_change = (LinearLayout) findViewById(R.id.llt_change);

        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_receivable_fee = (TextView) findViewById(R.id.tv_receivable_fee);
        tv_already_pay = (TextView) findViewById(R.id.tv_already_pay);
        tv_coupon = (TextView) findViewById(R.id.tv_coupon);
        tv_real_fee_title = (TextView) findViewById(R.id.tv_real_fee);
        tv_change = (TextView) findViewById(R.id.tv_change);

        tv_real_fee_value = (TextView) findViewById(R.id.tv_real_fee_value);
        tv_cash_charge = (TextView) findViewById(R.id.tv_cash_charge);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);

        {
            dialog_logout = new Dialog(this, R.style.patient_detail_dialog);
            dialog_logout.setContentView(R.layout.dialog_input);

            et_password = (EditText) dialog_logout.findViewById(R.id.et_password);

            dialog_logout.findViewById(R.id.btn_cancle).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_logout.dismiss();
                }
            });

            dialog_logout.findViewById(R.id.btn_sure).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog_logout.dismiss();

                    String loginPassword = PreferenceUtils.getPrefString(SettlementConfirmActivity.this, MemberConstant.PASSWORD, null);
                    if (loginPassword.equals(et_password.getText().toString())) {
                        logout();
                    } else {
                        showToast(R.string.invalid_password);
                    }
                }
            });
        }
    }

    @Override
    public void initMemberData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void setListener() {
        findViewById(R.id.tv_log_out).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_logout.show();
            }
        });
    }

    private void clearLayout() {
        llt_already_pay.setVisibility(View.VISIBLE);
        llt_change.setVisibility(View.VISIBLE);
        llt_coupon.setVisibility(View.VISIBLE);
        llt_cash_charge.setVisibility(View.VISIBLE);

        tv_name.setText("");
        tv_receivable_fee.setText("");
        tv_already_pay.setText("");
        tv_coupon.setText("");
        tv_real_fee_value.setText("");
        tv_cash_charge.setText("");
        tv_change.setText("");
    }

    private void refreshFee() {
        // 姓名
        tv_name.setText(mPatientName);

        // 应收
        tv_receivable_fee.setText(String.format("%.2f", mTotalFeeAdjust / 100f) + getResources().getString(R.string.yuan));


//        if (mTotalFeeReal > 0) {
//            tv_real_fee_title.setText(R.string.real_fee);
//            tv_already_pay.setText(String.format("%.2f", Float.valueOf(mTotalFeeReal) / 100) + getResources().getString(R.string.yuan));
//        } else {
//            tv_real_fee_title.setText(R.string.real_refund);
//            tv_already_pay.setText(String.format("%.2f", Math.abs(Float.valueOf(mTotalFeeAdjust - mTotalFeeReal)) / 100) + getResources().getString(R.string.yuan));
//        }
//
//        tv_real_fee_value.setText(String.format("%.2f",  Math.abs(mTotalFeeReal) / 100f) + getResources().getString(R.string.yuan));
//
//        String receivableString = String.format("%.2f", mTotalFeeAdjust / 100f);

        // 退款
        if (mTotalFeeReal < 0) {
            mIsRefund = true;

            tv_real_fee_title.setText(R.string.real_refund);
            tv_already_pay.setText(String.format("%.2f", Math.abs(Float.valueOf(mTotalFeeAdjust - mTotalFeeReal)) / 100) + getResources().getString(R.string.yuan));
            llt_already_pay.setVisibility(View.VISIBLE);
            llt_change.setVisibility(View.GONE);
            llt_coupon.setVisibility(View.GONE);
            llt_cash_charge.setVisibility(View.GONE);

            String couponString = String.format("%.2f", (mTotalFeeReal - mTotalFeeAdjust) / 100f);

            tv_coupon.setText(couponString + getResources().getString(R.string.yuan));
        } else {
            tv_real_fee_title.setText(R.string.real_fee);
            tv_already_pay.setText(String.format("%.2f", Float.valueOf(mTotalFeeReal) / 100) + getResources().getString(R.string.yuan));
            llt_already_pay.setVisibility(View.GONE);
            llt_coupon.setVisibility(View.VISIBLE);
            llt_change.setVisibility(View.VISIBLE);
            llt_cash_charge.setVisibility(View.VISIBLE);
            tv_coupon.setText("0.00" + getResources().getString(R.string.yuan));
        }

        // 优惠
        tv_coupon.setText(String.format("%.2f", (mTotalFeeAdjust - mTotalFeeReal) / 100f) + getResources().getString(R.string.yuan));

        // 实收或实退
        tv_real_fee_value.setText(String.format("%.2f", Math.abs(mTotalFeeReal) / 100f) + getResources().getString(R.string.yuan));

        // 现金收费
        tv_cash_charge.setText(String.format("%.2f", mTotalFeeRealCash / 100f) + getResources().getString(R.string.yuan));
        // 找零
        tv_change.setText(String.format("%.2f", (mTotalFeeRealCash - mTotalFeeReal) / 100f) + getResources().getString(R.string.yuan));

    }

    private void logout() {
        LogoutEmployee logoutEmployee = new LogoutEmployee(getApplicationContext());
        logoutEmployee.logoutEmployee(new RequestCallBack() {

            @Override
            public void onStart() {
                // mView.showProgress();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(ResponseData response) {
                if (response.getCode() == ResponseCode.Normal.getValue()) {
                    PreferenceUtils.setPrefInt(SettlementConfirmActivity.this, MemberConstant.LOGIN_STATUS,
                            MemberConstant.LOGIN_OUT);
                    PreferenceUtils.setPrefString(SettlementConfirmActivity.this, MemberConstant.PASSWORD, "");
                    Intent intent = new Intent();
                    intent.setClass(SettlementConfirmActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                int real_fee = 0;
                if (!StringUtils.isEmpty(tv_real_fee_value.getText().toString())) {
                    real_fee = (int) (Float.valueOf(StringUtils.trimUnit(tv_real_fee_value.getText().toString())) * 100);
                    if (mIsRefund) {
                        real_fee *= -1;
                    }
                }

                int cash_charge = 0;
                if (!StringUtils.isEmpty(tv_cash_charge.getText().toString())) {
                    cash_charge = (int) (Float.valueOf(StringUtils.trimUnit(tv_cash_charge.getText().toString())) * 100);
                }

                if (cash_charge - real_fee < 0) {
                    Toast.makeText(SettlementConfirmActivity.this, R.string.cash_insufficient, Toast.LENGTH_SHORT).show();
                    return;
                }

                new PayBilling(getApplicationContext()).payBilling(1, real_fee, mBillingID, mBillingType, new RequestCallBack() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

                    }

                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onSuccess(ResponseData response) {
                        if (response.getCode() == ResponseCode.Normal.getValue()) {
                            setResult(RESULT_OK);
                            finish();
                        } else if (response.getCode() == ResponseCode.AppCustom0.getValue()) {
                            Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
