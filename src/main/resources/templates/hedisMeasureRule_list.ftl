<div class="generic-container">
  <div class="panel panel-success" ng-if="!ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="hedisMeasureRule">List of HedisMeasureRules </span>
      <button type="button" ng-click="ctrl.addHedisMeasureRule()" ng-hide="ctrl.displayEditButton" class="btn btn-success  btn-xs custom-width floatRight"> Add </button>
      <button type="button" ng-click="ctrl.editHedisMeasureRule(ctrl.hedisMeasureRuleId)" ng-show="ctrl.displayEditButton" class="btn btn-primary btn-xs custom-width floatRight">Edit</button>
      <button type="button" ng-click="ctrl.removeHedisMeasureRule(ctrl.hedisMeasureRuleId)" ng-show="ctrl.displayEditButton" class="btn btn-danger btn-xs custom-width floatRight">Remove</button>
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
              <div class="form-group col-sm-12 required">
                <label for="plan">Effective Year(YYYY)</label>
                <select class=" form-control" ng-model="ctrl.effectiveYear" ng-options="effectiveYear for effectiveYear in ctrl.effectiveYears track by effectiveYear"> </select>
              </div>
            </div>
            <div class=" col-sm-6">
              <button type="button" ng-click="ctrl.generate()" class="btn btn-warning btn-xs align-bottom">Generate</button>
            </div>
          </div>
        </div>
        <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="display table-responsive  table table-striped table-hover rowClick" cellspacing="0" width="100%"></table>
      </div>
    </div>
  </div>


  <div class="panel panel-success" ng-if="ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="hedis">HedisMeasureRule</span>
      <button type="button" ng-click="ctrl.submit()" class="btn btn-primary btn-xs form-actions floatRight" ng-disabled="ctrl.myForm.$invalid || ctrl.myForm.$pristine"> {{!ctrl.hedisMeasureRule.id ? 'Add' : 'Update'}}</button>
      <button type="button" ng-click="ctrl.cancelEdit()" class="btn btn-warning btn-xs form-actions floatRight">Cancel</button>
      <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-xs form-actions floatRight" ng-if="!ctrl.hedisMeasureRule.id" ng-disabled="ctrl.myForm.$pristine">Reset Form</button>
    </div>
    <div class="panel-body">
      <div class="formcontainer">
        <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
        <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
        <form ng-submit="ctrl.submit()" name="ctrl.myForm" class="form-horizontal">
          <input type="hidden" ng-model="ctrl.hedisMeasureRule.id" />


          <div class="col-sm-6 ptInfo">


            <div class="form-group col-md-12 ">
              <label class="col-md-4 control-lable  drequired" for="uname">Effective Year(YYYY)</label>
              <div class="col-md-8">
                <select class=" form-control" ng-model="ctrl.effectiveYear" name="effectiveYear" ng-options="effectiveYear for effectiveYear in ctrl.effectiveYears track by effectiveYear">
                  <option></option>
                </select>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.effectiveYear.$error.required">This is a required field</span>
                </div>
              </div>
            </div>


            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">Hedis Code</label>
              <div class="col-md-8">
                <select class=" form-control" ng-model="ctrl.hedisMeasureRule.hedisMeasure" name="hedisMeasure" ng-options="hedisMeasure.codeAndDescription for hedisMeasure in ctrl.hedisMeasures | orderBy:'code' track by hedisMeasure.id ">
                  <option></option>
                </select>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.hedisMeasure.$error.required">This is a required field</span>
                </div>
              </div>
            </div>

            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">Description</label>
              <div class="col-md-8">
                <input type="text" ng-model="ctrl.hedisMeasureRule.description" name="description" class="username form-control input-sm" placeholder="Enter  Description" required ng-minlength="5" />
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.description.$error.required">This is a required field</span>
                  <span ng-show="ctrl.myForm.description.$error.minlength">Minimum length required is 5</span>
                  <span ng-show="ctrl.myForm.description.$invalid">This field is invalid </span>
                </div>
              </div>
            </div>

            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">Short Description</label>
              <div class="col-md-8">
                <input type="text" ng-model="ctrl.hedisMeasureRule.shortDescription" name="shortDescription" class="username form-control input-sm" placeholder="Enter Short  Description" required ng-minlength="2" />
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.shortDescription.$error.required">This is a required field</span>
                  <span ng-show="ctrl.myForm.shortDescription.$error.minlength">Minimum length required is 2</span>
                  <span ng-show="ctrl.myForm.shortDescription.$invalid">This field is invalid </span>
                </div>
              </div>
            </div>
            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">Insurance</label>
              <div class="col-md-8">
                <select class=" form-control" ng-model="ctrl.insurance" name="insurance" ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name" required>
                  <option></option>
                </select>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.insurance.$error.required">This is a required field</span>
                </div>
              </div>
            </div>

            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">Problem Flag</label>
              <div class="col-md-8">
                <label>
                  <input type="radio" ng-model="ctrl.hedisMeasureRule.problemFlag" data-ng-click="ctrl.hedisMeasureRule.problemFlag='Y'" name="problemFlag" value="Y">Yes </label>
                <label>
                  <input type="radio" ng-model="ctrl.hedisMeasureRule.problemFlag" data-ng-click="ctrl.hedisMeasureRule.problemFlag='N'" name="problemFlag" value="N">No </label>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.problemFlag.$error.required">This is a required field</span>
                </div>
              </div>
            </div>



            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">CPT / ICD / Medication</label>
              <div class="col-md-8">
                <label>
                  <input type="radio" ng-model="ctrl.hedisMeasureRule.cptOrIcd" data-ng-click="ctrl.hedisMeasureRule.cptOrIcd=0" name="cptOrIcd" value="0">CPT </label>
                <label>
                  <input type="radio" ng-model="ctrl.hedisMeasureRule.cptOrIcd" data-ng-click="ctrl.hedisMeasureRule.cptOrIcd=1" name="cptOrIcd" value="1">ICD </label>
                <label>
                  <input type="radio" ng-model="ctrl.hedisMeasureRule.cptOrIcd" data-ng-click="ctrl.hedisMeasureRule.cptOrIcd=2" name="cptOrIcd" value="2">CPT or ICD </label>
                <label>
                  <input type="radio" ng-model="ctrl.hedisMeasureRule.cptOrIcd" data-ng-click="ctrl.hedisMeasureRule.cptOrIcd=3" name="cptOrIcd" value="3">Medication </label>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.cptOrIcd.$error.required">This is a required field</span>
                </div>
              </div>
            </div>

            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">Frequency</label>
              <div class="col-md-8">
                <select class=" form-control" ng-model="ctrl.hedisMeasureRule.frequencyType" name="frequencyType" ng-options="frequencyType.description for frequencyType in ctrl.frequencyTypes  | orderBy:'shortName' track by frequencyType.id">
                  <option></option>
                </select>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.frequencyType.$error.required">This is a required field</span>
                </div>
              </div>
            </div>

            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">Gender</label>
              <div class="col-md-8">
                <select class=" form-control" ng-model="ctrl.hedisMeasureRule.genderId" name="gender" ng-options="gender.description for gender in ctrl.genders  | orderBy:'description' track by gender.id">
                  <option></option>
                </select>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.gender.$error.required">This is a required field</span>
                </div>
              </div>
            </div>

            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">Lower Age Limit</label>
              <div class="col-md-8">
                <input type="text" ng-model="ctrl.hedisMeasureRule.lowerAgeLimit" name="lowerAgeLimit" class="username form-control input-sm" placeholder="Enter Lower Age Limit" ng-minlength="1" />
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.lowerAgeLimit.$error.required">This is a required field</span>
                  <span ng-show="ctrl.myForm.lowerAgeLimit.$error.minlength">Minimum length required is 1</span>
                  <span ng-show="ctrl.myForm.lowerAgeLimit.$invalid">This field is invalid </span>
                </div>
              </div>
            </div>

            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">Upper Age Limit</label>
              <div class="col-md-8">
                <input type="text" ng-model="ctrl.hedisMeasureRule.upperAgeLimit" name="upperAgeLimit" class="username form-control input-sm" placeholder="Enter Upper Age Limit" ng-minlength="1" />
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.upperAgeLimit.$error.required">This is a required field</span>
                  <span ng-show="ctrl.myForm.upperAgeLimit.$error.minlength">Minimum length required is 1</span>
                  <span ng-show="ctrl.myForm.upperAgeLimit.$invalid">This field is invalid </span>
                </div>
              </div>
            </div>

            <div class="form-group col-md-12">
              <label class="col-md-4 control-lable" for="uname">Dose Count</label>
              <div class="col-md-8">
                <input type="text" ng-model="ctrl.hedisMeasureRule.doseCount" name="doseCount" class="username form-control input-sm" placeholder="Enter Total Dose Count" ng-minlength="1" />
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.doseCount.$error.required">This is a required field</span>
                  <span ng-show="ctrl.myForm.doseCount.$error.minlength">Minimum length required is 1</span>
                  <span ng-show="ctrl.myForm.doseCount.$invalid">This field is invalid </span>
                </div>
              </div>
            </div>

            <div class="form-group col-sm-12">
              <label class="col-md-4 control-lable" for="ageEffectiveFrom">Age Effective From</label>
              <div class="col-md-8">
                <div class="input-group date" id="ageEffectiveFrom" ng-model="ctrl.hedisMeasureRule.ageEffectiveFrom" date1-picker>
                  <input type="text" class="form-control netto-input" ng-model="ctrl.hedisMeasureRule.ageEffectiveFrom" date-picker-input>
                  <span class="input-group-addon">
				           							<span class="glyphicon glyphicon-calendar"></span>
                  </span>
                </div>
              </div>
            </div>

            <div class="form-group col-sm-12">
              <label class="col-md-4 control-lable" for="ageEffectiveTo">Age Effective To</label>
              <div class="col-md-8">
                <div class="input-group date" id="ageEffectiveTo" ng-model="ctrl.hedisMeasureRule.ageEffectiveTo" date1-picker>
                  <input type="text" class="form-control netto-input" ng-model="ctrl.hedisMeasureRule.ageEffectiveTo" date-picker-input>
                  <span class="input-group-addon">
			           							<span class="glyphicon glyphicon-calendar"></span>
                  </span>
                </div>
              </div>
            </div>

          </div>
          
           <div class="col-sm-6 ptInfo">
            <div class="form-group col-md-12" ng-if="ctrl.hedisMeasureRule.problemFlag=='Y'">
              <label class="col-md-2 control-lable" for="uname">Problem</label>
              <div class="col-md-8">
                <select class=" form-control" ng-model="ctrl.hedisMeasureRule.pbm" name="problem" ng-options="problem.description for problem in ctrl.problems | filter:{insId:{id:ctrl.insurance.id}, effectiveYear:ctrl.effectiveYear} | orderBy:'description' track by problem.id"
                ng-required="ctrl.hedisMeasureRule.problemFlag=='Y'">
                  <option></option>
                </select>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.problem.$error.required">This is a required field</span>
                </div>
              </div>
            </div>
            
              <div class="form-group col-md-12" ng-if="(ctrl.hedisMeasureRule.cptOrIcd==0 || ctrl.hedisMeasureRule.cptOrIcd==2)">
              <label class="col-md-2 control-lable" for="uname">CPT Codes</label>
              <div class="col-md-8">
                <select class=" form-control" ng-model="ctrl.currentHedisMeasureRuleCPTCodes" name="cptCodes" ng-options="cpt.codeAndDescription for cpt in ctrl.hedisMeasureRule.cptCodes  | orderBy:'code'" multiple size="10" ng-required="(ctrl.hedisMeasureRule.cptCodes.length === 0)">
                  <option></option>
                </select>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.cptCodes.$error.required">This is a required field</span>
                </div>
              </div>
              <div class="col-sm-2">
                <button type="button" class="btn btn-success btn-sm white-text" ng-click="ctrl.open('lg', 'CPT')"><span class="glyphicon glyphicon-plus-sign"></span>CPT</button>

                <button type="button" class="btn btn-success btn-sm white-text" ng-click="ctrl.removeHedisMeasureRuleCPTCodes(ctrl.currentHedisMeasureRuleCPTCodes)"> <span class="glyphicon glyphicon-minus-sign"></span>CPT</button>
              </div>
            </div>


            <div class="form-group col-md-12" ng-if="(ctrl.hedisMeasureRule.cptOrIcd==1 || ctrl.hedisMeasureRule.cptOrIcd==2)">
              <label class="col-md-2 control-lable" for="uname">ICD Codes</label>
              <div class="col-md-8">
                <select class=" form-control" ng-model="ctrl.currentHedisMeasureRuleICDCodes" name="icdCodes" ng-options="icd.codeAndDescription for icd in ctrl.hedisMeasureRule.icdCodes  | orderBy:'code'" multiple size="10" ng-required="(ctrl.hedisMeasureRule.icdCodes.length === 0)">
                  <option></option>
                </select>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm.icdCodes.$error.required">This is a required field</span>
                </div>
              </div>
              <div class="col-sm-2">
                <button type="button" class="btn btn-success btn-sm white-text" ng-click="ctrl.open('lg', 'ICD')"><span class="glyphicon glyphicon-plus-sign"></span>ICD</button>

                <button type="button" class="btn btn-success btn-sm white-text" ng-click="ctrl.removeHedisMeasureRuleICDCodes(ctrl.currentHedisMeasureRuleICDCodes)"> <span class="glyphicon glyphicon-minus-sign"></span>ICD</button>
              </div>
            </div>
            
            
            <div class="form-group col-md-12" ng-repeat=" i in ctrl.getNumber(ctrl.hedisMeasureRule.doseCount) ">
              <label class="col-md-2 control-lable" for="uname">dose{{i+1}}</label>
              <div class="col-md-4">
                  <input type="number"  min="1" max="99"  ng-model="ctrl.hedisMeasureRule['dose'+(i+1)]" name="dose{{(i + 1)}}" class="username form-control input-sm" placeholder="Enter  dose{{(i+1)}} months or years" required ng-minlength="1" />
                   <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm['dose'+(i+1)].$error.required">This is a required field</span>
                  <span ng-show="ctrl.myForm['dose'+(i+1)].$error.minlength">Minimum length required is 1</span>
                  <span ng-show="ctrl.myForm['dose'+(i+1)].$invalid">This field is invalid </span>
                </div>
              </div>
              <div class="col-sm-6">
               <select class=" form-control" ng-model="ctrl.hedisMeasureRule['datepart'+(i+1)]" name="datepart{{(i+1)}}" ng-options="datepart for datepart in ctrl.datePartList" required>
                  <option></option>
                </select>
                <div class="has-error" ng-show="ctrl.myForm.$dirty">
                  <span ng-show="ctrl.myForm['datepart'+(i+1)].$error.required">This is a required field</span>
                </div>
              </div>
            </div>
            
            
           </div>
        </form>
      </div>
    </div>
  </div>

  <script type="text/ng-template" id="myModalContent.html">
    <div class="modal-header">
      <h3 class="modal-title">{{popType}} Measures</h3>
    </div>
    <div class="modal-body">
      <table datatable="" id="content" dt-options="dtOptions" dt-columns="dtColumns" dt-instance="dtInstance" class="table-responsive table  bordered table-striped table-condensed datatable" cellspacing="0" width="100%"></table>
    </div>
    <div class="modal-footer">
      <button class="btn btn-primary" ng-click="ok()">OK</button>
      <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
    </div>
  </script>


</div>