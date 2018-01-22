<div class="generic-container">
  <div class="panel panel-success" ng-if="!ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="prvdr ">List of {{ctrl.currentScreen}} Providers  <i>(Click here for <a  ui-sref="{{ctrl.linkToScreen}}" ><span class="text-danger"> {{ctrl.toScreen}} <span> </a> </i> </span>)
      <button type="button" ng-click="ctrl.addProvider()" ng-hide="ctrl.displayEditButton" class="btn btn-success btn-xs custom-width  floatRight"> Add </button>
      <button type="button" ng-click="ctrl.editProvider(ctrl.prvdrId)" ng-show="ctrl.displayEditButton" class="btn btn-primary  btn-xs custom-width floatRight">Edit</button>
      <button type="button" ng-click="ctrl.removeProvider(ctrl.prvdrId)" ng-show="ctrl.displayEditButton" class="btn btn-danger  btn-xs custom-width floatRight">Remove</button>
      
    </div>
    <div class="table-responsive">
      <div class="panel-body">

        <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover " cellspacing="0" width="100%"></table>
      </div>
    </div>
  </div>


  <div class="panel panel-success" ng-if="ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="lead">Provider </span></div>
    <div class="panel-body">
      <div class="formcontainer">
        <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
        <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
        <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
          <input type="hidden" ng-model="ctrl.prvdr.id" />
          <div class="col-sm-6 ptInfo">
            <div class="col-sm-12 ptInfo">
              <div class="panel panel-success">
                <div class="panel-heading">Details</div>
                <div class="panel-body">
                  <div class="row">
                    <div class="form-group col-md-6">
                      <label class="col-md-4  control-lable" for="uname">Name</label>
                      <div class="col-md-8">
                        <input type="text" ng-model="ctrl.prvdr.name" name="name" class="username form-control input-sm" placeholder="Enter your name" required ng-minlength="5" />
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.name.$error.required">This is a required field</span>
                          <span ng-show="myForm.name.$error.minlength">Minimum length required is 5</span>
                          <span ng-show="myForm.name.$invalid">This field is invalid </span>
                        </div>
                      </div>
                    </div>

                    <div class="form-group col-md-6">
                      <label class="col-md-2 col-offset-md-2  control-lable" for="uname">NPI</label>
                      <div class="col-md-7">
                        <input type="text" ng-model="ctrl.prvdr.code" name="code" class="username form-control input-sm" placeholder="Enter NPI" required ng-minlength="8" />
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.code.$error.required">This is a required field</span>
                          <span ng-show="myForm.code.$error.minlength">Minimum length required is 8</span>
                          <span ng-show="myForm.code.$invalid">This field is invalid </span>
                        </div>
                      </div>
                    </div>
                  </div>

                  <div class="row">
                    <div class="form-group col-md-6">
                      <label class="col-md-4  control-lable" for="uname">Age Seen</label>
                      <div class="col-md-7">
                        <input type="text" ng-model="ctrl.prvdr.ageSeen" name="ageSeen" class="username form-control input-sm" placeholder="Enter Ages Seen" />
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.ageSeen.$invalid">This field is invalid </span>
                        </div>
                      </div>
                    </div>

                    <div class="form-group col-md-6">
                      <label class="col-md-4 control-lable" for="uname">Hours </label>
                      <div class="col-md-7">
                        <input type="text" ng-model="ctrl.prvdr.hrsOfOperation" name="hrsOfOperation" class="username form-control input-sm" placeholder="Enter Business Hours" />
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.hrsOfOperation.$invalid">This field is invalid </span>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="row">
                    <div class="form-group col-md-6">
                      <label class="col-md-4 control-lable" for="uname">Languages</label>
                      <div class="col-md-7">
                        <select ng-model="ctrl.prvdr.languages" name="languages" ng-options="language.description for language in ctrl.languages track by language.description"> </select>
                        <div class="has-error" ng-show="myForm.$dirty">
                          <span ng-show="myForm.languages.$error.required">This is a required field</span>
                        </div>
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
                      <input type="text" ng-model="ctrl.prvdr.contact.address1" id="address1" name="address1" class="username form-control input-sm" placeholder="Enter Address" ng-required="!ctrl.user.contact.mobilePhone" ng-minlength="6" ng-maxlength="100" />
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
                      <input type="text" ng-model="ctrl.prvdr.contact.address2" id="addres2" class="username form-control input-sm" placeholder="Enter Address" ng-maxlength="10" />
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
                        <input type="text" ng-model="ctrl.prvdr.contact.city" id="city" name="city" class="username form-control" placeholder="Enter City" ng-required="!ctrl.user.mobilePhone" ng-minlength="4" ng-maxlength="100" />
                        <span class="input-group-addon">-</span>
                        <select ng-model="ctrl.prvdr.contact.stateCode" class="form-control" name="state" ng-options="state.description for state in ctrl.states | orderBy:'description' track by state.description" ng-required="!ctrl.user.mobilePhone"></select>
                        <span class="input-group-addon">-</span>
                        <select ng-model="ctrl.prvdr.contact.zipCode" name="zipcode" class="form-control" ng-options="zipCode.code for zipCode in ctrl.prvdr.contact.stateCode.zipCodes | orderBy:'code'  track by zipCode.code" ng-required="!ctrl.user.mobilePhone"></select>

                      </div>
                    </div>
                  </div>
                </div>



              </div>

              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="homePhone">Home Phone</label>
                    <input type="text" ng-model="ctrl.prvdr.contact.homePhone" id="homePhone" name="homePhone" class="username form-control input-sm" placeholder="Enter Home phone" phone-input ng-required="!ctrl.prvdr.contact.address1" ng-minlength="10" />
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.homePhone.$error.required">This is a required field</span>
                      <span ng-show="myForm.homePhone.$error.minlength">Minimum length required is 10</span>
                    </div>
                  </div>
                </div>

                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="mobilePhone">Mobile Phone</label>
                    <input type="text" ng-model="ctrl.prvdr.contact.mobilePhone" name="mobilePhone" class="username form-control input-sm" placeholder="Enter Mobile phone" phone-input ng-minlength="10" />
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

          <div class="row">
            <div class="col-sm-12 contractInfo">
              <div class="panel panel-success">
                <div class="panel-heading">Standard Contract Details</div>
                <div class="panel-body">
                  <div class="row col-md-12">

                    <div class="row">
                      <div class="col-sm-3">
                        <div class="form-group col-sm-12">
                          <label for="contractNumber">Contract Number</label>
                          <input type="text" ng-model="ctrl.prvdr.prvdrRefContracts[0].contract.contractNBR" name="contractNumber" class="username form-control input-sm" placeholder="Enter Contract Number" required ng-minlength="5" />
                          <div class="has-error" ng-show="myForm.$dirty">
                            <span ng-show="myForm.contractNumber.$error.required">This is a required field</span>
                            <span ng-show="myForm.contractNumber.$error.minlength">Minimum length required is 5</span>
                            <span ng-show="myForm.contractNumber.$invalid">This field is invalid </span>
                          </div>
                        </div>
                      </div>

                      <div class="col-sm-3">
                        <div class="form-group col-sm-12">
                          <label for="contractNumber">Start Date</label>
                          <div class="input-group date" id="startDate" name="startDate" ng-model="ctrl.prvdr.prvdrRefContracts[0].contract.startDate" date1-picker>
                            <input type="text" class="form-control netto-input" ng-model="ctrl.prvdr.prvdrRefContracts[0].contract.startDate" date-picker-input ng-required="true">
                            <span class="input-group-addon">
			           							<span class="glyphicon glyphicon-calendar"></span>
                            </span>
                          </div>
                          <div class="has-error" ng-show="myForm.$dirty">
                            <span ng-show="myForm.startDate.$error.required">This is a required field</span>
                            <span ng-show="myForm.startDate.$error.minlength">Minimum length required is 5</span>
                            <span ng-show="myForm.startDate.$invalid">This field is invalid </span>
                          </div>
                        </div>
                      </div>

                      <div class="col-sm-3">
                        <div class="form-group col-sm-12">
                          <label for="contractNumber">End Date</label>
                          <div class="input-group date" id="startDate" name="endDate" ng-model="ctrl.prvdr.prvdrRefContracts[0].contract.endDate" date1-picker>
                            <input type="text" class="form-control netto-input" ng-model="ctrl.prvdr.prvdrRefContracts[0].contract.endDate" date-picker-input ng-required="true">
                            <span class="input-group-addon">
			           							<span class="glyphicon glyphicon-calendar"></span>
                            </span>
                          </div>
                          <div class="has-error" ng-show="myForm.$dirty">
                            <span ng-show="myForm.endDate.$error.required">This is a required field</span>
                            <span ng-show="myForm.endDate.$error.minlength">Minimum length required is 5</span>
                            <span ng-show="myForm.endDate.$invalid">This field is invalid </span>
                          </div>
                        </div>
                      </div>

                      <div class="col-sm-3">
                        <div class="group col-sm-11">
                          <label for="file">File </label>
                          <input class="col-sm-12  control-label form-control" name="fileUpload" type="file" file-model="ctrl.prvdr.prvdrRefContracts[0].myFile"  ng-model="ctrl.prvdr.prvdrRefContracts[0].myFile" id="myFileField"  />
                          <div class="group col-sm-1" ng-if="ctrl.prvdr.id">
                                     	<a  ng-click="ctrl.readUploadedFile(0)" style="display:block;">
          					                <span class="glyphicon glyphicon-file">{{ctrl.prvdr.prvdrRefContracts[0].contract.fileUpload.fileName}}</span>
        				                </a>
                           </div>
                                  
                        </div>
                      </div>

                    </div>


                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="row" >
            <div class="col-sm-12 thirdPartyContractInfo">
              <div class="panel panel-success">
                <div class="panel-heading">Third Party Agreement
                <button type="button" ng-click="ctrl.addInsPrvdrContract()" ng-disabled="!ctrl.prvdr.prvdrRefContracts[0].contract.id" class="btn btn-success btn-xs custom-width  floatRight"> Add </button>
                </div>
                <div ng-repeat=" prvdrInsContract in ctrl.prvdr.prvdrRefContracts" ng-if="$index > 0" >
              
                  <div class="panel panel-success panel-body">
                   
                      <div class="row">
                        <div class="col-sm-3">
                          <div class="form-group col-sm-12">
                            <label for="contractNumber">Contract Number</label>
                            <input type="text" ng-model="ctrl.prvdr.prvdrRefContracts[$index].contract.contractNBR" name="contractNumber" class="username form-control input-sm" placeholder="Enter Contract Number" required ng-minlength="5" />
                            <div class="has-error" ng-show="myForm.$dirty">
                              <span ng-show="myForm.contractNumber.$error.required">This is a required field</span>
                              <span ng-show="myForm.contractNumber.$error.minlength">Minimum length required is 5</span>
                              <span ng-show="myForm.contractNumber.$invalid">This field is invalid </span>
                            </div>
                          </div>
                        </div>


                        <div class="col-sm-3">
                          <div class="form-group col-sm-12">
                            <label for="plan">Insurance</label>
                            <select class=" form-control" ng-model="ctrl.prvdr.prvdrRefContracts[$index].ins" ng-change="ctrl.setInsPrvdrContractDates($index)" ng-options="insurance.name for insurance in ctrl.insurances  | orderBy:'name' track by insurance.id"></select>
                          </div>
                        </div>


                        <div class="col-sm-3">
                          <div class="form-group col-sm-12">
                            <label for="contractNumber">Start Date</label>
                            <div class="input-group date" id="startDate" name="startDate"  ng-model="ctrl.prvdr.prvdrRefContracts[$index].contract.startDate" date1-picker>
                              <input type="text" class="form-control netto-input" ng-model="ctrl.prvdr.prvdrRefContracts[$index].contract.startDate" date-picker-input ng-required="true">
                              <span class="input-group-addon">
			           							<span class="glyphicon glyphicon-calendar"></span>
                              </span>
                            </div>
                            <div class="has-error" ng-show="myForm.$dirty">
                              <span ng-show="myForm.startDate.$error.required">This is a required field</span>
                              <span ng-show="myForm.startDate.$error.minlength">Minimum length required is 5</span>
                              <span ng-show="myForm.startDate.$invalid">This field is invalid </span>
                            </div>
                          </div>
                        </div>

                        <div class="col-sm-3">
                          <div class="form-group col-sm-12">
                            <label for="contractNumber">End Date</label>
                            <div class="input-group date" id="startDate" ng-model="ctrl.prvdr.prvdrRefContracts[$index].contract.endDate"   name="endDate" date1-picker>
                              <input type="text" class="form-control netto-input" ng-model="ctrl.prvdr.prvdrRefContracts[$index].contract.endDate" date-picker-input ng-required="true">
                              <span class="input-group-addon">
			           							<span class="glyphicon glyphicon-calendar"></span>
                              </span>
                            </div>
                            <div class="has-error" ng-show="myForm.$dirty">
                              <span ng-show="myForm.endDate.$error.required">This is a required field</span>
                              <span ng-show="myForm.endDate.$error.minlength">Minimum length required is 5</span>
                              <span ng-show="myForm.endDate.$invalid">This field is invalid </span>
                            </div>
                          </div>
                        </div>

                      </div>

                      <div class="row">
                        <div class="col-sm-3">
                          <div class="form-group col-sm-12">
                            <label for="pmpm">PMPM</label>
                            <input type="text" ng-model="ctrl.prvdr.prvdrRefContracts[$index].contract.pmpm" name="pmpm" class="username form-control input-sm" placeholder="Enter PMPM" ng-required="true" ng-minlength="1" />
                            <div class="has-error" ng-show="myForm.$dirty">
                              <span ng-show="myForm.pmpm.$error.required">This is a required field</span>
                              <span ng-show="myForm.pmpm.$error.minlength">Minimum length required is 1</span>
                              <span ng-show="myForm.pmpm.$invalid">This field is invalid </span>
                            </div>
                          </div>
                        </div>

                        <div class="col-sm-3">
                          <div class="form-group col-sm-12">
                            <label for="serviceFund">Average ServiceFund</label>
                            <input type="text" ng-model="ctrl.prvdr.prvdrRefContracts[$index].contract.avgServiceFund" name="avgServiceFund" class="username form-control input-sm" placeholder="Enter ServiceFund"  ng-minlength="1" />
                            <div class="has-error" ng-show="myForm.$dirty">
                              <span ng-show="myForm.serviceFund.$error.required">This is a required field</span>
                              <span ng-show="myForm.serviceFund.$error.minlength">Minimum length required is 1</span>
                              <span ng-show="myForm.serviceFund.$invalid">This field is invalid </span>
                            </div>
                          </div>
                        </div>

                        <div class="col-sm-3">
                          <div class="form-group col-sm-12">
                            <label for="serviceFund">Provider Number</label>
                            <input type="text" ng-model="ctrl.prvdr.prvdrRefContracts[$index].contract.pcpPrvdrNBR" name="pcpPrvdrNBR" class="username form-control input-sm" placeholder="Enter ServiceFund" ng-required="true" ng-minlength="1" />
                            <div class="has-error" ng-show="myForm.$dirty">
                              <span ng-show="myForm.pcpPrvdrNBR.$error.required">This is a required field</span>
                              <span ng-show="myForm.pcpPrvdrNBR.$error.minlength">Minimum length required is 1</span>
                              <span ng-show="myForm.pcpPrvdrNBR.$invalid">This field is invalid </span>
                            </div>
                          </div>
                        </div>

                        <div class="col-sm-3">
                          <div class="group col-sm-12">
                            <label for="file">File </label>
                            <input class="col-sm-12  control-label form-control" name="fileUpload" type="file" file-model="ctrl.prvdr.prvdrRefContracts[$index].myFile" ng-model="ctrl.prvdr.prvdrRefContracts[$index].myFile"  id="myFileField"  />
                             <div class="group col-sm-1" ng-if="ctrl.prvdr.prvdrRefContracts[$index].contract.fileUpload.id">
                                     	<a  ng-click="ctrl.readUploadedFile($index)" style="display:block;">
          					                <span class="glyphicon glyphicon-file">{{ctrl.prvdr.prvdrRefContracts[$index].contract.fileUpload.fileName}}</span>
        				                </a>
                           </div>
                          </div>
                        </div>
                      </div>
                    
                  </div>
                </div>
              </div>
            </div>
          </div>

          <div class="row">
            <div class="form-actions floatRight">
              <input type="submit" value="{{!ctrl.prvdr.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-xs" ng-disabled="myForm.$invalid || myForm.$pristine">
              <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-xs">Cancel</button>
              <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-xs" ng-disabled="myForm.$pristine" ng-if="!ctrl.prvdr.id">Reset Form</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>


</div>