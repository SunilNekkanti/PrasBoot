'use strict';

app.controller('CountyController',
    ['CountyService', '$scope',  function( CountyService, $scope) {

    	
        var self = this;
        self.county = {};
        self.countys=[];
        self.display =false;
        self.submit = submit;
        self.addCounty = addCounty;
        self.getAllCounties = getAllCounties;
        self.createCounty = createCounty;
        self.updateCounty = updateCounty;
        self.removeCounty = removeCounty;
        self.editCounty = editCounty;
        self.reset = reset;

        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;

        function submit() {
            console.log('Submitting');
            if (self.county.id === undefined || self.county.id === null) {
                console.log('Saving New County', self.county);
                createCounty(self.county);
            } else {
                updateCounty(self.county, self.county.id);
                console.log('County updated with id ', self.county.id);
            }
        }

        function createCounty(county) {
            console.log('About to create county');
            CountyService.createCounty(county)
                .then(
                    function (response) {
                        console.log('County created successfully');
                        self.successMessage = 'County created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.county={};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating County');
                        self.errorMessage = 'Error while creating County: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateCounty(county, id){
            console.log('About to update county');
            CountyService.updateCounty(county, id)
                .then(
                    function (response){
                        console.log('County updated successfully');
                        self.successMessage='County updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                    },
                    function(errResponse){
                        console.error('Error while updating County');
                        self.errorMessage='Error while updating County '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeCounty(id){
            console.log('About to remove County with id '+id);
            CountyService.removeCounty(id)
                .then(
                    function(){
                        console.log('County '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing county '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllCounties(){
            return CountyService.getAllCounties();
        }

        function editCounty(id) {
            self.successMessage='';
            self.errorMessage='';
            CountyService.getCounty(id).then(
                function (county) {
                    self.county = county;
                    self.display =true;
                },
                function (errResponse) {
                    console.error('Error while removing county ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function addCounty() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.county={};
            $scope.myForm.$setPristine(); //reset Form
        }
    }


    ]);