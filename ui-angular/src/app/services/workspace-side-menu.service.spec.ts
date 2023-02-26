import {TestBed} from '@angular/core/testing';

import {WorkspaceSideMenuService} from '@app/service/workspace-side-menu.service';

describe('WorkspaceSideMenuService', () => {
    beforeEach(() => TestBed.configureTestingModule({}));

    it('should be created', () => {
        const service: WorkspaceSideMenuService = TestBed.get(WorkspaceSideMenuService);
        expect(service).toBeTruthy();
    });
});
