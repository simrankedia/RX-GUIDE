package com.example.rxguide;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Prescriptiondisplay extends Activity {

	String htmlpage="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_prescriptiondisplay);
		Intent intent = getIntent();
		for(String s : htmlgetter("http://rxguide.pythonanywhere.com/RXGUIDE/default/view_prescriptions?idno="+intent.getStringExtra(PatientID.EXTRA_MESSAGE))){
			new imgview().execute(s);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.prescriptiondisplay, menu);
		return true;
	}

	protected String[] htmlgetter(final String SERVER_URL){
		String[] image_links=null;
		Thread t=new Thread(new Runnable(){
			public void run(){
				try {

					URL server_url = new URL(SERVER_URL);
					BufferedReader in = new BufferedReader(new InputStreamReader(server_url.openStream()));
					String inputLine;
					while ((inputLine = in.readLine()) != null)
						htmlpage = htmlpage.concat(inputLine);
					in.close();

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		t.start();
		try {
			t.join();
			image_links=htmlpage.split(" <br />");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return image_links;
	}

	private class imgview extends AsyncTask<String, Void, Bitmap>{

		Bitmap bitmap;
		LinearLayout l = (LinearLayout) findViewById(R.id.root);
		ImageView image = new ImageView(Prescriptiondisplay.this);	

		protected Bitmap doInBackground(String... arg0) {
			try {
				URL url = new URL("http://rxguide.pythonanywhere.com"+arg0[0]);
				HttpGet httpRequest = new HttpGet((url).toURI());
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = (HttpResponse) httpclient.execute(httpRequest);
				HttpEntity entity = response.getEntity();
				BufferedHttpEntity b_entity = new BufferedHttpEntity(entity);
				InputStream input = b_entity.getContent();
				bitmap = BitmapFactory.decodeStream(input);
			} catch (Exception S) {
				S.printStackTrace();
			}
			return bitmap;
		}

		@Override 
		public void onPostExecute(Bitmap values) {   
			image.setImageBitmap(bitmap);
			image.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
			l.addView(image);
		}

		@Override
		protected void onPreExecute() {
		}

		@Override
		protected void onProgressUpdate(Void... values) {
		}
	}
}
