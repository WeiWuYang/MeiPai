package com.example.meipai;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.apache.http.Header;

import com.loopj.android.http.RequestParams;
import com.example.Adapter.MyAdapter;
import com.example.Model.Videos;
import com.example.Utlis.Config;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.app.Activity;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.MediaController;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.VideoView;

public class MainActivity extends Activity implements OnItemClickListener,OnScrollListener,OnRefreshListener {
	Handler mhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				List<Videos> video = (List<Videos>) msg.obj;
//				videos.clear();
				videos.addAll(video);
				adapter.notifyDataSetChanged();
				break;
			case 2:
				videos.clear();
				page=1;
				srl.setRefreshing(false);
				if(index==1){
					getdata(3, page);
				}else if(index==2){
					getdata(16, page);
				}else if(index==3){
					getdata(13, page);
				}
			break;
			default:
				break;
			}
		};
	};
	private MyAdapter adapter;
	private List<Videos> videos;
	private VideoView vv_MeiPai;
	private VideoShow vi;
	private Videos video;
	private int page=1;
	private SwipeRefreshLayout srl;
	int index=1;//1是旅游  2是明星 3是搞笑
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		RadioGroup rg_meipai = (RadioGroup) findViewById(R.id.rg_meipai);
	    srl=	(SwipeRefreshLayout) findViewById(R.id.srl);
	    srl.setOnRefreshListener(this);
		rg_meipai.check(R.id.rb_ly);
		getdata(3,page);
		rg_meipai.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.rb_ly) {
					videos.clear();
					index=1;
					page=1;
					getdata(3,page);
				} else if (checkedId == R.id.rb_star) {
					videos.clear();
					index=2;
					page=1;
					getdata(16,page);
				} else if (checkedId == R.id.rb_gx) {
					videos.clear();
					index=3;
					page=1;
					getdata(13,page);
				}
			}
		});
		
		vv_MeiPai = (VideoView) findViewById(R.id.vv_MeiPai);
		lv_meipai = (GridView) findViewById(R.id.lv_meipai);
		lv_meipai.setOnScrollListener(this);
		videos = new ArrayList<Videos>();
		vi = new VideoShow();
		vi.start();
		adapter = new MyAdapter(videos, this);
		lv_meipai.setAdapter(adapter);
		lv_meipai.setOnItemClickListener(new OnItemClickListener() {

		

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				video = videos.get(position);

				getPath(video.getUrl());
			}
		});
		vv_MeiPai.setMediaController(new MediaController(this));// 控制暂停播放

	}

	public void getdata(int id,int page) {
		// 数据请求
		AsyncHttpClient ac = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("id", id);
		params.put("page", page);
		ac.get(Config.ip + Config.lx, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
				String str = new String(arg2);
				Gson g = new Gson();
				List<Videos> data = g.fromJson(str,
						new TypeToken<List<Videos>>() {
						}.getType());
				Message message = mhandler.obtainMessage();
				message.what = 1;
				message.obj = data;
				mhandler.sendMessage(message);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	public void getPath(final String s) {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Document document = null;
				try {
					document = Jsoup.connect(s).get();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Element head = document.head();
				String attr = head.select("meta[property=\"og:video:url\"]")
						.attr("content");

				Message ms = han.obtainMessage();
				ms.obj = attr;
				ms.what = 1;
				han.sendMessage(ms);
			}
		}) {
		}.start();
	}

	Handler han = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				String s = (String) msg.obj;
				Message ms = vi.han.obtainMessage();
				ms.what = 1;
				ms.obj = s;
				vi.han.sendMessage(ms);
				break;
			}

		};
	};
	private GridView lv_meipai;

	class VideoShow extends Thread {
		Handler han = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 1:
					String s = (String) msg.obj;
					videoshow(s);
					break;

				default:
					break;
				}
			};
		};

		protected void videoshow(String s) {
			// TODO Auto-generated method stub
			Uri u = Uri.parse(s);
			vv_MeiPai.setVideoURI(u);
			vv_MeiPai.start();
		}

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		if(OnScrollListener.SCROLL_STATE_TOUCH_SCROLL==scrollState){
			//滚到到底部
			if(view.getLastVisiblePosition()==view.getCount()-1){
				// View v = (View) view.getChildAt(view.getChildCount() - 1);  
				 page=page+1;
					if(index==1){
						getdata(3,page);
					}else if(index==2){
						getdata(16,page);
					}else if(index==3){
						getdata(13,page);
					}
			}
			
			
			
			
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		totalItemCount=firstVisibleItem+visibleItemCount;
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
		mhandler.sendEmptyMessage(2);
	
	
		
	}

}
