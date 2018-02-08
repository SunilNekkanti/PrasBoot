'use strict';

app.service('EventWeekDayService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllEventWeekDays: loadAllEventWeekDays,
                getAllEventWeekDays: getAllEventWeekDays,
                getEventWeekDay: getEventWeekDay,
                createEventWeekDay: createEventWeekDay,
                updateEventWeekDay: updateEventWeekDay,
                removeEventWeekDay: removeEventWeekDay
            };

            return factory;

            function loadAllEventWeekDays() {
                console.log('Fetching all eventWeekDays');
                var deferred = $q.defer();
                $http.get(urls.EVENT_WEEKDAY_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all eventWeekDays');
                            $localStorage.eventWeekDays = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading eventWeekDays');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllEventWeekDays(){
            	console.log('$localStorage.eventWeekDays');
                return $localStorage.eventWeekDays;
            }

            function getEventWeekDay(id) {
                console.log('Fetching EventWeekDay with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.EVENT_WEEKDAY_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully EventWeekDay with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createEventWeekDay(user) {
                console.log('Creating EventWeekDay');
                var deferred = $q.defer();
                $http.post(urls.EVENT_WEEKDAY_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllEventWeekDays();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating EventWeekDay : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateEventWeekDay(user, id) {
                console.log('Updating EventWeekDay with id '+id);
                var deferred = $q.defer();
                $http.put(urls.EVENT_WEEKDAY_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllEventWeekDays();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating EventWeekDay with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeEventWeekDay(id) {
                console.log('Removing EventWeekDay with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.EVENT_WEEKDAY_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllEventWeekDays();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing EventWeekDay with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);