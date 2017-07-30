//package nl.mranderson.sittingapp.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.SeekBar;
//import android.widget.TextView;
//
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.charts.LineChart;
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.data.LineData;
//import com.github.mikephil.charting.data.LineDataSet;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import nl.mranderson.sittingapp.R;
//
//public class AnalyticsFragment extends android.support.v4.app.Fragment {
//
//    private BarChart mChart;
//    private SeekBar mSeekBarX, mSeekBarY;
//    private TextView tvX, tvY;
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_analytics, container, false);
//        return view;
//
//    }
//
//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//
//        LineChart chart = (LineChart) getView().findViewById(R.id.chart);
//
//
//        List<Entry> entries = new ArrayList<Entry>();
//
//
//        // turn your data into Entry objects
//        entries.add(new Entry(10, 12));
//        entries.add(new Entry(3, 12));
//        entries.add(new Entry(6, 12));
//
//        LineDataSet dataSet = new LineDataSet(entries, "Label");
//
//        LineData lineData = new LineData(dataSet);
//        chart.setData(lineData);
//        chart.invalidate(); // refresh
//    }
//}
