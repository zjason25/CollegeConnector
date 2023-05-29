package edu.uci.ics.fabflixmobile.ui.movielist;

import edu.uci.ics.fabflixmobile.R;
import edu.uci.ics.fabflixmobile.data.model.School;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SchoolListAdapter extends ArrayAdapter<School> {
    private final ArrayList<School> schools;


    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView rating;
        TextView location;
        TextView website;
        TextView genre;
        TextView telephone;
    }

    // constructor: passes in a movie arrayList
    public SchoolListAdapter(Context context, ArrayList<School> schools) {
        super(context, R.layout.school_list_row, schools);
        this.schools = schools;
    }

    @SuppressLint("SetTextI18n")
    @Override
    // decides how to show the movies
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the movie item for this position
        School school = schools.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.school_list_row, parent, false);
            viewHolder.name = convertView.findViewById(R.id.name);
            viewHolder.rating = convertView.findViewById(R.id.rating);
            viewHolder.location = convertView.findViewById(R.id.location);
            viewHolder.website = convertView.findViewById(R.id.website);
            viewHolder.genre = convertView.findViewById(R.id.genre);
            viewHolder.telephone= convertView.findViewById(R.id.telephone);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(school.getName());
        viewHolder.rating.setText(school.getRating() + "");
        viewHolder.location.setText(school.getLocation() + "");
        viewHolder.website.setText(school.getWebsite() + "");
        viewHolder.genre.setText(school.getGenre() + "");
        viewHolder.telephone.setText(school.getTelephone() + "");
        // Return the completed view to render on screen
        return convertView;
    }
}