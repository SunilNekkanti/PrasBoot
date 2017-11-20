<!DOCTYPE html>
<html ng-app="my-app">

<head>
  <link href="css/app.css" rel="stylesheet" />
  <link href="css/bootstrap.css" rel="stylesheet" />
  <link href="css/ui-navbar.css" rel="stylesheet" />
   <link href="css/bootstrap-multiselect.css" rel="stylesheet" />
  
<link rel="stylesheet" href="css/bootstrap.min.css">
<link rel="stylesheet" href="css/jquery.dataTables.min.css">
<link rel="stylesheet" href="css/dataTables.bootstrap.min.css">
<link rel="stylesheet" href="css/bootstrap-datepicker.min.css">
   
<script src="js/lib/jquery-1.10.1.min.js"></script>
<script src="js/lib/jquery.dataTables.min.js"></script>
<script src="js/lib/angular.min.js"></script>  
<script src="js/lib/angular-resource.min.js"></script> 
<script src="js/lib/angular-datatables.bootstrap.js"></script>
<script src="js/lib/moment-with-locales.js"></script>
<script src="js/lib/bootstrap.min.js"></script>
<script src="js/lib/bootstrap-datepicker.min.js"></script>
<script src="js/lib/bootstrap-datetimepicker.min.js"></script>
<script src="js/lib/dataTables.bootstrap.min.js"></script>
 <script src="js/lib/angular-animate.js"></script>
<script src="js/lib/angular-sanitize.min.js"></script>
<script src="js/lib/ui-bootstrap-tpls-2.5.0.min.js"></script>
<script src="js/lib/angular-datatables.min.js"></script>
<script src="js/lib/ngStorage.min.js"></script>
<script src="js/lib/angular-ui-router.min.js"></script>
<script src="js/lib/ui-bootstrap-tpls-0.12.0.min.js"></script>
<script src="js/lib/angular-bootstrap-multiselect.min.js"></script>

 
  <script src="js/app/app.js"></script>
  <script src="js/app/AttPhysicianService.js"></script>
  <script src="js/app/AttPhysicianController.js"></script>
  <script src="js/app/FileService.js"></script>
  <script src="js/app/FileController.js"></script>
  <script src="js/app/FileUploadController.js"></script>
  <script src="js/app/FileUploadService.js"></script>
  <script src="js/app/UserService.js"></script>
  <script src="js/app/UserController.js"></script>
  <script src="js/app/GenderService.js"></script>
  <script src="js/app/StateService.js"></script>
  <script src="js/app/MembershipActivityMonthController.js"></script>
  <script src="js/app/MembershipClaimsController.js"></script>
  <script src="js/app/MembershipClaimsService.js"></script>
  <script src="js/app/MembershipProblemController.js"></script>
  <script src="js/app/MembershipStatusService.js"></script>
  <script src="js/app/LanguageService.js"></script>
  <script src="js/app/ProblemService.js"></script>
  <script src="js/app/CPTMeasureController.js"></script>
  <script src="js/app/CPTMeasureService.js"></script>
  <script src="js/app/ICDMeasureController.js"></script>
  <script src="js/app/ICDMeasureService.js"></script>
  <script src="js/app/HedisMeasureController.js"></script>
  <script src="js/app/HedisMeasureService.js"></script>
  <script src="js/app/HedisMeasureGroupController.js"></script>
  <script src="js/app/HedisMeasureGroupService.js"></script>
  <script src="js/app/HedisMeasureRuleController.js"></script>
  <script src="js/app/HedisMeasureRuleService.js"></script>
  <script src="js/app/HedisReportModalInstanceController.js"></script>
  <script src="js/app/HospitalController.js"></script>
  <script src="js/app/HospitalService.js"></script>
  <script src="js/app/InsuranceService.js"></script>
  <script src="js/app/MedicalLossRatioController.js"></script>
  <script src="js/app/MedicalLossRatioService.js"></script>
  <script src="js/app/MembershipHedisController.js"></script>
  <script src="js/app/MembershipHedisService.js"></script>
  <script src="js/app/MembershipHedisMeasureService.js"></script>
  <script src="js/app/PlanTypeController.js"></script>
  <script src="js/app/PlanTypeService.js"></script>
  <script src="js/app/PlaceOfServiceController.js"></script>
  <script src="js/app/PlaceOfServiceService.js"></script>
  <script src="js/app/ProviderService.js"></script>
  <script src="js/app/RiskReconService.js"></script>
  <script src="js/app/LeadService.js"></script>
  <script src="js/app/LeadController.js"></script>
  <script src="js/app/ModalInstanceController.js"></script>
  <script src="js/app/RoleService.js"></script>
  <script src="js/app/RoleController.js"></script>
  <script src="js/app/FacilityTypeController.js"></script>
  <script src="js/app/FacilityTypeService.js"></script>
  <script src="js/app/EventController.js"></script>
  <script src="js/app/EventService.js"></script>
  <script src="js/app/FileUploadService.js"></script>
  <script src="js/app/FileTypeController.js"></script>
  <script src="js/app/FileTypeService.js"></script>
  <script src="js/app/FrequencyTypeController.js"></script>
  <script src="js/app/FrequencyTypeService.js"></script>
  <script src="js/app/LanguageController.js"></script>
  <script src="js/app/LanguageService.js"></script>
  <script src="js/app/ProblemController.js"></script>
  <script src="js/app/CountyService.js"></script>
  <script src="js/app/CountyController.js"></script>
   <script src="js/app/MembershipService.js"></script>
  <script src="js/app/MembershipController.js"></script>
  <script src="js/app/MembershipFollowupService.js"></script>
  <script src="js/app/ProviderService.js"></script>
  <script src="js/app/ProviderController.js"></script>
  <script src="js/app/MembershipStatusController.js"></script>
  <script src="js/app/InsuranceController.js"></script>
  <script src="js/app/EventFrequencyController.js"></script>
  <script src="js/app/EventFrequencyService.js"></script>
  <script src="js/app/EventMonthController.js"></script>
  <script src="js/app/EventMonthService.js"></script>
  <script src="js/app/EventWeekDayController.js"></script>
  <script src="js/app/EventWeekDayService.js"></script>
  <script src="js/app/EventWeekNumberService.js"></script>
  <script src="js/app/EventAssignmentService.js"></script>
  <script src="js/app/EventAssignmentController.js"></script>
  <script src="js/app/datetimepicker.js"></script>
  <script src="js/app/datetimepicker.templates.js"></script>
   
   <style>
     .dataTable > thead > tr > th[class*="sort"]::after{display: none}
   </style>
   
