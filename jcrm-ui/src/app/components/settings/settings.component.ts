import {Component, OnInit} from '@angular/core';
import {BaseWorkspaceComponent, WorkspaceComponent} from '@app/component/workspace/workspace.component';
import {WorkspaceService} from '@app/service/workspace.service';
import {ActivatedRoute, Router} from '@app/node-modules/@angular/router';
import {UsersComponent} from '@app/component/users/users.component';

@Component({
    selector: 'app-settings',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.css'],
})
export class SettingsComponent extends BaseWorkspaceComponent implements OnInit {

    public static readonly COMPONENT_NAME = 'settings';

    private static readonly INDEX_TO_TAB: Map<number, string> = new Map<number, string>([
        [0, 'undefined1'],
        [1, UsersComponent.COMPONENT_NAME],
        [2, 'undefined2']
    ]);

    private static readonly TAB_TO_INDEX: Map<string, number> = new Map<string, number>([
        ['undefined1', 0],
        [UsersComponent.COMPONENT_NAME, 1],
        ['undefined2', 2]
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
                this.selectedTab = SettingsComponent.TAB_TO_INDEX.get(tabName);
            }
        });
    }

    changeSelectedTab(index: number) {
        this.router.navigate([
            WorkspaceComponent.COMPONENT_NAME,
            SettingsComponent.COMPONENT_NAME,
            SettingsComponent.INDEX_TO_TAB.get(index)
        ]);
    }
}
