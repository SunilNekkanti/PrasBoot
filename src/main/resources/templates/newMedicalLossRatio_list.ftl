<div class="generic-container" >
  <div class="panel panel-success" ng-if="!ctrl.display" >
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="medicalLossRatio">Medical Loss Ratio </span> </div>
    <div class="table-responsive">
      <div class="panel-body">
        <div class="panel-body">
          <div class="formcontainer" >
            <div class="col-sm-2">
              <div class="form-group col-sm-12">
                <label for="plan">Insurance</label>
                <select class=" form-control" ng-model="ctrl.insurance"   placeholder="Choose an Insurance"  ng-change="ctrl.setProviders()" ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name"></select>
              </div>
            </div>


            <div class="col-sm-2">
              <div class="form-group col-sm-12">
                <label for="plan">Providers</label>
                <multiselect ng-model="ctrl.selectedPrvdrs" ng-change="ctrl.reset()"  placeholder="Choose a Provider"   options="ctrl.providers" id-prop="id" display-prop="name" show-search="true" show-select-all="true" show-unselect-all="true" search-limit="10"></multiselect>
              </div>
            </div>

            <div class="col-sm-2">
              <div class="form-group col-sm-12">
                <label for="plan">DataFile</label>
                <multiselect ng-model="ctrl.selectedReportMonths"  ng-change="ctrl.reset()"  placeholder="Choose a DataFile" options="ctrl.reportMonths" show-search="true" show-select-all="true" show-unselect-all="true" selection-limit="2" search-limit="10"></multiselect>

              </div>
            </div>
            
            
            <div class="col-sm-6">
               <div class="form-group col-sm-12">
                <label for="plan">Reporting Years:</label>
                <div class="btn-group" role="group">
                  <label ng-repeat="item in ctrl.reportingYears" btn-checkbox="item" class="item btn btn-default " id="{{item}}" ng-change="ctrl.sync(bool, item)"  ng-model="bool" ng-checked="ctrl.isChecked(item)">
                   <div ng-show="false" > {{bool=ctrl.isChecked(item)||false}} </div>
                    {{item}}
                  </label>
                </div>
               </div>
              
               <div class="form-group col-sm-12">
                <label for="plan">Reporting Quarters:</label>
                <div class="btn-group" role="group">
                  <label ng-repeat="item in ctrl.reportingQuarters" btn-checkbox="item" class="item btn btn-default " id="{{item.quarter}}" ng-change="ctrl.syncQuarters(bool, item)"  ng-model="bool" ng-checked="ctrl.isCheckedQuarter(item)">
                   <div ng-show="false" > {{bool=ctrl.isCheckedQuarter(item)||false}} </div>
                    {{item.quarter}}
                  </label>
                </div>
 			  </div>
 			  
              <div class="form-group col-sm-12">
                <label for="plan">Reporting Months:</label>
                <div class="btn-group" role="group">
                  <label   ng-repeat="item in ctrl.reportingMonths" btn-checkbox="item" class="item btn btn-default " id="{{item}}" ng-change="ctrl.syncMonths(bool, item)"  ng-model="bool" ng-checked="ctrl.isCheckedMonth(item)">
                   <div ng-show="false" > {{bool=ctrl.isCheckedMonth(item)||false}} </div>
                    {{item.month}}
                  </label>
                </div>
              </div>
              
            </div>
            
            <div class=" col-sm-1">
              <button type="button"   ng-click="ctrl.generate()" class="btn btn-warning btn-xs align-bottom" ng-disabled="((ctrl.selectedPrvdrs.length ===0) || (ctrl.selectedReportMonths.length ===0)|| (ctrl.selectedReportingYears.length ===0) || (ctrl.selectedReportingQuarters.length ===0 && ctrl.selectedReportingMonths.length===0))">Generate</button>
            </div>
          </div>
        </div>

         <div style="height:600px" ng-if="!ctrl.displayTable"> </div> 
       
         <div style="height:600px" ng-if="ctrl.displayTable">
               <tabset>
                <tab  heading="Summary">
	 				      <table datatable="" id="content1" dt-options="ctrl.dt1Options" dt-columns="ctrl.dt1Columns" dt-instance="ctrl.dt1InstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
	                       cellspacing="0" width="97%" ></table>
	            </tab>
                 <tab  heading="Detail"  ng-if="ctrl.displayTable" >
	                    <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns"  dt-instance="ctrl.dtInstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
	                    cellspacing="0" width="97%"></table>
	 			</tab>
	 				
             </tabset>
         </div>

      </div>
    </div>
  </div>


</div>