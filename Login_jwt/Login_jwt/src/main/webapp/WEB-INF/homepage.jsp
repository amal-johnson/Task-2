<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>


<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>Home page</title>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
</head>
<body>
<div class="jumbotron jumbotron-fluid" align=center>
<h2 class="display-4">Welcome to home page, Logged in as ${UserName}</h2>
<p style="color:brown" class="lead" >A token has been generated for you 😊</p>
<p style="color:green" class="lead" >${token}</p>
<p><a href="/test">Test</a></p>
<p><a href="/sample">Sample</a></p>
<p><a href="/signout">Logout</a>
</div>
</body>
</html>
