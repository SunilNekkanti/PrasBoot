(function(){
'use strict';
var app = angular.module('my-app');

app.service('LeadStatusDetailService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllLeadStatusDetails: loadAllLeadStatusDetails,
                loadLeadStatusDetails : loadLeadStatusDetails,
                getAllLeadStatusDetails: getAllLeadStatusDetails,
                getLeadStatusDetail: getLeadStatusDetail,
                createLeadStatusDetail: createLeadStatusDetail,
                updateLeadStatusDetail: updateLeadStatusDetail,
                removeLeadStatusDetail: removeLeadStatusDetail
            };

            return factory;

            function loadLeadStatusDetails(pageNo, length, search, order) {
                console.log('Fetching  LeadStatusDetails');
                var deferred = $q.defer();
                var pageable = {
                 		 page:pageNo, size:length,sort:order, search: search||''
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
            return     $http.get(urls.LEADSTATUS_DETAIL_SERVICE_API, config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  LeadStatusDetails');
                            $localStorage.LeadStatusDetail = response.data.content;
                            deferred.resolve(response);
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading LeadStatusDetails');
                            deferred.reject(errResponse);
                            return   errResponse ;
                        }
                    );
            }
            function loadAllLeadStatusDetails() {
                console.log('Fetching all LeadStatusDetails');
                var deferred = $q.defer();
                $http.get(urls.LEADSTATUS_DETAIL_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all LeadStatusDetails');
                            $localStorage.LeadStatusDetail = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading LeadStatusDetails');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllLeadStatusDetails(){
            	console.log('$localStorage.LeadStatusDetails');
                return $localStorage.LeadStatusDetail;
            }

            function getLeadStatusDetail(id) {
                console.log('Fetching LeadStatusDetail with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.LEADSTATUS_DETAIL_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully LeadStatusDetail with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createLeadStatusDetail(user) {
                console.log('Creating LeadStatusDetail');
                var deferred = $q.defer();
                $http.post(urls.STATUS_DETAIL_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllLeadStatusDetails();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating LeadStatusDetail : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateLeadStatusDetail(user, id) {
                console.log('Updating LeadStatusDetail with id '+id);
                var deferred = $q.defer();
                $http.put(urls.LEADSTATUS_DETAIL_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllLeadStatusDetails();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating LeadStatusDetail with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeLeadStatusDetail(id) {
                console.log('Removing LeadStatusDetail with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.LEADSTATUS_DETAIL_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllLeadStatusDetails();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing LeadStatusDetail with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();