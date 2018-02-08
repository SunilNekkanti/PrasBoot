(function() {
  'use strict';
  var app = angular.module('my-app');

  app.controller(
    'EventController', [
      'EventService',
      'EventTypeService',
      'UserService',
      'StateService',
      'FileUploadService',
      'EventFrequencyService',
      'EventMonthService',
      'EventWeekDayService',
      'EventWeekNumberService',
      '$scope',
      '$localStorage',
      '$state',
      '$stateParams',
      '$compile',
      '$sce',
      '$filter',
      'DTOptionsBuilder',
      'DTColumnBuilder',
      function(EventService, EventTypeService, UserService, StateService, FileUploadService, EventFrequencyService, EventMonthService, EventWeekDayService, EventWeekNumberService, $scope, $localStorage, $state, $stateParams, $compile, $sce, $filter,
        DTOptionsBuilder, DTColumnBuilder) {

        var self = this;
        self.event = {};
        self.events = [];
        self.myFiles = [];
        self.bool = false;
        self.eventTypes = [];
        self.eventFrequencies = ['DAILY', 'WEEKLY', 'MONTHLY', 'YEARLY'];
        self.eventIntervals = ['1', '2', '3', '4', '5'];
        self.eventOnDays = ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12', '13', '14', '15', '16', '17', '18', '19', '20', '21', '22', '23', '24', '25', '26', '27', '28', '29', '30', '31'];
        self.eventOnWeeks = [];
        self.eventEndOptions = ['Never', 'After', 'On date'];
        self.eventEndOption = 'Never';
        self.event.frequency = 'DAILY';
        self.eventOnWeekDays = [];
        self.onDayorThe = true;
        self.selectedWeekDays = [];
        self.eventEndCount = 1;
        self.eventUntil = new Date();
        self.eventMonths = [];
        self.eventMonth = {};
        self.users = [];
        self.display = $stateParams.eventDisplay || false;;
        self.states = [];
        self.displayEditButton = false;
        self.submit = submit;
        self.addEvent = addEvent;
        self.event.interval = self.event.interval || 1;
        self.getAllEvents = getAllEvents;
        self.createEvent = createEvent;
        self.updateEvent = updateEvent;
        self.removeEvent = removeEvent;
        self.editEvent = editEvent;
        self.getAllAgents = getAllAgents;
        self.getAllStates = getAllStates;
        self.getAllBrokerages = getAllBrokerages;
        self.getAllEventTypes = getAllEventTypes;
        self.getAllActivityTypes = getAllActivityTypes;
        self.getAllEventWeekNumbers = getAllEventWeekNumbers;
        self.getAllEventMonths = getAllEventMonths;
        self.getAllEventWeekDays = getAllEventWeekDays;
        self.getEventRepEmails = getEventRepEmails;
        self.parseEventRule = parseEventRule;
        self.findWeekDaysByShortNames = findWeekDaysByShortNames;
        self.findWeekDayByShortNames = findWeekDayByShortNames;
        self.readUploadedFile = readUploadedFile;
        self.cancelEdit = cancelEdit;
        self.adminOrManager = adminOrManager;
        self.uploadFile = uploadFile;
        self.eventrrule = eventrrule;
        self.convertToInt = convertToInt;
        //Lead from event
        self.addLead = addLead;
        self.isOnDayorThe = isOnDayorThe;
        self.sync = sync;
        self.isChecked = isChecked;
        self.dtInstance = {};
        self.eventId = null;
        self.reset = reset;
        self.repeatDisplay = false;
        self.repeat = repeat;
        self.eventEdit = eventEdit;
        self.successMessage = '';
        self.errorMessage = '';
        self.done = false;
        self.validEventDate = validEventDate;
        self.onlyIntegers = /^\d+$/;
        self.onlyNumbers = /^\d+([,.]\d+)?$/;
        self.checkBoxChange = checkBoxChange;
        self.errMessage = '';
        self.dtColumns = [
          DTColumnBuilder.newColumn('eventName')
          .withTitle('EVENT NAME')
          .withOption('defaultContent', '').renderWith(
            function(data, type, full,
              meta) {
              return '<a href="javascript:void(0)" class="' + full.id + '" ng-click="ctrl.eventEdit(' + full.id + ')">' + data + '</a>';
            }).withClass("text-left"),
          DTColumnBuilder.newColumn('eventDateStartTime')
          .withTitle('STARTDATE').renderWith(function(data, type) {
            return $filter('date')(new Date(data), 'MM/dd/yyyy'); //date filter
          }).withOption(
            'defaultContent', ''),
          DTColumnBuilder.newColumn('eventDateStartTime')
          .withTitle('STARTTIME').renderWith(function(data, type) {
            return $filter('date')(new Date(data), 'hh:mm a'); //date filter
          }).withOption(
            'defaultContent', ''),
          DTColumnBuilder.newColumn('eventDateEndTime')
          .withTitle('ENDDATE').renderWith(function(data, type) {
            return $filter('date')(new Date(data), 'MM/dd/yyyy'); //date filter
          }).withOption(
            'defaultContent', ''),

          DTColumnBuilder.newColumn('eventDateEndTime')
          .withTitle('ENDTIME').renderWith(function(data, type) {
            return $filter('date')(new Date(data), 'hh:mm a'); //date filter
          }).withOption(
            'defaultContent', ''),
          DTColumnBuilder.newColumn('eventType.description').withTitle(
            'FACILITY TYPE').withOption('defaultContent', ''),
          DTColumnBuilder.newColumn('contact.contactPerson')
          .withTitle('CONTACT PERSON').withOption(
            'defaultContent', ''),
          DTColumnBuilder.newColumn('contact.mobilePhone')
          .withTitle('CONTACT PHONE').withOption(
            'defaultContent', '')
        ];


        self.dtOptions = DTOptionsBuilder.newOptions()
          .withDisplayLength(20)
          .withOption('bServerSide', true)
          .withOption("bLengthChange", false)
          .withOption("bPaginate", true)
          .withOption('bProcessing', true)
          .withOption('bSaveState', true)
          .withOption('createdRow', createdRow)
          .withPaginationType('full_numbers')
          .withFnServerData(serverData);

        self.reloadData = reloadData;

        function createdRow(row, data, dataIndex) {
          // Recompiling so we can bind Angular directive
          // to the DT
          $compile(angular.element(row).contents())(
            $scope);
          console.log("test");
        }

        function checkBoxChange(checkStatus, eventId) {
          self.displayEditButton = checkStatus;
          self.eventId = eventId

        }

        function serverData(sSource, aoData, fnCallback) {
          // All the parameters you need is in the aoData
          // variable

          var order = aoData[2].value;
          var page = aoData[3].value / aoData[4].value;
          var length = aoData[4].value;
          var search = aoData[5].value;

          var paramMap = {};
          for (var i = 0; i < aoData.length; i++) {
            paramMap[aoData[i].name] = aoData[i].value;
          }

          var sortCol = '';
          var sortDir = '';
          // extract sort information
          if (paramMap['columns'] !== undefined && paramMap['columns'] !== null && paramMap['order'] !== undefined && paramMap['order'] !== null) {
            sortCol = paramMap['columns'][paramMap['order'][0]['column']].data;
            sortDir = paramMap['order'][0]['dir'];
          }

          // Then just call your service to get the
          // records from server side
          EventService
            .loadEvents(page, length, search.value, sortCol + ',' + sortDir)
            .then(
              function(result) {
                var records = {
                  'recordsTotal': result.data.totalElements || 0,
                  'recordsFiltered': result.data.totalElements || 0,
                  'data': result.data.content || {}
                };
                fnCallback(records);
              });
        }

        function reloadData() {
          var resetPaging = false;
          self.dtInstance.reloadData(callback,
            resetPaging);
        }

        function callback(json) {
          console.log(json);
        }

        function submit() {
          console.log('Submitting');
          self.event.repeatRule = eventrrule();
          uploadFile();

          self.displayEditButton = false;
        }

        function readUploadedFile(self_event_attachment_id, self_event_attachment_contentType) {
          console.log('About to read uploaded documents');

          FileUploadService.getFileUpload(self_event_attachment_id).then(
            function(response) {
              self.errorMessage = '';
              var file = new Blob([response], {
                type: self_event_attachment_contentType
              });
              var fileURL = URL.createObjectURL(file);
              self.content = $sce.trustAsResourceUrl(fileURL);

            },
            function(errResponse) {
              console
                .error('Error while reading consignment form');
              self.errorMessage = 'Error while reading consignment form: ' +
                errResponse.data.errorMessage;
              self.successMessage = '';
            });
        }

        function createEvent(event) {
          console.log('About to create event');
          EventService
            .createEvent(event)
            .then(
              function(response) {
                console
                  .log('Event created successfully');
                self.successMessage = 'Event created successfully';
                self.errorMessage = '';
                self.done = true;
                self.display = false;
                self.event = {};
                clearFiles();
                $scope.myForm
                  .$setPristine();
                self.dtInstance.reloadData();
                self.dtInstance.rerender();
                $state.go('main.event');
              },
              function(errResponse) {
                console
                  .error('Error while creating Event');
                self.errorMessage = 'Error while creating Event: ' +
                  errResponse.data.errorMessage;
                self.successMessage = '';
              });
        }

        function updateEvent(event, id) {
          console.log('About to update event');
          EventService
            .updateEvent(event, id)
            .then(
              function(response) {
                console
                  .log('Event updated successfully');
                self.successMessage = 'Event updated successfully';
                self.errorMessage = '';
                self.done = true;
                self.display = false;
                self.event = {};
                clearFiles();
                $scope.myForm
                  .$setPristine();
                self.dtInstance.reloadData();
                self.dtInstance.rerender();
                $state.go('main.event');
              },
              function(errResponse) {
                console
                  .error('Error while updating Event');
                self.errorMessage = 'Error while updating Event ' +
                  errResponse.data;
                self.successMessage = '';
              });
        }

        function removeEvent(id) {
          console.log('About to remove Event with id ' +
            id);
          EventService
            .removeEvent(id)
            .then(
              function() {
                console
                  .log('Event ' +
                    id +
                    ' removed successfully');
                self.dtInstance.reloadData();
                self.dtInstance.rerender();
              },
              function(errResponse) {
                console
                  .error('Error while removing event ' +
                    id +
                    ', Error :' +
                    errResponse.data);
              });
        }

        function editEvent(id) {

          self.successMessage = '';
          self.errorMessage = '';
          self.users = getAllAgents();
          self.states = getAllStates();
          self.eventTypes = getAllEventTypes();
          //self.eventFrequencies = getAllEventFrequencies();
          //self.eventMonths =getAllEventMonths();
          //self.eventOnWeekDays =getAllEventWeekDays();
          //self.eventOnWeeks =getAllEventWeekNumbers();
          EventService
            .getEvent(id)
            .then(
              function(event) {
                self.event = event;
                if (self.event.repeatRule === '') {
                  self.repeatDisplay = false;
                }
                self.display = true;
              },
              function(errResponse) {
                console
                  .error('Error while removing event ' +
                    id +
                    ', Error :' +
                    errResponse.data);
              });
        }

        function addEvent() {
          self.successMessage = '';
          self.errorMessage = '';
          var trans = $state.go('main.event.edit').transition;
          trans.onSuccess({}, function() {
            self.users = getAllAgents();
            self.states = getAllStates();
            self.eventTypes = getAllEventTypes();
            /*self.eventMonths =getAllEventMonths();
            self.eventMonth = self.eventMonths[0];
            self.eventOnWeekDays =getAllEventWeekDays();
            // self.eventOnWeekDaysss = self.eventOnWeekDays[0]; //not required
            self.eventOnWeeks =getAllEventWeekNumbers();
            self.eventOnWeek = self.eventOnWeeks[0];*/
            self.display = true;
          });
        }

        function reset() {
          self.successMessage = '';
          self.errorMessage = '';
          self.event = {};
          $scope.myForm.$setPristine(); // reset Form
        }

        function cancelEdit() {
          self.successMessage = '';
          self.errorMessage = '';
          self.event = {};
          $scope.myForm.$setPristine(); //reset Form
          self.display = false;
          $state.go('main.event', {}, {
            reload: true
          });
        }

        function convertToInt(id) {
          return parseInt(id, 10);
        }

        function uploadFile() {
          var promise = FileUploadService.uploadFileToUrl(self.myFiles);

          promise.then(function(response) {
            if (!self.event.attachments) {
              self.event.attachments = [];
            }
            self.event.attachments = response;
            if (self.repeatDisplay === false) {
              self.event.repeatRule = '';
            }
            if (self.event.id === undefined ||
              self.event.id === null) {

              createEvent(self.event);
            } else {
              updateEvent(self.event, self.event.id);
              console.log('Event updated with id ',
                self.event.id);
            }
            // createEvent(self.event);
          }, function() {
            self.serverResponse = 'An error has occurred';
          })
        };

        function eventrrule() {
          var rrule = [];

          var freq = 'FREQ=' + self.event.frequency;
          rrule.push(freq);
          var byDay;

          if (self.event.frequency == 'WEEKLY' && self.selectedWeekDays.length > 0) {
            self.selectedWeekDays = $filter('orderBy')(self.selectedWeekDays, 'id')
            byDay = ';BYDAY=' + self.selectedWeekDays.map(o => o.shortName).join();
            if (byDay) rrule.push(byDay);
          }

          if (self.event.frequency == 'MONTHLY') {
            if (self.onDayorThe && self.onDayorThe == true) {
              var byMonthDay = ';BYMONTHDAY=' + self.event.onDay;
              rrule.push(byMonthDay);
            } else {
              //	self.eventOnWeek = self.eventOnWeek||{};
              //	self.eventOnWeekDayss = self.eventOnWeekDayss||{};
              var byMonthWeekAndDay = ';BYSETPOS=' + self.eventOnWeek.id + ';BYDAY=' + self.eventOnWeekDay.shortName;
              rrule.push(byMonthWeekAndDay);
            }

          }

          if (self.event.frequency == 'YEARLY') {
            if (self.onDayorThe && self.onDayorThe == true) {
              var byMonthAndDay = ';BYMONTH=' + self.eventMonthOnDay.id + ';BYMONTHDAY=' + self.event.onDay;
              rrule.push(byMonthAndDay);

            } else {
              var byDayWeekNoAndMonth = ';BYDAY=' + self.eventOnWeekDay.shortName + ';BYSETPOS=' + self.eventOnWeek.id + ';BYMONTH=' + self.eventMonthOnThe.id;
              rrule.push(byDayWeekNoAndMonth);

            }

          }

          var interval = ';INTERVAL=' + self.event.interval
          if (self.event.interval) rrule.push(interval);

          if (self.eventEndOption == 'After') {
            var count = ';COUNT=' + self.eventEndCount;
            rrule.push(count);
          } else if (self.eventEndOption == 'On date') {
            var count = ';UNTIL=' + self.eventUntil;
            rrule.push(count);
          }

          return rrule.join('');

        }

        function parseEventRule() {

          var res = self.event.repeatRule.split(";");
          if (res.length > 1) {
            self.repeatDisplay = true;
            for (var i = 0; i < res.length; i++) {
              var ruleType = res[i].split("=");
              if (ruleType.length > 1) {
                switch (ruleType[0]) {
                  case "COUNT":
                    self.eventEndCount = ruleType[1];
                    self.eventEndOption = 'After';
                    break;
                  case "UNTIL":
                    self.eventUntil = ruleType[1];
                    self.eventEndOption == 'On date';
                    break;
                  case 'INTERVAL':
                    self.eventInterval = ruleType[1];
                    self.event.interval = ruleType[1];
                    break;
                  case 'BYDAY':
                    findWeekDayByShortNames(ruleType[1]);
                    findWeekDaysByShortNames(ruleType[1]);
                    break;
                  case 'BYSETPOS':
                    self.eventOnWeek = $filter('filter')(self.eventOnWeeks, {
                      id: parseInt(ruleType[1])
                    }, true)[0];
                    self.onDayorThe = false;
                    break;
                  case 'BYMONTH':
                    self.eventMonthOnDay = $filter('filter')(self.eventMonths, {
                      id: parseInt(ruleType[1])
                    }, true)[0];
                    self.onDayorThe = false;
                    break;
                  case 'BYMONTHDAY':
                    self.event.onDay = ruleType[1];
                    self.onDayorThe = true;
                    break;
                  case 'FREQ':
                    self.event.frequency = ruleType[1];
                    break;

                  default:
                    self.eventEndOption == 'Never';
                }
              }


            }
          }
        }

        function getEventRepEmails() {

          if (self.event.representatives) {
            var repEmails = self.event.representatives.map(function(agent) {
              return agent.email;
            });
            var str = repEmails.join();
            return str;
          }

          return "";

        }

        function adminOrManager() {
          if ($localStorage.loginUser.roleName === 'ADMIN' || $localStorage.loginUser.roleName === 'MANAGER') {
            return true;
          } else {
            return false;
          }
        }

        function repeat() {
          self.repeatDisplay = !self.repeatDisplay;
          if (self.repeatDisplay == true) {
            self.event.interval = 1;
            self.event.frequency = 'DAILY';
            self.event.onWeekDay = {
              "id": 1
            };
            self.event.onWeek = 'First';
            self.event.onDay = 1;
            self.event.month = {
              "id": 1
            };
            self.onDayorThe = true;
          } else {
            self.event.interval = 0;
            self.event.onWeekDay = {};
            self.event.onWeek = '';
            self.event.onDay = 0;
            //self.event.frequency = {};
            self.event.month = {};
            self.event.repeatRule = '';
          }
        }

        function addAgentEventAppointment() {
          self.event.agentEventAppointments.push(self.selectedAgentEventAppointment);
        }

        function getAllEventWeekDays() {
          return EventWeekDayService.getAllEventWeekDays();
        }

        function getAllEventMonths() {
          return EventMonthService.getAllEventMonths();
        }

        function getAllEventWeekNumbers() {
          return EventWeekNumberService.getAllEventWeekNumbers();
        }


        /*function getAllEventFrequencies(){
        	return EventFrequencyService.getAllEventFrequencies();
        }*/

        function getAllEvents() {
          return EventService.getAllEvents();
        }

        function getAllAgents() {
          return UserService
            .getAllUsers();
        }

        function getAllStates() {
          return StateService.getAllStates();
        }

        function getAllBrokerages() {
          return BrokerageService
            .getAllBrokerages();
        }

        function getAllEventTypes() {
          return EventTypeService
            .getAllEventTypes();
        }

        function getAllActivityTypes() {
          return ActivityTypeService
            .getAllActivityTypes();
        }


        function eventEdit(id) {
          //	var params = {"id":id,"eventDisplay":true};
          var trans = $state.go('main.event.edit').transition;
          trans.onSuccess({}, function() {

            editEvent(id);
          });
        }

        function addLead() {
          var params = {
            "eventId": self.event.id,
            "leadDisplay": true
          };
          $state.go('main.lead.edit', params);
        }

        function clearFiles() {
          angular.forEach(
            angular.element("input[type='file']"),
            function(inputElem) {
              angular.element(inputElem).val(null);
            });
        }

        function validEventDate(startDate, endDate) {
          console.log('startDate', new Date(startDate).getTime(), new Date(endDate).getTime());
          if (new Date(startDate).getTime() >= new Date(endDate).getTime()) {
            self.errMessage = 'End Date should be greater than start date';
            return true;
          }
          self.errMessage = '';
          return false;

        };


        function clear() {
          self.event.eventDateTime = null;
        }

        function isOnDayorThe() {
          if (self.onDayorThe) {
            self.event.month = self.event.month || {};
            self.event.month.id = self.event.month.id || 1;
          } else {
            self.event.month = self.event.month || {};
            self.event.month.id = self.event.month.id || 1;
            self.eventOnWeek = self.eventOnWeek || {};
            self.eventOnWeek.id = self.eventOnWeek.id || 1;
          }
        }

        function isChecked(id) {
          var match = false;
          for (var i = 0; i < self.selectedWeekDays.length; i++)
            if (self.selectedWeekDays[i].id == id) {
              match = true;
              break;
            }
          return match;
        };


        function sync(bool, item) {

          if (bool) {
            // add item
            self.selectedWeekDays.push(item);
          } else {
            // remove item
            for (var i = 0; i < self.selectedWeekDays.length; i++) {
              if (self.selectedWeekDays[i].id == item.id) {
                self.selectedWeekDays.splice(i, 1);
              }
            }
          }
        };

        function findWeekDaysByShortNames(shortNames) {
          self.selectedWeekDays = self.eventOnWeekDays.filter(function(weekday) {
            return shortNames.indexOf(weekday.shortName) > -1;
          });
        }

        function findWeekDayByShortNames(shortName) {
          self.eventOnWeekDay = self.eventOnWeekDays.filter(function(weekday) {
            return shortName.indexOf(weekday.shortName) > -1;
          })[0];
        }


      }
    ]);
})();
