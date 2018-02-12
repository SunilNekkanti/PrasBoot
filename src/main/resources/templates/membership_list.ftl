<div class="generic-container">
  <div class="panel panel-success" ng-if="!ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="membership">List of Memberships </span>
      <button type="button" ng-click="ctrl.addMembership()" ng-hide="ctrl.displayEditButton" class="btn btn-success btn-xs custom-width  floatRight"> Add </button>
      <button type="button" ng-click="ctrl.editMembership(ctrl.membershipId)" ng-show="ctrl.displayEditButton" class="btn btn-primary  btn-xs custom-width floatRight">Edit</button>
      <button type="button" ng-click="ctrl.removeMembership(ctrl.membershipId)" ng-show="ctrl.displayEditButton" class="btn btn-danger  btn-xs custom-width floatRight">Remove</button>
    </div>
    <div class="table-responsive">
      <div class="panel-body">
        <div class="panel-body">
          <div class="formcontainer">
            <div class="col-sm-3">
              <div class="form-group col-sm-12">
                <label for="plan">Insurance</label>
                <select class=" form-control" ng-model="ctrl.insurance" ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name"></select>
              </div>
            </div>

            <div class="col-sm-3">
              <div class="form-group col-sm-12">
                <label for="plan">Provider</label>
                <select class=" form-control" ng-model="ctrl.prvdr" ng-options="provider.name for provider in ctrl.prvdrs  | providerFilter: ctrl.insurance.id | orderBy:'name' track by provider.name"> </select>
              </div>
            </div>
            <div class=" col-sm-6">
              <button type="button" ng-click="ctrl.generate()" class="btn btn-warning btn-xs align-bottom">Generate</button>
            </div>
          </div>
        </div>
        <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
        cellspacing="0" width="100%"></table>

      </div>
    </div>
  </div>


  <div class="panel panel-success" ng-if="ctrl.display">
    <!-- Default panel contents -->
    <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
      <div class="panel-heading"><span class="lead">Membership </span>
        <input type="submit" value="{{!ctrl.membership.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-xs custom-width  floatRight" ng-disabled="myForm.$invalid || myForm.$pristine">
        <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-xs custom-width  floatRight">Cancel</button>
        <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-xs custom-width  floatRight" ng-disabled="myForm.$pristine" ng-if="!ctrl.membership.id">Reset Form</button>
      </div>
      <div class="panel-body">
        <div class="formcontainer">
          <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
          <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>

          <input type="hidden" ng-model="ctrl.membership.id" />
          <div class="col-sm-6 ptInfo">
            <div class="col-sm-12 ptInfo">
              <div class="panel panel-success">
                <div class="panel-heading">Details</div>
                <div class="panel-body">



                  <div class="row">
                    <div class="col-sm-6">
                      <div class="form-group col-sm-12">
                        <label for="address1">First Name</label>
                        <input type="text" ng-model="ctrl.membership.firstName" name="firstName" class="username form-control input-sm" placeholder="Enter  First Name" required ng-minlength="2" />
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.firstName.$error.required">This is a required field</span>
                          <span ng-show="myForm.firstName.$error.minlength">Minimum length required is 2</span>
                          <span ng-show="myForm.firstName.$invalid">This field is invalid </span>
                        </div>
                      </div>
                    </div>


                    <div class="col-sm-6">
                      <div class="form-group col-sm-12">
                        <label for="address1">Last Name</label>
                        <input type="text" ng-model="ctrl.membership.lastName" name="lastName" class="username form-control input-sm" placeholder="Enter Last Name" required ng-minlength="2" />
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.lastName.$error.required">This is a required field</span>
                          <span ng-show="myForm.lastName.$error.minlength">Minimum length required is 2</span>
                          <span ng-show="myForm.lastName.$invalid">This field is invalid </span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="row">
                    <div class="col-md-6">
                      <div class="form-group col-sm-12">
                        <label for="gender">Gender</label>
                        <select class="form-control col-sm-12 " ng-model="ctrl.membership.genderId" name="gender" ng-options="gender.description for gender in ctrl.genders track by gender.description" required>
                          <option> </option>
                        </select>
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.gender.$error.required">This is a required field</span>
                        </div>
                      </div>
                    </div>

                    <div class="col-md-6">
                      <div class="form-group col-sm-12">
                        <label for="uname">DOB </label>
                        <input type="text" ng-model="ctrl.membership.dob" name="dob" class="username form-control input-sm" placeholder="Enter Date Of Birth" required/>
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.dob.$error.required">This is a required field</span>
                          <span ng-show="myForm.dob.$invalid">This field is invalid </span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="row">
                    <div class=" col-md-6">
                      <div class="form-group col-md-12">
                        <label for="uname">Status</label>
                        <select class="form-control col-sm-12 " ng-model="ctrl.membership.status" name="status" ng-options="status.description for status in ctrl.statuses track by status.description" required>
                          <option></option>
                        </select>
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.status.$error.required">This is a required field</span>
                        </div>
                      </div>
                    </div>

                    <div class=" col-md-6">
                      <div class="form-group col-sm-12">
                        <label for="uname">Medicaid#</label>
                        <input type="text" ng-model="ctrl.membership.medicaidNo" name="dob" class="username form-control input-sm" placeholder="Enter Medicaid Numberth" required/>
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.medicaidNo.$error.required">This is a required field</span>
                          <span ng-show="myForm.medicaidNo.$invalid">This field is invalid </span>
                        </div>
                      </div>
                    </div>
                  </div>



                  <div class="row">
                    <div class=" col-md-6">
                      <div class="form-group col-sm-12">
                        <label for="plan">Insurance</label>
                        <select class="form-control col-sm-12 " readonly ng-model="ctrl.insurance" ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name"></select>
                      </div>
                    </div>


                    <div class="form-group col-md-6">
                      <div class="form-group col-sm-12">
                        <label class="col-md-4 control-lable" for="plan">Provider</label>
                        <select class="form-control col-sm-12 " readonly ng-model="ctrl.prvdr" ng-options="provider.name for provider in ctrl.prvdrs  | providerFilter: ctrl.insurance.id | orderBy:'name' track by provider.name"> </select>
                      </div>
                    </div>
                  </div>


                  <div class="row">
                    <div class=" col-md-6">
                      <div class="form-group col-md-6">
                          <label for="plan">Medicaid Benefits</label>
                          <input type="checkbox" class="form-control  col-sm-6" ng-model="ctrl.membership.hasMedicaid" name="hasMedicaid" ng-true-value="'Y'" ng-false-value="'N'">
                      </div>
                    </div>


                    <div class="form-group col-md-6">
                      <div class="form-group col-sm-6">
                         <label for="plan">Disability Benefits</label>
                        <input type="checkbox" class="form-control  col-sm-6" ng-model="ctrl.membership.hasDisability" name="hasMedicaid" ng-true-value="'Y'" ng-false-value="'N'">
                      </div>
                    </div>
                  </div>


                </div>
              </div>
            </div>
          </div>

          <div class="col-sm-6 cntInfo">
            <div class="panel panel-success">
              <div class="panel-heading">Contact Info</div>
              <div class="panel-body">

                <div class="row">
                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="address1">Address 1</label>
                      <input type="text" ng-model="ctrl.membership.contact.address1" id="address1" name="address1" class="username form-control input-sm" placeholder="Enter Address" ng-required="!ctrl.user.contact.mobilePhone" ng-minlength="6" ng-maxlength="100" />
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.address1.$error.required">This is a required field</span>
                        <span ng-show="myForm.address1.$error.minlength">Minimum length required is 5</span>
                        <span ng-show="myForm.address1.$invalid">This field is invalid </span>
                      </div>
                    </div>
                  </div>
                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="addres2">Address 2</label>
                      <input type="text" ng-model="ctrl.membership.contact.address2" id="addres2" class="username form-control input-sm" placeholder="Enter Address" ng-maxlength="10" />
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="col-sm-12">
                    <div class="form-group col-sm-12">
                      <label for="	Last Name "> City / State / Zip</label>
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.city.$error.required">City is a required field</span>
                        <span ng-show="myForm.city.$error.minlength">City Minimum length required is 4</span>
                        <span ng-show="myForm.city.$invalid">City field is invalid </span>
                      </div>

                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.state.$error.required">State is a required field</span>
                      </div>

                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.zipcode.$error.required">Zipcode is a required field</span>
                      </div>

                      <div class="input-group">
                        <input type="text" ng-model="ctrl.membership.contact.city" id="city" name="city" class="username form-control" placeholder="Enter City" ng-required="!ctrl.user.mobilePhone" ng-minlength="4" ng-maxlength="100" />
                        <span class="input-group-addon">-</span>
                        <select ng-model="ctrl.membership.contact.stateCode" class="form-control" name="state" ng-options="state.description for state in ctrl.states | orderBy:'description' track by state.description" ng-required="!ctrl.user.mobilePhone"></select>
                        <span class="input-group-addon">-</span>
                        <select ng-model="ctrl.membership.contact.zipCode" name="zipcode" class="form-control" ng-options="zipCode.code for zipCode in ctrl.membership.contact.stateCode.zipCodes | orderBy:'code'  track by zipCode.code" ng-required="!ctrl.user.mobilePhone"></select>

                      </div>
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="homePhone">Home Phone</label>
                      <input type="text" ng-model="ctrl.membership.contact.homePhone" id="homePhone" name="homePhone" class="username form-control input-sm" placeholder="Enter Home phone" phone-input ng-required="!ctrl.membership.contact.address1" ng-minlength="10" />
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.homePhone.$error.required">This is a required field</span>
                        <span ng-show="myForm.homePhone.$error.minlength">Minimum length required is 10</span>
                      </div>
                    </div>
                  </div>

                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="mobilePhone">Mobile Phone</label>
                      <input type="text" ng-model="ctrl.membership.contact.mobilePhone" name="mobilePhone" class="username form-control input-sm" placeholder="Enter Mobile phone" phone-input ng-minlength="10" />
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.mobilePhone.$error.minlength">Minimum length required is 10</span>
                      </div>
                    </div>
                  </div>

                </div>

                <div class="row">

                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="email" require>Email </label>
                      <input type="email" ng-model="ctrl.user.contact.email" id="email" name="email" class="username form-control input-sm" placeholder="Enter email" ng-minlength="5" />
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.email.$error.minlength">Minimum length required is 8</span>
                        <span ng-show="myForm.email.$invalid">This field is invalid </span>
                      </div>
                    </div>
                  </div>

                </div>
              </div>
            </div>
          </div>



        </div>

      </div>
    </form>

    <div class="panel panel-success" ng-hide="true" ng-if="ctrl.display">
      <div class="panel panel-success panel-heading"><span class="membership">Provider Details </span> </div>
      <div class="table-responsive">
        <div class="panel-body">
          <table datatable="" id="content1" dt-options="ctrl.dt1Options" dt-columns="ctrl.dt1Columns" dt-instance="ctrl.dt1InstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
          cellspacing="0" width="100%"></table>
        </div>
      </div>
    </div>

    <div class="panel panel-success" ng-hide="true" ng-if="ctrl.display">
      <div class="panel panel-success panel-heading"><span class="membership">Insurance Details </span> </div>
      <div class="table-responsive">
        <div class="panel-body">
          <table datatable="" id="content2" dt-options="ctrl.dt2Options" dt-columns="ctrl.dt2Columns" dt-instance="ctrl.dt2InstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
          cellspacing="0" width="100%"></table>
        </div>
      </div>
    </div>

    <tabset>
      <tab heading="Pending">
        <div class="panel panel-success" ng-if="ctrl.display">
          <div class="panel panel-success panel-heading"><span class="membership">Membership Hedis Measure </span> </div>
          <div class="table-responsive">
            <div class="panel-body">
              <table datatable="" id="content3" dt-options="ctrl.dt3Options" dt-columns="ctrl.dt3Columns" dt-instance="ctrl.dt3InstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
              cellspacing="0" width="100%"></table>
            </div>
          </div>
        </div>
      </tab>
      <tab heading="Completed">
        <div class="panel panel-success" ng-if="ctrl.display">
          <div class="panel panel-success panel-heading"><span class="membership">Membership Hedis Measure </span> </div>
          <div class="table-responsive">
            <div class="panel-body">
              <table datatable="" id="content7" dt-options="ctrl.dt7Options" dt-columns="ctrl.dt7Columns" dt-instance="ctrl.dt7InstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
              cellspacing="0" width="100%"></table>
            </div>
          </div>
        </div>

      </tab>
    </tabset>



    <div class="panel panel-success" ng-if="ctrl.display">
      <div class="panel panel-success panel-heading"><span class="membership">Membership Problem List </span>
        <span class="alert alert-success" role="alert" ng-if="ctrl.mbrPbmSuccessMessage">{{ctrl.mbrPbmSuccessMessage}}</span>
        <span class="alert alert-danger" role="alert" ng-if="ctrl.mbrPbmErrorMessage">{{ctrl.mbrPbmErrorMessage}}</span>
        <button type="button" ng-click="ctrl.addMembershipProblem()" ng-hide="ctrl.displayProblem" class="btn btn-success btn-xs custom-width  floatRight"> Add </button>
      </div>
      <div class="table-responsive" ng-if="!ctrl.displayProblem">
        <tabset>
          <tab heading="HCC">
            <div class="panel-body">
              <table datatable="" id="content4" dt-options="ctrl.dt4Options" dt-columns="ctrl.dt4Columns" dt-instance="ctrl.dt4InstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
              cellspacing="0" width="100%"></table>
            </div>
          </tab>
          <tab heading="NonHCC">
            <div class="panel-body">
              <table datatable="" id="content4NonHCC" dt-options="ctrl.dt4NonHCCOptions" dt-columns="ctrl.dt4Columns" dt-instance="ctrl.dt4NonHCCInstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
              cellspacing="0" width="100%"></table>
            </div>
          </tab>
          <tab heading="History">
            <div class="panel-body">
              <table datatable="" id="content4History" dt-options="ctrl.dt4HistoryOptions" dt-columns="ctrl.dt4Columns" dt-instance="ctrl.dt4HistoryInstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
              cellspacing="0" width="100%"></table>
            </div>
          </tab>
             <tab heading="Rx">
            <div class="panel-body">
              <table datatable="" id="content4Rx" dt-options="ctrl.dt4RxOptions" dt-columns="ctrl.dt4Columns" dt-instance="ctrl.dt4RxInstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
              cellspacing="0" width="100%"></table>
            </div>
          </tab>
          <tab heading="RxHistory">
            <div class="panel-body">
              <table datatable="" id="content4RxHistory" dt-options="ctrl.dt4RxHistoryOptions" dt-columns="ctrl.dt4Columns" dt-instance="ctrl.dt4RxHistoryInstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
              cellspacing="0" width="100%"></table>
            </div>
          </tab>
        </tabset>
      </div>

      <form ng-submit="ctrl.pbmsubmit()" name="pbmForm" class="form-horizontal">
        <div class="table-responsive" ng-if="ctrl.displayProblem">
          <input type="hidden" ng-model="ctrl.membership.id" />
          <div class="panel-body">

            <table class="table ">
              <thead>
                <tr>
                  <th> </th>
                  <th>Problem ICD</th>
                  <th>Start Date</th>
                  <th>Resolved Date</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <tr ng-repeat="record in ctrl.membershipProblems track by $index">
                  <td>
                    DX{{($index+1)}}
                  </td>
                  <td>

                    <ui-select ng-model="ctrl.membershipProblems[$index].icdMeasure" theme="selectize" ng-disabled="disabled" style="width: 300px;">
                      <ui-select-match placeholder="Select or search a ICD in the list...">{{$select.selected.code}}</ui-select-match>
                      <ui-select-choices repeat="icdMeasure in ctrl.icdMeasures | filter: $select.search">
                        <span ng-bind-html="icdMeasure.code | highlight: $select.search"></span>
                        <small ng-bind-html="icdMeasure.code | highlight: $select.search"></small>
                      </ui-select-choices>
                    </ui-select>
                  </td>
                  <td>
                    <div class="input-group date" id="startDate" name="startDate" ng-model="ctrl.membershipProblems[$index].startDate" date1-picker>
                      <input type="text" class="form-control netto-input" ng-model="ctrl.membershipProblems[$index].startDate" date-picker-input ng-required="true">
                      <span class="input-group-addon">
							           							<span class="glyphicon glyphicon-calendar"></span>
                      </span>
                    </div>
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="pbmForm.startDate.$error.required">This is a required field</span>
                      <span ng-show="pbmForm.startDate.$error.minlength">Minimum length required is 10</span>
                      <span ng-show="pbmForm.startDate.$invalid">This field is invalid </span>
                    </div>
                  </td>
                  <td>
                    <div class="input-group date" id="resolvedDate" name="resolvedDate" ng-model="ctrl.membershipProblems[$index].resolvedDate" date1-picker>
                      <input type="text" class="form-control netto-input" ng-model="ctrl.membershipProblems[$index].resolvedDate" date-picker-input>
                      <span class="input-group-addon">
							           							<span class="glyphicon glyphicon-calendar"></span>
                      </span>
                    </div>
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="pbmForm.resolvedDate.$error.minlength">Minimum length required is 10</span>
                      <span ng-show="pbmForm.resolvedDate.$invalid">This field is invalid </span>
                    </div>
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
            <input type="submit" value="Submit" ng-show="ctrl.displayProblem" class="btn btn-primary btn-xs" ng-disabled="pbmForm.$invalid || pbmForm.$pristine">
            <button type="button" ng-click="ctrl.cancelMbrPrblmEdit()" ng-show="ctrl.displayProblem" class="btn btn-warning btn-xs">Cancel</button>
            <button type="button" ng-click="ctrl.resetMbrPrblm()" ng-show="ctrl.displayProblem" class="btn btn-warning btn-xs" ng-disabled="pbmForm.$pristine">Reset Form</button>
          </div>
        </div>
      </form>

    </div>




    <div class="panel panel-success" ng-if="ctrl.display">
      <div class="panel panel-success panel-heading"><span class="membership">Membership Hospitalization List </span> </div>
      <div class="table-responsive">
        <div class="panel-body">
          <table datatable="" id="content5" dt-options="ctrl.dt5Options" dt-columns="ctrl.dt5Columns" dt-instance="ctrl.dt5InstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
          cellspacing="0" width="100%"></table>
        </div>
      </div>
    </div>

    <div class="panel panel-success" ng-hide="true" ng-if="ctrl.display">
      <div class="panel panel-success panel-heading"><span class="membership">Membership Activity Months </span> </div>
      <div class="table-responsive">
        <div class="panel-body">
          <table datatable="" id="content6" dt-options="ctrl.dt6Options" dt-columns="ctrl.dt6Columns" dt-instance="ctrl.dt6InstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
          cellspacing="0" width="100%"></table>
        </div>
      </div>
    </div>

  </div>


</div>
