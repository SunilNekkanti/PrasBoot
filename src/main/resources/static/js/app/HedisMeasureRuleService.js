(function(){
'use strict';
var app = angular.module('my-app');

app.service('HedisMeasureRuleService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllHedisMeasureRules: loadAllHedisMeasureRules,
                loadHedisMeasureRules: loadHedisMeasureRules,
                getAllHedisMeasureRules: getAllHedisMeasureRules,
                getHedisMeasureRule: getHedisMeasureRule,
                createHedisMeasureRule: createHedisMeasureRule,
                updateHedisMeasureRule: updateHedisMeasureRule,
                removeHedisMeasureRule: removeHedisMeasureRule
            };

            return factory;

            function loadAllHedisMeasureRules() {
                console.log('Fetching all hedisMeasureRules');
                var deferred = $q.defer();
                $http.get(urls.HEDIS_MEASURE_RULE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all hedisMeasureRules');
                            $localStorage.hedisMeasureRules = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading hedisMeasureRules');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadHedisMeasureRules(pageNo, length, search, order, insId, effectiveYear) {
                console.log('Fetching  HedisMeasureRules');
                var pageable = {
                  		 page:pageNo, size:length,sort:order, search: search||'',insId:insId,effectiveYear:effectiveYear
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.HEDIS_MEASURE_RULE_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  hedisMeasureRules');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading hedisMeasureRules');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllHedisMeasureRules(){
            	console.log('$localStorage.hedisMeasureRules');
                return $localStorage.hedisMeasureRules;
            }

            function getHedisMeasureRule(id) {
                console.log('Fetching HedisMeasureRule with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.HEDIS_MEASURE_RULE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully HedisMeasureRule with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createHedisMeasureRule(user) {
                console.log('Creating HedisMeasureRule');
                var deferred = $q.defer();
                $http.post(urls.HEDIS_MEASURE_RULE_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllHedisMeasureRules();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating HedisMeasureRule : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateHedisMeasureRule(user, id) {
                console.log('Updating HedisMeasureRule with id '+id);
                var deferred = $q.defer();
                $http.put(urls.HEDIS_MEASURE_RULE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllHedisMeasureRules();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating HedisMeasureRule with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeHedisMeasureRule(id) {
                console.log('Removing HedisMeasureRule with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.HEDIS_MEASURE_RULE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllHedisMeasureRules();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing HedisMeasureRule with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();