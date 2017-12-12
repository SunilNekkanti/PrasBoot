(function(){
'use strict';
var app = angular.module('my-app');

app.service('MembershipHedisService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadMembershipHedis: loadMembershipHedis,
                getMembershipHedisMeasures: getMembershipHedisMeasures
            };

            return factory;
 
            function loadMembershipHedis(pageNo, length, search, insId, prvdrId, hedisRuless,reportMonth, startDate,endDate,capss,rosterss,statusess) {
                console.log('Fetching  MembershipHediss');
                var pageable = {
                		 page:pageNo, size:length, search: search||'', insId:insId,prvdrId:prvdrId, reportMonth:reportMonth,hedisRules:hedisRuless,startDate:startDate,endDate:endDate,statuses:statusess,rosters:rosterss,caps:capss
                  		};

                  		var config = {
                  		 params: pageable,
                  		 headers : {'Accept' : 'application/json'}
                  		};
            return     $http.get(urls.HEDIS_REPORT_SERVICE_API,  config)
                    .then(
                        function (response) {
                            console.log('Fetched successfully  membershipHediss');
                         return     response ;
                        },
                        function (errResponse) {
                            console.error('Error while loading membershipHediss');
                            return   errResponse ;
                        }
                    );
            }
            
            function  getMembershipHedisMeasures(  mbrId, rulesList){
            	  console.log('Fetching  MembershipHedis per rules provided');
                  var pageable = {
                		  mbrId:mbrId,rulesList:rulesList
                   		};

                   		var config = {
                   		 params: pageable,
                   		 headers : {'Accept' : 'application/json'}
                   		};
             return     $http.get(urls.HEDIS_REPORT_MEMBERSHIP_SERVICE_API,  config)
                     .then(
                         function (response) {
                             console.log('Fetched successfully  membershipHediss');
                          return     response ;
                         },
                         function (errResponse) {
                             console.error('Error while loading membershipHediss');
                             return   errResponse ;
                         }
                     );
            }

           
        }
    ]);
})();