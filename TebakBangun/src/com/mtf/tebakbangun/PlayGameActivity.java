package com.mtf.tebakbangun;

import java.io.IOException;

import javax.microedition.khronos.opengles.GL10;

import org.anddev.andengine.audio.music.Music;
import org.anddev.andengine.audio.music.MusicFactory;
import org.anddev.andengine.audio.sound.Sound;
import org.anddev.andengine.audio.sound.SoundFactory;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.WakeLockOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
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
import com.mtf.tebakbangun.model.BasicShapeAnimated;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

public class PlayGameActivity extends BaseGameActivity implements 
		IOnSceneTouchListener, IOnAreaTouchListener, IAnimationListener {

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private enum SHAPE
	{
		CUBE(0),BLOCK(1),TUBE(2),CONE(3),PYRAMID(4),BALL(5),COUNT(6);
		private int value;
		
		private SHAPE(int value) {
	        this.value = value;
	    }

	    public int getValue() {
	        return value;
	    }
	    
	    public static String getString(int value)
	    {
	    	switch(value)
	    	{
	    		case 0:
	    			return "KUBUS";
	    		case 1:
	    			return "BALOK";
	    		case 2:
	    			return "TABUNG";
	    		case 3:
	    			return "KERUCUT";
	    		case 4:
	    			return "PIRAMIDA";
	    		case 6:
	    			return "BOLA";
	    		default:
	    			return "TIDAK DIKENAL";
	    	}
	    }
	};

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
	private int score = 0, level = 1;
	private ChangeableText mScoreTextValue, mLevelTextValue;
	//public static ChangeableText mGoldTextValue;
	private Text mScoreText, mLevelText;
	
	BitmapTextureAtlas mAnswerTextureAtlas;
	TextureRegion mAnswerTexture;
	
	BitmapTextureAtlas[] m_BasicShapeAtlas;
	PixelPerfectTiledTextureRegion[] m_BasicShapeTiledTextureRegion;
	String m_BasicShapeFilename[] = {"shape1.png","shape2.png","shape3.png","shape1.png","shape2.png","shape3.png"};
	BasicShapeAnimated questions[];
	
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
		m_mainScene.detachChild(questions[level%6]);
		this.level += 1;
		mLevelTextValue.setText(String.valueOf(this.level));
		CreateAnswerButton();
		CreateQuestion();
	}
	
	private void CreateAnswerButton()
	{
		AnswerButton answers[] = new AnswerButton[3];
		int correctAnswer = (int)(Math.random() * 3);
		int pX = 0;
		int pY = CAMERA_HEIGHT - AnswerButton.ANSWER_HEIGHT;
		for(int i=0;i<3;i++)
		{
			pX += AnswerButton.PADDING;
			//TODO: Do not Random it
			String answerText = SHAPE.getString((int)(Math.random() * 5));
			answers[i] = new AnswerButton(mAnswerTexture, m_Font, answerText, this);
			if(i == correctAnswer)
				answers[i].SetIsCorrectAnswer(true);
			answers[i].setPosition(pX, pY);
			answers[i].Draw();
			pX += AnswerButton.ANSWER_WIDTH;
			pX += AnswerButton.PADDING;
		}
	}
	
	private void CreateQuestion()
	{
		int QUESTION_WIDTH = 200, QUESTION_HEIGHT = 200;
		final int centerX = CAMERA_WIDTH / 2;
		final int centerY = CAMERA_HEIGHT / 2;
		//TODO: This will create an infinity question
		questions[level%6] = new BasicShapeAnimated(centerX-(QUESTION_WIDTH/2), centerY-(QUESTION_HEIGHT/2), 
				QUESTION_WIDTH, QUESTION_HEIGHT, m_BasicShapeTiledTextureRegion[(level-1)%6], this);
		m_mainScene.registerTouchArea(questions[level%6]);
		m_mainScene.attachChild(questions[level%6]);
		questions[level%6].animate(100);
	}
	
	@Override
	public Engine onLoadEngine() {
		this.m_Camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		m_engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
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
		questions = new BasicShapeAnimated[SHAPE.COUNT.getValue()];
		this.m_BasicShapeAtlas = new BitmapTextureAtlas[SHAPE.COUNT.getValue()];
		this.m_BasicShapeTiledTextureRegion = new PixelPerfectTiledTextureRegion[SHAPE.COUNT.getValue()];
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
		this.mAnswerTexture = BitmapTextureAtlasTextureRegionFactory
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
		final int centerX = (CAMERA_WIDTH - this.m_BackgroundTextureRegion.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.m_BackgroundTextureRegion.getHeight()) / 2;
		Sprite background = new Sprite(centerX, centerY, m_BackgroundTextureRegion);
		m_mainScene.attachChild(background);
		
		mScoreText = new Text(400, 5, this.m_Font, "Score:", HorizontalAlign.LEFT);
		mScoreTextValue = new ChangeableText(400 + mScoreText.getWidth() + 5, 5, this.m_Font, score + "", 
				HorizontalAlign.LEFT, 10);
		mLevelText = new Text(0,5, this.m_Font, "Level:", HorizontalAlign.LEFT);
		mLevelTextValue = new ChangeableText(mLevelText.getWidth() + 5, 5, this.m_Font, level+"",
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
		CreateAnswerButton();
		CreateQuestion();
		
		this.m_Camera.setHUD(m_TopHUD);
		
		m_mainScene.setTouchAreaBindingEnabled(true);
		m_mainScene.setOnAreaTouchListener(this);
		m_mainScene.setOnSceneTouchListener(this);
		
		mBackgroundMusic.play();
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
