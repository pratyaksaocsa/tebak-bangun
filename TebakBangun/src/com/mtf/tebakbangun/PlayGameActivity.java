package com.mtf.tebakbangun;

import java.io.IOException;
import java.util.Random;

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
	
	BitmapTextureAtlas mAnswerTextureAtlas;
	TextureRegion mAnswerTextureRegion;
	
	BitmapTextureAtlas[] m_Level1ShapeAtlas, m_Level2ShapeAtlas, m_Level3ShapeAtlas;
	PixelPerfectTiledTextureRegion[] m_Level1TiledTextureRegion, m_Level2TiledTextureRegion, m_Level3TiledTextureRegion;
	String m_Level1Filename[] = {"cube.png","block.png","tube.png","cone.png","pyramid.png","ball.png"};
	String m_Level2Filename[] = {"blockball.png", "conecube.png", "pyramidtube.png"};
	String m_Level3Filename[] = {"tublock.png", "pyracube.png", "coneball.png"};
	
	Questions questions[];
	
	public void addScore() {
		this.score += 10;
		m_TopHUD.SetScore(String.valueOf(score));
	}
	
	public Sound getClickSound()
	{
		return mClickSound;
	}
	
	public Scene getMainScene()
	{
		return m_mainScene;
	}
	
	public Font getFont()
	{
		return m_Font;
	}
	
	public void NextQuestion()
	{
		if (level < 4)
		{
			questions[level].Destroy();
			this.level += 1;
			m_TopHUD.SetLevel(String.valueOf(this.level+1));
			ShowQuestion(this.level);
		}
		else
		{
			//USER WIN
			m_TopHUD.SetScore("YOU WIN!!");
			level = 0;
		}
	}
	
	private void CreateQuestion()
	{
		questions = new Questions[5]; //Because we just have 5 levels
		Random rn = new Random();
		int indexLevel1 = rn.nextInt(6);
		int indexLevel2_1 = -1, indexLevel2_2 = -1;
		do
		{
			indexLevel2_1 = rn.nextInt(3);
			indexLevel2_2 = rn.nextInt(3);
		} while (indexLevel2_1 == indexLevel2_2);
		int indexLevel3_1 = -1, indexLevel3_2 = -1;
		do
		{
			indexLevel3_1 = rn.nextInt(3);
			indexLevel3_2 = rn.nextInt(3);
		} while (indexLevel3_1 == indexLevel3_2);
		int ID = indexLevel1; //FOR answer
		questions[0] = new Questions(ID, 1, m_BackgroundTextureRegion, mAnswerTextureRegion, m_Level1TiledTextureRegion[indexLevel1]);
		ID = indexLevel2_1 + 6;
		questions[1] = new Questions(ID, 2, m_BackgroundTextureRegion, mAnswerTextureRegion, m_Level2TiledTextureRegion[indexLevel2_1]);
		ID = indexLevel2_2 + 6;
		questions[2] = new Questions(ID, 3, m_BackgroundTextureRegion, mAnswerTextureRegion, m_Level2TiledTextureRegion[indexLevel2_2]);
		ID = indexLevel3_1 + 9;
		questions[3] = new Questions(ID, 4, m_BackgroundTextureRegion, mAnswerTextureRegion, m_Level3TiledTextureRegion[indexLevel3_1]);
		ID = indexLevel3_2 + 9;
		questions[4] = new Questions(ID, 5, m_BackgroundTextureRegion, mAnswerTextureRegion, m_Level3TiledTextureRegion[indexLevel3_2]);
	}
	
	private void ShowQuestion(int level)
	{
		m_mainScene.attachChild(questions[level]);
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
		this.m_Level1ShapeAtlas = new BitmapTextureAtlas[6]; //Because we only have 6 shape for level 1
		this.m_Level2ShapeAtlas = new BitmapTextureAtlas[3]; //Because we only have 3 shape for level 2
		this.m_Level3ShapeAtlas = new BitmapTextureAtlas[3]; //Because we only have 3 shape for level 3
		this.m_Level1TiledTextureRegion = new PixelPerfectTiledTextureRegion[6]; //Because we only have 6 shape for level 1
		this.m_Level2TiledTextureRegion = new PixelPerfectTiledTextureRegion[3]; //Because we only have 3 shape for level 2
		this.m_Level3TiledTextureRegion = new PixelPerfectTiledTextureRegion[3]; //Because we only have 3 shape for level 3
		for (int i = 0; i < m_Level1ShapeAtlas.length; i++) {
			this.m_Level1ShapeAtlas[i] = new BitmapTextureAtlas(512, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.m_Level1TiledTextureRegion[i] = PixelPerfectTextureRegionFactory
					.createTiledFromAsset(m_Level1ShapeAtlas[i], this, m_Level1Filename[i], 0, 0, 4, 2);
		}
		for (int i = 0; i < m_Level2ShapeAtlas.length; i++) {
			this.m_Level2ShapeAtlas[i] = new BitmapTextureAtlas(1024, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.m_Level2TiledTextureRegion[i] = PixelPerfectTextureRegionFactory
					.createTiledFromAsset(m_Level2ShapeAtlas[i], this, m_Level2Filename[i], 0, 0, 4, 2);
		}
		for (int i = 0; i < m_Level3ShapeAtlas.length; i++) {
			this.m_Level3ShapeAtlas[i] = new BitmapTextureAtlas(1024, 512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.m_Level3TiledTextureRegion[i] = PixelPerfectTextureRegionFactory
					.createTiledFromAsset(m_Level3ShapeAtlas[i], this, m_Level3Filename[i], 0, 0, 4, 2);
		}
		for (int i = 0; i < m_Level1Filename.length; i++) {
			this.mEngine.getTextureManager().loadTexture(m_Level1ShapeAtlas[i]);
		}
		for (int i = 0; i < m_Level2Filename.length; i++) {
			this.mEngine.getTextureManager().loadTexture(m_Level2ShapeAtlas[i]);
		}
		for (int i = 0; i < m_Level3Filename.length; i++) {
			this.mEngine.getTextureManager().loadTexture(m_Level3ShapeAtlas[i]);
		}
		
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

		m_TopHUD = new GameHUD(this.m_Camera, 0, 0, CAMERA_WIDTH, 15);
		this.m_Camera.setHUD(m_TopHUD);
		
		m_mainScene.setTouchAreaBindingEnabled(true);
		m_mainScene.setOnAreaTouchListener(this);
		m_mainScene.setOnSceneTouchListener(this);
		
		mBackgroundMusic.play();
		CreateQuestion();
		ShowQuestion(level);
		return m_mainScene;
	}

	@Override
	public void onLoadComplete() {

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
