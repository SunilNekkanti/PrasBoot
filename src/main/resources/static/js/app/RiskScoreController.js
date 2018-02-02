(function(){
'use strict';
var app = angular.module('my-app');
app.controller('RiskScoreController',
    ['RiskScoreService', '$scope', '$compile','$state','paymentYears', 'DTOptionsBuilder', 'DTColumnBuilder',  function( RiskScoreService, $scope, $compile, $state,paymentYears, DTOptionsBuilder, DTColumnBuilder) {

    	
        var self = this;
        self.riskScore = {};
        self.riskScores=[];
        self.display =true;
        self.displayEditButton = false;
        self.submit = submit;
        self.addRiskScore = addRiskScore;
        self.getAllRiskScores = getAllRiskScores;
        self.createRiskScore = createRiskScore;
        self.updateRiskScore = updateRiskScore;
        self.removeRiskScore = removeRiskScore;
        self.editRiskScore = editRiskScore;
        self.reset = reset;
        self.riskScoreId = null;
        self.cancelEdit = cancelEdit;
        self.dtInstance = {};
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.paymentYears = paymentYears;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.add = add;
        self.records = [];
  
        function add(){
         var array = [];
		        for(var i=0;i<6;i++){
		        array.push('');
		        }
		         
		         self.records.push(array);
			}
		add();
		self.remove = function(index){ self.records.splice(index, 1); } 

        function submit() {
            console.log('Submitting');
            var icdcodesList =  self.records.map(a => a[1]);
            var icdcodes =    icdcodesList.join();
            self.institutional  = self.institutional || '';
            self.gender  = self.gender || '';
            self.paymentYear  = self.paymentYear || '';
            self.dob  = self.dob || '';
            self.medicaid  = self.medicaid || '';
            self.isAgedOrDisabled  = self.isAgedOrDisabled || '';
            self.originallyDueToDis  = self.originallyDueToDis || '';
            
            console.log('self.institutional'+self.institutional);
            console.log('self.paymentYear'+self.paymentYear);
            console.log('self.gender'+self.gender);
            console.log('self.dob'+self.dob);
            console.log('self.medicaid'+self.medicaid);
            console.log('self.isAgedOrDisabled'+self.isAgedOrDisabled);
            console.log('self.originallyDueToDis'+self.originallyDueToDis);
     
              RiskScoreService.calcularRiskScore(icdcodes,self.institutional, self.paymentYear,self.gender, self.dob,self.medicaid,self.isAgedOrDisabled, self.originallyDueToDis).then(
                    function (response) {
                    console.log('*******response*****'+JSON.stringify(response));
                     self.records = response;
                     self.records.push(getRiskScoreSumRow());
                     },
                    function (errResponse) {
                        console.error('Error while creating RiskScore');
                        self.errorMessage = 'Error while creating RiskScore: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                    );
        }

        function createRiskScore(riskScore) {
            console.log('About to create riskScore');
            RiskScoreService.createRiskScore(riskScore)
                .then(
                    function (response) {
                        console.log('RiskScore created successfully');
                        self.successMessage = 'RiskScore created successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        self.riskScore={};
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function (errResponse) {
                        console.error('Error while creating RiskScore');
                        self.errorMessage = 'Error while creating RiskScore: ' + errResponse.data.errorMessage;
                        self.successMessage='';
                    }
                );
        }


        function updateRiskScore(riskScore, id){
            console.log('About to update riskScore');
            RiskScoreService.updateRiskScore(riskScore, id)
                .then(
                    function (response){
                        console.log('RiskScore updated successfully');
                        self.successMessage='RiskScore updated successfully';
                        self.errorMessage='';
                        self.done = true;
                        self.display =false;
                        $scope.myForm.$setPristine();
                        self.dtInstance.reloadData();
                        self.dtInstance.rerender();
                    },
                    function(errResponse){
                        console.error('Error while updating RiskScore');
                        self.errorMessage='Error while updating RiskScore '+errResponse.data;
                        self.successMessage='';
                    }
                );
        }


        function removeRiskScore(id){
            console.log('About to remove RiskScore with id '+id);
            RiskScoreService.removeRiskScore(id)
                .then(
                    function(){
                        console.log('RiskScore '+id + ' removed successfully');
                    },
                    function(errResponse){
                        console.error('Error while removing riskScore '+id +', Error :'+errResponse.data);
                    }
                );
        }


        function getAllRiskScores(){
            return RiskScoreService.getAllRiskScores();
        }

        function editRiskScore(id) {
            self.successMessage='';
            self.errorMessage='';
            RiskScoreService.getRiskScore(id).then(
                function (riskScore) {
                    self.riskScore = riskScore;
                    self.display =true;
                },
                function (errResponse) {
                    console.error('Error while removing riskScore ' + id + ', Error :' + errResponse.data);
                }
            );
        }
        
        function addRiskScore() {
            self.successMessage='';
            self.errorMessage='';
            self.display =true;
        }
        
        
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.riskScore={};
            $scope.myForm.$setPristine(); //reset Form
        }
        
        function cancelEdit(){
            self.successMessage='';
            self.errorMessage='';
            self.riskScore={};
            self.display = false;
            $state.go('main.riskScore', {}, {location: true,reload: true,notify: false});
        }
        
        function   getRiskScoreSumRow() {
            var sum = 0;
            for (var i = self.records.length - 1; i >= 0; i--) {
                sum +=  parseFloat(self.records[i][5] || 0);
            }
            var arr = [];
            for(var k=0;k<4;k++){
            arr.push('');
            }
            arr.push('Total Risk Score');
			arr.push(sum.toFixed(3));
            return arr;
        }
    
    }


    ]);
   })();