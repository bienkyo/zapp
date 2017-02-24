package vn.com.z11.z11app.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import vn.com.z11.z11app.ApiResponseModel.PackageResponse;
import vn.com.z11.z11app.GroupQuestionActivity;
import vn.com.z11.z11app.R;

/**
 * Created by kienlv58 on 12/23/16.
 */
public class ChapterAdapter extends RecyclerView.Adapter {
    ArrayList<PackageResponse.Chapters> list_chapter;
    Context context;

    public ChapterAdapter(ArrayList<PackageResponse.Chapters> list_chapter, Context context) {
        this.list_chapter = list_chapter;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_chapter, null);
        return new ChapterViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final PackageResponse.Chapters chapter = list_chapter.get(position);
        ChapterViewHolder viewChapter = (ChapterViewHolder) holder;
        ((ChapterViewHolder) holder).txtv_title.setText(chapter.name_text);
        ((ChapterViewHolder) holder).txtv_description.setText(chapter.describe_text);
        ((ChapterViewHolder) holder).btn_train.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toGroup = new Intent(holder.itemView.getContext(), GroupQuestionActivity.class);
                toGroup.putExtra("chapter_id",chapter.chapter_id);
                toGroup.putExtra("from","train");
                holder.itemView.getContext().startActivity(toGroup);
            }
        });
        ((ChapterViewHolder) holder).btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toGroup = new Intent(holder.itemView.getContext(), GroupQuestionActivity.class);
                toGroup.putExtra("chapter_id",chapter.chapter_id);
                toGroup.putExtra("from","test");
                holder.itemView.getContext().startActivity(toGroup);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toGroup = new Intent(holder.itemView.getContext(), GroupQuestionActivity.class);
                toGroup.putExtra("chapter_id",chapter.chapter_id);
                toGroup.putExtra("from","train");
                holder.itemView.getContext().startActivity(toGroup);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_chapter.size();
    }

    public class ChapterViewHolder extends RecyclerView.ViewHolder {
        TextView txtv_title;
        TextView txtv_description;
        Button btn_train,btn_test;

        public ChapterViewHolder(View itemView) {
            super(itemView);
            txtv_title = (TextView) itemView.findViewById(R.id.txtv_title);
            txtv_description = (TextView) itemView.findViewById(R.id.txtv_description);
            btn_train = (Button)itemView.findViewById(R.id.btn_train);
            btn_test = (Button)itemView.findViewById(R.id.btn_test);

        }
    }
}
