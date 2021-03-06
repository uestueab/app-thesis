package com.thesis.yokatta.commander.receiver;


import android.util.TypedValue;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.thesis.yokatta.PrefManager;
import com.thesis.yokatta.R;
import com.thesis.yokatta.commander.state.ShowDiagramState;

import java.util.ArrayList;
import java.util.List;

/* PlayPronunciation.class
 * ---------------
 * The class who's methods get called by the Command object.
 */
public class ShowDiagram {
    private static String pref_value = null;
    private ShowDiagramState state = null;


    // Since we have a state and the pref key, we can have a more sophisticated method
    public void show() {
        if (state == null)
            return;

        boolean diagram_enabled = PrefManager.get(pref_value, true);
        if (diagram_enabled) {
            //chart data
            List<BarEntry> entries = new ArrayList<>();

            for (int i = 0; i < state.getPastReviews().size(); i++){
                entries.add(new BarEntry(i, state.getPastReviews().get(i).getItemCount()));
            }


            //get the right colors from current Theme
            int textColor = getAttributeColor(R.attr.textColor);
            int cardReviewColor = getAttributeColor(R.attr.cardReviewColor);
            // add entries to dataset and give label a meaningful name;
            BarDataSet dataSet = new BarDataSet(entries, "Items reviewed");

            //set color for bar of chart and text displayed on top of them
            dataSet.setColor(cardReviewColor);
            dataSet.setValueTextColor(textColor);

            //transfer data set
            BarData barData = new BarData(dataSet);
            barData.setBarWidth(0.9f);

            //modify chart properties
            state.getBinding().chart.setData(barData);
            state.getBinding().chart.setFitBars(true);
            //no description is needed, data is self-explanatory
//            state.getBinding().chart.getDescription().setEnabled(false);
            state.getBinding().chart.getDescription().setText("< R5");
            state.getBinding().chart.getDescription().setYOffset(-25f);
            //change color of the bottom left legend to blend in with the rest
            state.getBinding().chart.getLegend().setTextColor(textColor);
            state.getBinding().chart.setScaleEnabled(false);

            //modify x-Axis
            state.getBinding().chart.getXAxis().setTextColor(textColor);
            state.getBinding().chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            state.getBinding().chart.getXAxis().setDrawGridLines(false);

            String[] XaxisLabels = new String[]{"R1","R2","R3","R4","R5"};

            ValueFormatter formatter = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return XaxisLabels[(int) value];
                }
            };

            state.getBinding().chart.getXAxis().setGranularity(1f);
            state.getBinding().chart.getXAxis().setValueFormatter(formatter);

            //modify y-Axis
            state.getBinding().chart.getAxisLeft().setTextColor(textColor);
            state.getBinding().chart.getAxisRight().setEnabled(false);

            // refresh and draw chart
            state.getBinding().chart.invalidate();

        }
    }

    public <E> void setState(E pref) {
        if (state == null)
            state = (ShowDiagramState) pref;
    }

    public <E> void setPref(E pref) {
        if (pref_value == null)
            pref_value = (String) pref;
    }

    private int getAttributeColor(int resId) {
        TypedValue typedValue = new TypedValue();
        state.getContext().getTheme().resolveAttribute(resId, typedValue, true);

        //color value as int
        return typedValue.data;
    }

}
