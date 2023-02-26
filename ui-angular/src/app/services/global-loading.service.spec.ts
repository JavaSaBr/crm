import {TestBed} from '@angular/core/testing';
import {GlobalLoadingService} from '@app/service/global-loading.service';

describe('Error Service', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: GlobalLoadingService = TestBed.get(GlobalLoadingService);
        expect(service).toBeTruthy();
    });
});
