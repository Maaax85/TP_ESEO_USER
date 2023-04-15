package com.octest.dao;

import java.util.List;

public interface VilleDAO {
	public List<String[]> getAllData();

	public double getDistanceVille(String codeCommune1, String codeCommune2);

	public List<String[]> getData(String villeCode);

	public void putData(String nomVille, String codeCommunal, String codePostal, String ligne5, String latitude,
			String longitude);
	
	public void inhibData(String villeCode);
	
	public void deleteData(String villeCode);
	
	public String[] getMeteo(String latitude, String longitude);
}
