<div class="generic-container">
  <div class="panel panel-success" ng-if="!ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="membership">List of Memberships </span>     </div>
    <div class="table-responsive">
      <div class="panel-body">
        <div class="panel-body">
          <div class="formcontainer">
            <div class="col-sm-2">
              <div class="form-group col-sm-12">
                <label for="plan">Insurance</label>
                <select class=" form-control" ng-model="ctrl.insurance"  ng-change="ctrl.setProviders()"  ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name"></select>
              </div>
            </div>

            <div class="col-sm-3">
              <div class="form-group col-sm-12">
                <label for="plan">Provider</label>
                 <select class=" form-control" ng-model="ctrl.prvdr" ng-change="ctrl.reset()" ng-options="provider.name for provider in ctrl.prvdrs | providerFilter:ctrl.insurance.id | orderBy:'name' track by provider.name"> </select>
              </div>
            </div>
            
              <div class="col-sm-2">
              <div class="form-group col-sm-12 required">
                <label for="plan">HedisMeasure(s)</label>
                 <multiselect ng-model="ctrl.selectedHedisRules"    ng-change ="ctrl.setColumnHeaders()" options="ctrl.hedisMeasureRules"  id-prop="id" display-prop="shortDescription" show-search="true" show-select-all="true" show-unselect-all="true"  search-limit="10"></multiselect>
              </div>
            </div>
            
             <div class="col-sm-1">
              <div class="form-group col-sm-12">
                <label for="plan">ReportMonth</label>
                 <select class=" form-control" ng-model="ctrl.selectedReportMonth"  ng-change="ctrl.reset()"  ng-options="reportMonth for reportMonth in ctrl.reportMonths"></select>
              </div>
            </div>
            
            <div class="col-sm-2">
           <div class="form-group col-sm-12">
                <label   for="startDate">StartDate </label>
               <div class="input-group date" id="startDate" ng-model="ctrl.startDate" date1-picker>
                    <input type="text" ng-model="ctrl.startDate" name="startDate" class="username form-control input-sm col-md-9" placeholder="Enter Date From" required/>
 					<span class="input-group-addon"><i class="glyphicon glyphicon-calendar"></i></span>
                    </div>
               </div>
             </div>
                  
               <div class="col-sm-2"> 
                  <div class="form-group col-md-12">
                   <label  for="endDate">EndDate</label>
                   <div class="input-group date" id="endDate" ng-model="ctrl.endDate" date1-picker>
                        <input type="text" ng-model="ctrl.endDate" name="endDate" class="username form-control input-sm" placeholder="Enter Date To" required/>
                         <span class="input-group-addon form-group"> <span class="glyphicon glyphicon-calendar"></span>  </span>
                    </div>
                  </div>
                </div>
             
              <div class="col-sm-1">
              <div class="form-group col-sm-12 required">
                <label for="plan">Status</label>
                 <multiselect ng-model="ctrl.selectedStatuses"  ng-change ="ctrl.reset()" options="ctrl.statuses" id-prop="id" display-prop="name" show-search="true" show-select-all="true" show-unselect-all="true"  ></multiselect>
              </div>
            </div>
                 
              <div class="col-sm-1">
              <div class="form-group col-sm-12 required">
                <label for="plan">InCap</label>
                 <multiselect ng-model="ctrl.selectedCaps"  ng-change ="ctrl.reset()" options="ctrl.isCaps" id-prop="id" display-prop="name" show-search="true" show-select-all="true" show-unselect-all="true"  ></multiselect>
              </div>
            </div>
            
              <div class="col-sm-1">
              <div class="form-group col-sm-12 required">
                <label for="plan">InRoster</label>
                 <multiselect ng-model="ctrl.selectedRosters"   ng-change ="ctrl.reset()" options="ctrl.isRosters" id-prop="id" display-prop="name" show-search="true" show-select-all="true" show-unselect-all="true"  ></multiselect>
              </div>
            </div>
            
              <div class=" col-sm-1">
                <div class="form-group col-sm-12 bottom-align-text">
	              <button type="button" ng-click="ctrl.generate()" ng-disabled="(!ctrl.insurance.id || !ctrl.prvdr.id || ctrl.selectedHedisRules.length == 0 
	    			  || !ctrl.startDate || !ctrl.endDate  || !ctrl.selectedReportMonth    ||  ctrl.selectedStatuses.length == 0  
	    			   ||  ctrl.selectedCaps.length == 0   ||  ctrl.selectedRosters.length == 0 )" class="btn btn-warning btn-xs align-bottom">Generate</button>
                </div>
             </div>
          </div>
        </div>
        
        <div style="height:600px" ng-if="(!ctrl.displayTable || ctrl.displayTable === false)">  </div>
        
		<div class="table-responsive" ng-if="ctrl.displayTable===true">
	                    <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="dtInstanceCallback" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
	                    cellspacing="0" width="100%"></table>
        </div>
        
        
      </div>
    </div>
  </div>

  <script type="text/ng-template" id="myModalContent.html">
    <div class="modal-header">
      <h3 class="modal-title">Membership Hedis Measures</h3>
    </div>
    <div class="modal-body">
          <table datatable="" id="content" dt-options="dtOptions" dt-columns="dtColumns" dt-instance="dtInstance" class="table-responsive table  bordered table-striped table-condensed datatable" cellspacing="0" width="100%"></table>
          
    </div>
    <div class="modal-footer">
     <div class="formcontainer">
            <div class="col-sm-11">
              <div class="form-group col-sm-12">
                <label for="plan">Notes</label>
                <textarea ng-model="notes" required rows="4" cols="100" ></textarea>
              </div>
            </div>
            
            <div class="col-sm-11">
              <div class="form-group col-sm-12">
                <label for="plan">History</label>
                <textarea ng-model="notesHistory" rows="6" cols="100" ng-disabled="true"></textarea>
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