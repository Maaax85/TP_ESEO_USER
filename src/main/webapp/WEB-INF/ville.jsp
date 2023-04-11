<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Ville page</title>
<style>
body {
  font-family: Arial, sans-serif;
  background-color: #f5f5f5;
  margin: 0;
  padding: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: flex-start;
  height: 100vh;
}

h1 {
  font-size: 32px;
  margin: 3px 0;
  text-align: center;
}

form {
  margin: 3px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

input[type="text"] {
  padding: 10px;
  font-size: 16px;
  border-radius: 5px;
  border: 1px solid #ccc;
  margin-bottom: 3px;
  width: 300px;
}

input[type="submit"] {
  background-color: #007bff;
  border-radius: 5px;
  color: #fff;
  display: inline-block;
  font-size: 14px;
  padding: 10px 20px;
  text-align: center;
  transition: background-color 0.2s ease-in-out;
}

input[type="submit"]:hover {
  background-color: #0062cc;
}

a {
  color: #555;
  text-decoration: none;
}

a:hover {
  text-decoration: underline;
}

p {
  color: #007bff;
  font-weight: bold;
  margin: 3px 0;
  text-align: center;
}

.image-cadre {
  border: 2px solid black;
  padding: 10px;
  background-color: #258eff;
  margin-top: 3px;
}




</style>
</head>
<body>

	<c:out value="${ nomVille }" />
	<c:out value="${ codePostal }" />
	<c:out value="${ ligne5 }" />
	<c:out value="${ latitude }" />
	<c:out value="${ longitude }" />

	<h1>
		Code communal :
		<c:out value="${ville[1]}" />
	</h1>

	<div>

		<form method="post" action="Ville">
	
			<p>
				<b>Nom :</b> <input type="text" name="nomVille" value="${ville[0]}"
					placeholder="Nouveau nom">
			</p>
			<p>
				<b>Code Postal:</b> <input type="text" name="codePostal"
					value="${ville[2]}" placeholder="Nouveau code postal">
			</p>
			<p>
				<b>Ligne 5:</b> <input type="text" name="ligne5" value="${ville[3]}"
					placeholder="Nouvelle ligne 5">
			</p>
			<p>
				<b>Latitude:</b> <input type="text" name="latitude"
					value="${ville[4]}" placeholder="Nouvelle latitude">
			</p>
			<p>
				<b>Longitude:</b> <input type="text" name="longitude"
					value="${ville[5]}" placeholder="Nouvelle longitude">
			</p>
	
			<input type="hidden" name="action" value="boutonModifier" /> <input
				type="submit" value="Enregistrer les modifications" />
	
		</form>

		<form method="post" action="Ville">
			<input type="hidden" name="action" value="boutonInhiber" /> <input
				type="submit"
				value="${ ville[6] == 1 ? 'Inhiber' : 'Désinhiber' } la ville" />
		</form>
	
	
		<form method="post" action="Ville">
			<input type="hidden" name="action" value="boutonDelete" /> <input
				type="submit" value="Supprimer la ville" />
		</form>
	
	</div>

	<img src="https://openweathermap.org/img/wn/${meteo[5]}@2x.png" alt="Image météo" class="image-cadre"/>

	<div>
	
		<p>Météo : <c:out value="${meteo[3]}" /></p>
		<p>Couverture nuageuse : <c:out value="${meteo[4]}" /> %</p>
		<p>Vent : <c:out value="${meteo[0]}" /> km/h</p>
		<p>Température : <c:out value="${meteo[1]}" /> °C</p>
		<p>Humidité : <c:out value="${meteo[2]}" /> %</p>

	</div>
	
	<p>
		<a href="${pageContext.request.contextPath}/Primary">Retour à la
			liste des villes</a>
	</p>

</body>

</html>