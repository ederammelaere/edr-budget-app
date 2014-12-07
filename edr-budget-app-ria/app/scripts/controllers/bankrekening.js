'use strict';

angular.module('edrBudgetAppRiaApp').factory('Bankrekening', ['$resource', 'baseRestPath', function($resource, baseRestPath) {
	  return $resource(baseRestPath + 'bankrekening/:id', {id:'@id'});
	}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BankrekeningCtrl', ['$scope', 'Bankrekening', '$modal', function ($scope, Bankrekening, $modal) {
	  
	function refresh()
	{
		$scope.bankrekeningen = Bankrekening.query();
	}
	
	refresh();
    
    $scope.save = function(formObj) {
    	var param = addId2(formObj);
    	Bankrekening.save(param, formObj, succesHandler(refresh), errorHandler); 
    };
    
    $scope.verwijderen = function(index) {
    	Bankrekening.remove({ id:$scope.bankrekeningen[index].id }, succesHandler(refresh), errorHandler); 
    };
    
    $scope.bijwerken = function(index) {
    	var formObj = angular.copy($scope.bankrekeningen[index]);
    	$scope.openModal(formObj);
    };
    
    $scope.toevoegen = function(index) {
    	$scope.openModal({});
    };
    
    $scope.openModal = function (formObj, actie) {

        var modalInstance = $modal.open({
          templateUrl: 'bankrekeningModal.html',
          controller: 'BankrekeningModalCtrl',
          resolve: {
              formObj: function () {
                return formObj;
              }
            }
        });
        
        modalInstance.formObj = formObj;
        
        modalInstance.result.then(function (formObj) {
            $scope.save(formObj);
          });
    };
    
  }]);

angular.module('edrBudgetAppRiaApp')
	.controller('BankrekeningModalCtrl', ['$scope', '$modalInstance', 'formObj', function ($scope, $modalInstance, formObj) {
		$scope.formObj = formObj;
		$scope.actie = (formObj.id) ? 'Bewerken' : 'Toevoegen';
		
		$scope.reset = function(){
	    	$modalInstance.dismiss('cancel');
	    };
	    $scope.save = function(){
	    	$modalInstance.close($scope.formObj);
	    };	    
	}]);
