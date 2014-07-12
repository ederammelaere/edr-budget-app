'use strict';

describe('Controller: BoekingCtrl', function () {

  // load the controller's module
  beforeEach(module('edrBudgetAppRiaApp'));

  var BoekingCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    BoekingCtrl = $controller('BoekingCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
