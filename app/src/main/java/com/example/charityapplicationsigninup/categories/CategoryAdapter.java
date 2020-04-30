package com.example.charityapplicationsigninup.categories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.charityapplicationsigninup.R;
import com.example.charityapplicationsigninup.problems.CreateProblem;
import com.example.charityapplicationsigninup.problems.ViewProblemsActivity;

import java.util.ArrayList;

/**
 * This class is the adapter for the category list
 */
public class CategoryAdapter extends ArrayAdapter<Categories> implements Filterable {
    RelativeLayout layout;
    Context context;
    CustomFilter customFilter;

    ArrayList<Categories> originalList,tempList;

    //constructor
    public CategoryAdapter(Context context, int resource, ArrayList<Categories> originalList) {
        super(context,resource,originalList);
        this.originalList = originalList;
        this.context = context;
        this.tempList = originalList;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.category_card, parent, false);
        }
        final Categories category = getItem(position);
        layout = convertView.findViewById(R.id.card);
        if (category.getBackgroundUrl().equals("blind")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.blind));
        } else if (category.getBackgroundUrl().equals("books")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.imgbook));
        } else if (category.getBackgroundUrl().equals("carfix")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.carfix));
        } else if (category.getBackgroundUrl().equals("carpenter")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.carpenter));
        } else if (category.getBackgroundUrl().equals("cloths")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.cloths));
        } else if (category.getBackgroundUrl().equals("deaf")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.deaf));
        } else if (category.getBackgroundUrl().equals("debt")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.debt));
        } else if (category.getBackgroundUrl().equals("disabled")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.disabled));
        } else if (category.getBackgroundUrl().equals("donations")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.donations));
        } else if (category.getBackgroundUrl().equals("education")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.education));
        } else if (category.getBackgroundUrl().equals("elderly")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.elderly));
        } else if (category.getBackgroundUrl().equals("electricity")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.electricity));
        } else if (category.getBackgroundUrl().equals("food")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.food));
        } else if (category.getBackgroundUrl().equals("homefix")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.homefix));
        } else if (category.getBackgroundUrl().equals("medicalc")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.medicalc));
        } else if (category.getBackgroundUrl().equals("meds")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.meds));
        } else if (category.getBackgroundUrl().equals("orphans")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.orphans));
        } else if (category.getBackgroundUrl().equals("pets")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.pets));
        } else if (category.getBackgroundUrl().equals("shelter")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.shelter));
        } else if (category.getBackgroundUrl().equals("transportation")) {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.transportation));
        } else {
            layout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.defoo));
        }
        TextView category_title = convertView.findViewById(R.id.category_title);
        TextView category_description = convertView.findViewById(R.id.category_description);
        Button post_btn = convertView.findViewById(R.id.post_btn);
        Button view_btn = convertView.findViewById(R.id.view_btn);
        post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "You pressed me man leh kda ! : " + category.getCategoryId(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getContext(), CreateProblem.class);
                i.putExtra("catNum", category.getCategoryId());
                getContext().startActivity(i);
            }
        });
        view_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "ya3am msh mwarek 7aga !: " + category.getCategoryId(), Toast.LENGTH_LONG).show();
                Intent i = new Intent(getContext(), ViewProblemsActivity.class);
                i.putExtra("catNum", category.getCategoryId());
                getContext().startActivity(i);
            }
        });
        category_title.setText(category.getTitle());
        category_description.setText(category.getDescription());
        return convertView;
    }

    @Override
    public int getCount() {
        return originalList.size();
    }

    @Nullable
    @Override
    public Categories getItem(int position) {
        return originalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        if(customFilter == null){
            customFilter = new CustomFilter();
        }
        return customFilter;
    }

    class CustomFilter extends Filter{

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(constraint != null && constraint.length() > 0) {
                constraint = constraint.toString().toUpperCase();
                ArrayList<Categories> filters = new ArrayList<>();
                for (int i = 0; i < tempList.size(); i++) {
                    if (tempList.get(i).getTitle().toUpperCase().contains(constraint)) {
                        Categories category = new Categories(tempList.get(i).getTitle(), tempList.get(i).getDescription(), tempList.get(i).getBackgroundUrl(), tempList.get(i).getCategoryId());
                        filters.add(category);
                    }
                }
                results.count = filters.size();
                results.values = filters;
            }else {
                results.count = tempList.size();
                results.values = tempList;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            originalList = (ArrayList<Categories>)results.values;
            notifyDataSetChanged();
        }
    }
}
