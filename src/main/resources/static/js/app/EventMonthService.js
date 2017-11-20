'use strict';

app.service('EventMonthService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllEventMonths: loadAllEventMonths,
                getAllEventMonths: getAllEventMonths,
                getEventMonth: getEventMonth,
                createEventMonth: createEventMonth,
                updateEventMonth: updateEventMonth,
                removeEventMonth: removeEventMonth
            };

            return factory;

            function loadAllEventMonths() {
                console.log('Fetching all eventMonths');
                var deferred = $q.defer();
                $http.get(urls.EVENT_MONTH_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all eventMonths');
                            $localStorage.eventMonths = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading eventMonths');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllEventMonths(){
            	console.log('$localStorage.eventMonths');
                return $localStorage.eventMonths;
            }

            function getEventMonth(id) {
                console.log('Fetching EventMonth with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.EVENT_MONTH_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully EventMonth with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createEventMonth(user) {
                console.log('Creating EventMonth');
                var deferred = $q.defer();
                $http.post(urls.EVENT_MONTH_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllEventMonths();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating EventMonth : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateEventMonth(user, id) {
                console.log('Updating EventMonth with id '+id);
                var deferred = $q.defer();
                $http.put(urls.FACILITYTYPE_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllEventMonths();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating EventMonth with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeEventMonth(id) {
                console.log('Removing EventMonth with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.FACILITYTYPE_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllEventMonths();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing EventMonth with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);