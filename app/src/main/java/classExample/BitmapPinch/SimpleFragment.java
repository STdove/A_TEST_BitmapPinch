package classExample.BitmapPinch;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SimpleFragment extends Fragment {

	TextView mEcho;
	DrawArea mDrawArea;
	
	public SimpleFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.simple_fragment_layout, container, false);
					// simple_fragment_layout: is the name of the layout (name of the xml file)!
		
		mEcho = (TextView) rootView.findViewById(R.id.fragEcho);
		mDrawArea = (DrawArea) rootView.findViewById(R.id.drawArea);
		
		// CANNOT do this! we dragged the area into the fragment layout in UI editor!
		// so the object is already created! Just like the mEcho(TextView)
		// 		mDrawArea = new DrawArea(rootView.getContext());
		
		mEcho.setText("Fragment CreateView done!");
		
		return rootView;
	}
}
