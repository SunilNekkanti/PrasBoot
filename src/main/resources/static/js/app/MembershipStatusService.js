(function(){
'use strict';
var app = angular.module('my-app');

app.service('MembershipStatusService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllMembershipStatuses: loadAllMembershipStatuses,
                loadMembershipStatuses : loadMembershipStatuses,
                getAllMembershipStatuses: getAllMembershipStatuses,
                getMembershipStatus: getMembershipStatus,
                createMembershipStatus: createMembershipStatus,
                updateMembershipStatus: updateMembershipStatus,
                removeMembershipStatus: removeMembershipStatus
            };

            return factory;

            function loadMembershipStatuses(pageNo, length, search, order) {
                console.log('Fetching  MembershipStatuses');
                var deferred = $q.defer();
                var pageable = {
                 		 page:pageNo, size:length,search: search||''
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
            return     $http.get(urls.STATUS_SERVICE_API, config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  MembershipStatuses');
                            $localStorage.membershipStatuses = response.data.content;
                            deferred.resolve(response);
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading MembershipStatuss');
                            deferred.reject(errResponse);
                            return   errResponse ;
                        }
                    );
            }
            function loadAllMembershipStatuses() {
                console.log('Fetching all MembershipStatuses');
                var deferred = $q.defer();
                $http.get(urls.STATUS_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all MembershipStatuss');
                            $localStorage.membershipStatuses = response.data.content;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading MembershipStatuss');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllMembershipStatuses(){
            	console.log('$localStorage.MembershipStatuss');
                return $localStorage.membershipStatuses;
            }

            function getMembershipStatus(id) {
                console.log('Fetching MembershipStatus with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.STATUS_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully MembershipStatus with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createMembershipStatus(user) {
                console.log('Creating MembershipStatus');
                var deferred = $q.defer();
                $http.post(urls.STATUS_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllMembershipStatuses();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating MembershipStatus : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateMembershipStatus(user, id) {
                console.log('Updating MembershipStatus with id '+id);
                var deferred = $q.defer();
                $http.put(urls.STATUS_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllMembershipStatuses();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating MembershipStatus with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeMembershipStatus(id) {
                console.log('Removing MembershipStatus with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.STATUS_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllMembershipStatuses();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing MembershipStatus with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();