package vn.com.z11.z11app.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.com.z11.z11app.ApiResponseModel.CategoryResponse;
import vn.com.z11.z11app.R;

/**
 * Created by kienlv58 on 12/5/16.
 */
public class GridViewFolderAdapter extends BaseAdapter {
    ArrayList<CategoryResponse.Folder> listFolder;
    Context context;
    LayoutInflater inflater;

    public GridViewFolderAdapter(ArrayList<CategoryResponse.Folder> listFolder, Context context) {
        this.listFolder = listFolder;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listFolder.size();
    }

    @Override
    public CategoryResponse.Folder getItem(int i) {
        return listFolder.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public class Holder {
        TextView textView;
        ImageView imageView;
        LinearLayout boder_layout;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_category, null);
        holder.textView = (TextView) rowView.findViewById(R.id.textView1);
        holder.imageView = (ImageView) rowView.findViewById(R.id.imageView1);
        holder.boder_layout  = (LinearLayout)rowView.findViewById(R.id.boder_layout);
        holder.boder_layout.setBackgroundColor(Color.WHITE);
        final CategoryResponse.Folder item = getItem(i);
        if(item == null){

        }else {
            if(item.translate_name_text !=null && item.translate_name_text.size() > 0)
                holder.textView.setText(item.translate_name_text.get(0).text_value);
                holder.imageView.setImageResource(R.drawable.folder_icon);

        }
        return rowView;
    }
}
