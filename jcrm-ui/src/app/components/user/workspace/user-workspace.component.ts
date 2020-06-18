import {Component} from '@angular/core';
import {WorkspaceService} from '@app/service/workspace.service';
import {ActivatedRoute} from '@angular/router';
import {GlobalLoadingService} from '@app/service/global-loading.service';
import {ErrorService} from '@app/service/error.service';
import {UserViewComponent} from '@app/component/user/view/user-view.component';
import {EntityViewWorkspaceComponent} from '@app/component/workspace/entity/view/entity-view-workspace.component';
import {User} from '@app/entity/user';
import {UserRepository} from '@app/repository/user/user.repository';

@Component({
    selector: 'app-user-workspace',
    templateUrl: './user-workspace.component.html',
    styleUrls: ['./user-workspace.component.css'],
})
export class UserWorkspaceComponent extends EntityViewWorkspaceComponent<User, UserViewComponent> {

    public static readonly componentName = 'user';

    public static readonly modeNew = 'new';
    public static readonly modeView = 'view';

    constructor(
        private readonly userRepository: UserRepository,
        route: ActivatedRoute,
        globalLoadingService: GlobalLoadingService,
        errorService: ErrorService,
        workspaceService: WorkspaceService
    ) {
        super(route, globalLoadingService, errorService, workspaceService);
    }

    getTitle(): string | null {
        return 'User view';
    }

    loadEntityById(id: number): Promise<User | null> {
        return this.userRepository.findById(id);
    }

    newEntity(): User {
        return User.create();
    }
}
