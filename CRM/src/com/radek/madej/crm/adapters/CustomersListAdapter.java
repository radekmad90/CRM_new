package com.radek.madej.crm.adapters;

import java.util.List;

import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.radek.madej.crm.R;
import com.radek.madej.crm.database.model.Customer;
import com.radek.madej.crm.utils.ApplicationUtils;

public class CustomersListAdapter extends ArrayAdapter<Customer> {
	private List<Customer> values;
	private Context context;
	
	
	public CustomersListAdapter(Context context, List<Customer> list){
		super(context, R.layout.customer_row_layout, list);
		this.values=list;
		this.context= context;
	}
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    final View rowView = inflater.inflate(R.layout.customer_row_layout, parent, false);

     	Customer row = values.get(position);
	    String companyName = row.getCompanyName();
	    TextView nameTextView = (TextView) rowView.findViewById(R.id.customer_name_row);
	    nameTextView.setText(companyName);
	    
	    String address = row.getAddress();
	    TextView textView = (TextView) rowView.findViewById(R.id.customer_address_row);
	    textView.setText(address);
	    
	    return rowView;
	  }
	
	
	
}
