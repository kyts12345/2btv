package com.scujcc.bbtv;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;


public class MovieListAdapter extends RecyclerView.Adapter< MovieListAdapter.MovieViewHolder >{
    private List<jsonObject> data;
    private Context context;

    public MovieListAdapter(Context context , List<jsonObject> data) {
        this.data = data;
        this.context = context;
    }
        /**
         * 定义点击事件接口
         */
    public interface OnItemClickListener{
        void onClick(int position);
    }
    public interface OnItemLongClickListener{
        void onLongClick(int position);
    }

    private  OnItemClickListener listener;
    private OnItemLongClickListener longListener;

    public void setOnItemClickListener ( OnItemClickListener listener ) {
        this.listener = listener;
    }
    public void setOnItemLongClickListener ( OnItemLongClickListener longListener ) {
        this.longListener = longListener;
    }

    /**
     * 找到电影行对应的xml
     */
    public MovieViewHolder onCreateViewHolder (ViewGroup viewGroup , int i ) {
        View container = LayoutInflater.from ( viewGroup.getContext () ).inflate (
                R.layout.layout_view ,viewGroup,false );
        return new MovieViewHolder(container);
    }
    /**
     * 填充每一行内容
     */
    public void onBindViewHolder ( MovieViewHolder movieViewHolder , final int i ) {
        movieViewHolder.bind ( this.context,data.get ( i ),i);
        movieViewHolder.itemView.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {
                if ( listener != null ){
                    listener.onClick ( i );
                }
            }
        } );
        movieViewHolder.itemView.setOnLongClickListener ( new View.OnLongClickListener ( ) {
            @Override
            public boolean onLongClick ( View v ) {
                longListener.onLongClick ( i );
                return true;
            }
        } );
    }

    public int getItemCount () {
        return data.size ();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder{
        private TextView moveName;
        private TextView exp;
        private ImageView imageView;

        private int[] img={
                R.drawable.c1,
                R.drawable.c2,
                R.drawable.c3,
                R.drawable.c4,
                R.drawable.c5,
                R.drawable.c6,
        };
        public MovieViewHolder(View container) {
            super(container);
            moveName = container.findViewById(R.id.view_title);
            imageView = container.findViewById(R.id.view_img);
            exp = container.findViewById ( R.id.view_exp );
        }

        public void bind(Context context, jsonObject jo ,int i) {
            this.moveName.setText(jo.getTitle ());
            this.exp.setText ( jo.getQuality () );
            Glide.with(context)
                    .load(jo.getImg ())
                    .override(320,320)
                    .placeholder ( img[i] )
                    .into(imageView);
        }
    }
}

