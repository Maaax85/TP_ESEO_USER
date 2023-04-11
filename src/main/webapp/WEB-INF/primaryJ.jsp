<%@ page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8" />
<title>Première page</title>
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

div a {
	background-color: #007bff;
	border-radius: 5px;
	color: #fff;
	display: inline-block;
	font-size: 14px;
	margin-right: 10px;
	padding: 10px 20px;
	text-align: center;
	transition: background-color 0.2s ease-in-out;
}

div a:hover {
	background-color: #0062cc;
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
	<c:if test="${ !empty erreur }">
		<p style="color: red;">
			<c:out value="${ erreur }" />
		</p>
	</c:if>

	<h1>
		<a href="Secondary">Seconde page</a>
	</h1>

	<table>
		<thead>
			<tr>
				<th>Nom de la commune</th>
				<th>Code commune</th>
				<th>Code Postal</th>
				<th>Ligne 5</th>
				<th>Latitude</th>
				<th>Longitude</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${dataPage}" var="ville">
				<tr>
					<td><a
						href="${pageContext.request.contextPath}/Ville?ville=${ville[1]}"
						<c:if test="${ville[6] == 0}">
        style="text-decoration: line-through;"
    </c:if>>
							<c:out value="${ville[0]}" />
					</a></td>
					<td><c:out value="${ville[1]}" /></td>
					<td><c:out value="${ville[2]}" /></td>
					<td><c:out value="${ville[3]}" /></td>
					<td><c:out value="${ville[4]}" /></td>
					<td><c:out value="${ville[5]}" /></td>
				</tr>
			</c:forEach>
		</tbody>
	</table>

	<div>
		<c:if test="${page > 1}">
			<a href="${pageContext.request.contextPath}/Primary?page=${page - 1}">Précédent</a>
		</c:if>
		<c:if test="${page < totalPages}">
			<a href="${pageContext.request.contextPath}/Primary?page=${page + 1}">Suivant</a>
		</c:if>
	</div>
</body>

</html>