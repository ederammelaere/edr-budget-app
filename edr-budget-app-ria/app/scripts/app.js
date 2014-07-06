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
      .when('/bankrekening', {
        templateUrl: 'views/bankrekening.html',
        controller: 'BankrekeningCtrl'
      })
      .when('/budgetstaat', {
        templateUrl: 'views/budgetstaat.html',
        controller: 'BudgetstaatCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });

edrBudgetAppRiaApp.constant('baseRestPath','http://localhost:8080/edr-budget-app-rest/rest/');
