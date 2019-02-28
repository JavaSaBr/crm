import {TestBed} from '@angular/core/testing';

import {CountryRepository} from './country.repository';

describe('CountryRepository', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: CountryRepository = TestBed.get(CountryRepository);
        expect(service).toBeTruthy();
    });
});
