package com.mtf.tebakbangun;

import java.io.IOException;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.WakeLockOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnAreaTouchListener;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.entity.scene.Scene.ITouchArea;
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.entity.sprite.AnimatedSprite.IAnimationListener;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.HorizontalAlign;

import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTextureRegionFactory;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTiledTextureRegion;
import com.mtf.tebakbangun.model.AnswerButton;
import com.mtf.tebakbangun.model.GameHUD;
import com.mtf.tebakbangun.model.Questions;
import com.mtf.tebakbangun.model.BasicShapeAnimated;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

public class PlayGameActivity extends BaseGameActivity implements 
		IOnSceneTouchListener, IOnAreaTouchListener, IAnimationListener {

	public final int CAMERA_WIDTH = 800;
	public final int CAMERA_HEIGHT = 480;

	private static PlayGameActivity instance = null;
	   
	/* A private Constructor prevents any other 
    * class from instantiating.
    */
	//public PlayGameActivity(){ }
   
	/* Static 'instance' method */
	public static PlayGameActivity getInstance( ) {
		//if(instance == null) {
	        //instance = new PlayGameActivity();
		//}
		return instance;
	}
   
	private Camera m_Camera;
	private Scene m_mainScene;
	BitmapTextureAtlas m_BackgroundAtlas;
	TextureRegion m_BackgroundTextureRegion;
	private BitmapTextureAtlas m_FontTextureAtlas;
	protected Font m_Font;
	private BitmapTextureAtlas m_FontTexture;
	private Music mBackgroundMusic;
	private Sound mClickSound;
	private EngineOptions m_engineOptions;
	
	private GameHUD m_TopHUD;
	private int score = 0, level = 0;
	private ChangeableText mScoreTextValue, mLevelTextValue;
	//public static ChangeableText mGoldTextValue;
	private Text mScoreText, mLevelText;
	
	BitmapTextureAtlas mAnswerTextureAtlas;
	TextureRegion mAnswerTextureRegion;
	
	BitmapTextureAtlas[] m_BasicShapeAtlas;
	PixelPerfectTiledTextureRegion[] m_BasicShapeTiledTextureRegion;
	String m_BasicShapeFilename[] = {"shape1.png","shape2.png","shape3.png","shape1.png","shape2.png","shape3.png"};
	
	Questions questions[];
	
	public void addScore() {
		this.score += 10;
		mScoreTextValue.setText(String.valueOf(score));
	}
	
	public Sound getClickSound()
	{
		return mClickSound;
	}
	
	public Scene getMainScene()
	{
		return m_mainScene;
	}
	
	public void NextQuestion()
	{
		if (level < 4)
		{
			m_mainScene.detachChild(questions[level]);
			this.level += 1;
			mLevelTextValue.setText(String.valueOf(this.level+1));
			CreateQuestion(this.level);
		}
		else
		{
			//USER WIN
			mScoreTextValue.setText("YOU WIN!!");
		}
	}
	
	private void CreateQuestion(int level)
	{
		try{
			questions = new Questions[5]; //Because we just have 5 levels
			for(int i=0;i<5;i++)
			{
				questions[i] = new Questions(m_BackgroundTextureRegion, mAnswerTextureRegion, m_BasicShapeTiledTextureRegion[level],m_Font);
			}
			m_mainScene.registerTouchArea(questions[level]);
			m_mainScene.attachChild(questions[level]);
		}
		catch(Exception x)
		{
			Log.d("Ocsa", "ERROR: Level = "+this.level);
		}
	}
	
	@Override
	public Engine onLoadEngine() {
		this.m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		m_engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new FillResolutionPolicy(),
				//new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.m_Camera).setNeedsMusic(true).setNeedsSound(true).setWakeLockOptions(WakeLockOptions.SCREEN_ON);
		return new Engine(m_engineOptions);
	}

	@Override
	public void onLoadResources() {
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("font/");
		MusicFactory.setAssetBasePath("mfx/");
		SoundFactory.setAssetBasePath("mfx/");
		PixelPerfectTextureRegionFactory.setAssetBasePath("gfx/");
		
		// load font
		this.m_FontTextureAtlas = new BitmapTextureAtlas(256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_Font = FontFactory.createFromAsset(m_FontTextureAtlas, this, "PORKYS_.TTF", 20, true, Color.BLACK);
		this.mEngine.getTextureManager().loadTexture(this.m_FontTextureAtlas);
		this.mEngine.getFontManager().loadFont(m_Font);
		
		// load shape
		this.m_BasicShapeAtlas = new BitmapTextureAtlas[6]; //Because we only have 6 shape
		this.m_BasicShapeTiledTextureRegion = new PixelPerfectTiledTextureRegion[6]; //Because we only have 6 shape
		for (int i = 0; i < m_BasicShapeAtlas.length; i++) {
			this.m_BasicShapeAtlas[i] = new BitmapTextureAtlas(512, 512,
					TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.m_BasicShapeTiledTextureRegion[i] = PixelPerfectTextureRegionFactory
					.createTiledFromAsset(m_BasicShapeAtlas[i], this, m_BasicShapeFilename[i], 0, 0,
							4, 2);
		}
		for (int i = 0; i < m_BasicShapeFilename.length; i++) {
			this.mEngine.getTextureManager().loadTexture(m_BasicShapeAtlas[i]);
		}
		/*this.mEnemy = new Enemy[ENEMY_SIZE];
		for (int i = 0; i < mEnemy.length; i++) {
			this.mEnemy[i] = new Enemy(0, 0,
					mEnemyPerfectTiledTextureRegion[i], mFont, i);
		}*/
		
		// load background
		this.m_BackgroundAtlas = new BitmapTextureAtlas(1024, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_BackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(m_BackgroundAtlas, this, "background.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(m_BackgroundAtlas);
		
		//load button
		this.mAnswerTextureAtlas = new BitmapTextureAtlas(256, 128,	TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mAnswerTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(mAnswerTextureAtlas, this, "button.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(mAnswerTextureAtlas);
		
		try {
			mClickSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), this, "click.wav");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			mBackgroundMusic = MusicFactory.createMusicFromAsset(mEngine.getMusicManager(), this, "background_mixed.mp3");
			mBackgroundMusic.setLooping(true);
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	@Override
	public Scene onLoadScene() {
		mEngine.registerUpdateHandler(new FPSLogger());
		m_mainScene = new Scene();
		instance = this;
		//final int centerX = (CAMERA_WIDTH - this.m_BackgroundTextureRegion.getWidth()) / 2;
		//final int centerY = (CAMERA_HEIGHT - this.m_BackgroundTextureRegion.getHeight()) / 2;
		//Sprite background = new Sprite(centerX, centerY, m_BackgroundTextureRegion);
		//m_mainScene.attachChild(background);
		
		mScoreText = new Text(400, 5, this.m_Font, "Score:", HorizontalAlign.LEFT);
		mScoreTextValue = new ChangeableText(400 + mScoreText.getWidth() + 5, 5, this.m_Font, score + "", 
				HorizontalAlign.LEFT, 10);
		mLevelText = new Text(0,5, this.m_Font, "Level:", HorizontalAlign.LEFT);
		mLevelTextValue = new ChangeableText(mLevelText.getWidth() + 5, 5, this.m_Font, (level+1)+"",
				HorizontalAlign.LEFT, 1);
		
		
		m_TopHUD = new GameHUD(this.m_Camera, 0, 0, CAMERA_WIDTH, 15);
		m_TopHUD.attachChild(mScoreText);
		m_TopHUD.attachChild(mScoreTextValue);
		m_TopHUD.attachChild(mLevelText);
		m_TopHUD.attachChild(mLevelTextValue);
		//mLifeHUD.setFrameColor(0.0f, 0.0f, 0.0f, 0.0f);
		//mLifeHUD.setBackColor(1.0f, 0.0f, 0.0f, 1.0f);
		//mLifeHUD.setProgressColor(0.0f, 1.0f, 0.0f, 1.0f);
		//mLifeHUD.attachChild(lifeHUDBackground);
		//mLifeHUD.attachChild(statusBackground);
		
		this.m_Camera.setHUD(m_TopHUD);
		
		m_mainScene.setTouchAreaBindingEnabled(true);
		m_mainScene.setOnAreaTouchListener(this);
		m_mainScene.setOnSceneTouchListener(this);
		
		mBackgroundMusic.play();
		CreateQuestion(level);
		return m_mainScene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub
	}

	@Override
	public void onAnimationEnd(AnimatedSprite pAnimatedSprite) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent, ITouchArea pTouchArea, float pTouchAreaLocalX,
			float pTouchAreaLocalY) {
		//m_mainScene.onSceneTouchEvent(pSceneTouchEvent);
		 //answer1.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		 //answer2.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		 //answer3.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
		 return true;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

}
