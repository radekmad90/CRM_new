package com.radek.madej.crm.fragments;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.radek.madej.crm.MainActivity;

/**
 * A placeholder fragment containing a simple view.
 */
public abstract class BaseDrawerFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	protected final String ARG_SECTION_NUMBER = "section_number";

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	//public abstract static AbstractMainFragment newInstance(final int sectionNumber); 

	public BaseDrawerFragment(int sectionNumber) {
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		setArguments(args);
	}
	public BaseDrawerFragment(){
		
	}
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}
	
}
