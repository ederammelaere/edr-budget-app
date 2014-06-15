'use strict';

describe('Controller: BoekrekeningCtrl', function () {

  // load the controller's module
  beforeEach(module('edrBudgetAppRiaApp'));

  var BoekrekeningCtrl,
    scope;

  // Initialize the controller and a mock scope
  beforeEach(inject(function ($controller, $rootScope) {
    scope = $rootScope.$new();
    BoekrekeningCtrl = $controller('BoekrekeningCtrl', {
      $scope: scope
    });
  }));

  it('should attach a list of awesomeThings to the scope', function () {
    expect(scope.awesomeThings.length).toBe(3);
  });
});
