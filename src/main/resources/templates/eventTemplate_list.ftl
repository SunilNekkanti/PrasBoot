<div class="generic-container" >

   <div class="panel panel-success" ng-hide="ctrl.display">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="user">List of Event Templates </span> 
               <button type="button"   ng-click="ctrl.addEventTemplate()" ng-hide="ctrl.displayEditButton" class="btn btn-success custom-width floatRight"> Add </button>   
               <button type="button" ng-click="ctrl.editEventTemplate(ctrl.eventTemplateId)" ng-show="ctrl.displayEditButton" class="btn btn-primary custom-width floatRight">Edit</button>  
              <button type="button" ng-click="ctrl.removeEventTemplate(ctrl.eventTemplateId)"  ng-show="ctrl.displayEditButton" class="btn btn-danger custom-width floatRight">Remove</button>  
        </div>
		<div class="panel-body">
			<div class="table-responsive">
	        <table datatable="" id="content"   dt-options="ctrl.dtOptions"  dt-columns="ctrl.dtColumns" dt-instance="ctrl.dtInstance" class="table table-hover table-responsive  bordered table-striped table-condensed datatable "></table>
	        
          </div>
		</div>
    </div>
     
     
    <div class="panel panel-success" ng-show="ctrl.display">
        <!-- Default panel contents -->
        <div class="panel-heading"><span class="eventTemplate">Event Template </span></div>
		<div class="panel-body">
	        <div class="formcontainer">
	            <div class="alert alert-success" role="alert" ng-if="ctrl.successMessage">{{ctrl.successMessage}}</div>
	            <div class="alert alert-danger" role="alert" ng-if="ctrl.errorMessage">{{ctrl.errorMessage}}</div>
	            <form ng-submit="ctrl.submit()" name="myForm" class="form-horizontal">
	                <input type="hidden" ng-model="ctrl.eventTemplate.id" />
	                <div class="row">
	                    <div class="form-group col-sm-12">
	                          <label class="col-sm-2  control-label" require for="name" require>Event Template Name </label>  
	                        <div class="col-sm-3">
	                            <input type="text" ng-model="ctrl.eventTemplate.name" id="name" name="name" class="username form-control input-sm" placeholder="Enter your name" required ng-minlength="5"/>
	                             <div class="has-error" ng-show="myForm.$dirty">
                                      <span ng-show="myForm.name.$error.required">This is a required field</span>
                                      <span ng-show="myForm.name.$error.minlength">Minimum length required is 5</span>
                                      <span ng-show="myForm.name.$invalid">This field is invalid </span>
                                  </div>
	                        </div>
	                    </div>
	                </div>
	                
	               
	              
	                <div class="row">
	                    <div class="form-group col-sm-12">
	                        <div  require>  <label class="col-sm-2  control-label" for="address1">Venue Address1</label></div>
	                        <div class="col-sm-3">
	                            <input type="text" ng-model="ctrl.eventTemplate.address1" id="address1" name="address1" class="username  form-control input-sm" placeholder="Enter venue address1" required ng-minlength="5"/>
	                  			<div class="has-error" ng-show="myForm.$dirty">
                                      <span ng-show="myForm.address1.$error.required">This is a required field</span>
                                      <span ng-show="myForm.address1.$error.minlength">Minimum length required is 5</span>
                                      <span ng-show="myForm.address1.$invalid">This field is invalid </span>
                                  </div>      
	                        </div>
	                    </div>
	                </div>
	                
	                <div class="row">
	                    <div class="form-group col-sm-12">
	                        <div  require>  <label class="col-sm-2  control-label" for="address2">Venue Address2</label></div>
	                        <div class="col-sm-3">
	                            <input type="text" ng-model="ctrl.eventTemplate.address2" id="address2" name="address2" class="username  form-control input-sm" placeholder="Enter venue address2"  ng-minlength="5"/>
	                        </div>
	                    </div>
	                </div>
	                
	                <div class="row">
	                    <div class="form-group col-sm-12">
	                        <div  require>  <label class="col-sm-2  control-label" for="city">Venue City</label></div>
	                        <div class="col-sm-3">
	                            <input type="text" ng-model="ctrl.eventTemplate.city" id="city" name="city" class="username  form-control input-sm" placeholder="Enter city" required ng-minlength="5"/>
	                  			<div class="has-error" ng-show="myForm.$dirty">
                                      <span ng-show="myForm.city.$error.required">This is a required field</span>
                                      <span ng-show="myForm.city.$error.minlength">Minimum length required is 5</span>
                                      <span ng-show="myForm.city.$invalid">This field is invalid </span>
                                  </div>      
	                        </div>
	                    </div>
	                </div>
	             
	             <div class="row">
	                   <div class="form-group col-sm-12">
	                   <div  require>  <label class="col-sm-2  control-label" for="city">Venue State</label></div>
	                        <div class="col-sm-7">
	                        <select ng-model="ctrl.eventTemplate.state" ng-options="state.description for state in ctrl.states track by state.description" required></select>
	                        </div>
	                    </div>
	                </div>
	                
	                <div class="row">
	                   <div class="form-group col-sm-12">
	                    <div  require>  <label class="col-sm-2  control-label" for="city">Zipcode</label></div>
	                        <div class="col-sm-7">
	                        <select ng-model="ctrl.eventTemplate.zipCode" ng-options="zipCode.code for zipCode in ctrl.eventTemplate.state.zipCodes track by zipCode.code" required></select>
	                        </div>
	                    </div>
	                </div>
	                
	                <div class="row">
	                    <div class="form-group col-sm-12">
	                        <div  require>  <label class="col-sm-2  control-label" for="contactPerson">Contact Person</label></div>
	                        <div class="col-sm-3">
	                            <input type="text" ng-model="ctrl.eventTemplate.contactPerson" id="contactPerson" name="contactPerson" class="username  form-control input-sm" placeholder="Enter venue contactPerson" required ng-minlength="5"/>
	                  			<div class="has-error" ng-show="myForm.$dirty">
                                      <span ng-show="myForm.contactPerson.$error.required">This is a required field</span>
                                      <span ng-show="myForm.contactPerson.$error.minlength">Minimum length required is 5</span>
                                      <span ng-show="myForm.contactPerson.$invalid">This field is invalid </span>
                                  </div>      
	                        </div>
	                    </div>
	                </div>
	                
	                <div class="row">
	                    <div class="form-group col-sm-12">
	                        <div  require>  <label class="col-sm-2  control-label" for="contactPhone">Contact Phone</label></div>
	                        <div class="col-sm-3">
	                            <input type="text" ng-model="ctrl.eventTemplate.contactPhone" id="contactPhone" name="contactPhone" class="username  form-control input-sm" phone-input placeholder="Enter venue contactPhone" required ng-minlength="5"/>
	                  			<div class="has-error" ng-show="myForm.$dirty">
                                      <span ng-show="myForm.contactPhone.$error.required">This is a required field</span>
                                      <span ng-show="myForm.contactPhone.$error.minlength">Minimum length required is 5</span>
                                      <span ng-show="myForm.contactPhone.$invalid">This field is invalid </span>
                                  </div>      
	                        </div>
	                    </div>
	                </div>
	                
	                <div class="row">
	                    <div class="form-group col-sm-12">
	                        <div  require>  <label class="col-sm-2  control-label" for="contactEmail">Contact Email</label></div>
	                        <div class="col-sm-3">
	                            <input type="email" ng-model="ctrl.eventTemplate.contactEmail" id="contactEmail" name="contactEmail" class="username  form-control input-sm"  placeholder="Enter email" required ng-minlength="5"/>
	                  			<div class="has-error" ng-show="myForm.$dirty">
                                      <span ng-show="myForm.contactEmail.$error.required">This is a required field</span>
                                      <span ng-show="myForm.contactEmail.$error.minlength">Minimum length required is 5</span>
                                      <span ng-show="myForm.contactEmail.$invalid">This field is invalid </span>
                                  </div>      
	                        </div>
	                    </div>
	                </div>
	                
	                 <div class="row fileUploadSection">
	                    <div class="form-group col-sm-12">
	                        <div  require>  <label class="col-sm-2  control-label" for="contactEmail">FileUpload</label></div>
	                        <div class="col-sm-3">
	                            <input type="file" name="fileUpload[]" class="form-control input-sm" />
	                  		</div>
	                    </div>
	                </div>
	                 <div class="row fileUploadSection">
	                    <div class="form-group col-sm-12">
	                       
	                        <div class="col-sm-offset-2 col-sm-3">
	                             <button class="add_field_button">Add More Files</button>
	                  		</div>
	                    </div>
	                </div>
	                
	                
	                <div class="row">
	                    <div class="form-actions floatCenter col-sm-offset-8">
	                        <input type="submit"  value="{{!ctrl.eventTemplate.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-sm" ng-disabled="myForm.$invalid || myForm.$pristine">
	                        <button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-sm" ng-disabled="myForm.$pristine">Reset Form</button>
	                    </div>
	                </div>
	            </form>
    	    </div>
		</div>	
    </div>

</div>
<script>
	$(document).ready(function() {
    var max_fields      = 6; //maximum input boxes allowed
    var wrapper         = $(".fileUploadSection"); //Fields wrapper
    var add_button      = $(".add_field_button"); //Add button ID
    
    var x = 1; //initlal text box count
    $(add_button).click(function(e){ //on add input button click
        e.preventDefault();
        if(x < max_fields){ //max input box allowed
            x++; //text box increment
            $(wrapper).append(' <div class="form-group col-sm-12"><div  require>  <label class="col-sm-2  control-label" for="contactEmail">FileUpload</label></div><div class="col-sm-3"><input type="file" name="fileUpload[]" class="form-control input-sm" /></div><a href="#" class="remove_field">Remove</a></div>'); 
        }
    });
    
    $(wrapper).on("click",".remove_field", function(e){ //user click on remove text
        e.preventDefault(); $(this).parent('div').remove(); x--;
    })
});


</script>