import {Component, OnInit} from '@angular/core';
import { WorkspaceComponent} from '@app/component/workspace/workspace.component';
import {WorkspaceService} from '@app/service/workspace.service';
import {ActivatedRoute, Router} from '@app/node-modules/@angular/router';
import {UsersComponent} from '@app/component/users/users.component';
import {BaseWorkspaceComponent} from '@app/component/workspace/base-workspace.component';
import {UserGroupsComponent} from '@app/component/user-groups/user-groups.component';

@Component({
    selector: 'app-settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.css'],
})
export class SettingsComponent extends BaseWorkspaceComponent implements OnInit {

    public static readonly componentName = 'settings';

    private static readonly indexToTab: Map<number, string> = new Map<number, string>([
        [0, 'undefined1'],
        [1, UsersComponent.componentName],
        [2, UserGroupsComponent.componentName]
    ]);

    private static readonly tabToIndex: Map<string, number> = new Map<string, number>([
        ['undefined1', 0],
        [UsersComponent.componentName, 1],
        [UserGroupsComponent.componentName, 2]
    ]);

    selectedTab: number = 0;

    constructor(
        private readonly router: Router,
        private readonly route: ActivatedRoute,
        workspaceService: WorkspaceService
    ) {
        super(workspaceService);
    }

    isNeedGlobalMenu(): boolean {
        return true;
    }

    ngOnInit(): void {
        this.route.paramMap.subscribe(value => {
            const tabName = value.get('selectedTab');
            if (tabName) {
                this.selectedTab = SettingsComponent.tabToIndex.get(tabName);
            }
        });
    }

    changeSelectedTab(index: number) {
        this.router.navigate([
            WorkspaceComponent.COMPONENT_NAME,
            SettingsComponent.componentName,
            SettingsComponent.indexToTab.get(index)
        ]);
    }
}
