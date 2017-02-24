package vn.com.z11.z11app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import vn.com.z11.z11app.ApiResponseModel.CategoryResponse;
import vn.com.z11.z11app.R;

/**
 * Created by kienlv58 on 12/2/16.
 */
public class GridviewAdapter extends BaseAdapter {
    ArrayList<CategoryResponse.Category> listCate;
    Context context;
    LayoutInflater inflater;

    public GridviewAdapter(ArrayList<CategoryResponse.Category> listCate, Context context) {
        this.listCate = listCate;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listCate.size();
    }

    @Override
    public CategoryResponse.Category getItem(int i) {
        return listCate.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }
    public class Holder{
        TextView textView;
        ImageView imageView;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        View rowView;
        rowView = inflater.inflate(R.layout.item_category,null);
        holder.textView = (TextView)rowView.findViewById(R.id.textView1);
        holder.imageView = (ImageView)rowView.findViewById(R.id.imageView1);
        final CategoryResponse.Category item = getItem(i);
        holder.textView.setText(item.category_code.toString());
        if(item.image == null) {
            Picasso.with(context).load(R.drawable.erro).into(holder.imageView);
            //holder.imageView.setImageResource(R.drawable.ic_menu_camera);
        }
        else{
            Picasso.with(context)
                    .load(item.image.toString())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.erro)
                    .into(holder.imageView);
        }

//        rowView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                // TODO Auto-generated method stub
//                ArrayList<CategoryResponse.CategoryData.FolderData> listFolder = item.getListFolder();
//                Intent intent = new Intent(context, FolderActivity.class);
//
//            }
//        });
        return rowView;
    }


}
