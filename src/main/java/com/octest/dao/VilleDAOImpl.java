package com.octest.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VilleDAOImpl implements VilleDAO {
	
	public static final String APP_JSON_PARAMETER = "application/json";
	public static final String ACCEPT_PARAMETER = "Accept";
	
	public static final String NOM_COMMUNE_PARAMETER = "nomCommune";
	public static final String CODE_COMMUNE_PARAMETER = "codeCommune";
	public static final String CODE_POSTAL_PARAMETER = "codePostal";
	public static final String LIGNE_PARAMETER = "ligne";
	public static final String LATITUDE_PARAMETER = "latitude";
	public static final String LONGITUDE_PARAMETER = "longitude";
	public static final String FLAG_PARAMETER = "flag";
	public static final String CONTENT_PARAMETER = "Content-Type";
	
	private static final String ERROR_SQL_STRING = "An SQL exception occurred";
	private static final Logger LOGGER = LoggerFactory.getLogger(VilleDAOImpl.class);

	public List<String[]> getAllData() {
		String urlString = "http://localhost:8181/getVille/?codePostal";
		List<String[]> villeDataList = new ArrayList<>();
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty(ACCEPT_PARAMETER, APP_JSON_PARAMETER);
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
			reader.close();
			connection.disconnect();
			String json = stringBuilder.toString();
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				String nomCommune = jsonObject.getString(NOM_COMMUNE_PARAMETER);
				String codeCommune = jsonObject.getString(CODE_COMMUNE_PARAMETER);
				String codePostal = jsonObject.getString(CODE_POSTAL_PARAMETER);
				String ligne = jsonObject.getString(LIGNE_PARAMETER);
				String latitude = jsonObject.getString(LATITUDE_PARAMETER);
				String longitude = jsonObject.getString(LONGITUDE_PARAMETER);
				String flag = "" + jsonObject.getInt(FLAG_PARAMETER);
				String[] villeData = { nomCommune, codeCommune, codePostal, ligne, latitude, longitude, flag };
				villeDataList.add(villeData);
			}
		} catch (IOException | JSONException e) {
			LOGGER.error(ERROR_SQL_STRING, e);
		}
		return villeDataList;
	}

	public String[] getData(String villeCode) {
		String urlString = "http://localhost:8181/getVille/?codeCommunal=" + villeCode;
		String[] villeData = new String[7];
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty(ACCEPT_PARAMETER, APP_JSON_PARAMETER);
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder stringBuilder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line);
			}
			reader.close();
			connection.disconnect();
			String json = stringBuilder.toString();
			JSONArray jsonArray = new JSONArray(json);
			JSONObject jsonObject = jsonArray.getJSONObject(0);
			villeData[0] = jsonObject.getString(NOM_COMMUNE_PARAMETER);
			villeData[1] = jsonObject.getString(CODE_COMMUNE_PARAMETER);
			villeData[2] = jsonObject.getString(CODE_POSTAL_PARAMETER);
			villeData[3] = jsonObject.getString(LIGNE_PARAMETER);
			villeData[4] = jsonObject.getString(LATITUDE_PARAMETER);
			villeData[5] = jsonObject.getString(LONGITUDE_PARAMETER);
			villeData[6] = "" + jsonObject.getInt(FLAG_PARAMETER);
		} catch (IOException | JSONException e) {
			LOGGER.error(ERROR_SQL_STRING, e);
		}
		return villeData;
	}

	public static final double R = 6371; // Rayon de la Terre en km

	private double calculDistance(double lat1, double lon1, double lat2, double lon2) {
		double dLat = Math.toRadians(lat2 - lat1);
		double dLon = Math.toRadians(lon2 - lon1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2) * Math.sin(dLon / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c;

		distance = Double.parseDouble(String.format("%.2f", distance).replace(",", "."));

		return distance;
	}

	public double getDistanceVille(String codeCommune1, String codeCommune2) {
		List<String[]> villes = this.getAllData();
		String[] ville1 = new String[6];
		String[] ville2 = new String[6];

		for (int i = 0; i < villes.size(); i++) {
			if (villes.get(i)[1].equals(codeCommune1)) {
				ville1 = villes.get(i);
			}
			if (villes.get(i)[1].equals(codeCommune2)) {
				ville2 = villes.get(i);
			}
		}

		double lat1 = Double.parseDouble(ville1[4]);
		double lon1 = Double.parseDouble(ville1[5]);
		double lat2 = Double.parseDouble(ville2[4]);
		double lon2 = Double.parseDouble(ville2[5]);

		return this.calculDistance(lat1, lon1, lat2, lon2);
	}

	public void putData(String nomVille, String codeCommunal, String codePostal, String ligne5, String latitude,
			String longitude) {

		String urlString = "http://localhost:8181/editVille";

		JSONObject villeJson = new JSONObject();
		villeJson.put(NOM_COMMUNE_PARAMETER, nomVille);
		villeJson.put(CODE_COMMUNE_PARAMETER, codeCommunal);
		villeJson.put(CODE_POSTAL_PARAMETER, codePostal);
		villeJson.put(LIGNE_PARAMETER, ligne5);
		villeJson.put(LATITUDE_PARAMETER, latitude);
		villeJson.put(LONGITUDE_PARAMETER, longitude);

		String jsonStr = villeJson.toString();

		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty(CONTENT_PARAMETER, APP_JSON_PARAMETER);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			os.write(jsonStr.getBytes());
			os.flush();
			os.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
		} catch (IOException e) {
			LOGGER.error(ERROR_SQL_STRING, e);
		}
	}

	public void inhibData(String villeCode) {
		String urlString = "http://localhost:8181/flagVille";

		JSONObject villeJson = new JSONObject();

		String[] ville = this.getData(villeCode);
		villeJson.put(NOM_COMMUNE_PARAMETER, ville[0]);
		villeJson.put(CODE_COMMUNE_PARAMETER, villeCode);

		String jsonStr = villeJson.toString();

		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty(CONTENT_PARAMETER, APP_JSON_PARAMETER);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			os.write(jsonStr.getBytes());
			os.flush();
			os.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			LOGGER.error(ERROR_SQL_STRING, e);
		}
	}

	public void deleteData(String villeCode) {
		String urlString = "http://localhost:8181/deleteVille";

		JSONObject villeJson = new JSONObject();

		String[] ville = this.getData(villeCode);
		villeJson.put(NOM_COMMUNE_PARAMETER, ville[0]);
		villeJson.put(CODE_COMMUNE_PARAMETER, villeCode);

		String jsonStr = villeJson.toString();

		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty(CONTENT_PARAMETER, APP_JSON_PARAMETER);
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			os.write(jsonStr.getBytes());
			os.flush();
			os.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuilder response = new StringBuilder();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
		} catch (IOException e) {
			LOGGER.error(ERROR_SQL_STRING, e);
		}
	}

	public String[] getMeteo(String latitude, String longitude) {
	    String url = "https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&APPID="+"07b9484736ecf815feecca1f4972f458&lang=fr";
	    String[] meteoRet = new String[6];
	    try {
	        URL urlObj = new URL(url);
	        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty(ACCEPT_PARAMETER, APP_JSON_PARAMETER);

	        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
	        String inputLine;
	        StringBuilder response = new StringBuilder();
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        in.close();

	        JSONObject json = new JSONObject(response.toString());

	        Double vitesseVentDouble = (json.getJSONObject("wind").getDouble("speed")*3.6);
	        String vitesseVent = ""+Double.parseDouble(String.format("%.2f", vitesseVentDouble).replace(",", "."));
	        Double temperatureDouble = (json.getJSONObject("main").getDouble("temp")-273.15);
	        String temperature = ""+Double.parseDouble(String.format("%.2f", temperatureDouble).replace(",", "."));
	        String humidite = ""+json.getJSONObject("main").getDouble("humidity");
	        String mainWeather = json.getJSONArray("weather").getJSONObject(0).getString("description");
	        mainWeather = mainWeather.substring(0, 1).toUpperCase() + mainWeather.substring(1);
	        String clouds = ""+json.getJSONObject("clouds").getDouble("all");
	        String icon = json.getJSONArray("weather").getJSONObject(0).getString("icon");

	        String[] meteo = { vitesseVent, temperature, humidite, mainWeather, clouds, icon };
	        meteoRet = meteo;

	    } catch (IOException | JSONException e) {
	    	LOGGER.error(ERROR_SQL_STRING, e);
	    }

	    return meteoRet;
	}


}
