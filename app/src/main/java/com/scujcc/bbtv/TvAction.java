package com.scujcc.bbtv;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.HashMap;
import java.util.Map;


public class TvAction extends AppCompatActivity {
    private PlayerView playerView;
    private SimpleExoPlayer player;
    MediaSource mediaSource;


    @Override
    protected void onCreate ( Bundle savedInstanceState ) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.layout_action );
        TextView tx = ( TextView ) findViewById ( R.id.action_head );
        Button bt_fill = (Button )findViewById ( R.id.action_fill );


        final Intent intent = getIntent ( );
        String getMessage = intent.getStringExtra ( MainActivity.EXTRA_MESSAGE );
        String[] msg = getMessage.split ( "@@@" );
        String net = checknet ( this );

        tx.setText ( msg[0].toString ().trim () + "   当前网络："+net);
        if ( getMessage != null ) {
            initView ();
            initExo ( msg[1].toString ().trim () );
        }
        bt_fill.setOnClickListener ( new View.OnClickListener ( ) {
            @Override
            public void onClick ( View v ) {

            }
        } );
    }

    public String checknet( Context context) {
        String strNetworkType = "";
        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获取代表联网状态的NetWorkInfo对象 NetworkInfo networkInfo = connManager.getActiveNetworkInfo(); /
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }
                        break;
                }
            }
        }
        return strNetworkType;
    }
    private void initExo ( String WebPosition ) {
        /**
         * 创建播放器
         */
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();// 得到默认合适的带宽
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);// 创建跟踪的工厂
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);// 创建跟踪器

        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);
        playerView.setPlayer(player);// 绑定player
        /**
         * 准备player
         */
        // 生成通过其加载媒体数据的DataSource实例
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(TvAction.this, "ExoPlayer"), bandwidthMeter);
        mediaSource = new HlsMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse ( WebPosition ));// 创建要播放的媒体的MediaSource
        player.prepare(mediaSource);// 准备播放器的MediaSource
        player.setPlayWhenReady(true);// 当准备完毕后直接播放
    }
    private void initView(){
        playerView = (PlayerView) findViewById ( R.id.action_player );
    }
    protected void onDestroy(){
        super.onDestroy ();
        myRelase ();
    }
    protected void onStop(){
        super.onStop ();
        myRelase ();
    }
    private void myRelase(){
        if(player != null){
            player.release ();
            player = null;
            mediaSource = null;
        }

    }

}
