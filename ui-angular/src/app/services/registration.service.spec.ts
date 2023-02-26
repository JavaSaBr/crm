import {TestBed} from '@angular/core/testing';
import {RegistrationService} from '@app/service/registration.service';

describe('SecurityService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: RegistrationService = TestBed.get(RegistrationService);
        expect(service).toBeTruthy();
    });
});
