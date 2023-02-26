import {TestBed} from '@angular/core/testing';
import {SecurityService} from '@app/service/security.service';

describe('SecurityService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: SecurityService = TestBed.get(SecurityService);
        expect(service).toBeTruthy();
    });
});
