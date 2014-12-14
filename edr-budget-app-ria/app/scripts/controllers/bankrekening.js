'use strict';

angular.module('edrBudgetAppRiaApp').factory('Bankrekening', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'bankrekening/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BankrekeningCtrl', ['$scope', 'Bankrekening', 'MyModalWindow', function ($scope, Bankrekening, MyModalWindow) {
	  
	function refresh()
	{
		$scope.bankrekeningen = Bankrekening.query();
	}
	
	refresh();
    
    $scope.save = function(formObj) {
    	Bankrekening.save(addIdObject(formObj), formObj, succesHandler(refresh), errorHandler); 
    };
    
    $scope.verwijderen = function(index) {
    	Bankrekening.remove({ id:$scope.bankrekeningen[index].id }, succesHandler(refresh), errorHandler); 
    };
    
    $scope.bijwerken = function(index) {
    	var formObj = angular.copy($scope.bankrekeningen[index]);
    	MyModalWindow.openModal(formObj, $scope.save, 'bankrekeningModal.html');
    };
    
    $scope.toevoegen = function(index) {
    	MyModalWindow.openModal({}, $scope.save, 'bankrekeningModal.html');
    };
            
  }]);

