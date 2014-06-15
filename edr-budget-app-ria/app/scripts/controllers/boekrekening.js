'use strict';

angular.module('edrBudgetAppRiaApp').factory('Boekrekening', ['$resource', function($resource) {
	  return $resource('/boekrekening/:rekeningnr',
	        {rekeningnr: '@rekeningnr'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekrekeningCtrl', ['$scope', 'Boekrekening', function ($scope, Boekrekening) {
    $scope.boekrekeningen = Boekrekening.query();
  }]);
