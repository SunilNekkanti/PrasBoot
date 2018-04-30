(function(){
'use strict';
var app = angular.module('my-app');

app.service('MembershipHospitalizationService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllMembershipHospitalizations: loadAllMembershipHospitalizations,
                loadMembershipHospitalizations: loadMembershipHospitalizations,
                getAllMembershipHospitalizations: getAllMembershipHospitalizations,
                getMembershipHospitalization: getMembershipHospitalization,
                createMembershipHospitalization: createMembershipHospitalization,
                updateMembershipHospitalization: updateMembershipHospitalization,
                removeMembershipHospitalization: removeMembershipHospitalization
            };

            return factory;

            function loadAllMembershipHospitalizations(insIds, prvdrIds) {
                console.log('Fetching all membershipHospitalizations');
                var deferred = $q.defer();
                var pageable = {
                 		 page:0, size:20, insIds:insIds,prvdrIds:prvdrIds
                 		};

                 		var config = {
                 		 params: pageable,
                 		 headers : {'Accept' : 'application/json'}
                 		};
                $http.get(urls.MEMBERSHIP_HOSPITALIZATION_SERVICE_API, config )
                    .then(
                        function (response) {
                            console.log('Fetched successfully all membershipHospitalizations');
                            if (localStorage.getItem("memberships") === null) {
                            	 $localStorage.memberships = response.data.content;
                           }else {
                           	localStorage.removeItem("membershipHospitalizations") ;
                             	$localStorage.memberships = response.data.content;
                           }
                            
                           
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading membershipHospitalizations');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function loadMembershipHospitalizations(pageNo, length, search, order, insIds, prvdrIds) {
                console.log('Fetching  MembershipHospitalizations');
                var pageable = {
                		 page:pageNo, size:length,sort: order,search: search||'',insIds:insIds,prvdrIds:prvdrIds
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.MEMBERSHIP_HOSPITALIZATION_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  membershipHospitalizations');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading membershipHospitalizations');
                            return   errResponse ;
                        }
                    );
            }
            
            function getAllMembershipHospitalizations(){
            	console.log('$localStorage.membershipHospitalizations');
                return $localStorage.memberships;
            }

            function getMembershipHospitalization(id) {
                console.log('Fetching MembershipHospitalization with id :'+id);
                var deferred = $q.defer();
                $http.get(urls.MEMBERSHIP_HOSPITALIZATION_SERVICE_API + id)
                    .then(
                        function (response) {
                            console.log('Fetched successfully MembershipHospitalization with id :'+id);
                            deferred.resolve(response.data);
                            return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading user with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function createMembershipHospitalization(user) {
                console.log('Creating MembershipHospitalization');
                var deferred = $q.defer();
                $http.post(urls.MEMBERSHIP_HOSPITALIZATION_SERVICE_API, user)
                    .then(
                        function (response) {
                            loadAllMemberships();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                           console.error('Error while creating MembershipHospitalization : '+errResponse.data.errorMessage);
                           deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateMembershipHospitalization(user, id) {
                console.log('Updating MembershipHospitalization with id '+id);
                var deferred = $q.defer();
                $http.put(urls.MEMBERSHIP_HOSPITALIZATION_SERVICE_API + id, user)
                    .then(
                        function (response) {
                            loadAllMemberships();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating MembershipHospitalization with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function removeMembershipHospitalization(id) {
                console.log('Removing MembershipHospitalization with id '+id);
                var deferred = $q.defer();
                $http.delete(urls.MEMBERSHIP_HOSPITALIZATION_SERVICE_API + id)
                    .then(
                        function (response) {
                            loadAllMemberships();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing MembershipHospitalization with id :'+id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

        }
    ]);
   })();