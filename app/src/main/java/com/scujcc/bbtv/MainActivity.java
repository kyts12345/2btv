package com.scujcc.bbtv;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.List;


public class MainActivity extends AppCompatActivity {
    private RecyclerView movieList;
    private MovieListAdapter movieAdapter;
    private List<jsonObject> data;


    public static final String EXTRA_MESSAGE = "com.example.ddtv.MESSAGE";
    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.layout_main );

        initData ();
        Log.d("==============","===================");
        Log.d("info",data.get ( 1 ).getTitle ());
        Log.d("info",data.get ( 2 ).getTitle ());
        Log.d("info",data.get ( 3 ).getTitle ());
        System.out.println ( data.size () );
        movieList = findViewById ( R.id.main_movieList );
        movieAdapter = new MovieListAdapter(this,this.data);
        movieList.setLayoutManager ( new LinearLayoutManager ( this ) );//设置列表样式
        movieList.setAdapter ( movieAdapter );
        movieAdapter.setOnItemClickListener ( new MovieListAdapter.OnItemClickListener ( ) {
            @Override
            public void onClick ( int position ) {
                Intent intent = new Intent ( MainActivity.this,TvAction.class );
                intent.putExtra ( EXTRA_MESSAGE, data.get ( position ).getTitle ()+"@@@"+data.get ( position ).getUrl ());
                startActivity ( intent );
            }
        } );
        movieAdapter.setOnItemLongClickListener ( new MovieListAdapter.OnItemLongClickListener ( ) {
            @Override
            public void onLongClick ( int position ) {
                Log.d("info",data.get ( position ).getTitle ()+"的Url："+data.get ( position ).getUrl ());
                Toast.makeText ( MainActivity.this,data.get ( position ).getTitle ()+"的Url："+data.get ( position ).getUrl (),Toast.LENGTH_LONG).show ();
            }
        } );
    }
    private void initData() {
        jsonGeter jg = new jsonGeter (this);
        this.data = jg.getJson ( "data.json" );
    }
}
