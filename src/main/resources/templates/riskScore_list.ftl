<div class="generic-container">

  <div class="panel panel-success" >
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="icd">Risk Score Calculator</span></div>
    <div class="panel-body">
      <div class="formcontainer">
        <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
        <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
        <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
          <input type="hidden" ng-model="ctrl.riskScore.id" />


          <div class="col-sm-12 ptInfo">

            <div class="row">
              <div class="col-sm-2 text-center">
                <label for="address1">Payment Year</label>
                <select class=" form-control" ng-model="ctrl.paymentYear" ng-options="paymentYear for paymentYear in ctrl.paymentYears"></select>
              </div>

              <div class="col-md-2 text-center">
                <label class="control-lable" for="uname">DOB </label>
                <div class="input-group date" id="dob" ng-model="ctrl.dob" date1-picker>
                  <input type="text" class="form-control netto-input" ng-model="ctrl.lead.dob" date-picker-input>
                  <span class="input-group-addon">
			           					<span class="glyphicon glyphicon-calendar"></span>
                  </span>
                </div>
              </div>


              <div class="col-md-2 text-center">
                <label class="control-lable" for="uname">Gender </label>
                <div id="gender" ng-model="ctrl.gender">
                  <input type="radio" ng-model="ctrl.gender"  value="Male">Male
                  <input type="radio" ng-model="ctrl.gender"  value="Female">Female
                </div>
              </div>

              <div class="col-md-3 text-center">
                <label class="control-lable" for="uname"> Demographic Risk Factors</label>
                <div>
                  Institutional
                  <input type="checkbox" ng-model="ctrl.institutional"   ng-true-value="'INST'" ng-false-value="''"> &nbsp; Medicaid
                  <input type="checkbox" ng-model="ctrl.medicaid"  ng-true-value="'Medicaid'" ng-false-value="''" >
                </div>

              </div>

            </div>


            &nbsp;
            <div class="row">
              <label class="control-lable"> Current Medicare Eligibility due to</label>
              <input type="radio" ng-model="ctrl.isAgedOrDisabled"  value="Aged">Aged &nbsp;
              <input type="radio" ng-model="ctrl.isAgedOrDisabled"  value="Disabled">Disabled
            </div>
            &nbsp;
            <div class="row">
              <label class="control-lable"> Original Medicare Eligibility due to Disability</label>
              <input type="radio" ng-model="ctrl.originallyDueToDis"  value="Originally Disabled">Yes &nbsp;
              <input type="radio" ng-model="ctrl.originallyDueToDis"  value="">No
            </div>

          </div>
<div> {{ ctrl.records.length }} </div>
 
          <div class="table-responsive">
            <div class="panel-body">

              <table class="table ">
                <thead>
                  <tr>
                    <th> </th>
                    <th> </th>
                    <th>Model</th>
                    <th>HCC</th>
                    <th>Description</th>
                    <th>Override</th>
                    <th>Score</th>
                    <th>Action</th>
                  </tr>
                </thead>
                <tbody>
                  <tr ng-repeat="record in ctrl.records track by $index">
                    <td>
                      DX{{($index+1)}}      
                    </td>
                    <td>
                      <input type="text" name="icdcode" class="form-control" ng-model="ctrl.records[$index][1]" />
                    </td>
                    <td>
                      <input type="text" name="model" readonly class="form-control" ng-model="ctrl.records[$index][0]" />
                    </td>
                    <td>
                      <input type="text" name="hcc" readonly class="form-control" ng-model="ctrl.records[$index][2]" />
                    </td>
                    <td>
                      <input type="text" name="description" readonly class="form-control" ng-model="ctrl.records[$index][3]" />
                    </td>
                    <td>
                      <input type="text" name="override" readonly class="form-control" ng-model="ctrl.records[$index][4]" />
                    </td>
                    <td>
                      <input type="text" name="score" readonly class="form-control" ng-model="ctrl.records[$index][5]" />
                    </td>
                    <td class="text-center">
                      <i class="fa fa-times-circle fa-lg text-danger" ng-if="!$first" ng-click="ctrl.remove($index)" title="Delete" aria-hidden="true"></i>

                      <i class="fa fa-plus-circle fa-lg text-success" ng-if="$last" ng-click="ctrl.add()" title="Add" aria-hidden="true"></i>
                    </td>
               
                  </tr>
                </tbody>
              </table>
            </div>
          </div>


          <div class="row">
            <div class="form-actions floatRight">
              <input type="submit" value="Submit" class="btn btn-primary btn-xs" ng-disabled="myForm.$invalid || myForm.$pristine">
              <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-xs">Cancel</button>
              <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-xs" ng-if="ctrl.riskScore.id" ng-disabled="myForm.$pristine">Reset Form</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>


</div>