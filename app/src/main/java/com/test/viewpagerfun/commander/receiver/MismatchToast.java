package com.test.viewpagerfun.commander.receiver;


import android.app.Activity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.test.viewpagerfun.PrefManager;
import com.test.viewpagerfun.R;
import com.test.viewpagerfun.commander.state.MismatchToastState;

/* ShuffleCards.class
 * ---------------
 * The class who's methods get called by the Command object.
 */
public class MismatchToast {
	private static String pref_value = null;
	private MismatchToastState state = null;


	// Since we have a state and the pref key, we can have a more sophisticated method
	public void show() {
		if(state == null)
			return;

		boolean mismatch_toast_enabled = PrefManager.get(pref_value,false);

		//Show toast when answer was not quiet correct
		if(mismatch_toast_enabled){
			if(state.getDistance() > 0 ){
				LayoutInflater inflater = state.getActivity().getLayoutInflater();
				View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) state.getActivity().findViewById(R.id.toast_root));

				Toast toast = new Toast(state.getActivity());
				TextView toast_text = layout.findViewById(R.id.tv_toast);
				toast_text.setText("Your answer was slightly off!" +"\n"
						+ "Correct answer was: " + state.getMeaning()
				);

				int[] cardView_position = new int[2];
				state.getBinding().cardView.getLocationOnScreen(cardView_position);
				int y_offset = cardView_position[1] - (state.getBinding().cardView.getHeight() / 2);

				toast.setGravity(Gravity.TOP|Gravity.CENTER,0,y_offset);
				toast.setDuration(Toast.LENGTH_SHORT);
				toast.setView(layout);
				toast.show();
			}

		}
	}


	public <E> void setState(E pref) {
		if(state == null)
			state = (MismatchToastState) pref;
	}
	public <E> void setPref(E pref) {
		if(pref_value == null)
			pref_value = (String) pref;
	}


}
