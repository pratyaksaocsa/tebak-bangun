package com.mtf.tebakbangun.model;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.texture.region.TextureRegion;

import com.mtf.tebakbangun.PlayGameActivity;

public class AnswerButton extends Sprite {

	private boolean m_isCorrectAnswer;
	private PlayGameActivity m_playGameActivity;
	
	public void SetIsCorrectAnswer(boolean answer)
	{
		m_isCorrectAnswer = answer;
	}
	
	public boolean IsCorrectAnswer()
	{
		return m_isCorrectAnswer;
	}
	
	public AnswerButton(float pX, float pY, float pWidth, float pHeight, TextureRegion pTextureRegion, PlayGameActivity play) {
		super(pX, pY, pWidth, pHeight, pTextureRegion);
		m_isCorrectAnswer = false;
		m_playGameActivity = play;
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY)
	{
		if (pSceneTouchEvent.isActionUp())
		{
			if(m_isCorrectAnswer)
			{
				m_playGameActivity.addScore();
			}
			m_playGameActivity.getClickSound().play();
			return true;
		}
		return false;
	}
}
