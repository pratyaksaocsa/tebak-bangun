package com.mtf.tebakbangun.model;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.util.HorizontalAlign;

import com.mtf.tebakbangun.PlayGameActivity;

public class AnswerButton extends Sprite {

	private boolean m_isCorrectAnswer;
	private PlayGameActivity m_playGameActivity;
	public static final int ANSWER_WIDTH = 200, ANSWER_HEIGHT = 75, PADDING = 20;
	
	public void SetIsCorrectAnswer(boolean answer)
	{
		m_isCorrectAnswer = answer;
	}
	
	public boolean IsCorrectAnswer()
	{
		return m_isCorrectAnswer;
	}
	
	public AnswerButton(TextureRegion pTextureRegion, Font pFont, String pText, PlayGameActivity play) {
		super(0, 0, ANSWER_WIDTH, ANSWER_HEIGHT, pTextureRegion);
		Text buttonText = new Text(0, 0, pFont, pText, HorizontalAlign.CENTER);
		m_isCorrectAnswer = false;
		m_playGameActivity = play;
		buttonText.setPosition((this.getX() + this.getWidth() - buttonText.getWidth()) / 2, (this.getY() + this.getHeight() - buttonText.getHeight()) / 2);
		this.attachChild(buttonText);
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
	
	public void Draw()
	{
		m_playGameActivity.getMainScene().registerTouchArea(this);
		m_playGameActivity.getMainScene().attachChild(this);
	}
}
