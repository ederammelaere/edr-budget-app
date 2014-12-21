'use strict';

angular.module('edrBudgetAppRiaApp').factory('Budget', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'budget/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BudgetCtrl', ['$scope', 'Budget', 'Boekrekening', 'MyModalWindow', function ($scope, Budget, Boekrekening, MyModalWindow) {
    
	function refresh()
	{
		$scope.budgetten = Budget.query({'jaar' : $scope.jaar});
	}
		
	$scope.jaar = new Date().getFullYear();
	refresh();
	
	$scope.$watch("jaar", function(newValue){ 
		if (newValue > 2000 && newValue < 3000) refresh(); });
	
    $scope.boekrekeningen = Boekrekening.query();
    
    $scope.save = function(formObj) {
    	Budget.save(addIdObject(formObj), formObj, succesHandler(refresh), errorHandler);
    };
    
    $scope.verwijderen = function(index) {
    	Budget.remove({ id:$scope.budgetten[index].id }, succesHandler(refresh), errorHandler); 
    };
    
    $scope.bijwerken = function(index) {
    	var formObj = angular.copy($scope.budgetten[index]);
    	MyModalWindow.openModal(formObj, $scope.save, 'budgetModal.html');
    };
    
    $scope.toevoegen = function() {
    	MyModalWindow.openModal({}, $scope.save, 'budgetModal.html');
    };
    
    $scope.reset = function(){
    	resetFormObj($scope);
    };
    
  }]);
