(function(){
'use strict';
var app = angular.module('my-app');

app.service('HospitalService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllHospitals: loadAllHospitals,
                loadHospitals: loadHospitals,
                getAllHospitals: getAllHospitals,
                getHospital: getHospital,
                createHospital: createHospital,
                updateHospital: updateHospital,
                removeHospital: removeHospital
            };

            return factory;

            function loadAllHospitals() {
                console.log('Fetching all hospitals');
                var deferred = $q.defer();
                $http.get(urls.HOSPITAL_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all hospitals');
                            $localStorage.hospitals = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading hospitals');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadHospitals(pageNo, length, search, order) {
                console.log('Fetching  Hospitals');
                var pageable = {
                  		 page:pageNo, size:length,sort: order,search: search||''
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.HOSPITAL_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  hospitals');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading hospitals');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllHospitals(){
            	console.log('$localStorage.hospitals');
                return $localStorage.hospitals;
            }

            function getHospital(id) {
                console.log('Fetching Hospital with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.HOSPITAL_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Hospital with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createHospital(user) {
                console.log('Creating Hospital');
                var deferred = $q.defer();
                $http.post(urls.HOSPITAL_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllHospitals();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating Hospital : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateHospital(user, id) {
                console.log('Updating Hospital with id '+id);
                var deferred = $q.defer();
                $http.put(urls.HOSPITAL_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllHospitals();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Hospital with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeHospital(id) {
                console.log('Removing Hospital with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.HOSPITAL_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllHospitals();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing Hospital with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
})();