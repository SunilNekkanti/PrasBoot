<div class="generic-container" ng-cloak >
  <div class="panel panel-success" ng-if="!ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="user">List of Leads </span>
      <button type="button" ng-click="ctrl.addLead()" ng-if="(ctrl.loginUserRole !== 'CARE_COORDINATOR')" ng-hide="ctrl.displayEditButton || ctrl.loginUserRole !== 'AGENT'" class="btn btn-success  btn-xs  custom-width floatRight"> Add </button>
      <button type="button" ng-click="ctrl.editLead(ctrl.leadId)" ng-show="ctrl.displayEditButton" class="btn btn-primary btn-xs custom-width floatRight">Edit</button>
      <button type="button" ng-click="ctrl.removeLead(ctrl.leadId)" ng-show="ctrl.displayEditButton" class="btn btn-danger btn-xs  custom-width floatRight">Remove</button>
    </div>


    <div class="panel-body" >
      <div class="table-responsive" >
           <tabset ng-if="(ctrl.loginUserRole === 'CARE_COORDINATOR')" >
           		<tab heading="ConvertedToBeScheduled">
	      		    <table datatable="" id="content" dt-options="ctrl.dtOptionsToBeScheduled" dt-columns="ctrl.dtColumnsToBeScheduled" dt-instance="ctrl.dtInstanceToBeScheduled" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover " cellspacing="0" width="100%"></table>
	      		</tab>
	            <tab heading="InactiveButScheduled">
	      			<table datatable="" id="content" dt-options="ctrl.dtOptionsScheduledInactive" dt-columns="ctrl.dtColumnsScheduledInactive" dt-instance="ctrl.dtInstanceScheduledInactive" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover " cellspacing="0" width="100%"></table>
	      		</tab>
	            <tab heading="ActiveButNotEngaged">
	       		    <table datatable="" id="content" dt-options="ctrl.dtOptionsActiveButNotEngaged" dt-columns="ctrl.dtColumnsActiveButNotEngaged" dt-instance="ctrl.dtInstanceActiveButNotEngaged" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover " cellspacing="0" width="100%"></table>
	        	</tab>
       		</tabset>

           <table ng-if="ctrl.loginUserRole !== 'CARE_COORDINATOR'"  datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover " cellspacing="0" width="100%"></table>
      </div>
    </div>
  </div>

  <div class="panel panel-success" ng-if="ctrl.display">
    <div class="panel-heading">List Details </div>
    <div class="panel-body">
     <div class="formcontainer">
     <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
     <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
      <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
        <input type="hidden" ng-model="ctrl.lead.id" />

        <div class="col-sm-6 ptInfo">
          <div class="panel panel-success">
            <div class="panel-heading">Lead Info</div>
            <div class="panel-body">
              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="firstName"> First Name </label>
                    <input type="text" ng-model="ctrl.lead.firstName" id="firstName" name="firstName" class="username form-control input-sm" placeholder="Enter firstname" required ng-minlength="2" />
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.firstName.$error.required">This is a required field</span>
                      <span ng-show="myForm.firstName.$error.minlength">Minimum length required is 2</span>
                      <span ng-show="myForm.firstName.$invalid">This field is invalid </span>
                    </div>
                  </div>
                </div>
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="lastName">Last Name</label>
                    <input type="text" ng-model="ctrl.lead.lastName" id="lastName" name="lastName" class="username  form-control input-sm" placeholder="Enter last name" required ng-minlength="2" />
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.lastName.$error.required">This is a required field</span>
                      <span ng-show="myForm.lastName.$error.minlength">Minimum length required is 2</span>
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
                <div class="col-sm-6" >
                  <div class="form-group col-sm-12">
                    <label for="currentPlan">Current Plan</label>
                    <input type="text" ng-model="ctrl.lead.initialInsurance"   name="initialInsurance" class="username  form-control input-sm" placeholder="Enter Present Insurance"  ng-minlength="3" />
                    <div class="has-error" ng-show="myForm.$dirty">
                   	 <span ng-show="myForm.initialInsurance.$error.minlength">Minimum length required is 3</span>
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


                <div class="col-sm-6" ng-if="ctrl.lead.id">
                  <div class="form-group col-sm-12">
                    <label for="language">File</label>
                    	<a  ng-click="ctrl.readUploadedFile()" style="display:block;">
          					<span class="glyphicon glyphicon-file">{{ctrl.lead.fileUpload.fileName}}</span>
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
                    <input type="text" ng-model="ctrl.lead.contact.address1" id="address1" name="address1" class="username form-control input-sm" placeholder="Enter Address" ng-required="!ctrl.lead.contact.homePhone" ng-minlength="6" ng-maxlength="100" />
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
                    <input type="text" ng-model="ctrl.lead.contact.address2" id="addres2" class="username form-control input-sm" placeholder="Enter Address" ng-maxlength="10" />
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
                      <input type="text" ng-model="ctrl.lead.contact.city" id="city" name="city" class="username form-control" placeholder="Enter City" ng-required="!ctrl.lead.contact.homePhone" ng-minlength="4" ng-maxlength="100" />
                      <span class="input-group-addon">-</span>
                      <select ng-model="ctrl.lead.contact.stateCode" class="form-control" name="state" ng-options="state.description for state in ctrl.states | orderBy:'description' track by state.description" ng-required="!ctrl.lead.contact.homePhone"></select>
                      <span class="input-group-addon">-</span>
                      <select ng-model="ctrl.lead.contact.zipCode" name="zipcode" class="form-control" ng-options="zipCode.code for zipCode in ctrl.lead.contact.stateCode.zipCodes | orderBy:'code'  track by zipCode.code" ng-required="!ctrl.lead.contact.homePhone"></select>

                    </div>
                  </div>
                </div>

              </div>


              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="homePhone">Home Phone</label>
                    <input type="text" ng-model="ctrl.lead.contact.homePhone" id="homePhone" name="homePhone" class="username form-control input-sm" placeholder="Enter Home phone" ng-required="!ctrl.lead.contact.address1" phone-input ng-minlength="10" />
                    <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.homePhone.$error.required">This is a required field</span>
                    </div>
                  </div>
                </div>

                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="mobilePhone">Mobile Phone</label>
                    <input type="text" ng-model="ctrl.lead.contact.mobilePhone" id="mobilePhone" class="username form-control input-sm" placeholder="Enter Mobile phone" phone-input ng-minlength="10" />
                  </div>
                </div>

              </div>

              <div class="row">
                <div class="col-sm-6">
                  <div class="form-group col-sm-12">
                    <label for="bestTimeToCall">Best Time to Call</label>
                     <select ng-model="ctrl.lead.bestTimeToCall" class="form-control" name="bestTimeToCall" ng-options="bestTimeToCall.description for bestTimeToCall in ctrl.bestTimeToCalls track by bestTimeToCall.description" required></select>
                      <div class="has-error" ng-show="myForm.$dirty">
                      <span ng-show="myForm.bestTimeToCall.$error.required">This is a required field</span>
                    </div>
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

        <div class="col-sm-12 additionalInfo"  >
          <div class="panel panel-success">
            <div class="panel-heading">Additional Details</div>
            <div class="panel-body">
              <div class="row col-md-6">
                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="agree consent form">Agree Consent Form </label>
                    <input type="checkbox" class="form-control bigCheckBox" ng-model="ctrl.consentFormSigned" name="consentFormSigned" ng-true-value="'Y'" ng-false-value="'N'" >
                  </div>
                </div>

                <div class="col-sm-12" >
                  <div class="form-group col-sm-12">
                    <label for="file">File </label>
                    <input class="col-sm-12  control-label form-control" name="fileUpload" type="file" file-model="ctrl.myFile"  ng-model="ctrl.myFile" id="myFileField"  ng-required="ctrl.consentFormSigned==='Y'"/>
                      <div class="has-error" ng-show="ctrl.consentFormSigned==='Y' && myForm.$dirty && !ctrl.myFile">
                        <span ng-show="myForm.fileUpload.$error.required">This is a required field</span>
                      </div>
                  </div>
                </div>
              </div>
              <div class="row col-md-6" ng-if="(ctrl.loginUserRole == 'EVENT_COORDINATOR'  || ctrl.lead.agentLeadAppointmentList.length == 0)">
                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="leadcreation">Notes </label>
                    <textarea name="leadcreationnotes" class="form-control" id="notes" ng-model="ctrl.notes"></textarea>
                    <textarea name="leadcreationnoteshistory" disabled class="form-control" id="notes" ng-model="ctrl.lead.notesHistory"></textarea>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
    <div class="col-sm-12 agentInfo" ng-if="ctrl.showAgentAssignment()" ng-show="(ctrl.lead.status && ctrl.lead.status.description == 'Agent')||ctrl.lead.agentLeadAppointmentList.length > 0 ">
          <div class="panel panel-success">
            <div class="panel-heading">Agent Assignment</div>
            <div class="panel-body">
              <div class="row col-md-6">
                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="	First Name ">Agent Name</label>
                    <select ng-model="ctrl.selectedAgentLeadAppointment.user" class="form-control" ng-options="agent.name for agent in ctrl.users  | filter:{role:'AGENT'} | orderBy:'name'  track by agent.name" required></select>
                  </div>
                </div>

                <div class="col-sm-12">
                  <div class="form-group col-sm-12">
                    <label for="appointmentnotes">Appointment </label>
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
                    <textarea name="notes" class="form-control" id="notes" ng-model="ctrl.notes"></textarea>
                    <textarea name="notes" disabled class="form-control" id="notes" ng-model="ctrl.lead.notesHistory"></textarea>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>


        <div class="col-sm-12 statusInfo" >
          <div class="panel panel-success">
            <div class="panel-heading">Status</div>
            <div class="panel-body">
            	<div class="row">
                	<div class="col-sm-3">
                  		<div class="form-group col-sm-12">
                  			<label class="control-label" for="selectbasic">Status</label>
              				<select ng-model="ctrl.lead.status"  ng-init="ctrl.lead.status = ctrl.lead.status||ctrl.statuses[0]" class="form-control" ng-options="status.description for status in ctrl.statuses | orderBy:'description' track by status.description" required="ctrl.lead.id" ng-required></select>
                 	 	</div>
               		</div>

               		<!-- div class="col-sm-3">
                  		<div class="form-group col-sm-12">
                  			<label class="control-label" for="selectbasic">Status Detail</label>
              				<select ng-model="ctrl.lead.statusDetail" class="form-control" ng-options="status.description for status in ctrl.statusDetails | filter:{leadStatus:{id: ctrl.lead.status.id}} | orderBy:'description' track by status.description" required="ctrl.lead.id"></select>
                 	 	</div>
               		</div -->

               		<div class="col-sm-6" ng-if="ctrl.showStatusNotes()">
                  		<div class="form-group col-sm-12">
                  			 <label class="control-label" for="statusNotes">Notes</label>
                  			 <textarea name="notes" class="form-control" id="notes" ng-model="ctrl.notes" ></textarea>
                    		 <textarea name="notes" disabled class="form-control" id="notes" ng-model="ctrl.lead.notesHistory"></textarea>
                  		</div>
               		</div>

               	</div>


            </div>
          </div>
        </div>


        <div class="col-sm-12 pcpInfo" ng-if="ctrl.showStatusChangeDetails()">
          <div class="panel panel-success">
            <div class="panel-heading">PCP Details</div>
            <div class="panel-body">


              <div class="row">
                <div class="col-sm-4">
                  <div class="form-group col-sm-12">
                    <label for="PCP">PCP </label>
                    <select class="form-control" ng-model="ctrl.lead.prvdr" ng-options="prvdr.name for prvdr in ctrl.providers | orderBy:'name' track by prvdr.name" required="ctrl.lead.status.id && ctrl.lead.status.id == 2"></select>
                  </div>
                </div>

                <div class="col-sm-2">
                  <div class="form-group ">

                    <label for="effectiveDate">Effective From</label>
                    <div class="input-group input-append date " ng-model="ctrl.lead.effectiveFrom" date2-picker>
                      <input type="text"  class="form-control col-sm-12 netto-input" ng-model="ctrl.lead.effectiveFrom" date-picker-input required="ctrl.lead.status.id && ctrl.lead.status.id == 2">
                      <span class="input-group-addon">
			           							<span class="glyphicon glyphicon-calendar"></span>
                      </span>
                    </div>
                  </div>
                </div>
                <div class="col-sm-4">
                  <div class="form-group col-sm-12">
                    <label for="plan">Plan</label>
                      <div ng-if="(ctrl.loggedUserInsId == 0)">
                      <select class=" form-control" ng-model="ctrl.lead.insurance"  ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'description' track by insurance.name"></select>
                      </div>
                      <div  ng-if="(ctrl.loggedUserInsId != 0)">
                      <select class=" form-control" ng-model="ctrl.lead.insurance" ng-init="ctrl.lead.insurance = ctrl.lead.insurance||(ctrl.insurances | orderBy:'description' | filter :{id:ctrl.loggedUserInsId})[0]"  ng-options="insurance.name for insurance in ctrl.insurances  | filter :{id:ctrl.loggedUserInsId} | orderBy:'description' track by insurance.name"></select>
                      </div>

                  </div>
                </div>


              </div>

            </div>

          </div>
        </div>
            <span style="display:hidden" ng-show="flase">{{ ctrl.invalid = myForm.$inValid}}</span>
            <span style="display:hidden" ng-show="flase">{{ ctrl.pristine = myForm.$pristine}}</span>
          <div class="row col-sm-12" style="padding-bottom:20px;">
            <div class="form-actions floatCenter col-sm-offset-9">
              <input type="submit" value="{{!ctrl.lead.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-sm" ng-disabled="ctrl.showAddorUpdateButton()"/>
              <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-sm"   >Cancel</button>
              <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-sm"  ng-if="!ctrl.lead.id"  ng-disabled="myForm.$pristine">Reset Form</button>
            </div>
          </div>

      </form>
      </div>
    </div>
   </div>



 <script type="text/ng-template" id="myModalContent.html">
    <div class="modal-header">
      <h3 class="modal-title">Lead Membership Flags</h3>
    </div>
    <div class="modal-body">

          <div class="panel panel-success">
            <div class="panel-heading">Lead Membership Flags</div>
            <div class="panel-body">
              <div class="row">
                <div class="col-md-2">
                  <div class="form-group col-md-12">
                    <label for="agree consent form">Scheduled</label>
                    <input type="checkbox" class="form-control  col-sm-2" ng-change="onScheduledFlagCheckBoxChange()" ng-model="leadMembership.leadMembershipFlag.scheduledFlag" name="scheduledFlag" ng-true-value="'Y'" ng-false-value="'N'" >
                  </div>
                </div>


                <div class="col-sm-4">
                  <div class="form-group col-sm-12">
                    <label for="dob">Scheduled Date</label>
                    <div class="input-group date" id="scheduledDate"  ng-model="leadMembership.leadMembershipFlag.scheduledDate" date1-picker>
                      <input type="text" class="form-control netto-input" ng-model="leadMembership.leadMembershipFlag.scheduledDate" date-picker-input>
                      <span class="input-group-addon">
                              <span class="glyphicon glyphicon-calendar"></span>
                      </span>
                    </div>
                  </div>
                </div>
      </div>
      <div class="row">
                <div class="col-md-2">
                  <div class="form-group col-md-12">
                    <label for="agree consent form">Engaged</label>
                    <input type="checkbox" class="form-control  col-sm-2" ng-change="onEngagedFlagCheckBoxChange()" ng-model="leadMembership.leadMembershipFlag.engagedFlag" name="engagedFlag" ng-true-value="'Y'" ng-false-value="'N'" >
                  </div>
                </div>

              <div class="col-sm-4">
                <div class="form-group col-sm-12">
                  <label for="dob">Engaged Date</label>
                  <div class="input-group date" id="engagedDate" ng-model="leadMembership.leadMembershipFlag.engagedDate" date1-picker>
                    <input type="text" class="form-control netto-input" ng-model="leadMembership.leadMembershipFlag.engagedDate" date-picker-input>
                    <span class="input-group-addon">
                            <span class="glyphicon glyphicon-calendar"></span>
                    </span>
                  </div>
                </div>
              </div>
            </div>
   			  <div class="col-sm-11">
              <div class="form-group col-sm-12">
                <label for="plan">Notes</label>
                <textarea ng-model="notes" required rows="4" cols="100" ></textarea>
              </div>
            </div>


            </div>

        </div>

    </div>
    <div class="modal-footer">
     <div class="formcontainer">

            <div class="col-sm-11">
              <div class="form-group col-sm-12">
                <label for="plan">History</label>
                <textarea ng-model="leadMembership.notesHistory" rows="6" cols="100" ng-disabled="true"></textarea>
              </div>
            </div>
            <div class="col-sm-4 col-sm-offset-7">
             <button class="btn btn-success" ng-click="ok()" ng-disabled="disableCheck()">Submit</button>
              <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
            </div>
          </div>

    </div>
  </script>


</div>


  <style>
  	.bigCheckBox{
  		width:30px;
  		height:12px;
  	}
  </style>
