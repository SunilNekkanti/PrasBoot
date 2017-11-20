<div class="generic-container">

  <div class="panel panel-success" ng-hide="ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="user">List of Events </span>
      <button type="button" ng-if="ctrl.adminOrManager()" ng-click="ctrl.addEvent()" ng-hide="ctrl.displayEditButton" class="btn btn-success btn-xs custom-width floatRight"> Add </button>
      <button type="button" ng-if="ctrl.adminOrManager()" ng-click="ctrl.editEvent(ctrl.eventId)" ng-show="ctrl.displayEditButton" class="btn btn-primary btn-xs custom-width floatRight">Edit</button>
      <button type="button" ng-if="ctrl.adminOrManager()" ng-click="ctrl.removeEvent(ctrl.eventId)" ng-show="ctrl.displayEditButton" class="btn btn-danger btn-xs custom-width floatRight">Remove</button>
    </div>
    <div class="panel-body">
      <div class="table-responsive">
        <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="table table-hover table-responsive  bordered table-striped table-condensed datatable"></table>

      </div>
    </div>
  </div>


  <div class="panel panel-success" ng-show="ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="event">Event </span>
    <button type="button"  ng-click="ctrl.addLead()" ng-show="ctrl.event.id"   class="btn btn-success  btn-sm floatRight">Add Lead</button>
    <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-sm floatRight"   >Cancel</button>
    <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-sm floatRight" ng-disabled="myForm.$pristine" ng-if="ctrl.adminOrManager() && !ctrl.event.id" >Reset Form</button>
    <input type="button" value="{{!ctrl.event.id ? 'Add' : 'Update'}}" ng-click="ctrl.submit()" class="btn btn-primary btn-sm floatRight" ng-disabled="myForm.$invalid || myForm.$pristine" ng-show="ctrl.adminOrManager()">{{!ctrl.event.id ? 'Add' : 'Update'}}</button>
            
   </div>
    <div class="panel-body">
      <div class="formcontainer">
        <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
        <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
        <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">


          <div class="panel-heading">
            <div class="form-actions floatCenter col-md-offset-4">
              
            </div>
          </div>

          <input type="hidden" ng-model="ctrl.event.id" />

          <div class="form-group col-md-6">

            <div class="row">
              <div class="form-group col-md-12">
                <label class="col-md-2  control-label" for="eventName" require>Name </label>
                <div class="col-md-10">
                  <input type="text" ng-model="ctrl.event.eventName" id="eventName" name="eventName" class="username form-control input-md" placeholder="Enter event name" required ng-minlength="5" />
                  <div class="has-error" ng-show="myForm.$dirty">
                    <span ng-show="myForm.eventName.$error.required">This is a required field</span>
                    <span ng-show="myForm.eventName.$error.minlength">Minimum length required is 5</span>
                    <span ng-show="myForm.eventName.$invalid">This field is invalid </span>
                  </div>
                </div>
              </div>
            </div>

            <div class="row">
              <div class="form-group col-md-12">
                <label class="col-md-2  control-label" for="eventDateTime">StartTime</label>
                <div class="col-md-4">
                  <div class="input-group date" id="eventDateStartTime" ng-model="ctrl.event.eventDateStartTime" date-picker>
                    <input type="text" class="form-control netto-input" ng-model="ctrl.event.eventDateStartTime" date-picker-input required>
                    <span class="input-group-addon">
           								<span class="glyphicon glyphicon-calendar"></span>
                    </span>
                  </div>
                </div>

                <label class="col-md-2  control-label" for="eventDateEndTime">EndTime</label>
                <div class="col-md-4">
                  <div class="input-group date" id="eventDateEndTime" ng-model="ctrl.event.eventDateEndTime" date-picker>
                    <input type="text" class="form-control netto-input" ng-model="ctrl.event.eventDateEndTime" date-picker-input required>
                    <span class="input-group-addon">
           								<span class="glyphicon glyphicon-calendar"></span>
                    </span>
                  </div>
                </div>
              </div>
            </div>


            <div class="row">
              <div class="form-group col-md-12">

                <label class="col-md-2  control-label" for="facilityType">Facility</label>
                <div class="col-md-4">
                  <select class="col-md-12 form-control" ng-model="ctrl.event.facilityType" name="facilityType" ng-options="facilityType.description for facilityType in ctrl.facilityTypes track by facilityType.description" required></select>
                  <div class="has-error" ng-show="myForm.$dirty">
                    <span ng-show="myForm.facilityType.$error.required">This is a required field</span>
                  </div>
                </div>

              </div>
            </div>

            <div class="row">
              <div class="form-group col-md-12">
                <div require>
                  <label class="col-md-2 control-label" for="notes">Notes</label>
                </div>
                <div class="col-md-10">
                  <textarea name="notes" style="height:320px" class="form-control" id="notes" ng-model="ctrl.event.notes"></textarea>
                </div>
              </div>
            </div>

            <div class="row fileUploadSection">
              <div class="form-group col-md-12 fileUploadRow">
                <div require>
                  <label class="col-md-2  control-label" for="contactEmail">FileUpload</label>
                </div>
                <div class="col-md-3">
                  <input class="form-control input-md" type="file" name="files" file-model="ctrl.myFiles" ng-model="ctrl.myFiles" multiple />
                </div>
              </div>
              
               <div class="form-group col-md-12 uploadedFiles" ng-if="ctrl.event.id">
                <div require>
                  <label class="col-md-2  control-label" for="contactEmail">Uploaded Files</label>
                </div>
                <div ng-repeat="attachment in ctrl.event.attachments" class="col-md-3">
						<a  class="form-control no-border" ng-click="ctrl.readUploadedFile(attachment.id, attachment.contentType)"  style="display:block;">
          					<span class="glyphicon glyphicon-file"></span>
        				</a>
                </div>
              </div>
              
              
            </div>
          </div>
           <div class="form-group col-sm-6 cntInfo">
            <div class="panel panel-success">
              <div class="panel-heading">Contact Info</div>
              <div class="panel-body">

                <div class="row">
                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="address1">Address 1</label>
                      <input type="text" ng-model="ctrl.event.contact.address1" id="address1" name="address1" class="username form-control input-sm" placeholder="Enter Address" ng-required="!ctrl.event.contact.homePhone" ng-minlength="6" ng-maxlength="100" />
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
                      <input type="text" ng-model="ctrl.event.contact.address2" id="addres2" class="username form-control input-sm" placeholder="Enter Address" ng-maxlength="10" />
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
                        <input type="text" ng-model="ctrl.event.contact.city" id="city" name="city" class="username form-control" placeholder="Enter City" ng-required="!ctrl.event.homePhone" ng-minlength="4" ng-maxlength="100" />
                        <span class="input-group-addon">-</span>
                        <select ng-model="ctrl.event.contact.stateCode" class="form-control" name="state" ng-options="state.description for state in ctrl.states | orderBy:'description' track by state.description" ng-required="!ctrl.event.homePhone"></select>
                        <span class="input-group-addon">-</span>
                        <select ng-model="ctrl.event.contact.zipCode" name="zipcode" class="form-control" ng-options="zipCode.code for zipCode in ctrl.event.contact.stateCode.zipCodes | orderBy:'code'  track by zipCode.code" ng-required="!ctrl.event.homePhone"></select>

                      </div>
                    </div>
                  </div>

                </div>


                <div class="row">
                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="homePhone">Phone</label>
                      <input type="text" ng-model="ctrl.event.contact.homePhone" id="homePhone" name="homePhone" class="username form-control input-sm" placeholder="Enter Home phone" ng-required="!ctrl.event.address1"  ng-minlength="10" phone-input />
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.homePhone.$error.required">This is a required field</span>
                        <span ng-show="myForm.homePhone.$error.minlength">Minimum length required is 10</span>
                      </div>
                    </div>
                  </div>

                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="mobilePhone">Mobile Phone</label>
                      <input type="text" ng-model="ctrl.event.contact.mobilePhone" id="mobilePhone" name="mobilePhone" class="username form-control input-sm" placeholder="Enter Mobile phone" ng-minlength="10" phone-input  />
                        <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.mobilePhone.$error.minlength">Minimum length required is 10</span>
                      </div>
                    </div>
                  </div>

                </div>

                <div class="row">
                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="bestTimeToCall">Contact Person</label>
                      <input type="text" class="form-control netto-input" name="contactPerson" ng-model="ctrl.event.contact.contactPerson" ng-required="true" ng-minlength="3" >
                      <div class="has-error" ng-show="myForm.$dirty">
                        <span ng-show="myForm.contactPerson.$error.required">This is a required field</span>
                        <span ng-show="myForm.contactPerson.$error.minlength">Minimum length required is 3</span>
                        <span ng-show="myForm.contactPerson.$invalid">This field is invalid </span>
                      </div>
                    </div>
                  </div>

                  <div class="col-sm-6">
                    <div class="form-group col-sm-12">
                      <label for="email" require>Email </label>
                      <input type="email" ng-model="ctrl.event.contact.email" id="email" name="email" class="username form-control input-sm" placeholder="Enter email"  ng-minlength="8" />
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

      


        </form>
      </div>
    </div>
  </div>
</div>