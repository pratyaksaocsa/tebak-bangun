package com.mtf.tebakbangun;

import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.anddev.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.anddev.andengine.ui.activity.BaseSplashActivity;

import android.app.Activity;

public class SplashActivity extends BaseSplashActivity {

	@Override
	protected ScreenOrientation getScreenOrientation() {
		return ScreenOrientation.LANDSCAPE;
	}

	@Override
	protected IBitmapTextureAtlasSource onGetSplashTextureAtlasSource() {
		return new AssetBitmapTextureAtlasSource(this, "gfx/splash.png");
	}

	@Override
	protected float getSplashDuration() {
		return 2;
	}

	@Override
	protected Class<? extends Activity> getFollowUpActivity() {
		return PlayGameActivity.class;
	}

}
