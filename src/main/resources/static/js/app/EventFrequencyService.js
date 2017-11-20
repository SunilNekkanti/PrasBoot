'use strict';

app.service('EventFrequencyService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllEventFrequencies: loadAllEventFrequencies,
                getAllEventFrequencies: getAllEventFrequencies,
                getEventFrequency: getEventFrequency,
                createEventFrequency: createEventFrequency,
                updateEventFrequency: updateEventFrequency,
                removeEventFrequency: removeEventFrequency
            };

            return factory;

            function loadAllEventFrequencies() {
                console.log('Fetching all eventFrequencies');
                var deferred = $q.defer();
                $http.get(urls.EVENT_FREQUENCY_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all eventFrequencies');
                            $localStorage.eventFrequencys = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading eventFrequencies');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllEventFrequencies(){
            	console.log('$localStorage.eventFrequencies');
                return $localStorage.eventFrequencys;
            }

            function getEventFrequency(id) {
                console.log('Fetching EventFrequency with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.EVENT_FREQUENCY_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully EventFrequency with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createEventFrequency(user) {
                console.log('Creating EventFrequency');
                var deferred = $q.defer();
                $http.post(urls.EVENT_FREQUENCY_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllEventFrequencies();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating EventFrequency : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateEventFrequency(user, id) {
                console.log('Updating EventFrequency with id '+id);
                var deferred = $q.defer();
                $http.put(urls.FACILITYTYPE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllEventFrequencies();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating EventFrequency with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeEventFrequency(id) {
                console.log('Removing EventFrequency with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.FACILITYTYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllEventFrequencies();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing EventFrequency with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);