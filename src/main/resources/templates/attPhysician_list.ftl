<div class="generic-container">
   <div class="panel panel-success" ng-if="!ctrl.display">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="attPhysician">ATTENTIVE Physician </span> 
               <button type="button"  ng-click="ctrl.addattPhysician()" ng-hide="ctrl.displayEditButton" class="btn btn-success btn-xs custom-width floatRight"> Add </button>   
               <button type="button" ng-click="ctrl.editattPhysician(ctrl.attPhysicianId)" ng-show="ctrl.displayEditButton" class="btn btn-primary btn-xs custom-width floatRight">Edit</button>  
              <button type="button" ng-click="ctrl.removeattPhysician(ctrl.attPhysicianId)"  ng-show="ctrl.displayEditButton" class="btn btn-danger btn-xs custom-width floatRight">Remove</button>  
        </div>
        <div class="table-responsive">
			<div class="panel-body">
	        	<table datatable="" id="content"   dt-options="ctrl.dtOptions"  dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"  cellspacing="0" width="100%" ></table>
            </div>
		</div>
    </div>
    

    <div class="panel panel-success" ng-if="ctrl.display">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="attPhysician">ATTENTIVE Physician</span></div>
		<div class="panel-body">
	        <div class="formcontainer">
	            <div class="alert alert-success" attPhysician="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
	            <div class="alert alert-danger" attPhysician="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
	            <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
	                <input type="hidden" ng-model="ctrl.attPhysician.id" />
	                <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 col-md-offset-4 control-lable" for="uname">name</label>
	                        <div class="col-md-3">
	                            <input type="text" ng-model="ctrl.attPhysician.name" id="uname" class="username form-control input-sm" placeholder="Enter Name" required ng-minlength="3"/>
	                        </div>
	                    </div>
	                </div>

	                 <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 col-md-offset-4 control-lable" for="uname">code</label>
	                        <div class="col-md-3">
	                            <input type="text" ng-model="ctrl.attPhysician.code" id="uname" class="username form-control input-sm" placeholder="Enter Code" required ng-minlength="2"/>
	                        </div>
	                    </div>
	                </div>
	                
	                <div class="row">
	                    <div class="form-actions floatCenter col-md-offset-8">
	                        <input type="submit"  value="{{!ctrl.attPhysician.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-xs" ng-disabled="myForm.$invalid || myForm.$pristine">
	                        <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-xs" ng-show="!ctrl.attPhysician.id" ng-disabled="myForm.$pristine">Reset Form</button>
	                        <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-xs" ng-show="ctrl.attPhysician.id" >Cancel</button>
	                    </div>
	                </div>
	            </form>
    	    </div>
		</div>	
    </div>
    
 
</div>