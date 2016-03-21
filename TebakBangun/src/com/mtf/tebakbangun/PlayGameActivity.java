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
import org.anddev.andengine.entity.scene.menu.MenuScene;
import org.anddev.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.anddev.andengine.entity.scene.menu.item.IMenuItem;
import org.anddev.andengine.entity.scene.menu.item.TextMenuItem;
import org.anddev.andengine.entity.scene.menu.item.decorator.ColorMenuItemDecorator;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.content.Intent;
import android.graphics.Color;

public class PlayGameActivity extends BaseGameActivity implements IOnMenuItemClickListener {

	// ===========================================================
	// Constants
	// ===========================================================
	private static final int CAMERA_WIDTH = 720;
	private static final int CAMERA_HEIGHT = 480;
	
	private enum MENU
	{
		PLAY,
		SCORES,
		CREDIT,
		HELP,
		QUIT,
		UNKNOWN;
		
		public static MENU fromInteger(int x) {
	        switch(x) {
			case 0:
				return PLAY;
			case 1:
				return SCORES;
	        case 2:
	            return CREDIT;
			case 3:
				return HELP;
	        case 4:
	            return QUIT;
	        }
	        return UNKNOWN;
	    }
		
		 public static int toInteger(MENU x) {
	        switch(x) {
			case PLAY:
				return 0;
			case SCORES:
				return 1;
	        case CREDIT:
	            return 2;
			case HELP:
				return 3;
	        case QUIT:
	            return 4;
	            default:
	        return -1;
	        }
	    }
	};
	
	// ===========================================================
	// Fields
	// ===========================================================
	private Camera mCamera;
	private Scene mMainScene;
	private BitmapTextureAtlas mBackgroundTextureAtlas;
	private TextureRegion mBackgroundTexture;
	private MenuScene mStaticMenuScene;
	private BitmapTextureAtlas mFontTextureAtlas;
	protected Font mFont;
	private Music mBackgroundMusic;
	private Sound mClickSound;
	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mCamera).setNeedsMusic(true).setNeedsSound(true)
				.setWakeLockOptions(WakeLockOptions.SCREEN_ON));
	}

	@Override
	public void onLoadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBackgroundTextureAtlas = new BitmapTextureAtlas(1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackgroundTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTextureAtlas, this,
						"menubg.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(
				this.mBackgroundTextureAtlas);
		this.mFontTextureAtlas = new BitmapTextureAtlas(256, 256,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(mFontTextureAtlas, this,
				"BILLO.TTF", 32, true, Color.RED);
		this.mEngine.getTextureManager().loadTexture(this.mFontTextureAtlas);
		this.mEngine.getFontManager().loadFont(this.mFont);

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

	private void createStaticMenu() {
		// TODO Auto-generated method stub
		this.mStaticMenuScene = new MenuScene(this.mCamera);
		final IMenuItem playMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU.toInteger(MENU.PLAY), mFont, "PLAY GAME"), 0f, 0f, 0.5f,
				0.5f, 0.5f, 0.5f);
		playMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(playMenuItem);
		final IMenuItem scoreMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU.toInteger(MENU.SCORES), mFont, "SCORES"), 0f, 0f, 0.5f,
				0.5f, 0.5f, 0.5f);
		scoreMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(scoreMenuItem);
		final IMenuItem helpMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU.toInteger(MENU.HELP), this.mFont, "HELP"), 0f, 0f, 0.5f,
				0.5f, 0.5f, 0.5f);
		helpMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(helpMenuItem);
		final IMenuItem creditMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU.toInteger(MENU.CREDIT), this.mFont, "CREDIT"), 0f, 0f,
				0.5f, 0.5f, 0.5f, 0.5f);
		creditMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(creditMenuItem);
		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU.toInteger(MENU.QUIT), this.mFont, "QUIT"), 0f, 0f, 0.5f,
				0.5f, 0.5f, 0.5f);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,
				GL10.GL_ONE_MINUS_SRC_ALPHA);
		this.mStaticMenuScene.addMenuItem(quitMenuItem);
		this.mStaticMenuScene.buildAnimations();
		this.mStaticMenuScene.setBackgroundEnabled(false);
		this.mStaticMenuScene.setOnMenuItemClickListener(this);
	}
	
	@Override
	public Scene onLoadScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		mBackgroundMusic.play();
		createStaticMenu();
		final int centerX = (CAMERA_WIDTH - this.mBackgroundTexture.getWidth()) / 2;
		final int centerY = (CAMERA_HEIGHT - this.mBackgroundTexture.getHeight()) / 2;
		this.mMainScene = new Scene();
		Sprite background = new Sprite(centerX, centerY, mBackgroundTexture);
		//mMainScene.getChildScene().attachChild(background);
		mMainScene.attachChild(background);
		mMainScene.setChildScene(mStaticMenuScene);
		return mMainScene;
	}

	@Override
	public void onLoadComplete() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX,
			float pMenuItemLocalY) {
		mClickSound.play();
		//Intent intent = null;
		MENU selectedMenu = MENU.fromInteger(pMenuItem.getID());
		switch (selectedMenu) {
		/*case (MENU_PLAY):
			intent = new Intent(BubbleDefenseActivity.this,
					PlayGameActivity.class);
			BubbleDefenseActivity.this.startActivity(intent);
			finish();
			break;
		case (MENU_SCORES):
			intent = new Intent(BubbleDefenseActivity.this,
					ScoreActivity.class);
			BubbleDefenseActivity.this.startActivity(intent);
			finish();
			break;
		case (MENU_HELP):
			intent = new Intent(BubbleDefenseActivity.this, HelpActivity.class);
			BubbleDefenseActivity.this.startActivity(intent);
			finish();
			break;
		case (MENU_CREDIT):
			intent = new Intent(BubbleDefenseActivity.this,
					CreditActivity.class);
			BubbleDefenseActivity.this.startActivity(intent);
			finish();
			break;*/
		case QUIT:
			finish();
			return true;
		default:
		}
		return true;
	}

}
