package com.gsf;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;


public class SmsViewAdapter extends BaseAdapter {
	private ArrayList<SmsMsg> msgarr;
	private Context mcontext;

	public SmsViewAdapter(Context context, ArrayList<SmsMsg> smsmsglist) {
		mcontext = context;
		this.msgarr = smsmsglist;
	}

	public int getCount() {
		return msgarr.size();
	}

	public Object getItem(int position) {
		return msgarr.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		SmsMsg entity = msgarr.get(position);
		int itemLayout = entity.getBg();

		LinearLayout layout = new LinearLayout(mcontext);
		LayoutInflater vi = (LayoutInflater) mcontext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		vi.inflate(itemLayout, layout, true);

		TextView tvText = (TextView) layout.findViewById(R.id.tv_chatmsg_text);
		TextView tvTime = (TextView) layout.findViewById(R.id.tv_chatmsg_time);
		tvText.setText(entity.getBody());
		tvTime.setText(entity.getDate());
		return layout;
	}
}
