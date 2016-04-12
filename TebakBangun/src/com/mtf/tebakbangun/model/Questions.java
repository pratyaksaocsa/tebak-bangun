package com.mtf.tebakbangun.model;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.mtf.tebakbangun.PlayGameActivity;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTiledTextureRegion;

public class Questions extends Sprite {
	
	AnswerButton answerButtons[];
	private TextureRegion mAnswerTextureRegion;
	private TiledTextureRegion mShapeTextureRegion;
	private Font m_Font;
	
	BasicShapeAnimated image;
	
	public enum SHAPE
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
	
	public Questions(TextureRegion backgroundTexture, TextureRegion answerTexture, PixelPerfectTiledTextureRegion shapeTexture, Font mFont)
	{
		super(0, 0, PlayGameActivity.getInstance().CAMERA_WIDTH, PlayGameActivity.getInstance().CAMERA_HEIGHT, backgroundTexture);
		mAnswerTextureRegion = answerTexture;
		mShapeTextureRegion = shapeTexture;
		m_Font = mFont;
		InitAnswerButton();
		InitShape();
	}
	
	private void InitAnswerButton()
	{
		answerButtons = new AnswerButton[3];
		int correctAnswer = (int)(Math.random() * 3);
		int pX = 0;
		int pY = PlayGameActivity.getInstance().CAMERA_HEIGHT - AnswerButton.ANSWER_HEIGHT;
		for(int i=0;i<3;i++)
		{
			pX += AnswerButton.PADDING;
			//TODO: Do not Random it
			String answerText = SHAPE.getString((int)(Math.random() * 5));
			answerButtons[i] = new AnswerButton(mAnswerTextureRegion, m_Font, answerText);
			if(i == correctAnswer)
				answerButtons[i].SetIsCorrectAnswer(true);
			answerButtons[i].setPosition(pX, pY);
			pX += AnswerButton.ANSWER_WIDTH;
			pX += AnswerButton.PADDING;
		}
		for(int i=0;i<3;i++)
		{
			PlayGameActivity.getInstance().getMainScene().registerTouchArea(answerButtons[i]);
			this.attachChild(answerButtons[i]);
		}
	}
	
	private void InitShape()
	{
		int ANIM_WIDTH = 200, ANIM_HEIGHT = 200;
		final int centerX = PlayGameActivity.getInstance().CAMERA_WIDTH / 2;
		final int centerY = PlayGameActivity.getInstance().CAMERA_HEIGHT / 2;
		image = new BasicShapeAnimated(centerX-(ANIM_WIDTH/2), centerY-(ANIM_HEIGHT/2),
				ANIM_WIDTH, ANIM_HEIGHT, mShapeTextureRegion);
		this.attachChild(image);
		image.animate(100);
	}
}
