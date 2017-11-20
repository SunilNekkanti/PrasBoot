  
    
<div class="generic-container" >
   <div class="panel panel-success" ng-if="!ctrl.display">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="problem">List of Problems </span> 
               <button type="button"   ng-click="ctrl.addProblem()" ng-hide="ctrl.displayEditButton" class="btn btn-success  btn-xs custom-width floatRight"> Add </button>   
               <button type="button" ng-click="ctrl.editProblem(ctrl.problemId)" ng-show="ctrl.displayEditButton" class="btn btn-primary btn-xs custom-width floatRight">Edit</button>  
              <button type="button" ng-click="ctrl.removeProblem(ctrl.problemId)"  ng-show="ctrl.displayEditButton" class="btn btn-danger btn-xs custom-width floatRight">Remove</button>  
        </div>
        
        <div class="table-responsive">
			<div class="panel-body">
			  <div class="panel-body">
                 <div class="formcontainer">
                    <div class="col-sm-3">
              <div class="form-group col-sm-12">
                <label for="plan">Insurance</label>
                <select class=" form-control" ng-model="ctrl.insurance" ng-init="ctrl.insurance=ctrl.insurance||ctrl.insurances[0]" ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name"></select>
              </div>
            </div>

            <div class="col-sm-3">
              <div class="form-group col-sm-12">
                <label for="plan">Effective Year</label>
                <select class=" form-control" ng-model="ctrl.effectiveYear" ng-options="effectiveYear for effectiveYear in ctrl.effectiveYears"> </select>
              </div>
            </div>
            <div class=" col-sm-6">
              <button type="button" ng-click="ctrl.generate()" class="btn btn-warning btn-xs align-bottom">Generate</button>
            </div>
          </div>
        </div>
	        	<table datatable="" id="content"   dt-options="ctrl.dtOptions"  dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="table-responsive table  bordered table-striped table-condensed datatable" cellspacing="0" width="100%"></table>
          </div>
		</div>
    </div>
     
   
    <div class="panel panel-success" ng-if="ctrl.display">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="hedis">Problem</span>
                            <button type="button" ng-click="ctrl.submit()"   class="btn btn-primary btn-xs form-actions floatRight" ng-disabled="ctrl.myForm.$invalid || ctrl.myForm.$pristine"> {{!ctrl.problem.id ? 'Add' : 'Update'}}</button>
	                        <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-xs form-actions floatRight"    >Cancel</button>
	                        <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-xs form-actions floatRight"  ng-if="!ctrl.problem.id" ng-disabled="ctrl.myForm.$pristine">Reset Form</button>
        </div>
		<div class="panel-body">
	        <div class="formcontainer">
	            <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
	            <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
	            <form ng-submit="ctrl.submit()" name="ctrl.myForm" class="form-horizontal">
	                <input type="hidden" ng-model="ctrl.problem.id" />
	                
	                
	                   <div class="col-sm-12 ptInfo">
	                 
	                 
	                <div class="row">
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="uname">Effective Year</label>
	                        <div class="col-md-8">
	                         <select class=" form-control" ng-model="ctrl.effectiveYear" name="effectiveYear" ng-options="effectiveYear for effectiveYear in ctrl.effectiveYears">
	                           <option></option>
	                          </select>
	                             <div class="has-error" ng-show="ctrl.myForm.$dirty">
	                                  <span ng-show="ctrl.myForm.effectiveYear.$error.required">This is a required field</span>
                                  </div>
	                        </div>
	                    </div>
	                    
	                      <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="uname">Description</label>
	                        <div class="col-md-8">
	                            <input type="text" ng-model="ctrl.problem.description" name="description" class="username form-control input-sm" placeholder="Enter  Description" required ng-minlength="5"/>
	                             <div class="has-error" ng-show="ctrl.myForm.$dirty">
	                                  <span ng-show="ctrl.myForm.description.$error.required">This is a required field</span>
                                      <span ng-show="ctrl.myForm.description.$error.minlength">Minimum length required is 5</span>
                                      <span ng-show="ctrl.myForm.description.$invalid">This field is invalid </span>
                                  </div>
	                        </div>
	                    </div>
	               
	                  <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="uname">Insurance</label>
	                        <div class="col-md-8">
	                            <select class=" form-control" ng-model="ctrl.insurance" name="insurance" ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name"  required>
                                  <option></option>
                                </select>
                                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                                   <span ng-show="ctrl.myForm.insurance.$error.required">This is a required field</span>
                               </div>
	                        </div>
	                   </div>
	                   
	                    <div class="form-group col-md-12">
	                        <label class="col-md-2 control-lable" for="uname">ICD Codes</label>
	                        <div class="col-md-8">
	                            <select class=" form-control" ng-model="ctrl.currentProblemICDCodes"   name="icdCodes" ng-options="icd.codeAndDescription for icd in ctrl.problem.icdCodes  | orderBy:'code'"  multiple size="20" ng-required="(ctrl.problem.icdCodes.length === 0)">
                                  <option></option>
                                </select>
                                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                                   <span ng-show="ctrl.myForm.icdCodes.$error.required">This is a required field</span>
                               </div>
                            </div>
                               <div class="col-sm-2">
                                 <button type="button" class="btn btn-success btn-sm white-text"  ng-click="ctrl.open('lg')"><span
									class="glyphicon glyphicon-plus-sign"></span>ICD</button>
									
								<button type="button"  class="btn btn-success btn-sm white-text" ng-click="ctrl.removeProblemICDCodes(ctrl.currentProblemICDCodes)"> <span
										class="glyphicon glyphicon-minus-sign"></span>ICD</button>
				               </div>
	                   </div>
	                    
                  </div>
        
          
	            </form>
    	    </div>
		</div>	
    </div>
    
    <script type="text/ng-template" id="myModalContent.html">
        <div class="modal-header">
            <h3 class="modal-title">ICD Measures</h3>
        </div>
        <div class="modal-body">
       		 <table datatable="" id="content"   dt-options="dtOptions"  dt-columns="dtColumns" dt-instance="dtInstance" class="table-responsive table  bordered table-striped table-condensed datatable" cellspacing="0" width="100%"></table>
        </div>
        <div class="modal-footer">
            <button class="btn btn-primary" ng-click="ok()">OK</button>
            <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
        </div>
    </script>
    
    
</div>