import {AfterViewInit, Component, OnInit, Type} from '@angular/core';
import {Router} from '@angular/router';
import {SecurityService} from '@app/service/security.service';
import {NoAuthHomeComponent} from '@app/component/no-auth-home/no-auth-home.component';
import {WorkspaceMode, WorkspaceService} from '@app/service/workspace.service';

export abstract class BaseWorkspaceComponent implements AfterViewInit {

    protected constructor(protected readonly workspaceService: WorkspaceService) {
    }

    ngAfterViewInit(): void {
        setTimeout(() => {
            this.workspaceService.activate(this.getComponentType(), this.getTitle());
            this.workspaceService.switchWorkspaceMode(this.getWorkspaceMode());
        });
    }

    protected getWorkspaceMode(): WorkspaceMode {
        return WorkspaceMode.DEFAULT;
    }

    protected getTitle(): string | null {
        return null;
    }

    abstract getComponentType(): Type<BaseWorkspaceComponent>;
}

export abstract class ObjectCreationWorkspaceComponent extends BaseWorkspaceComponent {

    protected getWorkspaceMode(): WorkspaceMode {
        return WorkspaceMode.OBJECT_CREATION;
    }
}

@Component({
    selector: 'app-workspace',
    templateUrl: './workspace.component.html',
    styleUrls: ['./workspace.component.css'],
    host: {'class': 'flex-column'}
})
export class WorkspaceComponent implements OnInit {

    public static readonly COMPONENT_PATH = 'workspace';

    currentTitle: string;
    ready: boolean;

    constructor(
        private readonly router: Router,
        private readonly securityService: SecurityService,
        private readonly workspaceService: WorkspaceService
    ) {
        this.ready = false;
        this.currentTitle = '';
        this.workspaceService.currentTitle
            .subscribe(value => this.currentTitle = value);
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
