import {TestBed} from '@angular/core/testing';

import {NoAuthHomeService} from './no-auth-home.service';

describe('NoAuthHomeService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: NoAuthHomeService = TestBed.get(NoAuthHomeService);
        expect(service).toBeTruthy();
    });
});
