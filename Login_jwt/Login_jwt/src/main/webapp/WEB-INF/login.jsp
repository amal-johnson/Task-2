<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css" integrity="sha384-9aIt2nRpC12Uk9gS9baDl411NQApFmC26EwAOH8WgZl5MYYxFfc+NcPb1dKGj7Sk" crossorigin="anonymous">
</head>
<body>
<br>
<div align="center">
	<form action="authenticate" method="post">
	<h2>Login page</h2>
	<div class="form-group">UserName: <input type="text" name="username" required></div>
	<div class="form-group">Password: &nbsp<input type="password" name="password" required></div>
	<h5 style="color:red">${error}</h5>
	<div class="form-group"><input type="submit" value="Login" class="btn btn-primary">
	<input type="button" value="Register" class="btn btn-outline-primary" onClick="goToRegister()"></div>
	</form>
</div>
<script type="text/javascript">
	function goToRegister(){
		alert("Going to register microservice")
		window.location.href="/register";
		}
	
</script>
</body>
</html>

