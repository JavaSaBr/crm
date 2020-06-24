import {Component} from '@angular/core';
import {FabButtonElement} from '@app/component/fab-button/fab-button.component';
import {Router} from '@angular/router';
import {User} from '@app/entity/user';
import {UserRepository} from '@app/repository/user/user.repository';
import {EntityPage} from '@app/entity/entity-page';
import {DatePipe} from '@angular/common';
import {ErrorService} from '@app/service/error.service';
import {GlobalLoadingService} from '@app/service/global-loading.service';
import {AbstractEntityTableComponent} from '@app/component/table/abstract-entity-table.component';
import {UserWorkspaceComponent} from '@app/component/user/workspace/user-workspace.component';
import {WorkspaceComponent} from '@app/component/workspace/workspace.component';
import {SettingsComponent} from '@app/component/settings/settings.component';

@Component({
    selector: 'app-users',
    templateUrl: './users.component.html',
    styleUrls: ['./users.component.css'],
    host: {'class': 'flex-column'}
})
export class UsersComponent extends AbstractEntityTableComponent<User> {

    public static readonly componentName = 'users';

    private static readonly fabActions: FabButtonElement[] = [
        {
            routerLink: `../${UserWorkspaceComponent.componentName}/${UserWorkspaceComponent.modeNew}`,
            icon: 'perm_identity',
            tooltip: 'Add new user',
            callback: null
        },
    ];

    private static readonly displayedColumns: string[] = [
        'select',
        'creation_date',
        'full_name',
        'email',
        'actions',
    ];

    constructor(
        private readonly userRepository: UserRepository,
        private readonly router: Router,
        datePipe: DatePipe,
        errorService: ErrorService,
        globalLoadingService: GlobalLoadingService
    ) {
        super(datePipe, errorService, globalLoadingService);
    }

    createDisplayedColumns(): string[] {
        return UsersComponent.displayedColumns;
    }

    createFabActions(): FabButtonElement[] {
        return UsersComponent.fabActions;
    }

    loadEntityPage(pageSize: number, offset: number): Promise<EntityPage<User>> {
        return this.userRepository.findEntityPage(pageSize, offset);
    }

    createdToString(user: User): string {

        const now = new Date();
        const currentDate = now.getDate();

        const created = user.created;
        const createdDate = created.getDate();

        if (currentDate === createdDate) {
            return `Today ${this.datePipe.transform(created, 'HH:mm')}`;
        } else {
            return `${this.datePipe.transform(created, 'yyyy/mm/dd HH:mm')}`;
        }
    }

    openEntity(user: User) {
        this.router.navigate([
            WorkspaceComponent.COMPONENT_NAME,
            SettingsComponent.componentName,
            UserWorkspaceComponent.componentName,
            UserWorkspaceComponent.modeView,
            user.id
        ]);
    }
}
