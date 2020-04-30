package com.example.charityapplicationsigninup.problems;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.charityapplicationsigninup.R;

import java.util.List;

/**
 * This class is the adapter for the category list
 */
public class MyProblemAdapter extends ArrayAdapter<Problem> {
    //constructor

    public MyProblemAdapter(Context context, int resource, List<Problem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.problem_card, parent, false);
        }
        TextView problem_title = convertView.findViewById(R.id.view_prob_title);
        TextView problem_descr = convertView.findViewById(R.id.view_prob_descr);
        Problem problem = getItem(position);
        problem_title.setText(problem.getmTitle());
        problem_descr.setText(problem.getmProblem());
        return convertView;
    }
}
