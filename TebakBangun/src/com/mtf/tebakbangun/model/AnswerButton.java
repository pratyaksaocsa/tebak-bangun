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
	public static final int ANSWER_WIDTH = 200, ANSWER_HEIGHT = 75, PADDING = 30;
	
	public void SetIsCorrectAnswer(boolean answer)
	{
		m_isCorrectAnswer = answer;
	}
	
	public boolean IsCorrectAnswer()
	{
		return m_isCorrectAnswer;
	}
	
	public AnswerButton(TextureRegion pTextureRegion, Font pFont, String pText) {
		super(0, 0, ANSWER_WIDTH, ANSWER_HEIGHT, pTextureRegion);
		Text buttonText = new Text(0, 0, pFont, pText, HorizontalAlign.CENTER);
		m_isCorrectAnswer = false;
		buttonText.setPosition((this.getX() + this.getWidth() - buttonText.getWidth()) / 2, (this.getY() + this.getHeight() - buttonText.getHeight()) / 2);
		this.attachChild(buttonText);
	}
	
	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX, float pTouchAreaLocalY)
	{
		if (pSceneTouchEvent.isActionUp())
		{
			PlayGameActivity.getInstance().getClickSound().play();
			if(m_isCorrectAnswer)
			{
				PlayGameActivity.getInstance().addScore();
				PlayGameActivity.getInstance().NextQuestion();
			}
			return true;
		}
		return false;
	}
}
