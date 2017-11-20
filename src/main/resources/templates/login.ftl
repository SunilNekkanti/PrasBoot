<!DOCTYPE html>
<html >

<head>
  <link data-require="bootstrap-css@3.3.1" data-semver="3.3.1" rel="stylesheet" href="//maxcdn.bootstrapcdn.com/bootstrap/3.3.1/css/bootstrap.min.css" />
  
     
  <script src="js/app/LoginController.js"></script>
</head>



<body>
<div class="container">

 <nav class="navbar navbar-default myNavbar"  >
    <div class="container-fluid">
    
     </div>
  </nav>  
  
	<div  class="col-xs-12 col-sm-8 col-md-6 col-sm-offset-2 col-md-offset-3" >
		<form    action="loginform.do" method="post" name="myForm" class="form-horizontal">
				<h2>Please Sign In</h2>

				<hr class="colorgraph">
				<div class="form-group">
				 <input type="text"  ng-model="ctrl.login.username" name="username" class="username form-control input-sm" placeholder="Enter user name" required ng-minlength="5"/>
				</div>
				<div class="form-group">
					 <input type="password"  ng-model="ctrl.login.password"  name="password" class="password form-control input-sm" placeholder="Enter password" required ng-minlength="5"/>
				</div>
				<span class="button-checkbox">
					<button type="button" class="btn" data-color="info">Remember
						Me</button> <input type="checkbox" name="remember_me" id="remember_me"
					checked="checked" class="hidden"> <a href=""
					class="btn btn-link pull-right">Forgot Password?</a>
				</span>
				<hr class="colorgraph">
				<div class="row">
					<div class="col-xs-6 col-sm-6 col-md-6">
						<input type="submit" class="btn btn-lg btn-success btn-block"
							value="Sign In"  ng-disabled="myForm.$invalid || myForm.$pristine">
					</div>
					<div class="col-xs-6 col-sm-6 col-md-6">
						<a href="" class="btn btn-lg btn-primary btn-block">Register</a>
					</div>
				</div>
		</form>
	</div>
</div>
</body>

</html>
