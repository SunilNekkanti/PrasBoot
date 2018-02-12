(function(){
'use strict';
var app = angular.module('my-app');

app.service('EventTypeService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllEventTypes: loadAllEventTypes,
                loadEventTypes : loadEventTypes,
                getAllEventTypes: getAllEventTypes,
                getEventType: getEventType,
                createEventType: createEventType,
                updateEventType: updateEventType,
                removeEventType: removeEventType
            };

            return factory;

            function loadEventTypes(pageNo, length, search, order) {
                console.log('Fetching  eventTypes');
                var deferred = $q.defer();
                var pageable = {
                 		 page:pageNo, size:length,sort:order,search: search||''
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
            return     $http.get(urls.EVENTTYPE_SERVICE_API, config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  eventTypes');
                            $localStorage.eventTypes = response.data.content;
                            deferred.resolve(response);
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading eventTypes');
                            deferred.reject(errResponse);
                            return   errResponse ;
                        }
                    );
            }
            
            function loadAllEventTypes() {
                console.log('Fetching all eventTypes');
                var deferred = $q.defer();
                $http.get(urls.EVENTTYPE_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all eventTypes');
                            $localStorage.eventTypes = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading eventTypes');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllEventTypes(){
            	console.log('$localStorage.eventTypes');
                return $localStorage.eventTypes;
            }

            function getEventType(id) {
                console.log('Fetching EventType with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.EVENTTYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully EventType with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading eventType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createEventType(user) {
                console.log('Creating EventType');
                var deferred = $q.defer();
                $http.post(urls.EVENTTYPE_SERVICE_API, user)
                    .then(
                        function (response) {
                        	loadEventTypes(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating EventType : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateEventType(user, id) {
                console.log('Updating EventType with id '+id);
                var deferred = $q.defer();
                $http.put(urls.EVENTTYPE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                        	loadEventTypes(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating EventType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeEventType(id) {
                console.log('Removing EventType with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.EVENTTYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                        	loadEventTypes(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing EventType with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();