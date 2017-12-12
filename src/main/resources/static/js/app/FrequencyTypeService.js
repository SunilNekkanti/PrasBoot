(function(){
'use strict';
var app = angular.module('my-app');
app.service('FrequencyTypeService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllFrequencyTypes: loadAllFrequencyTypes,
                loadFrequencyTypes: loadFrequencyTypes,
                getAllFrequencyTypes: getAllFrequencyTypes,
                getFrequencyType: getFrequencyType,
                createFrequencyType: createFrequencyType,
                updateFrequencyType: updateFrequencyType,
                removeFrequencyType: removeFrequencyType
            };

            return factory;

            function loadAllFrequencyTypes() {
                console.log('Fetching all frequencyTypes');
                var deferred = $q.defer();
                $http.get(urls.FREQUENCY_TYPE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all frequencyTypes');
                            $localStorage.frequencyTypes = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading frequencyTypes');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadFrequencyTypes(pageNo, length, search, order) {
                console.log('Fetching  FrequencyTypes');
                var pageable = {
                  		 page:pageNo, size:length,sort: order,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.FREQUENCY_TYPE_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  frequencyTypes');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading frequencyTypes');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllFrequencyTypes(){
            	console.log('$localStorage.frequencyTypes');
                return $localStorage.frequencyTypes;
            }

            function getFrequencyType(id) {
                console.log('Fetching FrequencyType with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.FREQUENCY_TYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully FrequencyType with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createFrequencyType(user) {
                console.log('Creating FrequencyType');
                var deferred = $q.defer();
                $http.post(urls.FREQUENCY_TYPE_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllFrequencyTypes();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating FrequencyType : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateFrequencyType(user, id) {
                console.log('Updating FrequencyType with id '+id);
                var deferred = $q.defer();
                $http.put(urls.FREQUENCY_TYPE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllFrequencyTypes();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating FrequencyType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeFrequencyType(id) {
                console.log('Removing FrequencyType with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.FREQUENCY_TYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllFrequencyTypes();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing FrequencyType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();