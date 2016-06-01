package com.mtf.tebakbangun;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.font.FontFactory;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.anddev.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.anddev.andengine.util.HorizontalAlign;

import android.content.Intent;
import android.graphics.Color;

public class CreditActivity extends BaseGameActivity {

	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;

	private Camera mCamera;
	private Scene mCreditScene;
	private BitmapTextureAtlas mBackgroundTextureAtlas;
	private TextureRegion mBackgroundTextureRegion;
	private BitmapTextureAtlas mButtonHomeTextureAtlas;
	private TextureRegion mButtonHomeTextureRegion;
	private BitmapTextureAtlas mFontTextureAtlas;
	private Font mFont;

	@Override
	public Engine onLoadEngine() {
		this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		return new Engine(new EngineOptions(true, ScreenOrientation.LANDSCAPE,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT),
				this.mCamera));
	}

	@Override
	public void onLoadResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mBackgroundTextureAtlas = new BitmapTextureAtlas(1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mBackgroundTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mBackgroundTextureAtlas, this,
						"menu2.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(mBackgroundTextureAtlas);
		this.mButtonHomeTextureAtlas = new BitmapTextureAtlas(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mButtonHomeTextureRegion = BitmapTextureAtlasTextureRegionFactory
				.createFromAsset(this.mButtonHomeTextureAtlas, this,
						"buttonHome.png", 0, 0);
		this.mEngine.getTextureManager().loadTexture(mButtonHomeTextureAtlas);
		FontFactory.setAssetBasePath("font/");
		this.mFontTextureAtlas = new BitmapTextureAtlas(1024, 1024,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		this.mFont = FontFactory.createFromAsset(this.mFontTextureAtlas, this,
				"PORKYS_.TTF", 24, true, Color.BLACK);
		this.mEngine.getTextureManager().loadTexture(mFontTextureAtlas);
		this.mEngine.getFontManager().loadFont(this.mFont);
	}

	@Override
	public Scene onLoadScene() {
		this.mCreditScene = new Scene();

		Sprite backgroundSprite = new Sprite(
				(CAMERA_WIDTH - this.mBackgroundTextureRegion.getWidth()) / 2,
				(CAMERA_HEIGHT - this.mBackgroundTextureRegion.getHeight()) / 2,
				this.mBackgroundTextureRegion);
		this.mCreditScene.attachChild(backgroundSprite);
		Sprite buttonHomeSprite = new Sprite(
				(CAMERA_WIDTH - this.mButtonHomeTextureRegion.getWidth()) / 2,
				CAMERA_HEIGHT - this.mButtonHomeTextureRegion.getHeight(),
				this.mButtonHomeTextureRegion) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				Intent myIntent = new Intent(CreditActivity.this, MainMenuActivity.class);
				CreditActivity.this.startActivity(myIntent);
				finish();
				return true;

			}
		};
		this.mCreditScene.attachChild(buttonHomeSprite);
		this.mCreditScene.registerTouchArea(buttonHomeSprite);
		String stringCredit = "Created by :\n"
				+ "Pratyaksa Ocsa \n"
				+ "Don Bosco Surya Atmaja \n"
				+ "Va Seangly \n";
		Text mTextCredit = new Text(110, 100, this.mFont, stringCredit, HorizontalAlign.LEFT);
		mCreditScene.attachChild(mTextCredit);
		this.mCreditScene.setTouchAreaBindingEnabled(true);
		this.mCreditScene.setBackgroundEnabled(false);
		return mCreditScene;
	}

	@Override
	public void onLoadComplete() {
	}
}