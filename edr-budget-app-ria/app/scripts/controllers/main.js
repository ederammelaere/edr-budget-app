'use strict';

angular.module('edrBudgetAppRiaApp')
  .controller('MainCtrl', ['$scope', '$http', 'baseRestPath', function ($scope, $http, baseRestPath) {
	  
	$http({method: "GET", url: baseRestPath+'werkgebied'})
	  .success(function(data){ 
		  $scope.werkgebied = data;
	  }
	);
  }]);
