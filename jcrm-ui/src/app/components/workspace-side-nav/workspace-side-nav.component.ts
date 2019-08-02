import {Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {MatHorizontalStepper, MatSidenav} from '@angular/material';
import {SideMenuService} from '../../services/side-menu.service';

@Component({
    selector: 'app-workspace-side-nav',
    templateUrl: './workspace-side-nav.component.html',
    styleUrls: ['./workspace-side-nav.component.scss'],
    host: {'class': 'flex-column'}
})
export class WorkspaceSideNavComponent implements OnInit {

    @ViewChild(MatSidenav, {static: true})
    matSidenav: MatSidenav;

    constructor(private sideMenuService: SideMenuService) {
        this.sideMenuService.requestMenuProperty()
            .subscribe(open => {
                this.toggleMenu(open);
            });
    }

    ngOnInit() {
        this.matSidenav.openedStart.subscribe(() => {
            this.sideMenuService.notifyStartOpening();
        });
        this.matSidenav.closedStart.subscribe(() => {
            this.sideMenuService.notifyStartClosing();
        });
    }

    private toggleMenu(open: boolean) {

        if (this.matSidenav.opened === open) {
            return;
        }

        this.sideMenuService.notifyStartChanging();
        this.matSidenav.toggle()
            .then(result => {

                if (result === 'open') {
                    this.sideMenuService.notifyOpened();
                } else {
                    this.sideMenuService.notifyClosed();
                }

                this.sideMenuService.notifyFinishChanging();
            });
    }
}
