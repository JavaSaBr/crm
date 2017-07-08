import { TestBed, inject } from '@angular/core/testing';

import { BlankTokenService } from './blank-token.service';

describe('BlankTokenService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BlankTokenService]
    });
  });

  it('should be created', inject([BlankTokenService], (service: BlankTokenService) => {
    expect(service).toBeTruthy();
  }));
});
