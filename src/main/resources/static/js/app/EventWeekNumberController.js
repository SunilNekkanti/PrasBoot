'use strict';

app.controller('EventWeekNumberController',
    ['EventWeekNumberService', '$scope',  function( EventWeekNumberService, $scope) {

    	
        var self = this;
        self.eventWeekNumber = {};
        self.eventWeekNumbers=[];
        self.display =false;
        self.submit = submit;
        self.addEventWeekNumber = addEventWeekNumber;
        self.getAllEventWeekNumbers = getAllEventWeekNumbers;
        self.createEventWeekNumber = createEventWeekNumber;
        self.updateEventWeekNumber = updateEventWeekNumber;
        self.removeEventWeekNumber = removeEventWeekNumber;
        self.editEventWeekNumber = editEventWeekNumber;
        self.reset = reset;

        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;

        function submit() {
            console.log('Submitting');
            if (self.eventWeekNumber.id === undefined || self.eventWeekNumber.id === null) {
                console.log('Saving New EventWeekNumber', self.eventWeekNumber);
                createEventWeekNumber(self.eventWeekNumber);
            } else {
                updateEventWeekNumber(self.eventWeekNumber, self.eventWeekNumber.id);
                console.log('EventWeekNumber updated with id ', self.eventWeekNumber.id);
            }
        }

        function createEventWeekNumber(eventWeekNumber) {
            console.log('About to create eventWeekNumber');
            EventWeekNumberService.createEventWeekNumber(eventWeekNumber)
                .then(
                    function (response) {
                        console.log('EventWeekNumber created successfully');
                        self.successMessage = 'EventWeekNumber created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.eventWeekNumber={};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating EventWeekNumber');
                        self.errorMessage = 'Error while creating EventWeekNumber: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateEventWeekNumber(eventWeekNumber, id){
            console.log('About to update eventWeekNumber');
            EventWeekNumberService.updateEventWeekNumber(eventWeekNumber, id)
                .then(
                    function (response){
                        console.log('EventWeekNumber updated successfully');
                        self.successMessage='EventWeekNumber updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                    },
                    function(errResponse){
                        console.error('Error while updating EventWeekNumber');
                        self.errorMessage='Error while updating EventWeekNumber '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeEventWeekNumber(id){
            console.log('About to remove EventWeekNumber with id '+id);
            EventWeekNumberService.removeEventWeekNumber(id)
                .then(
                    function(){
                        console.log('EventWeekNumber '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing eventWeekNumber '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllEventWeekNumbers(){
            return EventWeekNumberService.getAllEventWeekNumbers();
        }

        function editEventWeekNumber(id) {
            self.successMessage='';
            self.errorMessage='';
            EventWeekNumberService.getEventWeekNumber(id).then(
                function (eventWeekNumber) {
                    self.eventWeekNumber = eventWeekNumber;
                    self.display =true;
                },
                function (errResponse) {
                    console.error('Error while removing eventWeekNumber ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function addEventWeekNumber() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.eventWeekNumber={};
            $scope.myForm.$setPristine(); //reset Form
        }
    }


    ]);