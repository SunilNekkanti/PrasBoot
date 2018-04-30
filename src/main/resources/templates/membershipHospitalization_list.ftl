<div class="generic-container">

  <div class="panel panel-success" ng-if="!ctrl.display">
    <!-- Default panel contents -->
    <div class="panel-heading"><span class="membership">List of Memberships </span>
      <button type="button" ng-click="ctrl.addMembership()" ng-hide="ctrl.displayEditButton" class="btn btn-success btn-xs custom-width  floatRight"> Add </button>
      <button type="button" ng-click="ctrl.editMembership(ctrl.membershipId)" ng-show="ctrl.displayEditButton" class="btn btn-primary  btn-xs custom-width floatRight">Edit</button>
      <button type="button" ng-click="ctrl.removeMembership(ctrl.membershipId)" ng-show="ctrl.displayEditButton" class="btn btn-danger  btn-xs custom-width floatRight">Remove</button>
    </div>
    <div  class="table-responsive">
      <div class="panel-body">
        <div class="panel-body">
          <div class="formcontainer">

            <div class="col-sm-6">
             <div class="row">
              <div class="col-sm-6">
                <div class="form-group col-sm-12">
                  <label for="plan">Insurance</label>
                  <multiselect ng-model="ctrl.selectedInsurances" ng-change="ctrl.setProviders()" placeholder="Choose Insurance(s)" options="ctrl.insurances" id-prop="id" display-prop="name" show-search="true" show-select-all="true" show-unselect-all="true" search-limit="10"></multiselect>
                </div>
              </div>

              <div class="col-sm-6">
                <div class="form-group col-sm-12">
                  <label for="plan">Provider</label>
                  <multiselect ng-model="ctrl.selectedPrvdrs" placeholder="Choose Provider(s)" options="ctrl.providers" id-prop="id" display-prop="name" show-search="true" show-select-all="true" show-unselect-all="true" search-limit="10"></multiselect>

                </div>
              </div>
             </div>
  
            </div>

            <div class=" col-sm-3">
              <button type="button" ng-click="ctrl.generate()" class="btn btn-warning btn-xs align-bottom" ng-disabled="((ctrl.selectedInsurances === undefined) ||  (ctrl.selectedInsurances.length ===0) ||(ctrl.selectedPrvdrs ===undefined) || (ctrl.selectedPrvdrs.length ===0) )">Generate</button>
            </div>
          </div>
        </div>


        <table datatable="" id="content" dt-options="ctrl.dtOptions" dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" dt-disable-deep-watchers="true" class="table table-hover table-responsive  bordered table-striped table-condensed datatable dt-responsive nowrap dataTable row-border hover"
        cellspacing="0" width="100%">
        </table>

      </div>
    </div>
  </div>
  <div style="height:600px" ng-if="!ctrl.displayTable"> </div>
</div>
<script type="text/ng-template" id="myModalContent.html">
    <div class="modal-header">
      <h3 class="modal-title">Membership Hospitalization FollowUp</h3>
    </div>
    <div class="modal-body">
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
        </div>
    </div>
    <div class="modal-footer">
      <div class="col-sm-4 col-sm-offset-7">
        <button class="btn btn-primary" ng-click="ok()">OK</button>
        <button class="btn btn-warning" ng-click="cancel()">Cancel</button>
      </div> 
    </div>
  </script>

