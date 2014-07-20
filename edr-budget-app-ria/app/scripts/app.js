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
      .when('/boeking', {
        templateUrl: 'views/boeking.html',
        controller: 'BoekingCtrl'
      })
      .when('/budget', {
        templateUrl: 'views/budget.html',
        controller: 'BudgetCtrl'
      })
      .when('/journaal', {
        templateUrl: 'views/journaal.html',
        controller: 'JournaalCtrl'
      })
      .otherwise({
        redirectTo: '/'
      });
  });

edrBudgetAppRiaApp.constant('baseRestPath','http://localhost:8080/edr-budget-app-rest/rest/');
