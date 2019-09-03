import {TestBed} from '@angular/core/testing';
import {UserRepository} from '@app/repository/user/user.repository';

describe('UserRepository', () => {
    beforeEach(() => TestBed.configureTestingModule({}));
    it('should be created', () => {
        const service: UserRepository = TestBed.get(UserRepository);
        expect(service).toBeTruthy();
    });
});
