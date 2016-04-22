package com.mdground.guest.activity.settlement;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mdground.guest.R;
import com.mdground.guest.activity.base.BaseActivity;
import com.mdground.guest.api.base.RequestCallBack;
import com.mdground.guest.api.base.ResponseCode;
import com.mdground.guest.api.base.ResponseData;
import com.mdground.guest.api.server.clinic.PayBilling;
import com.mdground.guest.constant.MemberConstant;
import com.mdground.guest.util.DecimalDigitsInputFilter;
import com.mdground.guest.util.StringUtils;
import com.mdground.guest.util.ViewUtil;
import com.socks.library.KLog;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class SettlementConfirmActivity extends BaseActivity implements OnClickListener {

    private LinearLayout llt_already_pay, llt_coupon, llt_cash_charge, llt_change;
    private TextView tv_receivable_fee, tv_already_pay, tv_coupon, tv_minus, tv_real_fee, tv_change;
    private EditText et_real_fee, et_cash_charge;
    private Button btn_confirm;

    private CustomXGPushReceiver mXGReceiver;

    private int mTradePlatfrom, mTotalFeeAdjust, mTotalFeeReal, mBillingID;
    private int mBillingType = 3;

    private boolean mIsRefund, mIsTextChangeManual, mIsReturnSaleDrug;

    class CustomXGPushReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MemberConstant.XG_INTENT_TAG.equals(intent.getAction())) {
                 Toast.makeText(SettlementConfirmActivity.this, "收到推送", Toast.LENGTH_SHORT).show();

                KLog.e("收到推送");

                String title = intent.getStringExtra("title");
                String content = intent.getStringExtra("content");
                String customContent = intent.getStringExtra("customContent");

                KLog.e("拿到的title : " + title);
                KLog.e("拿到的content : " + content);
                KLog.e("拿到的customContent : " +
                        customContent);

                try {
                    JSONObject json = new JSONObject(customContent);
                    String functionName = json.getString("FunctionName");

                    // 更新列表的推送
                    if ("RefreshAppointment".equals(functionName)) {

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settlement_confirm);

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

        tv_receivable_fee = (TextView) findViewById(R.id.tv_receivable_fee);
        tv_already_pay = (TextView) findViewById(R.id.tv_already_pay);
        tv_coupon = (TextView) findViewById(R.id.tv_coupon);
        tv_minus = (TextView) findViewById(R.id.tv_minus);
        tv_real_fee = (TextView) findViewById(R.id.tv_real_fee);
        tv_change = (TextView) findViewById(R.id.tv_change);

        et_real_fee = (EditText) findViewById(R.id.et_real_fee);
        et_cash_charge = (EditText) findViewById(R.id.et_cash_charge);

        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    @Override
    public void initMemberData() {
        mIsReturnSaleDrug = getIntent().getBooleanExtra(MemberConstant.IS_RETURN_SALE_DRUG, false);
        if (mIsReturnSaleDrug) {
            llt_coupon.setVisibility(View.GONE);
            llt_cash_charge.setVisibility(View.GONE);
            llt_change.setVisibility(View.GONE);
            btn_confirm.setText(R.string.close);
            tv_real_fee.setText(R.string.real_refund);
            et_real_fee.setEnabled(false);

            float returnAmount = getIntent().getFloatExtra(MemberConstant.RETURN_SALE_DRUG_AMOUNT, 0);

            tv_already_pay.setHint(StringUtils.addYuanUnit(String.valueOf(returnAmount)));
            et_real_fee.setHint(StringUtils.addYuanUnit(String.valueOf(returnAmount)));
        } else {
            String jsonString = getIntent().getStringExtra(MemberConstant.CASHIER_BILLING_DETAIL);
            try {
                JSONObject jsonObject = new JSONObject(jsonString);

                mTotalFeeAdjust = jsonObject.getInt("TotalFeeAdjust");
                mTotalFeeReal = jsonObject.getInt("TotalFeeReal");
                mBillingID = jsonObject.getInt("BillingID");
                mBillingType = jsonObject.getInt("BillingType");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mTotalFeeReal > 0) {
                tv_already_pay.setHint(String.format("%.2f", mTotalFeeReal / 100f) + getResources().getString(R.string.yuan));
            } else {
                llt_already_pay.setVisibility(View.GONE);
            }

            // 退款
            if (mTotalFeeReal >= mTotalFeeAdjust && mTotalFeeAdjust > 0) {
                mIsRefund = true;

                if (mTotalFeeReal != mTotalFeeAdjust) {
                    btn_confirm.setText(R.string.confirm_refund);
                    tv_real_fee.setText(R.string.real_refund);
                }
//            tv_minus.setVisibility(View.VISIBLE);
                et_real_fee.setFocusable(false);
                llt_coupon.setVisibility(View.GONE);

                String couponString = String.format("%.2f", (mTotalFeeReal - mTotalFeeAdjust) / 100f);

                tv_coupon.setHint(couponString + getResources().getString(R.string.yuan));
            } else {
                tv_coupon.setHint("0.00" + getResources().getString(R.string.yuan));
            }

            String receivableString = String.format("%.2f", mTotalFeeAdjust / 100f);;

            tv_receivable_fee.setHint(receivableString + getResources().getString(R.string.yuan));
            et_cash_charge.setText(receivableString + getResources().getString(R.string.yuan));
            tv_change.setHint("0.00" + getResources().getString(R.string.yuan));

            if (mIsRefund) {
                et_real_fee.setText(String.format("%.2f", (mTotalFeeReal - mTotalFeeAdjust) / 100f) + getResources().getString(R.string.yuan));
            } else {
                String realString = String.format("%.2f", (mTotalFeeAdjust - mTotalFeeReal) / 100f);
                et_real_fee.setText(realString + getResources().getString(R.string.yuan));
            }

            if (!getIntent().getBooleanExtra(MemberConstant.SETTLEMENT_BY_CASH, false) || mTotalFeeReal > 0) {
                llt_cash_charge.setVisibility(View.GONE);
                llt_change.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public void setListener() {
        ViewUtil.setLoseFocusWhenDoneButtonPress(et_real_fee);
        ViewUtil.setLoseFocusWhenDoneButtonPress(et_cash_charge);
        et_real_fee.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3)});
        et_cash_charge.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(3)});

        et_real_fee.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = et_real_fee.getText().toString();
                if (hasFocus) {
                    et_real_fee.setText(StringUtils.trimUnit(text));
                    et_real_fee.selectAll();
                } else {
                    if (!StringUtils.isEmpty(text)) {
                        float realFee = Float.valueOf(text);
                        et_real_fee.setText(String.format("%.2f", realFee) + getResources().getString(R.string.yuan));
                    } else {
                        et_real_fee.setText("0.00" + getResources().getString(R.string.yuan));
                    }
                }
            }
        });

        et_cash_charge.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String text = et_cash_charge.getText().toString();
                if (hasFocus) {
                    et_cash_charge.setText(StringUtils.trimUnit(text));
                    et_cash_charge.selectAll();
                } else {
                    if (!StringUtils.isEmpty(text)) {
                        et_cash_charge.setText(String.format("%.2f", Float.valueOf(text)) + getResources().getString(R.string.yuan));
                    } else {
                        et_cash_charge.setText("0.00" + getResources().getString(R.string.yuan));
                    }
                }
            }
        });

        et_real_fee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshFee();
            }
        });

        et_cash_charge.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                refreshFee();
            }
        });
    }

    private void refreshFee() {
        if (!mIsTextChangeManual) {
            int realFeeInteger = 0;

            String realFeeText = StringUtils.trimUnit(et_real_fee.getText().toString());
            if (!StringUtils.isEmpty(realFeeText)) {
                float realFeeFloat = Float.valueOf((realFeeText));
                if (mIsRefund) {
                    if (realFeeFloat > (mTotalFeeReal - mTotalFeeAdjust) / 100f) {
                        realFeeFloat = (mTotalFeeReal - mTotalFeeAdjust) / 100f;

                        mIsTextChangeManual = true;
                        et_real_fee.setText(String.format("%.2f", realFeeFloat));
                        int position = et_real_fee.length();
                        Editable etext = et_real_fee.getText();
                        Selection.setSelection(etext, position);
                        mIsTextChangeManual = false;
                    }
                } else {
                    if (realFeeFloat > mTotalFeeAdjust / 100f) {
                        realFeeFloat = mTotalFeeAdjust / 100f;

                        mIsTextChangeManual = true;
                        et_real_fee.setText(String.format("%.2f", realFeeFloat));
                        int position = et_real_fee.length();
                        Editable etext = et_real_fee.getText();
                        Selection.setSelection(etext, position);
                        mIsTextChangeManual = false;
                    }
                }

                realFeeInteger = (int) (realFeeFloat * 100);
            }

            // 优惠
            tv_coupon.setHint(String.format("%.2f", (mTotalFeeAdjust - realFeeInteger) / 100f) + getResources().getString(R.string.yuan));

            // 找零
            int cash_charge = 0;
            if (!StringUtils.isEmpty(et_cash_charge.getText().toString())) {
                cash_charge = (int) (Float.valueOf(StringUtils.trimUnit(et_cash_charge.getText().toString())) * 100);
            }

            int minus = cash_charge - realFeeInteger;
            tv_change.setHint(String.format("%.2f", minus / 100f) + getResources().getString(R.string.yuan));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:

                if (mIsReturnSaleDrug) {
                    setResult(RESULT_OK);
                    finish();
                } else {
                    int real_fee = 0;
                    if (!StringUtils.isEmpty(et_real_fee.getText().toString())) {
                        real_fee = (int) (Float.valueOf(StringUtils.trimUnit(et_real_fee.getText().toString())) * 100);
                        if (mIsRefund) {
                            real_fee *= -1;
                        }
                    }

                    int cash_charge = 0;
                    if (!StringUtils.isEmpty(et_cash_charge.getText().toString())) {
                        cash_charge = (int) (Float.valueOf(StringUtils.trimUnit(et_cash_charge.getText().toString())) * 100);
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
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
