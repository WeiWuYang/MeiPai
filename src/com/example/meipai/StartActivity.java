package com.example.meipai;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

public class StartActivity extends Activity{
	Handler mhandler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 1:
				Intent intent=new Intent(StartActivity.this,MainActivity.class);
				startActivity(intent);
			    finish();
				break;

			default:
				break;
			}
		};
	};
           @Override
        protected void onCreate(Bundle savedInstanceState) {
        	// TODO Auto-generated method stub
        	super.onCreate(savedInstanceState);
        setContentView(R.layout.start_item);
     ImageView   img_start= (ImageView) findViewById(R.id.img_start);
    mhandler.postDelayed(new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			mhandler.sendEmptyMessage(1);
		}
	}, 2000);
           }
           
}
