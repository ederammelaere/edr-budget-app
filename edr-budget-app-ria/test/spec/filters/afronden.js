'use strict';

describe('Filter: afronden', function () {

  // load the filter's module
  beforeEach(module('edrBudgetAppRiaApp'));

  // initialize a new instance of the filter before each test
  var afronden;
  beforeEach(inject(function ($filter) {
    afronden = $filter('afronden');
  }));

  it('should return the input prefixed with "afronden filter:"', function () {
    var text = 'angularjs';
    expect(afronden(text)).toBe('afronden filter: ' + text);
  });

});
