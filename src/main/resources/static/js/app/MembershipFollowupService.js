(function(){
'use strict';
var app = angular.module('my-app');

app.service('MembershipFollowupService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllMembershipFollowups: loadAllMembershipFollowups,
                getAllMembershipFollowups: getAllMembershipFollowups,
                getMembershipFollowup: getMembershipFollowup,
                createMembershipFollowup: createMembershipFollowup,
                updateMembershipFollowup: updateMembershipFollowup,
                removeMembershipFollowup: removeMembershipFollowup,
                getAllMembershipFollowupsPerMbr: getAllMembershipFollowupsPerMbr
            };

            return factory;

            function loadAllMembershipFollowups() {
                console.log('Fetching all membershipFollowups');
                var deferred = $q.defer();
                $http.get(urls.GENDER_SERVICE_API)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all membershipFollowups');
                            $localStorage.membershipFollowups = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading membershipFollowups');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllMembershipFollowups(){
            	console.log('$localStorage.membershipFollowups');
                return $localStorage.membershipFollowups;
            }

            function getMembershipFollowup(id) {
                console.log('Fetching MembershipFollowup with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.MEMBERSHIP_FOLLOWUP_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully MembershipFollowup with id :'+id);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createMembershipFollowup(mbrFollowup) {
                console.log('Creating MembershipFollowup');
                var deferred = $q.defer();
                $http.post(urls.MEMBERSHIP_FOLLOWUP_SERVICE_API, mbrFollowup)
                    .then(
                        function (response) {
                            loadAllMembershipFollowups();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating MembershipFollowup : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateMembershipFollowup(mbrFollowup, id) {
                console.log('Updating MembershipFollowup with id '+id);
                var deferred = $q.defer();
                $http.put(urls.MEMBERSHIP_FOLLOWUP_SERVICE_API + id, mbrFollowup)
                    .then(
                        function (response) {
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating MembershipFollowup with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeMembershipFollowup(id) {
                console.log('Removing MembershipFollowup with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.MEMBERSHIP_FOLLOWUP_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllMembershipFollowups();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing MembershipFollowup with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            function getAllMembershipFollowupsPerMbr(mbrId) {
                console.log('Fetching MembershipFollowups with mbrId :'+mbrId);
                var deferred = $q.defer();
                $http.get(urls.MEMBERSHIP_FOLLOWUPDETAILS_SERVICE_API + mbrId)
                    .then(
                        function (response) {
                            console.log('Fetched successfully MembershipFollowup with mbrId :'+mbrId);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while loading MembershipFollowup with mbrId :'+mbrId);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }


        }
    ]);
   })();