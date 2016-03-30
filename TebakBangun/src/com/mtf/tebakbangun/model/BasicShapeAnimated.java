package com.mtf.tebakbangun.model;

import org.anddev.andengine.entity.sprite.AnimatedSprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.mtf.tebakbangun.PlayGameActivity;

public class BasicShapeAnimated extends AnimatedSprite {

	private PlayGameActivity m_playGameActivity;
	
	public BasicShapeAnimated(float pX, float pY, float pWidth, float pHeight, TiledTextureRegion pTextureRegion, PlayGameActivity play) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		m_playGameActivity = play;
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY)
	{
		/*if (pSceneTouchEvent.isActionUp())
		{
			if(m_isCorrectAnswer)
			{
				m_playGameActivity.addScore();
			}
			m_playGameActivity.getClickSound().play();
			return true;
		}*/
		return false;
	}
}
