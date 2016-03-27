package com.mtf.tebakbangun.model;

public class Cube extends BasicShape {

	private int sisi;
	
	int GetSisi()
	{
		return sisi;
	}
	
	void SetSisi(int s)
	{
		sisi = s;
	}
	
	@Override
	public double GetLuas() {
		return 6 * sisi * sisi;
	}

	@Override
	public double GetVolume() {
		return sisi * sisi * sisi;
	}

}
