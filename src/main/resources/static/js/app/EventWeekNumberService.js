'use strict';

app.service('EventWeekNumberService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllEventWeekNumbers: loadAllEventWeekNumbers,
                getAllEventWeekNumbers: getAllEventWeekNumbers,
                getEventWeekNumber: getEventWeekNumber,
                createEventWeekNumber: createEventWeekNumber,
                updateEventWeekNumber: updateEventWeekNumber,
                removeEventWeekNumber: removeEventWeekNumber
            };

            return factory;

            function loadAllEventWeekNumbers() {
                console.log('Fetching all eventWeekNumbers');
                var deferred = $q.defer();
                $http.get(urls.EVENT_WEEKNUMBER_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all eventWeekNumbers');
                            $localStorage.eventWeekNumbers = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading eventWeekNumbers');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllEventWeekNumbers(){
            	console.log('$localStorage.eventWeekNumbers');
                return $localStorage.eventWeekNumbers;
            }

            function getEventWeekNumber(id) {
                console.log('Fetching EventWeekNumber with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.EVENT_WEEKNUMBER_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully EventWeekNumber with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createEventWeekNumber(user) {
                console.log('Creating EventWeekNumber');
                var deferred = $q.defer();
                $http.post(urls.EVENT_WEEKNUMBER_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllEventWeekNumbers();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating EventWeekNumber : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateEventWeekNumber(user, id) {
                console.log('Updating EventWeekNumber with id '+id);
                var deferred = $q.defer();
                $http.put(urls.EVENT_WEEKNUMBER_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllEventWeekNumbers();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating EventWeekNumber with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeEventWeekNumber(id) {
                console.log('Removing EventWeekNumber with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.EVENT_WEEKNUMBER_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllEventWeekNumbers();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing EventWeekNumber with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);