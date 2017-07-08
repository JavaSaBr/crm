import { TestBed, inject } from '@angular/core/testing';

import { BlankService } from './blank.service';

describe('BlankService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BlankService]
    });
  });

  it('should be created', inject([BlankService], (service: BlankService) => {
    expect(service).toBeTruthy();
  }));
});
