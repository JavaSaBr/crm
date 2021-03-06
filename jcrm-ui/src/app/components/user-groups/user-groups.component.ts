import {Component} from '@angular/core';
import {FabButtonElement} from '@app/component/fab-button/fab-button.component';
import {Router} from '@angular/router';
import {EntityPage} from '@app/entity/entity-page';
import {DatePipe} from '@angular/common';
import {ErrorService} from '@app/service/error.service';
import {GlobalLoadingService} from '@app/service/global-loading.service';
import {AbstractEntityTableComponent} from '@app/component/table/abstract-entity-table.component';
import {UserWorkspaceComponent} from '@app/component/user/workspace/user-workspace.component';
import {WorkspaceComponent} from '@app/component/workspace/workspace.component';
import {SettingsComponent} from '@app/component/settings/settings.component';
import {UserGroup} from '@app/entity/user-group';
import {UserGroupRepository} from '@app/repository/user-group/user-group-repository.service';
import {UserGroupWorkspaceComponent} from '@app/component/user-group/workspace/user-group-workspace.component';

@Component({
    selector: 'app-user-groups',
    templateUrl: './user-groups.component.html',
    styleUrls: ['./user-groups.component.css'],
    host: {'class': 'flex-column'}
})
export class UserGroupsComponent extends AbstractEntityTableComponent<UserGroup> {

    public static readonly componentName = 'user-groups';

    private static readonly fabActions: FabButtonElement[] = [
        {
            routerLink: `../${UserGroupWorkspaceComponent.componentName}/${UserGroupWorkspaceComponent.modeNew}`,
            icon: 'group_add',
            tooltip: 'Add new group',
            callback: null
        },
    ];

    private static readonly displayedColumns: string[] = [
        'select',
        'creation_date',
        'name',
        'modified_date',
        'actions',
    ];

    constructor(
        private readonly userGroupRepository: UserGroupRepository,
        private readonly router: Router,
        datePipe: DatePipe,
        errorService: ErrorService,
        globalLoadingService: GlobalLoadingService
    ) {
        super(datePipe, errorService, globalLoadingService);
    }

    createDisplayedColumns(): string[] {
        return UserGroupsComponent.displayedColumns;
    }

    createFabActions(): FabButtonElement[] {
        return UserGroupsComponent.fabActions;
    }

    loadEntityPage(pageSize: number, offset: number): Promise<EntityPage<UserGroup>> {
        return this.userGroupRepository.findEntityPage(pageSize, offset);
    }

    dateToString(date: Date): string {

        const now = new Date();
        const currentDate = now.getDate();
        const createdDate = date.getDate();

        if (currentDate === createdDate) {
            return `Today ${this.datePipe.transform(date, 'HH:mm')}`;
        } else {
            return `${this.datePipe.transform(date, 'yyyy/mm/dd HH:mm')}`;
        }
    }

    openEntity(userGroup: UserGroup) {
        this.router.navigate([
            WorkspaceComponent.COMPONENT_NAME,
            SettingsComponent.componentName,
            UserGroupWorkspaceComponent.componentName,
            UserGroupWorkspaceComponent.modeView,
            userGroup.id
        ]);
    }
}
