'use strict';

var edrBudgetAppRiaApp = angular.module('edrBudgetAppRiaApp', [
  'ngCookies',
  'ngResource',
  'ngSanitize',
  'ngRoute'
])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'views/main.html',
        controller: 'MainCtrl'
      })
      .when('/boekrekening', {
        templateUrl: 'views/boekrekening.html',
        controller: 'BoekrekeningCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });

if(window.location.host=='localhost:9000') {
	edrBudgetAppRiaApp.constant('baseRestPath','http://localhost:44300/edr-budget-app-rest/rest/');
} else {
	edrBudgetAppRiaApp.constant('baseRestPath','/pensmand-rest/rest/');
}
