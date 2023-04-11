package com.octest.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VilleDAOImpl implements VilleDAO {

	DaoFactory daoFactory;

	public VilleDAOImpl(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public ArrayList<String[]> getAllData() {
		String urlString = "http://localhost:8181/getVille/?codePostal";
		ArrayList<String[]> villeDataList = new ArrayList<String[]>();
		try {
			URL url = new URL(urlString);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Accept", "application/json");
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
				String nomCommune = jsonObject.getString("nomCommune");
				String codeCommune = jsonObject.getString("codeCommune");
				String codePostal = jsonObject.getString("codePostal");
				String ligne = jsonObject.getString("ligne");
				String latitude = jsonObject.getString("latitude");
				String longitude = jsonObject.getString("longitude");
				String flag = "" + jsonObject.getInt("flag");
				String[] villeData = { nomCommune, codeCommune, codePostal, ligne, latitude, longitude, flag };
				villeDataList.add(villeData);
			}
		} catch (IOException | JSONException e) {
			e.printStackTrace();
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
			connection.setRequestProperty("Accept", "application/json");
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
			villeData[0] = jsonObject.getString("nomCommune");
			villeData[1] = jsonObject.getString("codeCommune");
			villeData[2] = jsonObject.getString("codePostal");
			villeData[3] = jsonObject.getString("ligne");
			villeData[4] = jsonObject.getString("latitude");
			villeData[5] = jsonObject.getString("longitude");
			villeData[6] = "" + jsonObject.getInt("flag");
		} catch (IOException | JSONException e) {
			e.printStackTrace();
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
		ArrayList<String[]> villes = this.getAllData();
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
		villeJson.put("nomCommune", nomVille);
		villeJson.put("codeCommune", codeCommunal);
		villeJson.put("codePostal", codePostal);
		villeJson.put("ligne", ligne5);
		villeJson.put("latitude", latitude);
		villeJson.put("longitude", longitude);

		String jsonStr = villeJson.toString();

		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			os.write(jsonStr.getBytes());
			os.flush();
			os.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void inhibData(String villeCode) {
		String urlString = "http://localhost:8181/flagVille";

		JSONObject villeJson = new JSONObject();

		String[] ville = this.getData(villeCode);
		villeJson.put("nomCommune", ville[0]);
		villeJson.put("codeCommune", villeCode);

		String jsonStr = villeJson.toString();

		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("PUT");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			os.write(jsonStr.getBytes());
			os.flush();
			os.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void deleteData(String villeCode) {
		String urlString = "http://localhost:8181/deleteVille";

		JSONObject villeJson = new JSONObject();

		String[] ville = this.getData(villeCode);
		villeJson.put("nomCommune", ville[0]);
		villeJson.put("codeCommune", villeCode);

		String jsonStr = villeJson.toString();

		URL url;
		try {
			url = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("DELETE");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setDoOutput(true);

			OutputStream os = conn.getOutputStream();
			os.write(jsonStr.getBytes());
			os.flush();
			os.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			System.out.println(response.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String[] getMeteo(String latitude, String longitude) {
	    String url = "https://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude+"&APPID="+"07b9484736ecf815feecca1f4972f458&lang=fr";
	    String[] meteoRet = new String[6];
	    try {
	        URL urlObj = new URL(url);
	        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
	        connection.setRequestMethod("GET");
	        connection.setRequestProperty("Accept", "application/json");

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
	        e.printStackTrace();
	    }

	    return meteoRet;
	}


}
