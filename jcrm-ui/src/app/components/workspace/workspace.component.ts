import {AfterViewInit, Component, OnInit, Type} from '@angular/core';
import {Router} from '@angular/router';
import {SecurityService} from '../../services/security.service';
import {NoAuthHomeComponent} from '../no-auth-home/no-auth-home.component';
import {WorkspaceMode, WorkspaceService} from '../../services/workspace.service';

export abstract class BaseWorkspaceComponent implements AfterViewInit {

    protected constructor(protected readonly workspaceService: WorkspaceService) {
    }

    ngAfterViewInit(): void {
        setTimeout(() => {
            this.workspaceService.activate(this.getComponentType());
            this.workspaceService.switchWorkspaceMode(this.getWorkspaceMode());
        }, 10);
    }

    protected getWorkspaceMode(): WorkspaceMode {
        return WorkspaceMode.DEFAULT;
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

    ready: boolean;

    constructor(
        private readonly router: Router,
        private readonly securityService: SecurityService,
        private readonly workspaceService: WorkspaceService
    ) {
        this.ready = false;
    }

    ngOnInit() {
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
