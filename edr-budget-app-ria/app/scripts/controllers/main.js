'use strict';

angular.module('edrBudgetAppRiaApp')
  .controller('MainCtrl', ['$scope', '$http', 'baseRestPath', function ($scope, $http, baseRestPath) {
	  
	$http({method: "GET", url: baseRestPath+'werkgebied'})
	  .success(function(data){ 
		  $scope.werkgebied = data;
	  }
	);
	
	var hash = window.location.hash;
	if (hash == '')
	{
		hash = '#/';
	}
	$('#mainMenu a[ng-href="' + hash + '"]').closest('li').addClass("active");
	
	$scope.clickMenu = function($event)
	{
		$($event.target).closest('ul').children('li').removeClass("active");
		$($event.target).closest('li').addClass("active");
	}
  }]);
