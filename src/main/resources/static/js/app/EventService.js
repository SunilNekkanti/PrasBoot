'use strict';

app.service('EventService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllEvents: loadAllEvents,
                loadEvents: loadEvents,
                getAllEvents: getAllEvents,
                getEvent: getEvent,
                createEvent: createEvent,
                updateEvent: updateEvent,
                removeEvent: removeEvent
            };

            return factory;

            function loadAllEvents() {
                console.log('Fetching all events');
                var deferred = $q.defer();
                var pageable = {
                  		 page:0, size:100
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
                $http.get(urls.EVENT_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all events');
                            $localStorage.events = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading events');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function loadEvents(pageNo, length, search, order) {
                console.log('Fetching  events');
                var deferred = $q.defer();
                var pageable = {
                  		 page:pageNo, size:length,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.EVENT_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  events');
                            $localStorage.events = response.data.content;
                            deferred.resolve(response);
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading events');
                            deferred.reject(errResponse);
                           // return   errResponse ;
                        }
                    );
            }

            function getAllEvents(){
            	console.log('$localStorage.events');
                return $localStorage.events;
            }

            function getEvent(id) {
                console.log('Fetching Event with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.EVENT_SERVICE_API + id)
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

            function createEvent(event) {
                console.log('Creating Event');
                var deferred = $q.defer();
                $http.post(urls.EVENT_SERVICE_API, event)
                    .then(
                        function (response) {
                        	loadEvents(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Event : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateEvent(event, id) {
                console.log('Updating Event with id '+id);
                var deferred = $q.defer();
                $http.put(urls.EVENT_SERVICE_API + id, event)
                    .then(
                        function (response) {
                        	loadEvents(0,20,'',null);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Event with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeEvent(id) {
                console.log('Removing Event with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.EVENT_SERVICE_API + id)
                    .then(
                        function (response) {
                        	loadEvents(0,20,'',null);
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