'use strict';

angular.module('edrBudgetAppRiaApp').factory('Boekrekening', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'boekrekening/');
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekrekeningCtrl', ['$scope', 'Boekrekening', function ($scope, Boekrekening) {
    $scope.boekrekeningen = Boekrekening.query();
  }]);
