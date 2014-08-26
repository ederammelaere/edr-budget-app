'use strict';

angular.module('edrBudgetAppRiaApp').factory('Budget', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'budget/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BudgetCtrl', ['$scope', 'Budget', 'Boekrekening', function ($scope, Budget, Boekrekening) {
    
	function refresh()
	{
		$scope.budgetten = Budget.query({'jaar' : $scope.jaar});
	}
		
	$scope.jaar = new Date().getFullYear();
	refresh();
	
	$scope.$watch("jaar", function(newValue){ 
		if (newValue > 2000 && newValue < 3000) refresh(); });
	
	resetFormObj($scope);
	
    $scope.boekrekeningen = Boekrekening.query();
    
    $scope.save = function() {
    	var param = addId($scope);
    	Budget.save(param, $scope.formObj, succesHandler(refresh), errorHandler);
    	resetFormObj($scope);
    };
    
    $scope.verwijderen = function(index) {
    	Budget.remove({ id:$scope.budgetten[index].id }, succesHandler(refresh), errorHandler); 
    };
    
    $scope.bijwerken = function(index) {
    	$scope.formObj = angular.copy($scope.budgetten[index]);
    };
    
    $scope.reset = function(){
    	resetFormObj($scope);
    };
    
  }]);
