package com.mtf.tebakbangun.model;

import java.util.Random;

import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TiledTextureRegion;

import com.mtf.tebakbangun.PlayGameActivity;
import com.qwerjk.andengine.opengl.texture.region.PixelPerfectTiledTextureRegion;

public class Questions extends Sprite {
	
	private int ID;
	private int forLevel;
	AnswerButton answerButtons[];
	private TextureRegion mAnswerTextureRegion;
	private TiledTextureRegion mShapeTextureRegion;
	private Font m_Font;
	
	BasicShapeAnimated image;
	
	public enum SHAPE
	{
		CUBE(0),BLOCK(1),TUBE(2),CONE(3),PYRAMID(4),BALL(5), //Level 1
		BLOCK_BALL(6), CONE_CUBE(7), PYRAMID_TUBE(8), //Level 2
		TUBEBLOCK(9), PYRACUBE(10), CONEBALL(11), //Level 3
		COUNT(12);
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
	    		case 5:
	    			return "BOLA";
	    		case 6:
	    			return "BALOK & BOLA";
	    		case 7:
	    			return "KERUCUT & KUBUS";
	    		case 8:
	    			return "PIRAMIDA & TABUNG";
	    		case 9:
	    			return "TABUNG & BALOK";
	    		case 10:
	    			return "PIRAMIDA & KUBUS";
	    		case 11:
	    			return "KERUCUT & BOLA";
	    		default:
	    			return "TIDAK DIKENAL";
	    	}
	    }
	};
	
	public Questions(int id, int level, TextureRegion backgroundTexture, TextureRegion answerTexture, PixelPerfectTiledTextureRegion shapeTexture)
	{
		super(0, 0, PlayGameActivity.getInstance().CAMERA_WIDTH, PlayGameActivity.getInstance().CAMERA_HEIGHT, backgroundTexture);
		this.ID = id;
		this.forLevel = level;
		mAnswerTextureRegion = answerTexture;
		mShapeTextureRegion = shapeTexture;
		m_Font = PlayGameActivity.getInstance().getFont();
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
		int usedAnswer = -1;
		for(int i=0;i<3;i++)
		{
			pX += AnswerButton.PADDING;
			if(i == correctAnswer)
			{
				String answerText = SHAPE.getString(this.ID);
				answerButtons[i] = new AnswerButton(mAnswerTextureRegion, m_Font, answerText);
				answerButtons[i].SetIsCorrectAnswer(true);
			}
			else
			{
				int ans = -1;
				boolean doAgain = true;
				do
				{
					if(forLevel == 2 || forLevel == 3)
						ans = rn.nextInt((8 - 6) + 1) + 6;
					else if (forLevel == 4 || forLevel == 5)
						ans = rn.nextInt((11 - 9) + 1) + 9;
					else
						ans = rn.nextInt(6);
					doAgain = (ans == this.ID || ans == usedAnswer);
				} while (doAgain);
				usedAnswer = ans;	
				String answerText = SHAPE.getString(ans);
				answerButtons[i] = new AnswerButton(mAnswerTextureRegion, m_Font, answerText);
				answerButtons[i].SetIsCorrectAnswer(false);
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
		if (forLevel == 2 || forLevel == 3)
		{
			ANIM_WIDTH += 200;
			ANIM_HEIGHT += 100;
		}
		else if (forLevel == 4 || forLevel == 5)
		{
			ANIM_WIDTH += 200;
			ANIM_HEIGHT += 200;
		}
		final int centerX = PlayGameActivity.getInstance().CAMERA_WIDTH / 2;
		final int centerY = PlayGameActivity.getInstance().CAMERA_HEIGHT / 2;
		image = new BasicShapeAnimated(centerX-(ANIM_WIDTH/2), centerY-(ANIM_HEIGHT/2),
				ANIM_WIDTH, ANIM_HEIGHT, mShapeTextureRegion);
		this.attachChild(image);
		image.animate(100);
	}
}
