'use strict';

angular.module('edrBudgetAppRiaApp').factory('Journaal', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'journaal?jaar=:jaar', {jaar:'@jaar'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('JournaalCtrl', ['$scope', 'Journaal', function ($scope, Journaal) {
    $scope.journaal = Journaal.query({'jaar' : new Date().getFullYear()});
    $scope.jaar = new Date().getFullYear();
    $scope.refresh = function(){
    	$scope.journaal = Journaal.query({'jaar' : $scope.jaar});
    };
    $scope.editeerZichtbaar = new Array;
    for (var i=0; i<$scope.journaal.length; i++)
    {
    	$scope.editeerZichtbaar[i] = false;
    }
    
    $scope.bewerken = function(index){
    	for (var i=0; i<$scope.journaal.length; i++)
        {
        	$scope.editeerZichtbaar[i] = false;
        }
    	$scope.editeerZichtbaar[index] = true;
    };
  }]);
