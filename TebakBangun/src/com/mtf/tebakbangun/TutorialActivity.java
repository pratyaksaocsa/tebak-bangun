package com.mtf.tebakbangun;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

public class TutorialActivity extends Activity {

	private VideoView m_videoView;
	private int m_position = 0;
	private ProgressDialog m_progressDialog;
	private MediaController m_mediaControls; 
	
	@Override
	protected void onCreate(final Bundle savedInstance)	{
		super.onCreate(savedInstance);
		setContentView(R.layout.activity_tutorial);
		if(m_mediaControls == null){
			m_mediaControls = new MediaController(TutorialActivity.this);
		}
		m_videoView = (VideoView) findViewById(R.id.video_view);
		m_progressDialog = new ProgressDialog(TutorialActivity.this);
		m_progressDialog.setTitle("");
		m_progressDialog.setMessage("Harap tunggu sebentar");
		m_progressDialog.setCancelable(false);
		m_progressDialog.show();
		try {
			m_videoView.setMediaController(m_mediaControls);
			m_videoView.setVideoURI(Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.tutorial));
		} 
		catch(Exception ex)	{
			Log.e("Ocsa", ex.getMessage());
			ex.printStackTrace();
		}
		m_videoView.requestFocus();
		m_videoView.setOnPreparedListener(new OnPreparedListener() {
			
			public void onPrepared(MediaPlayer mediaPlayer) {
				m_progressDialog.dismiss();
				m_videoView.seekTo(m_position);
				if(m_position == 0){
					m_videoView.start();
				} else {
					m_videoView.pause();
				}
			}
		});
	}
	
	@Override
	public void onSaveInstanceState(Bundle savedInstance) {
		super.onSaveInstanceState(savedInstance);
		savedInstance.putInt("Position", m_videoView.getCurrentPosition());
		m_videoView.pause();
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstance){
		super.onRestoreInstanceState(savedInstance);
		m_position = savedInstance.getInt("Position");
		m_videoView.seekTo(m_position);
	}
}