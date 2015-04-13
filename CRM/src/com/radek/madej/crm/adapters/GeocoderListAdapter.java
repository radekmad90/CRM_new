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
import com.radek.madej.crm.utils.ApplicationUtils;

public class GeocoderListAdapter extends ArrayAdapter<Address> {
	private List<Address> values;
	private Context context;
	
	
	public GeocoderListAdapter(Context context, List<Address> list){
		super(context, R.layout.geocoded_row_layout, list);
		this.values=list;
		this.context= context;
	}
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    Address row = values.get(position);
	    String address = ApplicationUtils.getAddressLinesFromAddress(row);
	    //TODO zrob na itemie listy klawisz pokaz na mapie
	    View rowView = inflater.inflate(R.layout.geocoded_row_layout, parent, false);
	    
	    TextView textView = (TextView) rowView.findViewById(R.id.geocoded_element);
	    textView.setText(address);
	    
	    return rowView;
	  }
	
	
	
}
