(function(){
'use strict';
var app = angular.module('my-app');
app.service(
				'MembershipProblemService',
				[
						'$localStorage',
						'$http',
						'$q',
						'urls',
						function($localStorage, $http, $q, urls) {

							var factory = {
								loadMembershipProblem : loadMembershipProblem,
								getMembershipProblems  : getMembershipProblems,
								updateMembershipProblem :updateMembershipProblem,
								addMembershipProblems :addMembershipProblems
							};

							return factory;

							function loadMembershipProblem(pageNo, length,
									search, insId, prvdrId, hedisRuless,
									reportMonth, startDate, endDate, capss,
									rosterss, statusess) {
								console
										.log('Fetching  MembershipProblems');
								var pageable = {
									page : pageNo,
									size : length,
									search : search || '',
									insId : insId,
									prvdrId : prvdrId,
									reportMonth : reportMonth,
									hedisRules : hedisRuless,
									startDate : startDate,
									endDate : endDate,
									statuses : statusess,
									rosters : rosterss,
									caps : capss
								};

								var config = {
									params : pageable,
									headers : {
										'Accept' : 'application/json'
									}
								};
								return $http
										.get(urls.HEDIS_REPORT_SERVICE_API,
												config)
										.then(
												function(response) {
													console
															.log('Fetched successfully  membershipHediss');
													return response;
												},
												function(errResponse) {
													console
															.error('Error while loading membershipHediss');
													return errResponse;
												});
							}

							function getMembershipProblems(mbrId,
									rulesList) {
								console
										.log('Fetching  MembershipProblem per rules provided');
								var pageable = {
									mbrId : mbrId,
									hedisRules : rulesList
								};

								var config = {
									params : pageable,
									headers : {
										'Accept' : 'application/json'
									}
								};
								return $http
										.get(
												urls.HEDIS_REPORT_MEMBERSHIP_SERVICE_API,
												config)
										.then(
												function(response) {
													console
															.log('Fetched successfully  membershipHediss');
													return response;
												},
												function(errResponse) {
													console
															.error('Error while loading membershipHediss');
													return errResponse;
												});
							}
							
							
							function updateMembershipProblem(mbrHedisMeasure, id) {
				                console.log('Updating mbrHedisMeasure with id '+id);
				                var deferred = $q.defer();
				                $http.put(urls.HEDIS_REPORT_MEMBERSHIP_SERVICE_API + id, mbrHedisMeasure)
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
				            
				            
				            function addMembershipProblems(membershipProblems, membershipId) {
								console
										.log('adding  MembershipProblems');

								var config = {
									params :  {
									membershipProblems : membershipProblems,
									membershipId : membershipId
								},
									headers : {
										'Accept' : 'application/json'
									}
								};
								return $http
										.post(urls.MEMBERSHIP_PROBLEM_SERVICE_API,
												config)
										.then(
												function(response) {
													console.log('Fetched successfully  membershipHediss');
													return response;
												},
												function(errResponse) {
													console.error('Error while loading membershipHediss');
													return errResponse;
												});
							}
				            
				            

						} ]);
})();