
<div class="generic-container">
  <div class="panel panel-success" ng-if="!ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="user">List of Users </span>
      <button type="button" ng-click="ctrl.addUser()" ng-hide="ctrl.displayEditButton" class="btn btn-success btn-xs custom-width floatRight"> Add </button>
      <button type="button" ng-click="ctrl.editUser(ctrl.userId)" ng-show="ctrl.displayEditButton" class="btn btn-primary  btn-xs custom-width floatRight">Edit</button>
      <button type="button" ng-click="ctrl.removeUser(ctrl.userId)" ng-show="ctrl.displayEditButton" class="btn btn-danger  btn-xs custom-width floatRight">Remove</button>
    </div>
    <div class="table-responsive">
      <div class="panel-body">

        <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="table-responsive table  bordered table-striped table-condensed datatable "></table>
      </div>
    </div>
  </div>


  <div class="panel panel-success" ng-if="ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="lead">User </span></div>
    <div class="panel-body">
      <div class="formcontainer">
        <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
        <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
        <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
          <input type="hidden" ng-model="ctrl.user.id" />

          <div class="col-sm-6 ptInfo">
            <div class="panel panel-success">
              <div class="panel-heading">Details</div>
              <div class="panel-body">


                <div class="row">
                  <div class="form-group col-md-12">
                    <label class="col-md-2 control-lable" for="uname">Name</label>
                    <div class="col-md-7">
                      <input type="text" ng-model="ctrl.user.name" name="name" class="name form-control input-sm" placeholder="Enter your name" ng-required="true" ng-minlength="5" />
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.name.$error.required">This is a required field</span>
                        <span ng-show="myForm.name.$error.minlength">Minimum length required is 5</span>
                        <span ng-show="myForm.name.$invalid">This field is invalid </span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="form-group col-md-12">
                    <label class="col-md-2 control-lable" for="uname">Username</label>
                    <div class="col-md-7">
                      <input type="text" ng-model="ctrl.user.username" name="username" class="username form-control input-sm" placeholder="Enter your username" ng-required="true" ng-minlength="5" />
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.username.$error.required">This is a required field</span>
                        <span ng-show="myForm.username.$error.minlength">Minimum length required is 5</span>
                        <span ng-show="myForm.username.$invalid">This field is invalid </span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="form-group col-md-12">
                    <label class="col-md-2 control-lable" for="age">Password</label>
                    <div class="col-md-7">
                      <input type="text" ng-model="ctrl.user.password" name="password" class="form-control input-sm" placeholder="Enter your Password." ng-required="true" ng-minlength="5" />
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.password.$error.required">This is a required field</span>
                        <span ng-show="myForm.password.$error.minlength">Minimum length required is 5</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="form-group col-md-12">
                    <label class="col-md-2 control-lable" for="uname">Role</label>
                    <div class="col-md-7">
                      <select ng-model="ctrl.user.role" name="role" ng-options="role.role for role in ctrl.roles | orderBy:'role' track by role.role" ng-required="true"></select>
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.role.$error.required">This is a required field</span>
                      </div>
                    </div>
                  </div>
                </div>

                <div class="row">
                  <div class="form-group col-md-12">
                    <label class="col-md-2 control-lable" for="uname">Language</label>
                    <div class="col-md-7">
                      <select ng-model="ctrl.user.language" name="language" ng-options="language as language.description for language in ctrl.languages | orderBy:'description' track by language.description" ng-required="(ctrl.user.roles|filter:{role:'AGENT'}).length > 0"></select>
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.language.$error.required">This is a required field</span>
                      </div>
                    </div>
                  </div>
                </div>



                <div class="row">
                  <div class="form-group col-md-12">
                    <label class="col-md-2 control-lable" for="insurance">Insurance</label>
                    <div class="col-md-9">
                      <select ng-model="ctrl.user.insurance" name="insurance" ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name" ng-required="(ctrl.user.roles|filter:{role:'AGENT'}).length > 0">
                        <option value="">ALL</option>
                      </select>
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="(ctrl.user.roles|filter:{role:'AGENT'}).length > 0 && myForm.insurance.$error.required">This is a required field</span>
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
                      <input type="text" ng-model="ctrl.user.contact.address1" id="address1" name="address1" class="username form-control input-sm" placeholder="Enter Address" ng-required="!ctrl.user.contact.mobilePhone" ng-minlength="6" ng-maxlength="100" />
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
                      <input type="text" ng-model="ctrl.user.contact.address2" id="addres2" class="username form-control input-sm" placeholder="Enter Address" ng-maxlength="10" />
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
                        <input type="text" ng-model="ctrl.user.contact.city" id="city" name="city" class="username form-control" placeholder="Enter City" ng-required="!ctrl.user.mobilePhone" ng-minlength="4" ng-maxlength="100" />
                        <span class="input-group-addon">-</span>
                        <select ng-model="ctrl.user.contact.stateCode" class="form-control" name="state" ng-options="state.description for state in ctrl.states | orderBy:'description' track by state.description" ng-required="!ctrl.user.mobilePhone"></select>
                        <span class="input-group-addon">-</span>
                        <select ng-model="ctrl.user.contact.zipCode" name="zipcode" class="form-control" ng-options="zipCode.code for zipCode in ctrl.user.contact.stateCode.zipCodes | orderBy:'code'  track by zipCode.code" ng-required="!ctrl.user.mobilePhone"></select>

                      </div>
                    </div>
                  </div>

                </div>


                <div class="row">
                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="homePhone">Home Phone</label>
                      <input type="text" ng-model="ctrl.user.contact.homePhone" id="homePhone" name="homePhone" class="username form-control input-sm" placeholder="Enter Home phone" phone-input ng-minlength="10" />
                      <div class="has-error" ng-show="myForm.$dirty">
                         <span ng-show="myForm.homePhone.$error.minlength">Minimum length required is 10</span>
                      </div>
                    </div>
                  </div>

                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="mobilePhone">Mobile Phone</label>
                      <input type="text" ng-model="ctrl.user.contact.mobilePhone" name="mobilePhone" class="username form-control input-sm" placeholder="Enter Mobile phone"  ng-required="!ctrl.user.address1"  phone-input ng-minlength="10" />
                       <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.mobilePhone.$error.required">This is a required field</span>
                         <span ng-show="myForm.mobilePhone.$error.minlength">Minimum length required is 10</span>
                      </div>
                    </div>
                  </div>

                </div>

                <div class="row">
                  <div class="col-sm-6" ng-if="ctrl.lead.id">
                    <div class="form-group col-sm-12">
                      <label for="bestTimeToCall">Best Time to Call</label>
                      <input type="text" class="form-control netto-input" ng-model="ctrl.user.contact.bestTimeToCall" date-picker-input>
                    </div>
                  </div>

                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="email" require>Email </label>
                      <input type="email" ng-model="ctrl.user.contact.email" id="email" name="email" class="username form-control input-sm" placeholder="Enter email" ng-required="true" ng-minlength="5" />
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

          <div class="row">
            <div class="form-actions floatRight">
              <input type="submit" value="{{!ctrl.user.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-xs" ng-disabled="myForm.$invalid || myForm.$pristine">
              <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-xs" ng-show="!ctrl.user.id" ng-disabled="myForm.$pristine">Reset Form</button>
              <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-xs">Cancel</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>

</div>