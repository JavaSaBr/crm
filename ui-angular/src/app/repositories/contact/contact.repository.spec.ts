import {TestBed} from '@angular/core/testing';
import {ContactRepository} from '@app/repository/contact/contact.repository';

describe('ContactRepository', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: ContactRepository = TestBed.get(ContactRepository);
        expect(service).toBeTruthy();
    });
});
