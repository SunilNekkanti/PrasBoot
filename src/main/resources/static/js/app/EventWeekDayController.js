'use strict';

app.controller('EventWeekDayController',
    ['EventWeekDayService', '$scope',  function( EventWeekDayService, $scope) {

    	
        var self = this;
        self.eventWeekDay = {};
        self.eventWeekDays=[];
        self.display =false;
        self.submit = submit;
        self.addEventWeekDay = addEventWeekDay;
        self.getAllEventWeekDays = getAllEventWeekDays;
        self.createEventWeekDay = createEventWeekDay;
        self.updateEventWeekDay = updateEventWeekDay;
        self.removeEventWeekDay = removeEventWeekDay;
        self.editEventWeekDay = editEventWeekDay;
        self.reset = reset;

        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;

        function submit() {
            console.log('Submitting');
            if (self.eventWeekDay.id === undefined || self.eventWeekDay.id === null) {
                console.log('Saving New EventWeekDay', self.eventWeekDay);
                createEventWeekDay(self.eventWeekDay);
            } else {
                updateEventWeekDay(self.eventWeekDay, self.eventWeekDay.id);
                console.log('EventWeekDay updated with id ', self.eventWeekDay.id);
            }
        }

        function createEventWeekDay(eventWeekDay) {
            console.log('About to create eventWeekDay');
            EventWeekDayService.createEventWeekDay(eventWeekDay)
                .then(
                    function (response) {
                        console.log('EventWeekDay created successfully');
                        self.successMessage = 'EventWeekDay created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.eventWeekDay={};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating EventWeekDay');
                        self.errorMessage = 'Error while creating EventWeekDay: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateEventWeekDay(eventWeekDay, id){
            console.log('About to update eventWeekDay');
            EventWeekDayService.updateEventWeekDay(eventWeekDay, id)
                .then(
                    function (response){
                        console.log('EventWeekDay updated successfully');
                        self.successMessage='EventWeekDay updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                    },
                    function(errResponse){
                        console.error('Error while updating EventWeekDay');
                        self.errorMessage='Error while updating EventWeekDay '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeEventWeekDay(id){
            console.log('About to remove EventWeekDay with id '+id);
            EventWeekDayService.removeEventWeekDay(id)
                .then(
                    function(){
                        console.log('EventWeekDay '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing eventWeekDay '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllEventWeekDays(){
            return EventWeekDayService.getAllEventWeekDays();
        }

        function editEventWeekDay(id) {
            self.successMessage='';
            self.errorMessage='';
            EventWeekDayService.getEventWeekDay(id).then(
                function (eventWeekDay) {
                    self.eventWeekDay = eventWeekDay;
                    self.display =true;
                },
                function (errResponse) {
                    console.error('Error while removing eventWeekDay ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function addEventWeekDay() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.eventWeekDay={};
            $scope.myForm.$setPristine(); //reset Form
        }
    }


    ]);