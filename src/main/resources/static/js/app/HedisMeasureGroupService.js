(function(){
'use strict';
var app = angular.module('my-app');

app.service('HedisMeasureGroupService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllHedisMeasureGroups: loadAllHedisMeasureGroups,
                loadHedisMeasureGroups: loadHedisMeasureGroups,
                getAllHedisMeasureGroups: getAllHedisMeasureGroups,
                getHedisMeasureGroup: getHedisMeasureGroup,
                createHedisMeasureGroup: createHedisMeasureGroup,
                updateHedisMeasureGroup: updateHedisMeasureGroup,
                removeHedisMeasureGroup: removeHedisMeasureGroup
            };

            return factory;

            function loadAllHedisMeasureGroups() {
                console.log('Fetching all hedisMeasureGroups');
                var deferred = $q.defer();
                $http.get(urls.HEDIS_MEASURE_GROUP_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all hedisMeasureGroups');
                            $localStorage.hedisMeasureGroups = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading hedisMeasureGroups');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadHedisMeasureGroups(pageNo, length, search, order) {
                console.log('Fetching  HedisMeasureGroups');
                var pageable = {
                  		 page:pageNo, size:length,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.HEDIS_MEASURE_GROUP_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  hedisMeasureGroups');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading hedisMeasureGroups');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllHedisMeasureGroups(){
            	console.log('$localStorage.hedisMeasureGroups');
                return $localStorage.hedisMeasureGroups;
            }

            function getHedisMeasureGroup(id) {
                console.log('Fetching HedisMeasureGroup with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.HEDIS_MEASURE_GROUP_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully HedisMeasureGroup with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createHedisMeasureGroup(user) {
                console.log('Creating HedisMeasureGroup');
                var deferred = $q.defer();
                $http.post(urls.HEDIS_MEASURE_GROUP_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllHedisMeasureGroups();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating HedisMeasureGroup : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateHedisMeasureGroup(user, id) {
                console.log('Updating HedisMeasureGroup with id '+id);
                var deferred = $q.defer();
                $http.put(urls.HEDIS_MEASURE_GROUP_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllHedisMeasureGroups();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating HedisMeasureGroup with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeHedisMeasureGroup(id) {
                console.log('Removing HedisMeasureGroup with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.HEDIS_MEASURE_GROUP_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllHedisMeasureGroups();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing HedisMeasureGroup with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();