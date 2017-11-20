<div class="generic-container">
   <div class="panel panel-success" ng-if="!ctrl.display">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="fileType">File Type List </span> 
               <button type="button"  ng-click="ctrl.addFileType()" ng-hide="ctrl.displayEditButton" class="btn btn-success btn-xs custom-width floatRight"> Add </button>   
               <button type="button" ng-click="ctrl.editFileType(ctrl.fileTypeId)" ng-show="ctrl.displayEditButton" class="btn btn-primary btn-xs custom-width floatRight">Edit</button>  
              <button type="button" ng-click="ctrl.removeFileType(ctrl.fileTypeId)"  ng-show="ctrl.displayEditButton" class="btn btn-danger btn-xs custom-width floatRight">Remove</button>  
        </div>
        <div class="table-responsive">
			<div class="panel-body">
	        	<table datatable="" id="content"   dt-options="ctrl.dtOptions"  dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover " cellspacing="0" width="100%"></table>
            </div>
		</div>
    </div>
    

    <div class="panel panel-success" ng-if="ctrl.display">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="fileType">File Type </span></div>
		<div class="panel-body">
	        <div class="formcontainer">
	            <div class="alert alert-success" fileType="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
	            <div class="alert alert-danger" fileType="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
	            <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
	                <input type="hidden" ng-model="ctrl.fileType.id" />
	                <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 col-md-offset-4 control-lable" for="uname">File Type</label>
	                        <div class="col-md-3">
	                            <input type="text" ng-model="ctrl.fileType.description" id="uname" class="username form-control input-sm" placeholder="Enter File Type" required ng-minlength="4"/>
	                        </div>
	                    </div>
	                    
	                     <div class="form-group col-sm-12">
                            <label class="col-md-2 col-md-offset-4 control-lable" for="insurance">Insurance</label>
                            <div class="col-md-3">
	                            <select class=" form-control" ng-model="ctrl.fileType.ins" name="insurance" ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name" required>
	                              <option> </option>
	                            </select>
	                            <div class="has-error" ng-show="ctrl.myForm.$dirty">
					                  <span ng-show="ctrl.myForm.insurance.$error.required">This is a required field</span>
					             </div>
					         </div>    
                          </div>
                          
                           <div class="form-group col-md-12">
				              <label class="col-md-2 col-md-offset-4 control-lable" for="uname">Activity Month Ind</label>
				              <div class="col-md-6">
				                <label>
				                  <input type="radio" ng-model="ctrl.fileType.activityMonthInd" data-ng-click="ctrl.fileType.activityMonthInd='Y'" name="activityMonthInd" value="Y">Yes </label>
				                <label>
				                  <input type="radio" ng-model="ctrl.fileType.activityMonthInd" data-ng-click="ctrl.fileType.activityMonthInd='N'" name="activityMonthInd" value="N">No </label>
				                <div class="has-error" ng-show="ctrl.myForm.$dirty">
				                  <span ng-show="ctrl.myForm.activityMonthInd.$error.required">This is a required field</span>
				                </div>
				              </div>
				            </div>
				            
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 col-md-offset-4 control-lable" for="uname">Insurance Code</label>
	                        <div class="col-md-3">
	                            <input type="text" ng-model="ctrl.fileType.insuranceCode" id="uname" class="insuranceCode form-control input-sm" placeholder="Enter Insurance Code" required ng-minlength="2"/>
	                        </div>
	                    </div>
	                    
	                </div>

	                <div class="row">
	                    <div class="form-actions floatCenter col-md-offset-8">
	                        <input type="submit"  value="{{!ctrl.fileType.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-xs" ng-disabled="myForm.$invalid || myForm.$pristine">
	                        <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-xs" ng-show="!ctrl.fileType.id" ng-disabled="myForm.$pristine">Reset Form</button>
	                        <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-xs" ng-show="ctrl.fileType.id" >Cancel</button>
	                    </div>
	                </div>
	            </form>
    	    </div>
		</div>	
    </div>
    
 
</div>