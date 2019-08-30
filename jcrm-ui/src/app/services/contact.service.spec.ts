import {TestBed} from '@angular/core/testing';
import {ContactService} from '@app/service/contact.service';

describe('SecurityService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: ContactService = TestBed.get(ContactService);
        expect(service).toBeTruthy();
    });
});
