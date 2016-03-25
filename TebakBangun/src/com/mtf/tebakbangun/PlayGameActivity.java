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
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTextureRegionFactory;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTiledTextureRegion;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;

public class PlayGameActivity extends BaseGameActivity implements 
		IOnSceneTouchListener, IOnAreaTouchListener, IAnimationListener {

	private static final int CAMERA_WIDTH = 720;
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
	
	BitmapTextureAtlas[] m_BasicShapeAtlas;
	PixelPerfectTiledTextureRegion[] m_BasicShapeTiledTextureRegion;
	String m_BasicShapeFilename[] = {"shape1.png","shape2.png","shape3.png","shape1.png","shape2.png","shape3.png"};

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
		
		// load font
		this.m_FontTextureAtlas = new BitmapTextureAtlas(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_Font = FontFactory.createFromAsset(m_FontTextureAtlas, this, "PORKYS_.TTF", 32, true, Color.BLACK);
		this.mEngine.getTextureManager().loadTexture(this.m_FontTextureAtlas);
		this.mEngine.getFontManager().loadFont(m_Font);
		
		// load shape
		this.m_BasicShapeAtlas = new BitmapTextureAtlas[SHAPE.COUNT.getValue()];
		this.m_BasicShapeTiledTextureRegion = new PixelPerfectTiledTextureRegion[SHAPE.COUNT.getValue()];
		for (int i = 0; i < m_BasicShapeAtlas.length; i++) {
			this.m_BasicShapeAtlas[i] = new BitmapTextureAtlas(512, 512,
					TextureOptions.BILINEAR_PREMULTIPLYALPHA);
			this.m_BasicShapeTiledTextureRegion[i] = PixelPerfectTextureRegionFactory
					.createTiledFromAsset(m_BasicShapeAtlas[i], this, m_BasicShapeFilename[i], 0, 0,
							4, 2);
		}
		/*this.mEnemy = new Enemy[ENEMY_SIZE];
		for (int i = 0; i < mEnemy.length; i++) {
			this.mEnemy[i] = new Enemy(0, 0,
					mEnemyPerfectTiledTextureRegion[i], mFont, i);
		}*/
		
		// load background
		this.m_BackgroundAtlas = new BitmapTextureAtlas(1024, 1024, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.m_BackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(m_BackgroundAtlas, this, "maps1.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(m_BackgroundAtlas);
		
		
		SoundFactory.setAssetBasePath("mfx/");
		try {
			mClickSound = SoundFactory.createSoundFromAsset(
					mEngine.getSoundManager(), this, "click.wav");
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		MusicFactory.setAssetBasePath("mfx/");
		try {
			mBackgroundMusic = MusicFactory.createMusicFromAsset(
					mEngine.getMusicManager(), this, "background_mixed.mp3");
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
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		// TODO Auto-generated method stub
		return false;
	}

}
