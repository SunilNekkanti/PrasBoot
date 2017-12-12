(function(){
'use strict';
var app = angular.module('my-app');

app.service('MembershipClaimsService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllMembershipClaims: loadAllMembershipClaims,
                loadMembershipClaims: loadMembershipClaims,
                getAllMembershipClaims: getAllMembershipClaims,
                loadAllReportMonths: loadAllReportMonths,
                getAllReportMonths: getAllReportMonths,
                loadAllRiskCategories:loadAllRiskCategories,
                getAllRiskCategories: getAllRiskCategories
            };

            return factory;

            function loadAllMembershipClaims(insId, prvdrId, reportMonth, category) {
                console.log('Fetching all membershipClaims');
                var deferred = $q.defer();
                var pageable = {
                 		 page:0, size:1000, insId:insId,prvdrId:prvdrId
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
                $http.get(urls.CLAIM_REPORT_SERVICE_API, config )
                    .then(
                        function (response) {
                            console.log('Fetched successfully all membershipClaims');
                            if (localStorage.getItem("membershipClaims") === null) {
                            	 $localStorage.membershipClaims = response.data.content;
                           }else {
                           	localStorage.removeItem("membershipClaims") ;
                             	$localStorage.membershipClaims = response.data.content;
                           }
                            
                           
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading membershipClaims');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            
            function loadMembershipClaims(insId, prvdrIds,mbrId,claimTypes,categories, reportMonth,activityMonth, caps,rosters ,levelNo, maxReportMonth) {
                console.log('Fetching  MembershipClaims');
                var pageable = {
                		 insId:insId,prvdrIds:prvdrIds,mbrId:mbrId, reportMonth:reportMonth,activityMonth:activityMonth,categories:categories,claimTypes:claimTypes, inCap:caps,inRoster:rosters,levelNo:levelNo,maxReportMonth:maxReportMonth
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
                  		
            return     $http.get(urls.CLAIM_REPORT_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  membershipClaims');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading membershipClaims');
                            return   errResponse ;
                        }
                    );
            }
            
            function loadAllReportMonths() {
                console.log('Fetching all reportMonths');
                var deferred = $q.defer();
                var pageable = {
                 		 page:0, size:1000
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
                $http.get(urls.REPORTMONTH_SERVICE_API, config )
                    .then(
                        function (response) {
                            console.log('Fetched successfully all reportMonths');
                            if (localStorage.getItem("reportMonths") === null) {
                            	 $localStorage.reportMonths = response.data;
                           }else {
                           	localStorage.removeItem("reportMonths") ;
                             	$localStorage.reportMonths = response.data;
                           }
                            
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading membershipClaims');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function loadAllRiskCategories() {
                console.log('Fetching all riskCategories');
                var deferred = $q.defer();
                var pageable = {
                 		 page:0, size:100
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
                $http.get(urls.CLAIM_RISK_CATEGORY_SERVICE_API, config )
                    .then(
                        function (response) {
                            console.log('Fetched successfully all categories');
                            if (localStorage.getItem("categories") === null) {
                            	 $localStorage.categories = response.data;
                           }else {
                           	localStorage.removeItem("categories") ;
                             	$localStorage.categories = response.data;
                           }
                            
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading categories');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function getAllMembershipClaims(){
            	console.log('$localStorage.membershipClaims');
                return $localStorage.membershipClaims;
            }

            function getAllReportMonths(){
            	console.log('$localStorage.reportMonths');
                return $localStorage.reportMonths;
            }
            
            function getAllRiskCategories(){
            	console.log('$localStorage.categories');
                return $localStorage.categories;
            }
           
        }
    ]);
   })();