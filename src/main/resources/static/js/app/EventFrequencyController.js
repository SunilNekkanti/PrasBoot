'use strict';

app.controller('EventFrequencyController',
    ['EventFrequencyService', '$scope',  function( EventFrequencyService, $scope) {

    	
        var self = this;
        self.eventFrequency = {};
        self.eventFrequencies=[];
        self.display =false;
        self.submit = submit;
        self.addEventFrequency = addEventFrequency;
        self.getAllEventFrequencies = getAllEventFrequencies;
        self.createEventFrequency = createEventFrequency;
        self.updateEventFrequency = updateEventFrequency;
        self.removeEventFrequency = removeEventFrequency;
        self.editEventFrequency = editEventFrequency;
        self.reset = reset;

        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;

        function submit() {
            console.log('Submitting');
            if (self.eventFrequency.id === undefined || self.eventFrequency.id === null) {
                console.log('Saving New EventFrequency', self.eventFrequency);
                createEventFrequency(self.eventFrequency);
            } else {
                updateEventFrequency(self.eventFrequency, self.eventFrequency.id);
                console.log('EventFrequency updated with id ', self.eventFrequency.id);
            }
        }

        function createEventFrequency(eventFrequency) {
            console.log('About to create eventFrequency');
            EventFrequencyService.createEventFrequency(eventFrequency)
                .then(
                    function (response) {
                        console.log('EventFrequency created successfully');
                        self.successMessage = 'EventFrequency created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.eventFrequency={};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating EventFrequency');
                        self.errorMessage = 'Error while creating EventFrequency: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateEventFrequency(eventFrequency, id){
            console.log('About to update eventFrequency');
            EventFrequencyService.updateEventFrequency(eventFrequency, id)
                .then(
                    function (response){
                        console.log('EventFrequency updated successfully');
                        self.successMessage='EventFrequency updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                    },
                    function(errResponse){
                        console.error('Error while updating EventFrequency');
                        self.errorMessage='Error while updating EventFrequency '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeEventFrequency(id){
            console.log('About to remove EventFrequency with id '+id);
            EventFrequencyService.removeEventFrequency(id)
                .then(
                    function(){
                        console.log('EventFrequency '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing eventFrequency '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllEventFrequencies(){
            return EventFrequencyService.getAllEventFrequencies();
        }

        function editEventFrequency(id) {
            self.successMessage='';
            self.errorMessage='';
            EventFrequencyService.getEventFrequency(id).then(
                function (eventFrequency) {
                    self.eventFrequency = eventFrequency;
                    self.display =true;
                },
                function (errResponse) {
                    console.error('Error while removing eventFrequency ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function addEventFrequency() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.eventFrequency={};
            $scope.myForm.$setPristine(); //reset Form
        }
    }


    ]);