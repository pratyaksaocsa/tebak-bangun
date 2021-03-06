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

public class MainMenuActivity extends BaseGameActivity implements IOnMenuItemClickListener {

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	
	private static class MENU
	{
		public static final int PLAY = 0;
		public static final int CREDIT = 1;
		public static final int HELP = 2;
		public static final int QUIT = 3;
	};
	
	private Camera mCamera;
	private Scene mMainScene;
	private BitmapTextureAtlas mBackgroundTextureAtlas;
	private TextureRegion mBackgroundTexture;
	private MenuScene mStaticMenuScene;
	private BitmapTextureAtlas mFontTextureAtlas;
	protected Font mFont;
	private Music mBackgroundMusic;
	private Sound mClickSound;

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
		this.mBackgroundTextureAtlas = new BitmapTextureAtlas(1024, 512,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackgroundTexture = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTextureAtlas, this,
						"menu.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(this.mBackgroundTextureAtlas);
		this.mFontTextureAtlas = new BitmapTextureAtlas(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		FontFactory.setAssetBasePath("font/");
		this.mFont = FontFactory.createFromAsset(mFontTextureAtlas, this, "PORKYS_.TTF", 32, true, Color.RED);
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
		this.mStaticMenuScene = new MenuScene(this.mCamera);
		final IMenuItem playMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU.PLAY, mFont, "PLAY GAME"), 0f, 0f, 0.5f,
				0.5f, 0.5f, 0.5f);
		playMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA,GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		this.mStaticMenuScene.addMenuItem(playMenuItem);
		final IMenuItem helpMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU.HELP, this.mFont, "HELP"), 0f, 0f, 0.5f,
				0.5f, 0.5f, 0.5f);
		helpMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		this.mStaticMenuScene.addMenuItem(helpMenuItem);
		final IMenuItem creditMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU.CREDIT, this.mFont, "CREDIT"), 0f, 0f,
				0.5f, 0.5f, 0.5f, 0.5f);
		creditMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		this.mStaticMenuScene.addMenuItem(creditMenuItem);
		final IMenuItem quitMenuItem = new ColorMenuItemDecorator(
				new TextMenuItem(MENU.QUIT, this.mFont, "QUIT"), 0f, 0f, 0.5f,
				0.5f, 0.5f, 0.5f);
		quitMenuItem.setBlendFunction(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		
		this.mStaticMenuScene.addMenuItem(quitMenuItem);
		this.mStaticMenuScene.buildAnimations();
		this.mStaticMenuScene.setBackgroundEnabled(false);
		this.mStaticMenuScene.setOnMenuItemClickListener(this);
		
		playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() - 20);
		helpMenuItem.setPosition(helpMenuItem.getX(), playMenuItem.getY() + 45);
		creditMenuItem.setPosition(creditMenuItem.getX(), helpMenuItem.getY() + 45);
		quitMenuItem.setPosition(quitMenuItem.getX(), creditMenuItem.getY() + 50);
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
		mMainScene.attachChild(background);
		mMainScene.setChildScene(mStaticMenuScene);
		return mMainScene;
	}

	@Override
	public void onLoadComplete() {
		

	}

	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX,
			float pMenuItemLocalY) {
		mClickSound.play();
		Intent intent = null;
		switch (pMenuItem.getID()) {
			case MENU.PLAY:
				intent = new Intent(MainMenuActivity.this, PlayGameActivity.class);
				MainMenuActivity.this.startActivity(intent);
				finish();
				break;
			case MENU.HELP:
				intent = new Intent(MainMenuActivity.this, TutorialActivity.class);
				MainMenuActivity.this.startActivity(intent);
				finish();
				break;
			case MENU.CREDIT:
				intent = new Intent(MainMenuActivity.this, CreditActivity.class);
				MainMenuActivity.this.startActivity(intent);
				finish();
				break;
			case MENU.QUIT:
				finish();
				return true;
			default:
		}
		return true;
	}

}
