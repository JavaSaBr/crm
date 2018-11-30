import {Component, OnInit, ViewChild} from '@angular/core';
import {MatSidenav} from '@angular/material';
import {SideMenuService} from '../../services/side-menu.service';

@Component({
    selector: 'app-side-nav',
    templateUrl: './side-nav.component.html',
    styleUrls: ['./side-nav.component.scss']
})
export class SideNavComponent implements OnInit {

    @ViewChild(MatSidenav)
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
