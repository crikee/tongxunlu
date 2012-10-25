package com.gsf;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;



public class ContactsSelectViewAdapter extends BaseAdapter {
	private ArrayList<Contacts> conarr;
	private LayoutInflater mInflater;
	private Context mcontext;
	private ArrayList<Contacts> conToAdd ;

	public ContactsSelectViewAdapter(Context context, ArrayList<Contacts> contactslist) {
		mcontext = context;
		mInflater = LayoutInflater.from(context);
		conarr = contactslist;
		conToAdd = new ArrayList<Contacts>();
	}

	public int getCount() {
		return conarr.size();
	}

	public Object getItem(int position) {
		return conarr.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		return position;
	}
	
	public ArrayList<Contacts> getConToAdd()
	{
		return conToAdd ;
	}
	
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final ViewHolder holder ;
		
		if(convertView==null)
		{
			 convertView = mInflater.inflate(R.layout.contactslist_item, null);
			 holder = new ViewHolder();
			 holder.tv_phone = (TextView)convertView.findViewById(R.id.tv_contactslistitem_phone);
			 holder.tv_name  = (TextView)convertView.findViewById(R.id.tv_contactslistitem_name);
			 holder.cb_add   = (CheckBox)convertView.findViewById(R.id.cb_contactslistitem_add);
			 //绑定ViewHolder对象
			 convertView.setTag(holder);
		}
		else
		{
			//取出ViewHolder对象
			 holder = (ViewHolder)convertView.getTag();			 
		}
		
		if(conToAdd.contains(conarr.get(position)))
			 holder.cb_add.setChecked(true);
		 else
			 holder.cb_add.setChecked(false);
		
		holder.tv_phone.setText(conarr.get(position).getPhone());
		holder.tv_name.setText(conarr.get(position).getName());
		
		holder.cb_add.setOnClickListener(new Button.OnClickListener(){
			public void onClick(View v) {
				if(holder.cb_add.isChecked())				
					conToAdd.add(conarr.get(position));				
				else
					conToAdd.remove(conarr.get(position));
			}
		});		
		
		return convertView ;
	}
	
	private final class ViewHolder
	{
		TextView tv_name ;
		TextView tv_phone ;
		CheckBox cb_add;
	}
}

