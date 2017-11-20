'use strict';

var loginapp = angular.module('login-app', ['ui.bootstrap','ui.router','ngStorage','datatables','ngAnimate', 'ngSanitize']);

loginapp.controller('LoginController',
    [ '$scope', '$window', function(  $scope, $window) {

    	 $window.localStorage.clear();
         
        var self = this;
        self.login = {};
        self.headerDisplay = false;
        self.display =false;
        self.submit = submit;
        self.reset = reset;

        self.errorMessage = '';

        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.test = test;
        
        function test(){
        	alert("hi");
        }
        function submit() {
            console.log('Login Submitting');
            if (self.login.username === undefined || self.login.username === null || self.login.password === undefined || self.login.password === null) {
            	 console.error('Error while entering  Login credentials');
            } else {
                console.log('Login updated with id ', self.login.id);
            }
        }
       
        function reset(){
            self.successMessage='';
            self.errorMessage='';
            self.login={};
            $scope.myForm.$setPristine(); //reset Form
        }
    }


    ]);