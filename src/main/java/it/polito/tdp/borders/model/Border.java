package it.polito.tdp.borders.model;

public class Border {

	private int country1;
	private int country2;
	
	public Border(int country1, int country2) {
		this.country1 = country1;
		this.country2 = country2;
	}

	public int getCountry1() {
		return country1;
	}

	public void setCountry1(int country1) {
		this.country1 = country1;
	}

	public int getCountry2() {
		return country2;
	}

	public void setCountry2(int country2) {
		this.country2 = country2;
	}

	
}
