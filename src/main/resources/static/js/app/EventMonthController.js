'use strict';

app.controller('EventMonthController',
    ['EventMonthService', '$scope',  function( EventMonthService, $scope) {

    	
        var self = this;
        self.eventMonth = {};
        self.eventMonths=[];
        self.display =false;
        self.submit = submit;
        self.addEventMonth = addEventMonth;
        self.getAllEventMonths = getAllEventMonths;
        self.createEventMonth = createEventMonth;
        self.updateEventMonth = updateEventMonth;
        self.removeEventMonth = removeEventMonth;
        self.editEventMonth = editEventMonth;
        self.reset = reset;

        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;

        function submit() {
            console.log('Submitting');
            if (self.eventMonth.id === undefined || self.eventMonth.id === null) {
                console.log('Saving New EventMonth', self.eventMonth);
                createEventMonth(self.eventMonth);
            } else {
                updateEventMonth(self.eventMonth, self.eventMonth.id);
                console.log('EventMonth updated with id ', self.eventMonth.id);
            }
        }

        function createEventMonth(eventMonth) {
            console.log('About to create eventMonth');
            EventMonthService.createEventMonth(eventMonth)
                .then(
                    function (response) {
                        console.log('EventMonth created successfully');
                        self.successMessage = 'EventMonth created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.eventMonth={};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating EventMonth');
                        self.errorMessage = 'Error while creating EventMonth: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateEventMonth(eventMonth, id){
            console.log('About to update eventMonth');
            EventMonthService.updateEventMonth(eventMonth, id)
                .then(
                    function (response){
                        console.log('EventMonth updated successfully');
                        self.successMessage='EventMonth updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                    },
                    function(errResponse){
                        console.error('Error while updating EventMonth');
                        self.errorMessage='Error while updating EventMonth '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeEventMonth(id){
            console.log('About to remove EventMonth with id '+id);
            EventMonthService.removeEventMonth(id)
                .then(
                    function(){
                        console.log('EventMonth '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing eventMonth '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllEventMonths(){
            return EventMonthService.getAllEventMonths();
        }

        function editEventMonth(id) {
            self.successMessage='';
            self.errorMessage='';
            EventMonthService.getEventMonth(id).then(
                function (eventMonth) {
                    self.eventMonth = eventMonth;
                    self.display =true;
                },
                function (errResponse) {
                    console.error('Error while removing eventMonth ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function addEventMonth() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.eventMonth={};
            $scope.myForm.$setPristine(); //reset Form
        }
    }


    ]);