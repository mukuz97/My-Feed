package com.em.news.myfeed;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import database.DBAdapter;
import database.Model;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private DBAdapter dbAdapter;
    private Context context;

    RecyclerAdapter(Context context, DBAdapter dbAdapter){
        this.dbAdapter = dbAdapter;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.article_mini_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Model article = dbAdapter.getItem(i);
        viewHolder.title.setText(article.getTitle());
        viewHolder.description.setText(article.getDescription());
        viewHolder.pub_date.setText(article.getPub_date());
        Picasso.get().cancelRequest(viewHolder.imageView);
        Picasso.get().load(article.getImage())
                .fit().centerCrop()
                .into(viewHolder.imageView);
    }

    @Override
    public int getItemCount() {
        return dbAdapter.getSize();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        CardView content;
        ImageView imageView;
        TextView title, description, pub_date;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            content = itemView.findViewById(R.id.content_container);
            imageView = itemView.findViewById(R.id.content_image);
            title = itemView.findViewById(R.id.content_title);
            description = itemView.findViewById(R.id.content_description);
            pub_date = itemView.findViewById(R.id.content_pub_date);

            content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    Model article = dbAdapter.getItem(pos);
                    Intent i = new Intent(context, ContentDisplay.class);
                    i.putExtra("src", article.getSource());
                    context.startActivity(i);
                }
            });
        }
    }
}
