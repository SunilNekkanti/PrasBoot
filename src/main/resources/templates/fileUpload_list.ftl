<div class="generic-container">

  <div class="panel panel-success">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="facilityType">File Upload </span></div>
    <div class="panel-body">
      <div class="formcontainer">
        <div class="alert alert-success" facilityType="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
        <div class="alert alert-danger" facilityType="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
         <div class="alert alert-primary" facilityType="alert" ng-if="ctrl.warningMessage">{{ctrl.errorMessage}}</div>
        <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
          <input type="hidden" ng-model="ctrl.facilityType.id" />
          <div class="row">
            <div class="col-sm-12">
              <div class="form-group  col-sm-2 ">
                <label for="plan">Insurance</label>
                <select class=" form-control" ng-model="ctrl.insurance" ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name"></select>
              </div>
               <div class="form-group  col-sm-1 "> </div>
              <div class="form-group  col-sm-2" ng-if="ctrl.insurance.id">
                <label for="plan">File Type</label>
                <select class=" form-control" ng-model="ctrl.fileType" ng-options="fileType.description for fileType in ctrl.fileTypes | filter:{ins:{id:ctrl.insurance.id }} | orderBy:'description' track by fileType.description"></select>
              </div>
               <div class="form-group  col-sm-1 "> </div>
              <div class="form-group col-md-2 " ng-if="ctrl.fileType.activityMonthInd ==='Y'">
                <label class="control-lable" for="uname">Activity Month</label>
                <div class="input-group date" id="dob" ng-model="ctrl.activityMonth" date1-picker>
                  <input type="text" class="form-control netto-input" ng-model="ctrl.activityMonth" date-picker-input>
                  <span class="input-group-addon">
						           							<span class="glyphicon glyphicon-calendar"></span>
                  </span>
                </div>
              </div>
 				<div class="form-group  col-sm-1 "> </div>
              <div class="form-group col-sm-3  col-sm-offset-1" ng-if="(ctrl.fileType.activityMonthInd ==='Y' && ctrl.activityMonth) ||  (ctrl.fileType.activityMonthInd === 'N' && ctrl.fileType.description)">
                <label for="file">{{ctrl.fileType.description}} </label>
                <input class="form-control  control-label form-control" name="fileUpload" type="file" file-model="ctrl.myFile" ng-model="ctrl.myFile" id="myFileField" />
              </div>
            </div>
          </div>

          <div class="row">
            <div class="form-actions floatCenter col-md-offset-8">
             <button type="button" ng-click="ctrl.uploadFile()"   ng-show="ctrl.myFile && ctrl.displayUploadButton" class="btn btn-primary btn-sm" ng-disabled="myForm.$invalid || myForm.$pristine">Upload</button>
              <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-sm" ng-show="ctrl.insurance.id" ng-disabled="myForm.$pristine">Reset Form</button>
              <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-sm" ng-show="ctrl.facilityType.id">Cancel</button>
            </div>
          </div>
        </form>
      </div>
    </div>
  </div>


  <div class="panel panel-success">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="file">List of Files Uploaded </span>
    </div>
    <div class="table-responsive">
      <div class="panel-body">
        <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
        cellspacing="0" width="100%"></table>
      </div>
    </div>
  </div>


</div>