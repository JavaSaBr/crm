import {TestBed} from '@angular/core/testing';
import {UserGroupRepository} from '@app/repository/user-group/user-group-repository.service';

describe('UserGroupRepository', () => {
    beforeEach(() => TestBed.configureTestingModule({}));
    it('should be created', () => {
        const service: UserGroupRepository = TestBed.get(UserGroupRepository);
        expect(service).toBeTruthy();
    });
});
