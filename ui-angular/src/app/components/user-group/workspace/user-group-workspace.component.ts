import {Component} from '@angular/core';
import {WorkspaceService} from '@app/service/workspace.service';
import {ActivatedRoute} from '@angular/router';
import {GlobalLoadingService} from '@app/service/global-loading.service';
import {ErrorService} from '@app/service/error.service';
import {EntityViewWorkspaceComponent} from '@app/component/workspace/entity/view/entity-view-workspace.component';
import {UserGroup} from '@app/entity/user-group';
import {UserGroupRepository} from '@app/repository/user-group/user-group-repository.service';
import {UserGroupViewComponent} from '@app/component/user-group/view/user-group-view.component';

@Component({
    selector: 'app-user-group-workspace',
    templateUrl: './user-group-workspace.component.html',
    styleUrls: ['./user-group-workspace.component.css'],
})
export class UserGroupWorkspaceComponent extends EntityViewWorkspaceComponent<UserGroup, UserGroupViewComponent> {

    public static readonly componentName = 'user-group';

    public static readonly modeNew = 'new';
    public static readonly modeView = 'view';

    constructor(
        private readonly userGroupRepository: UserGroupRepository,
        route: ActivatedRoute,
        globalLoadingService: GlobalLoadingService,
        errorService: ErrorService,
        workspaceService: WorkspaceService
    ) {
        super(route, globalLoadingService, errorService, workspaceService);
    }

    getTitle(): string | null {
        return 'Group view';
    }

    loadEntityById(id: number): Promise<UserGroup | null> {
        return this.userGroupRepository.findById(id);
    }

    newEntity(): UserGroup {
        return UserGroup.create();
    }
}
