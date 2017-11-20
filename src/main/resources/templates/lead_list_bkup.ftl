<div class="generic-container">
  <div class="panel panel-success" ng-hide="ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="user">List of Leads </span>
      <button type="button" ng-click="ctrl.addLead()" ng-hide="ctrl.displayEditButton" ng-show="ctrl.loginUser.roleName != 'AGENT'" class="btn btn-success  btn-xs  custom-width floatRight"> Add </button>
      <button type="button" ng-click="ctrl.editLead(ctrl.leadId)" ng-show="ctrl.displayEditButton" class="btn btn-primary btn-xs custom-width floatRight">Edit</button>
      <button type="button" ng-click="ctrl.removeLead(ctrl.leadId)" ng-show="ctrl.displayEditButton" class="btn btn-danger btn-xs  custom-width floatRight">Remove</button>
    </div>

    <div class="panel-body">
      <div class="table-responsive">
        <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="table table-hover table-responsive  bordered table-striped table-condensed datatable "></table>
      </div>
    </div>
  </div>
  
  
  <div class="panel panel-success" ng-show="ctrl.display">
    <div class="panel-heading">List Details</div>
    <div class="panel-body">
      <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
        <input type="hidden" ng-model="ctrl.lead.id" />

        <div class="col-sm-12 sourceInfo" ng-show="ctrl.showEventSource()">
          <div class="panel panel-success">
            <div class="panel-heading">Source</div>
            <div class="panel-body">
              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="agentEvent">Event </label>
                     <select ng-model="ctrl.lead.event" class="form-control" ng-options="event.eventName for event in ctrl.events  | orderBy:'eventName'  track by event.eventName" required></select>
                  </div>
                </div>

             
              </div>
            </div>
          </div>
        </div>


        <div class="col-sm-6 ptInfo">
          <div class="panel panel-success">
            <div class="panel-heading">Patient Info</div>
            <div class="panel-body">
              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="firstName"> First Name </label>
                    <input type="text" ng-model="ctrl.lead.firstName" id="firstName" name="firstName" class="username form-control input-sm" placeholder="Enter firstname" required ng-minlength="5" />
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.firstName.$error.required">This is a required field</span>
                      <span ng-show="myForm.firstName.$error.minlength">Minimum length required is 5</span>
                      <span ng-show="myForm.firstName.$invalid">This field is invalid </span>
                    </div>
                  </div>
                </div>
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="lastName">Last Name</label>
                    <input type="text" ng-model="ctrl.lead.lastName" id="lastName" name="lastName" class="username  form-control input-sm" placeholder="Enter last name" required ng-minlength="5" />
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.lastName.$error.required">This is a required field</span>
                      <span ng-show="myForm.lastName.$error.minlength">Minimum length required is 5</span>
                      <span ng-show="myForm.lastName.$invalid">This field is invalid </span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="gender">Gender</label>
                    <select ng-model="ctrl.lead.gender" name="gender" class="form-control" ng-options="gender.description for gender in ctrl.genders | orderBy:'description' track by gender.description" required></select>
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.gender.$error.required">This is a required field</span>
                    </div>
                  </div>
                </div>
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="dob">Date Of Birth</label>
                    <div class="input-group date" id="dob" ng-model="ctrl.lead.dob" date1-picker>
                      <input type="text" class="form-control netto-input" ng-model="ctrl.lead.dob" date-picker-input>
                      <span class="input-group-addon">
			           							<span class="glyphicon glyphicon-calendar"></span>
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="planType">Plan Type</label>
                    <select ng-model="ctrl.lead.planType" class="form-control" name="planType" ng-options="planType.description for planType in ctrl.planTypes | orderBy:'description' track by planType.description" required></select>
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.planType.$error.required">This is a required field</span>
                    </div>
                  </div>
                </div>
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="currentPlan">Current Plan</label>
                    <select ng-model="ctrl.lead.insurance" class="form-control" name="insurance" ng-options="insurance.name for insurance in ctrl.insurances  | orderBy:'name' | filter:{planType:{id:ctrl.lead.planType.id}} track by insurance.name" required="ctrl.lead.planType.id !=1"></select>
                    <div class="has-error" ng-show="myForm.$dirty && ctrl.lead.planType.id !=1">
                      <span ng-show="myForm.insurance.$error.required">This is a required field</span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="language">Language</label>
                    <select ng-model="ctrl.lead.language" name="language" class="form-control"   ng-options="language.description for language in ctrl.languages | orderBy:'description' track by language.description" required></select>
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.language.$error.required">This is a required field</span>
                    </div>
                  </div>
                </div>
                
              
                <div class="col-sm-6" ng-show="ctrl.lead.id">
                  <div class="form-group col-sm-12">
                    <label for="language">File</label>
                    	<a  ng-click="ctrl.readUploadedFile()" style="display:block;">
          					<span class="glyphicon glyphicon-file"></span>
        				</a>
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
                    <input type="text" ng-model="ctrl.lead.address1" id="address1" class="username form-control input-sm" placeholder="Enter Address" required="!ctrl.lead.homePhone" ng-minlength="6" ng-maxlength="100" />
                  </div>
                </div>
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="addres2">Address 2</label>
                    <input type="text" ng-model="ctrl.lead.address2" id="addres2" class="username form-control input-sm" placeholder="Enter Address" ng-maxlength="10" />
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="	Last Name "> City / State / Zip</label>
                    <div class="input-group">

                      <input type="text" ng-model="ctrl.lead.city" id="city" class="username form-control" placeholder="Enter City" required="!ctrl.lead.homePhone" ng-minlength="4" ng-maxlength="100" />
                      <span class="input-group-addon">-</span>
                      <select ng-model="ctrl.lead.stateCode" class="form-control" ng-options="state.description for state in ctrl.states | orderBy:'description' track by state.description" required="!ctrl.lead.homePhone"></select>
                      <span class="input-group-addon">-</span>
                      <select ng-model="ctrl.lead.zipCode" class="form-control" ng-options="zipCode.code for zipCode in ctrl.lead.stateCode.zipCodes | orderBy:'code'  track by zipCode.code" required="!ctrl.lead.homePhone"></select>
                    </div>
                  </div>
                </div>

              </div>

              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="homePhone">Home Phone</label>
                    <input type="text" ng-model="ctrl.lead.homePhone" id="homePhone" name="homePhone" class="username form-control input-sm" placeholder="Enter Home phone" required="!ctrl.lead.address1" phone-input ng-minlength="10" />
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.homePhone.$error.required">This is a required field</span>
                    </div>
                  </div>
                </div>

                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="mobilePhone">Mobile Phone</label>
                    <input type="text" ng-model="ctrl.lead.mobilePhone" id="mobilePhone" class="username form-control input-sm" placeholder="Enter Mobile phone" phone-input ng-minlength="10" />
                  </div>
                </div>

              </div>

              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="bestTimeToCall">Best Time to Call</label>
                    <input type="text" class="form-control netto-input" ng-model="ctrl.lead.bestTimeToCall" date-picker-input>
                  </div>
                </div>

                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="email" require>Email </label>
                      <input type="email" ng-model="ctrl.lead.email" id="email" name="email" class="username form-control input-sm" placeholder="Enter email" ng-minlength="5" />
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

        <div class="col-sm-12 additionalInfo" ng-show="ctrl.showLeadAdditionalDetails()">
          <div class="panel panel-success">
            <div class="panel-heading">Additional Details</div>
            <div class="panel-body">
              <div class="row col-md-6">
                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="agree consent form">Agree Consent Form </label>
                    <input type="checkbox" class="form-control bigCheckBox" ng-model="ctrl.lead.consentFormSigned" name="consentFormSigned" ng-true-value="'Y'" ng-false-value="'N'" required="!ctrl.lead.id">
                    <div ng-if="!ctrl.lead.id">
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.consentFormSigned.$error.required">This is a required field</span>
                      </div>
                    </div>
                  </div>
                </div>

				myForm.$dirty {{myForm.$dirty}}
				myForm.fileUpload.$error.required {{myForm.fileUpload.$error.required}}
                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="file">File </label>
                    <input class="col-sm-12  control-label form-control" name="fileUpload" type="file" file-model="ctrl.myFile" ng-model="ctrl.myFile"  id="myFileField"  required="!ctrl.lead.id"/>
                      <div class="has-error" ng-show="!ctrl.lead.id && myForm.$dirty && !ctrl.myFile">
                        <span ng-show="myForm.fileUpload.$error.required">This is a required field</span>
                      </div>
                  </div>
                </div>
              </div>

              <div class="row col-md-6">
                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="appointmentnotes">Notes </label>
                    <textarea name="notes" class="form-control" id="notes" ng-model="ctrl.selectedAgentLeadAppointment.notes"></textarea>
                    <textarea name="notes" disabled class="form-control" id="notes" ng-model="ctrl.selectedAgentLeadAppointment.notes"></textarea>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-sm-12 agentInfo" ng-show="ctrl.showAgentAssignment()">
          <div class="panel panel-success">
            <div class="panel-heading">Agent Assignment</div>
            <div class="panel-body">
              <div class="row col-md-6">
                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="	First Name ">Agent Name</label>
                    <select ng-model="ctrl.selectedAgentLeadAppointment.user" class="form-control" ng-options="agent.username for agent in ctrl.users  | orderBy:'username' | filter:{roles:[{role:'AGENT'}]} track by agent.username" required></select>
                  </div>
                </div>

                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="	Last Name ">Appointment </label>
                    <div class="input-group date" id="appointment" ng-model="ctrl.selectedAgentLeadAppointment.appointmentTime" date-picker>
                      <input type="text" class="form-control netto-input" ng-model="ctrl.selectedAgentLeadAppointment.appointmentTime" date-picker-input>
                      <span class="input-group-addon">
		           								<span class="glyphicon glyphicon-calendar"></span>
                      </span>
                    </div>
                  </div>
                </div>
              </div>

              <div class="row col-md-6">
                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="	Last Name ">Notes </label>
                    <textarea name="notes" class="form-control" id="notes" ng-model="ctrl.selectedAgentLeadAppointment.notes"></textarea>
                    <textarea name="notes" disabled class="form-control" id="notes" ng-model="ctrl.selectedAgentLeadAppointment.notes"></textarea>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="col-sm-12 statusInfo" ng-show="ctrl.showEventStatus()">
          <div class="panel panel-success">
            <div class="panel-heading">Status</div>
            <div class="panel-body">
            	<div class="row">
                	<div class="col-sm-6">
                  		<div class="form-group col-sm-12">
                  			<label class="control-label" for="selectbasic">Status</label>
              				<select ng-model="ctrl.lead.status" class="form-control" ng-options="status.description for status in ctrl.statuses | orderBy:'description' track by status.description" required="ctrl.lead.id"></select>
                 	 	</div>
               		</div>  
               		
               		<div class="col-sm-6">
                  		<div class="form-group col-sm-12">
                  			 <label class="control-label" for="statusNotes">Notes</label>	
                  			 <textarea name="notes" class="form-control" ></textarea>
                    		 <textarea name="notes" disabled class="form-control"></textarea>	
                  		</div>
               		</div> 
               		
               	</div>	
            
              
            </div>
          </div>
        </div>



        <div class="col-sm-12 pcpInfo" ng-show="ctrl.showStatusChangeDetails()">
          <div class="panel panel-success">
            <div class="panel-heading">PCP Details</div>
            <div class="panel-body">


              <div class="row">
                <div class="col-sm-4">
                  <div class="form-group col-sm-12">
                    <label for="PCP">PCP </label>
                    <select class="form-control" ng-model="ctrl.selectedAgentLeadAppointment.prvdr" ng-options="prvdr.name for prvdr in ctrl.providers | orderBy:'name' track by prvdr.name" required="ctrl.lead.status.id && ctrl.lead.status.id == 2"></select>
                  </div>
                </div>

                <div class="col-sm-4">
                  <div class="form-group col-sm-12">

                    <label for="effectiveDate">Effective From</label>
                    <div class="input-group date" ng-model="ctrl.selectedAgentLeadAppointment.effectiveFrom" date1-picker>
                      <input type="text" class="form-control netto-input" ng-model="ctrl.selectedAgentLeadAppointment.effectiveFrom" date-picker-input required="ctrl.lead.status.id && ctrl.lead.status.id == 2">
                      <span class="input-group-addon">
			           							<span class="glyphicon glyphicon-calendar"></span>
                      </span>
                    </div>
                  </div>
                </div>

                <div class="col-sm-4">
                  <div class="form-group col-sm-12">
                    <label for="planType">Plan Type</label>
                    <select class=" form-control" ng-model="ctrl.selectedAgentLeadAppointment.planType" ng-options="planType.description for planType in ctrl.planTypes | orderBy:'description' track by planType.description" required="ctrl.lead.status.id && ctrl.lead.status.id == 2"></select>
                  </div>
                </div>
              </div>

              <div class="row">
                <div class="col-sm-4">
                  <div class="form-group col-sm-12">
                    <label for="plan">Plan</label>
                    <select class=" form-control" ng-model="ctrl.selectedAgentLeadAppointment.leadMbrInsuranceList"  ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'description' | filter :{planType:{id:ctrl.selectedAgentLeadAppointment.planType.id}} track by insurance.name"></select>
                  </div>
                </div>

                <div class="col-sm-4">
                  <div class="form-group col-sm-12">
                    <label class="control-label" for="drappointment">Dr Appointment </label>
                    <div class="input-group" name="drappointment" ng-model="ctrl.selectedAgentLeadAppointment.drappointment" date-picker>
                      <input type="text" class="form-control netto-input" ng-model="ctrl.selectedAgentLeadAppointment.drappointment" date-picker-input required="ctrl.lead.status.id && ctrl.lead.status.id == 2">
                      <span class="input-group-addon">
			           							<span class="glyphicon glyphicon-calendar"></span>
                      </span>
                    </div>
                  </div>
                </div>

                <div class="col-sm-4">
                  <div class="form-group col-sm-12">
                    <label for="transportation">Transportation</label>
                    <input type="checkbox" class="form-control bigCheckBox" ng-model="ctrl.selectedAgentLeadAppointment.transportation" name="transportation" ng-true-value="'Y'" ng-false-value="'N'" />
                  </div>
                </div>
              </div>

            </div>

          </div>
        </div>
        
      
           myForm.$error.pattern{{myForm.$error.pattern}}


          <div class="row col-sm-12" style="padding-bottom:20px;">
            <div class="form-actions floatCenter col-sm-offset-9">
              <input type="submit" value="{{!ctrl.lead.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-sm" ng-disabled="!ctrl.myFile || ctrl.lead.consentFormSigned == 'N' || myForm.$invalid || myForm.$pristine">
              <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-sm" ng-disabled="myForm.$pristine">Reset Form</button>
            </div>
          </div>

      </form>
      </div>

    </div>


</div>

  
  <style>
  	.bigCheckBox{
  		width:30px;
  		height:12px;
  	}
  </style>