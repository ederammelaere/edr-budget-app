'use strict';

angular.module('edrBudgetAppRiaApp').factory('Bankrekening', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'bankrekening/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BankrekeningCtrl', ['$scope', 'Bankrekening', function ($scope, Bankrekening) {
	  
	function refresh()
	{
		$scope.bankrekeningen = Bankrekening.query();
	}
	
	refresh();
	resetFormObj($scope);
    
    $scope.save = function() {
    	var param = addId($scope);
    	
    	Bankrekening.save(param, $scope.formObj, succesHandler(refresh), errorHandler); 

    	resetFormObj($scope);
    };
    
    $scope.verwijderen = function(index) {
    	Bankrekening.remove({ id:$scope.bankrekeningen[index].id }, succesHandler(refresh), errorHandler); 
    };
    
    $scope.bijwerken = function(index) {
    	$scope.formObj = angular.copy($scope.bankrekeningen[index]);
    };
    
    $scope.reset = function(){
    	resetFormObj($scope);
    };
    
  }]);
