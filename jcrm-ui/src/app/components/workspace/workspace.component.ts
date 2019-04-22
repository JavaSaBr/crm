import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {SecurityService} from '../../services/security.service';

@Component({
    selector: 'app-workspace',
    templateUrl: './workspace.component.html',
    styleUrls: ['./workspace.component.css'],
    host: {'class': 'flex-column'}
})
export class WorkspaceComponent implements OnInit {

    ready: boolean;

    constructor(
        private readonly router: Router,
        private readonly securityService: SecurityService
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
                    this.router.navigate(['/no-auth']);
                } else {
                    this.ready = true;
                }
            });
    }
}
