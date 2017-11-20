'use strict';

app.service('EventAssignmentService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllEventAssignments: loadAllEventAssignments,
                loadEventAssignments: loadEventAssignments,
                getAllEventAssignments: getAllEventAssignments,
                getEventAssignment: getEventAssignment,
                createEventAssignment: createEventAssignment,
                updateEventAssignment: updateEventAssignment,
                removeEventAssignment: removeEventAssignment
            };

            return factory;

            function loadAllEventAssignments() {
                console.log('Fetching all events');
                var deferred = $q.defer();
                var pageable = {
                  		 page:0, size:20
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
                $http.get(urls.EVENT_ASSIGNMENT_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all events');
                            $localStorage.eventAssignments = response.data.content;
                            deferred.resolve(response.data.content);
                        },
                        function (errResponse) {
                            console.error('Error while loading events');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function loadEventAssignments(draw, length, search, order) {
                console.log('Fetching  events');
                var deferred = $q.defer();
                var pageable = {
                		page:draw, size:length,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.EVENT_ASSIGNMENT_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  events');
                            $localStorage.eventAssignments = response.data.content;
                            deferred.resolve(response.data.content);
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading events');
                            deferred.reject(errResponse);
                           // return   errResponse ;
                        }
                    );
            }

            function getAllEventAssignments(){
            	console.log('$localStorage.events');
                return $localStorage.events;
            }

            function getEventAssignment(id) {
                console.log('Fetching Event with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.EVENT_ASSIGNMENT_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Event with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading event with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createEventAssignment(event) {
                console.log('Creating Event');
                var deferred = $q.defer();
                $http.post(urls.EVENT_ASSIGNMENT_SERVICE_API, event)
                    .then(
                        function (response) {
                        	loadEventAssignments(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Event : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateEventAssignment(event, id) {
                console.log('Updating Event with id '+id);
                var deferred = $q.defer();
                $http.put(urls.EVENT_ASSIGNMENT_SERVICE_API + id, event)
                    .then(
                        function (response) {
                        	loadEventAssignments(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Event with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeEventAssignment(id) {
                console.log('Removing Event with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.EVENT_ASSIGNMENT_SERVICE_API + id)
                    .then(
                        function (response) {
                        	loadEventAssignments(0,10,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Event with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);