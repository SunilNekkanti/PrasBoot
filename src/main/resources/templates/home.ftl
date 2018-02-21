<!DOCTYPE html>
<html ng-app="my-app">

<head>
  <link href="css/app.css" rel="stylesheet" />
  <link href="css/bootstrap.css" rel="stylesheet" />
  <link href="css/ui-navbar.css" rel="stylesheet" />
  <link href="css/bootstrap-multiselect.css" rel="stylesheet" />

  <link rel="stylesheet" href="css/bootstrap.min.css" />
  <link rel="stylesheet" href="https://cdn.datatables.net/1.10.16/css/jquery.dataTables.min.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/angular-datatables/0.6.2/plugins/bootstrap/datatables.bootstrap.min.css" />
  <link rel="stylesheet" href="css/bootstrap-datepicker.min.css" />
  <link rel="stylesheet" href="https://cdn.datatables.net/fixedcolumns/3.2.4/css/fixedColumns.dataTables.min.css" />
  <link rel="stylesheet" href="//cdn.datatables.net/buttons/1.3.1/css/buttons.dataTables.min.css" />
  <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-select/0.20.0/select.min.css" />
  <link rel="stylesheet" href="http://cdnjs.cloudflare.com/ajax/libs/selectize.js/0.8.5/css/selectize.default.css">

  <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" />
  <script src="https://code.jquery.com/jquery-1.12.4.js"></script>
  <script src="https://cdn.datatables.net/1.10.16/js/jquery.dataTables.min.js"></script>
  <script src="js/lib/angular.min.js"></script>
  <script src="js/lib/angular-resource.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-datatables/0.6.2/plugins/bootstrap/angular-datatables.bootstrap.min.js"></script>
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
  <script src="js/lib/ocLazyLoad.js"></script>
  <script src="https://cdn.datatables.net/fixedcolumns/3.2.4/js/dataTables.fixedColumns.min.js"></script>
  <script src="https://cdn.datatables.net/buttons/1.2.2/js/dataTables.buttons.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/jszip/3.1.3/jszip.min.js"></script>
  <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.colVis.min.js"></script>
  <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.flash.min.js"></script>
  <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.html5.min.js"></script>
  <script src="https://cdn.datatables.net/buttons/1.2.2/js/buttons.print.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.32/pdfmake.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/pdfmake/0.1.32/vfs_fonts.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-ui-select/0.20.0/select.min.js"></script>

  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-datatables/0.6.2/plugins/buttons/angular-datatables.buttons.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-datatables/0.6.2/plugins/fixedcolumns/angular-datatables.fixedcolumns.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-datatables/0.6.2/plugins/fixedheader/angular-datatables.fixedheader.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/angular-datatables/0.6.2/plugins/tabletools/angular-datatables.tabletools.min.js"></script>


  <script src="js/app/app.js"></script>
  <script src="js/app/UserService.js"></script>
  <script src="js/app/datetimepicker.js"></script>
  <script src="js/app/datetimepicker.templates.js"></script>

  <style>
    .dataTable>thead>tr>th[class*="sort"]::after {
      display: none
    }
  </style>

</head>

<body class="ng-cloak" ng-controller="NavbarController">
  <nav class="navbar   navbar-inverse" ng-if="displayNavbar" role="navigation">
    <div class="container-fluid">

      <div class="navbar-collapse collapse in " collapse="isCollapsed" aria-expanded="true">
        <ul class="nav navbar-nav">
          <li><a ui-sref="main.insurance" ui-sref-active="active">Insurances</a> </li>
          <li><a ui-sref="main.provider" ui-sref-active="active">Providers</a> </li>
          <li><a ui-sref="main.membership" ui-sref-active="active">Memberships</a> </li>
          <li><a ui-sref="main.lead" ui-sref-active="active">Leads</a></li>
          <li class="dropdown" dropdown ng-if="(loginUser.roleName === 'ADMIN' || loginUser.roleName === 'MANAGER')">
            <a href="#" class="dropdown-toggle" dropdown-toggle role="button" aria-expanded="false">Admin <span class="caret"></span></a>
            <ul class="dropdown-menu" role="menu">
              <li class="dropdown-submenu" dropdown>
                <a href="#" class="dropdown-toggle" dropdown-toggle role="button" aria-expanded="false">Quality Measures</a>
                <ul class="dropdown-menu" role="submenu">
                  <li><a ui-sref="main.cpt" ui-sref-active="active">CPT</a></li>
                  <li><a ui-sref="main.hedis" ui-sref-active="active">Hedis</a></li>
                  <li><a ui-sref="main.icd" ui-sref-active="active">ICD</a> </li>
                  <li><a ui-sref="main.riskScore" ui-sref-active="active">Risk Score</a> </li>
                  <li class="divider"></li>
                  <li><a ui-sref="main.problem" ui-sref-active="active">Problem List</a> </li>
                  <li class="divider"></li>
                  <li><a ui-sref="main.hedisMeasureRule" ui-sref-active="active">Hedis Rules</a> </li>
                </ul>
              </li>

              <li><a ui-sref="main.user" ui-sref-active="active">User Accounts</a> </li>
              <li class="divider"></li>
              <li class="dropdown-submenu" dropdown>
                <a href="#" class="dropdown-toggle" dropdown-toggle role="button" aria-expanded="false">Lookups</a>
                <ul class="dropdown-menu" role="submenu">
                  <li><a ui-sref="main.hedisGroup" ui-sref-active="active">Hedis Groups</a></li>
                  <li><a ui-sref="main.planType" ui-sref-active="active">Plan Types</a></li>
                  <li><a ui-sref="main.fileType" ui-sref-active="active">File Types</a> </li>
                  <li><a ui-sref="main.roomType" ui-sref-active="active">Room Types</a></li>
                  <li><a ui-sref="main.frequencyType" ui-sref-active="active">Frequency Type</a></li>
                  <li><a ui-sref="main.hospital" ui-sref-active="active">Hospitals</a></li>
                  <li><a ui-sref="main.attPhysician" ui-sref-active="active">ATT Physician</a></li>
                  <li><a ui-sref="main.eventType" ui-sref-active="active">Event Types</a></li>
                  <li><a ui-sref="main.leadStatus" ui-sref-active="active">Lead Statuses</a></li>
                  <li><a ui-sref="main.leadStatusDetail" ui-sref-active="active">Lead Status Details</a></li>
                  <li><a ui-sref="main.role" ui-sref-active="active">Roles</a> </li>
              </ul>
          </li>
          <li><a ui-sref="main.fileUpload" ui-sref-active="active">File Uploads</a> </li>

          </ul>
          </li>
          <li class="dropdown" dropdown ng-if="(loginUser.roleName === 'ADMIN' || loginUser.roleName === 'MANAGER')">
            <a href="#" class="dropdown-toggle" dropdown-toggle role="button" aria-expanded="false">Reports <span class="caret"></span></a>
            <ul class="dropdown-menu" role="menu">
              <li><a ui-sref="main.membershipHedis" ui-sref-active="active">Hedis</a></li>
              <li><a ui-sref="main.membershipHospitalization" ui-sref-active="active">Hospitalization</a></li>
              <li><a ui-sref="main.membershipClaims" ui-sref-active="active">Claims</a></li>
              <li><a ui-sref="main.membershipProblem" ui-sref-active="active">Problems</a></li>
              <li><a ui-sref="main.membershipActivityMonth" ui-sref-active="active">Activity	Month</a></li>
              <li><a ui-sref="main.newMedicalLossRatio" ui-sref-active="active">New Medical Loss Ratio(MLR) </a></li>
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


  <ui-view> </ui-view>
</body>

</html>
