package com.mtf.tebakbangun.model;

import java.util.Random;

import org.anddev.andengine.entity.IEntity;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.mtf.tebakbangun.PlayGameActivity;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTiledTextureRegion;

import android.util.Log;

public class Questions extends Sprite {
	
	private int ID;
	AnswerButton answerButtons[];
	private TextureRegion mAnswerTextureRegion;
	private TiledTextureRegion mShapeTextureRegion;
	private Font m_Font;
	
	BasicShapeAnimated image;
	
	public enum SHAPE
	{
		CUBE(5),BLOCK(1),TUBE(2),CONE(3),PYRAMID(4),BALL(0),COUNT(6);
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
	    			return "BOLA";
	    		case 1:
	    			return "BALOK";
	    		case 2:
	    			return "TABUNG";
	    		case 3:
	    			return "KERUCUT";
	    		case 4:
	    			return "PIRAMIDA";
	    		case 5:
	    			return "KUBUS";
	    		default:
	    			return "TIDAK DIKENAL";
	    	}
	    }
	};
	
	public Questions(int id, TextureRegion backgroundTexture, TextureRegion answerTexture, PixelPerfectTiledTextureRegion shapeTexture, Font mFont)
	{
		super(0, 0, PlayGameActivity.getInstance().CAMERA_WIDTH, PlayGameActivity.getInstance().CAMERA_HEIGHT, backgroundTexture);
		this.ID = id;
		mAnswerTextureRegion = answerTexture;
		mShapeTextureRegion = shapeTexture;
		m_Font = mFont;
		InitAnswerButton();
		InitShape();
	}
	
	public void Destroy()
	{
		for(int i=0;i<3;i++)
		{
			PlayGameActivity.getInstance().getMainScene().unregisterTouchArea(answerButtons[i]);
			this.detachChild(answerButtons[i]);
		}
		this.detachChild(image);
	}
	
	private void InitAnswerButton()
	{
		answerButtons = new AnswerButton[3];
		Random rn = new Random();
		int correctAnswer = rn.nextInt(3);
		int pX = 0;
		int pY = PlayGameActivity.getInstance().CAMERA_HEIGHT - AnswerButton.ANSWER_HEIGHT;
		for(int i=0;i<3;i++)
		{
			pX += AnswerButton.PADDING;
			answerButtons[i] = new AnswerButton(mAnswerTextureRegion, m_Font, "");
			if(i == correctAnswer)
			{
				String answerText = SHAPE.getString(this.ID);
				answerButtons[i].SetText(answerText);
				answerButtons[i].SetIsCorrectAnswer(true);
			}
			else
			{
				//int ans = -1;
				//do
				//{
				//	ans = (int)(Math.random() * 5);
				//} while (ans == this.ID);
				//String answerText = SHAPE.getString(ans);
				//answerButtons[i].SetText(answerText);
				//answerButtons[i].SetIsCorrectAnswer(false);
			}
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
