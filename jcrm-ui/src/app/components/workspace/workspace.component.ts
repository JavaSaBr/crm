import {AfterViewInit, Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {SecurityService} from '@app/service/security.service';
import {NoAuthHomeComponent} from '@app/component/no-auth-home/no-auth-home.component';
import {WorkspaceService} from '@app/service/workspace.service';

export abstract class BaseWorkspaceComponent implements AfterViewInit {

    protected constructor(protected readonly workspaceService: WorkspaceService) {
    }

    ngAfterViewInit(): void {
        setTimeout(() => this.workspaceService.activate(this));
    }

    getTitle(): string | null {
        return null;
    }

    isFullScreen(): boolean {
        return false;
    }

    isNeedGlobalSearch(): boolean {
        return false;
    }

    isNeedGlobalMenu(): boolean {
        return false;
    }

    isNeedContentPadding(): boolean {
        return false;
    }
}

@Component({
    selector: 'app-workspace',
    templateUrl: './workspace.component.html',
    styleUrls: ['./workspace.component.css'],
    host: {'class': 'flex-column'}
})
export class WorkspaceComponent implements OnInit {

    public static readonly COMPONENT_NAME = 'workspace';

    currentTitle: string;
    ready: boolean;

    constructor(
        private readonly router: Router,
        private readonly securityService: SecurityService,
        private readonly workspaceService: WorkspaceService
    ) {
        this.ready = false;
        this.currentTitle = '';
        this.workspaceService.component
            .subscribe(value => this.currentTitle = value ? value.getTitle() : null);
    }

    ngOnInit(): void {
        this.ready = false;

        if (this.securityService.isAuthenticated()) {
            this.ready = true;
            return;
        }

        this.securityService.tryToRestoreToken()
            .then(restored => {
                if (!restored) {
                    this.router.navigate(['/' + NoAuthHomeComponent.COMPONENT_PATH]);
                } else {
                    this.ready = true;
                }
            });
    }
}
