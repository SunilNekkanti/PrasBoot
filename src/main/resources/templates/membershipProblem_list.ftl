<div class="generic-container">
  <div class="panel panel-success" ng-if="!ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="membership">List of Memberships </span>     </div>
    <div class="table-responsive">
      <div class="panel-body">
        <div class="panel-body">
          <div class="formcontainer">
            <div class="col-sm-3">
              <div class="form-group col-sm-12">
                <label for="plan">Insurance</label>
                <select class=" form-control" ng-model="ctrl.insurance"  ng-change="ctrl.setProviders()"  ng-options="insurance.name for insurance in ctrl.insurances | orderBy:'name' track by insurance.name"></select>
              </div>
            </div>

            <div class="col-sm-3">
              <div class="form-group col-sm-12">
                <label for="plan">Provider</label>
                <select class=" form-control" ng-model="ctrl.prvdr" ng-options="provider.name for provider in ctrl.prvdrs | providerFilter:ctrl.insurance.id | orderBy:'name' track by provider.name"> </select>
              </div>
            </div>
            
            <div class="col-sm-4">
              <div class="form-group col-sm-12 required">
                <label for="plan">Problem(s)</label>
                 <multiselect ng-model="ctrl.selectedProblems"  ng-change ="ctrl.setColumnHeaders()" options="ctrl.problems" id-prop="id" display-prop="description" show-search="true" show-select-all="true" show-unselect-all="true"  search-limit="10"></multiselect>
              </div>
            </div>
            
            <div class=" col-sm-2">
              <button type="button" ng-click="ctrl.generate()" class="btn btn-warning btn-xs align-bottom">Generate</button>
            </div>
          </div>
        </div>
        
        <div style="height:600px" ng-if="(!ctrl.displayTable || ctrl.displayTable === false)">  </div>
        
        <div ng-if="ctrl.displayTable===true">
		        <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
		        cellspacing="0" width="100%"></table>
        </div>
        
      </div>
    </div>
  </div>


  

</div>