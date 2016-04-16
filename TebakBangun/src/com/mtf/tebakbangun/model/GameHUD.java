package com.mtf.tebakbangun.model;

import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.camera.hud.HUD;
import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.primitive.Rectangle;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.util.HorizontalAlign;

import com.mtf.tebakbangun.PlayGameActivity;

public class GameHUD extends HUD {
	// ===========================================================
	// Constants
	// ===========================================================
	private static final float FRAME_LINE_WIDTH = 5f;
	// ===========================================================
	// Fields
	// ===========================================================
	private final Line[] mFrameLines = new Line[4];
	private final Rectangle mBackgroundRectangle;
	private final Rectangle mProgressRectangle;

	private final float mPixelsPerPercentRatio;

	private ChangeableText mScoreTextValue, mLevelTextValue;
	//public static ChangeableText mGoldTextValue;
	private Text mScoreText, mLevelText;
	
	// ===========================================================
	// Constructors
	// ===========================================================
	public GameHUD(final Camera pCamera, final float pX, final float pY, final float pWidth, final float pHeight) {
		super();
		super.setCamera(pCamera);

		this.mBackgroundRectangle = new Rectangle(pX, pY, pWidth, pHeight);

		/*this.mFrameLines[0] = new Line(pX, pY, pX + pWidth, pY, FRAME_LINE_WIDTH); // Top line.
		this.mFrameLines[1] = new Line(pX + pWidth, pY, pX + pWidth, pY + pHeight, FRAME_LINE_WIDTH); // Right line.
		this.mFrameLines[2] = new Line(pX + pWidth, pY + pHeight, pX, pY+ pHeight, FRAME_LINE_WIDTH); // Bottom line.
		this.mFrameLines[3] = new Line(pX, pY + pHeight, pX, pY,FRAME_LINE_WIDTH); // Left line.*/
		
		this.mProgressRectangle = new Rectangle(pX, pY, pWidth, pHeight);
		/*
		super.attachChild(this.mBackgroundRectangle); // This one is drawn first.
		super.attachChild(this.mProgressRectangle); // The progress is drawn afterwards.
		for (int i = 0; i < this.mFrameLines.length; i++)
			super.attachChild(this.mFrameLines[i]); // Lines are drawn last, so
													// they'll override
													// everything.*/
		this.mPixelsPerPercentRatio = pWidth / 100;
		
		mScoreText = new Text(400, 5, PlayGameActivity.getInstance().getFont(), "Score:", HorizontalAlign.LEFT);
		mScoreTextValue = new ChangeableText(400 + mScoreText.getWidth() + 5, 5, PlayGameActivity.getInstance().getFont(),
				"0", HorizontalAlign.LEFT, 10);
		mLevelText = new Text(0,5, PlayGameActivity.getInstance().getFont(), "Level:", HorizontalAlign.LEFT);
		mLevelTextValue = new ChangeableText(mLevelText.getWidth() + 5, 5, PlayGameActivity.getInstance().getFont(), 
				"1", HorizontalAlign.LEFT, 1);
		
		this.attachChild(mScoreText);
		this.attachChild(mScoreTextValue);
		this.attachChild(mLevelText);
		this.attachChild(mLevelTextValue);
	}

	// ===========================================================
	// Getter & Setter
	// ===========================================================
	public void SetScore(String score) {
		mScoreTextValue.setText(score);
	}
	
	public void SetLevel(String level) {
		mLevelTextValue.setText(level);
	}
	
	public void setBackColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mBackgroundRectangle.setColor(pRed, pGreen, pBlue, pAlpha);
	}

	public void setFrameColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		for (int i = 0; i < this.mFrameLines.length; i++)
			this.mFrameLines[i].setColor(pRed, pGreen, pBlue, pAlpha);
	}

	public void setProgressColor(final float pRed, final float pGreen, final float pBlue, final float pAlpha) {
		this.mProgressRectangle.setColor(pRed, pGreen, pBlue, pAlpha);
	}

	/**
	 * Set the current progress of this progress bar.
	 * 
	 * @param pProgress is <b> BETWEEN </b> 0 - 100.
	 */
	public void setProgress(final float pProgress) {
		if (pProgress < 0)
			this.mProgressRectangle.setWidth(0); // This is an internal check
													// for my specific game, you
													// can remove it.
		this.mProgressRectangle.setWidth(this.mPixelsPerPercentRatio * pProgress);
	}
}
