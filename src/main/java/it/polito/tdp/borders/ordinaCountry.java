package it.polito.tdp.borders;

import java.util.Comparator;

import it.polito.tdp.borders.model.Country;

public class ordinaCountry implements Comparator<Country> {

	@Override
	public int compare(Country c1, Country c2) {
		return c1.getNome().compareTo(c2.getNome());
	}

}
