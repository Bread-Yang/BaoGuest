package com.mdground.guest.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.mdground.guest.R;
import com.mdground.guest.bean.Patient;
import com.mdground.guest.util.StringUtils;


public class SearchSimpleAdapter extends SimpleAdapter<Patient> {

	protected static class ViewHolder extends BaseViewHolder {
		private TextView name;
		private TextView sex;
		private TextView age;
	}

	public SearchSimpleAdapter(Context context) {
		super(context, ViewHolder.class);
	}

	@Override
	protected void bindData(SimpleAdapter.BaseViewHolder holder, int position, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;
		
		Patient patient = (Patient) getItem(position);
		viewHolder.name.setText(patient.getPatientName());
		viewHolder.age.setText(StringUtils.getAge(patient.getDOB()));
		viewHolder.sex.setText(patient.getGenderStr());

	}

	@Override
	protected void initHolder(SimpleAdapter.BaseViewHolder holder, View convertView) {
		if (!(holder instanceof ViewHolder)) {
			return;
		}
		ViewHolder viewHolder = (ViewHolder) holder;
		
		viewHolder.name = (TextView) convertView.findViewById(R.id.name);
		viewHolder.sex = (TextView) convertView.findViewById(R.id.sex);
		viewHolder.age = (TextView) convertView.findViewById(R.id.age);

	}

	@Override
	protected int getViewResource() {
		return R.layout.item_search_simple;
	}

}