</head>

<body class="ng-cloak" ng-controller="NavbarController">
  <nav class="navbar   navbar-inverse"  ng-if="displayNavbar" role="navigation">
    <div class="container-fluid" >
    
      <div class="navbar-collapse collapse in " collapse="isCollapsed" aria-expanded="true" >
        <ul class="nav navbar-nav">
        <li><a   ng-click="callMe('insurance','')">Insurances</a> </li>
        <li><a   ng-click="callMe('provider','')">Providers</a> </li>
        <li><a   ng-click="callMe('membership','')">Memberships</a> </li>
              				
          <li class="dropdown" dropdown ng-if="(loginUser.roleName === 'ADMIN' || loginUser.roleName === 'MANAGER')">
            <a href="#" class="dropdown-toggle" dropdown-toggle role="button" aria-expanded="false">Admin <span class="caret"></span></a>
            <ul class="dropdown-menu" role="menu">
              <li class="dropdown-submenu" dropdown>
            		<a href="#" class="dropdown-toggle" dropdown-toggle role="button" aria-expanded="false">Quality Measures</a>
            			<ul class="dropdown-menu" role="submenu">
              				<li><a  ng-click="callMe('cpt','')">CPT</a></li>
            				<li><a   ng-click="callMe('hedis','')">Hedis</a></li>
              				<li><a   ng-click="callMe('icd','')">ICD</a> </li>
              				<li class="divider"></li>
              				<li><a   ng-click="callMe('problem','')">Problem List</a> </li>
              				<li class="divider"></li>
              				<li><a   ng-click="callMe('hedisMeasureRule','')">Hedis Rules</a> </li>
           				 </ul>
          		</li>
          		
              <li><a ng-click="callMe('user','')">User Accounts</a> </li>
              <li class="divider"></li>
              <li class="dropdown-submenu" dropdown>
            		<a href="#" class="dropdown-toggle" dropdown-toggle role="button" aria-expanded="false">Lookups</a>
            			<ul class="dropdown-menu" role="submenu">
              				<li><a  ng-click="callMe('hedisGroup','')">Hedis Groups</a></li>
            				<li><a   ng-click="callMe('planType','')">Plan Types</a></li>
              				<li><a   ng-click="callMe('fileType','')">File Types</a> </li>
              				<li><a   ng-click="callMe('roomType','')">Room Types</a></li>
            				<li><a   ng-click="callMe('frequencyType','')">Frequency Type</a></li>
              				<li><a  ng-click="callMe('hospital','')">Hospitals</a></li>
            				<li><a   ng-click="callMe('attPhysician','')">ATT Physician</a></li>
           				 </ul>
          		</li>
          	<li><a ng-click="callMe('fileUpload','')">File Uploads</a> </li>	
          	
            </ul>
          </li>
           <li class="dropdown" dropdown ng-if="(loginUser.roleName === 'ADMIN' || loginUser.roleName === 'MANAGER')">
            <a href="#" class="dropdown-toggle" dropdown-toggle role="button" aria-expanded="false">Reports <span class="caret"></span></a>
            <ul class="dropdown-menu" role="menu">
							<li><a  ng-click="callMe('membershipHedis','')">Hedis</a></li>
							<li><a  ng-click="callMe('membershipHospitalization','')">Hospitalization</a></li>
							<li><a  ng-click="callMe('membershipClaims','')">Claims</a></li>
							<li><a  ng-click="callMe('membershipProblem','')">Problems</a></li>
							<li><a  ng-click="callMe('membershipActivityMonth','')">Activity	Month</a></li>
							<li><a  ng-click="callMe('medicalLossRatio','')">Medical Loss Ratio(MLR) </a></li>
             </ul>
             </li> 
        </ul>
        <ul class="nav navbar-nav navbar-right">
        	<li> <a href="#"> <span class="glyphicon glyphicon-user"></span> ${username}</a></li>
        	<li> <a ng-click="callMe('logout')"><span class="glyphicon glyphicon-log-out"></span> Logout</a></li>
        </ul>
      </div>
    </div>
  </nav>


    <div ui-view></div>
</body>

</html>
