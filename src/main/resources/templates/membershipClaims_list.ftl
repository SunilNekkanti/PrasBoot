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

            <div class="col-sm-2">
              <div class="form-group col-sm-12">
                <label for="plan">Provider</label>
                <multiselect ng-model="ctrl.selectedPrvdrs"  ng-change ="ctrl.reset()" options="ctrl.providers" id-prop="id" display-prop="name" show-search="true" show-select-all="true" show-unselect-all="true" search-limit="10"></multiselect>
              </div>
            </div>
            
              <div class="col-sm-2">
              <div class="form-group col-sm-12 required">
                <label for="plan">ClaimType(s)</label>
                 <multiselect ng-model="ctrl.selectedClaimTypes"    ng-change ="ctrl.reset()" options="ctrl.claimTypes"  id-prop="id" display-prop="id" show-search="true" show-select-all="true" show-unselect-all="true"  search-limit="10"></multiselect>
              </div>
            </div>
            
            <div class="col-sm-2">
              <div class="form-group col-sm-12 required">
                <label for="plan">Category</label>
                 <multiselect ng-model="ctrl.selectedCategories"  ng-change ="ctrl.reset()" options="ctrl.categories" id-prop="id" display-prop="name" show-search="true" show-select-all="true" show-unselect-all="true"  search-limit="10"></multiselect>
              </div>
            </div>
            
            <div class="col-sm-1">
              <div class="form-group col-sm-12">
                <label for="plan">ReportMonth</label>
                <select class=" form-control"  ng-model="ctrl.selectedReportMonth"  ng-change ="ctrl.reset()" ng-options="reportMonth for reportMonth in ctrl.reportMonths" ></select>

              </div>
            </div>
            
              <div class="col-sm-1">
              <div class="form-group col-sm-12 required">
                <label for="plan">InCap</label>
                 <multiselect ng-model="ctrl.selectedCaps"  ng-change ="ctrl.reset()" options="ctrl.isCaps" id-prop="id" display-prop="id" show-search="true" show-select-all="true" show-unselect-all="true"  ></multiselect>
              </div>
            </div>
            
              <div class="col-sm-1">
              <div class="form-group col-sm-12 required">
                <label for="plan">InRoster</label>
                 <multiselect ng-model="ctrl.selectedRosters"   ng-change ="ctrl.reset()" options="ctrl.isRosters" id-prop="id" display-prop="id" show-search="true" show-select-all="true" show-unselect-all="true"  ></multiselect>
              </div>
            </div>
            
            <div class=" col-sm-1">
              <button type="button" ng-click="ctrl.generate()" ng-disabled="(!ctrl.insurance.id || ctrl.selectedPrvdrs.length == 0 || ctrl.selectedClaimTypes.length == 0 
    			   || ctrl.selectedCategories.length == 0 || !ctrl.selectedReportMonth  
    			   ||  ctrl.selectedCaps.length == 0   ||  ctrl.selectedRosters.length == 0 )" class="btn btn-warning btn-xs align-bottom">Generate</button>
            </div>
          </div>
        </div>
        
        <div style="height:600px" ng-if="(!ctrl.displayTable || ctrl.displayTable === false)">  </div>
        
		<div class="table-responsive" ng-if="ctrl.displayTable===true">
               <tabset>
                 <tab heading="IPA Level"  ng-if="(ctrl.displayTable===true && ctrl.levelNo>=1)" active="(ctrl.levelNo ===1 )" ng-click="ctrl.activateLevel1()">
	                  <div >
	                    <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="dtInstance" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
	                    cellspacing="0" width="100%"></table>
	                  </div>
	 				</tab>
	 				 <tab heading="Provider Level" ng-if="(ctrl.displayTable1===true && ctrl.levelNo >=2)" active="(ctrl.levelNo ===2 )" ng-click="ctrl.activateLevel2()">
	 				  <div class="panel-body">
	 				      <table datatable="" id="content1" dt-options="ctrl.dt1Options" dt-columns="ctrl.dt1Columns" dt-instance="dt1Instance" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
	                    cellspacing="0" width="100%"></table>
	 				  </div>
	                 </tab>
	                 <tab heading="Membership Level" ng-if="(ctrl.displayTable2===true && ctrl.levelNo >=3)" active="(ctrl.levelNo ===3 )" ng-click="ctrl.activateLevel3()">
	 				  <div class="panel-body">
	 				      <table datatable="" id="content2" dt-options="ctrl.dt2Options" dt-columns="ctrl.dt2Columns" dt-instance="dt2Instance" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
	                    cellspacing="0" width="100%"></table>
	 				  </div>
	                 </tab>
	                 <tab heading="Individual Membership Level" ng-if="(ctrl.displayTable3===true && ctrl.levelNo >=4)" active="(ctrl.levelNo ===4 )" >
	 				  <div class="panel-body">
	 				      <table datatable="" id="content3" dt-options="ctrl.dt3Options" dt-columns="ctrl.dt3Columns" dt-instance="dt3Instance" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
	                    cellspacing="0" width="100%"></table>
	 				  </div>
	                 </tab>
             </tabset>
        </div>
        
        
      </div>
    </div>
  </div>


</div>