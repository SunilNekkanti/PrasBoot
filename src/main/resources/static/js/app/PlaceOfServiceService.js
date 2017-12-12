(function(){
'use strict';
var app = angular.module('my-app');

app.service('PlaceOfServiceService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllPlaceOfServices: loadAllPlaceOfServices,
                loadPlaceOfServices: loadPlaceOfServices,
                getAllPlaceOfServices: getAllPlaceOfServices,
                getPlaceOfService: getPlaceOfService,
                createPlaceOfService: createPlaceOfService,
                updatePlaceOfService: updatePlaceOfService,
                removePlaceOfService: removePlaceOfService
            };

            return factory;

            function loadAllPlaceOfServices() {
                console.log('Fetching all placeOfServices');
                var deferred = $q.defer();
                $http.get(urls.PLACE_OF_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all placeOfServices');
                            $localStorage.placeOfServices = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading placeOfServices');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadPlaceOfServices(pageNo, length, search, order) {
                console.log('Fetching  PlaceOfServices');
                var pageable = {
                  		 page:pageNo, size:length,sort: order,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.PLACE_OF_SERVICE_API,  config)
                    .then(
                        function (response) {
                        	$localStorage.placeOfServices = response.data.content;
                            console.log('Fetched successfully  placeOfServices');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading placeOfServices');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllPlaceOfServices(){
            	console.log('$localStorage.placeOfServices');
                return $localStorage.placeOfServices;
            }

            function getPlaceOfService(id) {
                console.log('Fetching PlaceOfService with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.PLACE_OF_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully PlaceOfService with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createPlaceOfService(user) {
                console.log('Creating PlaceOfService');
                var deferred = $q.defer();
                $http.post(urls.PLACE_OF_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllPlaceOfServices();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating PlaceOfService : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updatePlaceOfService(user, id) {
                console.log('Updating PlaceOfService with id '+id);
                var deferred = $q.defer();
                $http.put(urls.PLACE_OF_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllPlaceOfServices();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating PlaceOfService with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removePlaceOfService(id) {
                console.log('Removing PlaceOfService with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.PLACE_OF_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllPlaceOfServices();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing PlaceOfService with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();