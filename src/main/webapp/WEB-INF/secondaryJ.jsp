<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<title>Second Page</title>
<style>
body {
	font-family: Arial, sans-serif;
	background-color: #f5f5f5;
	margin: 0;
	padding: 0;
}

h1 {
	font-size: 32px;
	margin: 20px 0 10px 0;
}

select {
	padding: 10px 20px;
	font-size: 14px;
	border-radius: 5px;
	border: none;
	margin-right: 10px;
	transition: background-color 0.2s ease-in-out;
	background-color: #f2f2f2;
	color: #555;
}

input[type="submit"] {
	padding: 10px 20px;
	font-size: 14px;
	border-radius: 5px;
	border: none;
	margin-right: 10px;
	transition: background-color 0.2s ease-in-out;
	background-color: #007bff;
	color: #fff;
}

select:hover, input[type="submit"]:hover {
	background-color: #ddd;
	cursor: pointer;
}

table {
	border-collapse: collapse;
	margin: 20px 0;
	width: 100%;
}

th, td {
	border: 1px solid #ccc;
	padding: 10px;
	text-align: center;
}

th {
	background-color: #eee;
}

tbody tr:nth-child(odd) {
	background-color: #f9f9f9;
}

a {
	color: #555;
	text-decoration: none;
}

a:hover {
	text-decoration: underline;
}

div {
	margin-top: 20px;
	text-align: center;
}

p {
	color: red;
	font-weight: bold;
	margin: 20px 0;
	text-align: center;
}
</style>
</head>
<body>
	<h1>
		<a href="Primary">Première page</a>
	</h1>

	<div>
		<form method="post"
			action="${pageContext.request.contextPath}/Secondary">
			<label for="ville1">Ville 1 :</label> <select name="codeCommune1"
				id="ville1">
				<c:forEach var="data" items="${allData}" varStatus="status">
					<option value="${data[1]}"
						<c:if test="${data[1] == param.codeCommune1}">selected</c:if>>${data[1]}(${data[0]})</option>
				</c:forEach>
			</select> <br> <label for="ville2">Ville 2 :</label> <select
				name="codeCommune2" id="ville2">
				<c:forEach var="data" items="${allData}" varStatus="status">
					<option value="${data[1]}"
						<c:if test="${data[1] == param.codeCommune2}">selected</c:if>>${data[1]}(${data[0]})</option>
				</c:forEach>
			</select> <br> <input type="submit" value="Calculer la distance">
		</form>

		<c:if test="${not empty distance}">
			<p>La distance entre les deux villes est de ${distance} km.</p>
		</c:if>
	</div>

</body>


</html>