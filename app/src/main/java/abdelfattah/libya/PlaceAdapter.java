package abdelfattah.libya;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PlaceAdapter extends ArrayAdapter<Place> {

    private Context context;
    PlaceAdapter(Context context, ArrayList<Place> places) {
        super(context, 0, places );
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.single_list_item_view, parent, false);
        }
        Place currentWord = getItem(position);
        ImageView mImageView = listItemView.findViewById((R.id.location_image));
        if(currentWord != null) {
            mImageView.setImageResource(currentWord.getImageResourceId());

            TextView mTextView = listItemView.findViewById(R.id.location_name);
            mTextView.setText(context.getString(currentWord.getNameStringId()));

            mTextView = listItemView.findViewById(R.id.location_address);
            mTextView.setText(context.getString(currentWord.getAddressStringId()));
        }
        return listItemView;
    }
}
