'use strict';

angular.module('edrBudgetAppRiaApp')
  .filter('afronden', function () {
    return function (input) {
      return Math.round(input * 100.0) / 100.0;
    };
  });
