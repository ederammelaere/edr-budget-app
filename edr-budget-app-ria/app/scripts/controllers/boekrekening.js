'use strict';

angular.module('edrBudgetAppRiaApp').factory('Boekrekening', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'boekrekening/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekrekeningCtrl', ['$scope', 'Boekrekening', function ($scope, Boekrekening) {
	  
	function refresh()
	{
		$scope.boekrekeningen = Boekrekening.query();
	}
	
	refresh();
    resetFormObj($scope);
        
    $scope.save = function() {
    	addFalse($scope, "budgeteerbaar");
    	addFalse($scope, "boekbaar");
    	var param = addId($scope);

    	Boekrekening.save(param, $scope.formObj, succesHandler(refresh), errorHandler);
    	
    	resetFormObj($scope);
    };
    
    $scope.verwijderen = function(index) {
    	Boekrekening.remove({ id:$scope.boekrekeningen[index].id }, succesHandler(refresh),	errorHandler);
    };
    
    $scope.bijwerken = function(index) {
    	$scope.formObj = angular.copy($scope.boekrekeningen[index]);
    };
    
    $scope.reset = function(){
    	resetFormObj($scope);
    };
    
  }]);
