'use strict';

angular.module('edrBudgetAppRiaApp').factory('Boeking', ['$resource', 'baseRestPath', function ($resource, baseRestPath) {
  return $resource(baseRestPath + 'boeking/:id', {id: '@id'});
}]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekingCtrl', ['$scope', 'Boeking', 'MyModalWindow', '$modal', function ($scope, Boeking, MyModalWindow, $modal) {

    function refresh() {
      $scope.boekingen = Boeking.query({'jaar': $scope.jaar});
    }

    $scope.jaar = new Date().getFullYear();
    refresh();

    $scope.$watch("jaar", function (newValue, oldValue) {
      if (newValue === oldValue) return;
      if (newValue > 2000 && newValue < 3000) refresh();
    });

    $scope.save = function (formObj) {
      formObj.datum = formObj.datum.split("-").reverse().toString().replace(/,/g, "/");
      Boeking.save(addIdObject(formObj), formObj, succesHandler(refresh), errorHandler);
    };

    $scope.verwijderen = function (index) {
      Boeking.remove({id: $scope.boekingen[index].id}, succesHandler(refresh), errorHandler);
    };

    $scope.bijwerken = function (index) {
      var formObj = angular.copy($scope.boekingen[index]);
      formObj.datum = formObj.datum.split("/").reverse().toString().replace(/,/g, "-");
      MyModalWindow.openModal(formObj, $scope.save, 'boekingModal.html');
    };

    $scope.toevoegen = function () {
      MyModalWindow.openModal({}, $scope.save, 'boekingModal.html');
    };

    $scope.inputFilter = "";
    $scope.tekstFilter = function (boeking) {
      if ($scope.inputFilter == "") {
        return true;
      }
      else {
        var regexpFilter = RegExp($scope.inputFilter, "i");

        return regexpFilter.test(boeking.bankrekening.rekeningnr) ||
          regexpFilter.test(boeking.bankrekening.omschrijving) ||
          regexpFilter.test(boeking.boekrekening.rekeningnr) ||
          regexpFilter.test(boeking.boekrekening.omschrijving) ||
          regexpFilter.test(boeking.omschrijving);
      }
    };

    $scope.toevoegenManueel = function () {
      var modalInstance = $modal.open({
        templateUrl: 'boekingManueelModal.html',
        controller: 'BoekingModalInstanceCtrl',
        windowClass: 'app-modal-window',
      });

      modalInstance.result.then(function (boekingen) {
        for (var i = 0; i < boekingen.length; i++) {
          $scope.save(boekingen[i]);
        }
      });
    };
  }]);

angular.module('edrBudgetAppRiaApp')
  .controller('BoekingModalInstanceCtrl', ['$scope', '$modalInstance', '$http', 'baseRestPath',
    function ($scope, $modalInstance, $http, baseRestPath) {

      $scope.boekdatum = Date.today().set({day: 1}).addMonths(-1).toString('yyyy-MM-dd');
      $scope.referentiedatum = Date.today().set({day: 1}).addMonths(-2).toString('yyyy-MM-dd');

      $scope.search = function () {

        var myurl = baseRestPath + 'boeking/manueel/' + $scope.referentiedatum.replace(/-/g,'/');

        $http({
          method: "GET",
          url: myurl
        })
          .success(function (data) {
            $scope.boekingen = data;
          });
      };

      $scope.search();

      $scope.verwijderen = function (index) {
        $scope.boekingen.splice(index, 1);
      };

      $scope.save = function () {
        for(var i = 0; i<$scope.boekingen.length; i++){
          $scope.boekingen[i].datum = $scope.boekdatum;
          delete $scope.boekingen[i].id;
        }

        $modalInstance.close($scope.boekingen);
      };

      $scope.reset = function () {
        $modalInstance.dismiss('cancel');
      };

    }]);
