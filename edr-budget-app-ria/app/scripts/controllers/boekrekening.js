'use strict';

angular.module('edrBudgetAppRiaApp').factory('Boekrekening', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'boekrekening/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekrekeningCtrl', ['$scope', 'Boekrekening', 'MyModalWindow', function ($scope, Boekrekening, MyModalWindow) {
	  
	function refresh()
	{
		$scope.boekrekeningen = Boekrekening.query();
	}
	
	refresh();
            
    $scope.save = function(formObj) {
    	Boekrekening.save(addIdObject(formObj), formObj, succesHandler(refresh), errorHandler);
    };
    
    $scope.verwijderen = function(index) {
    	Boekrekening.remove({ id:$scope.boekrekeningen[index].id }, succesHandler(refresh),	errorHandler);
    };
    
    $scope.bijwerken = function(index) {
    	var formObj = angular.copy($scope.boekrekeningen[index]);
    	MyModalWindow.openModal(formObj, $scope.save, 'boekrekeningModal.html');
    };
    
    $scope.toevoegen = function() {
    	MyModalWindow.openModal({}, $scope.save, 'boekrekeningModal.html');
    };
        
}]);
