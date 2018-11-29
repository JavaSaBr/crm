import {Component, OnInit, ViewChild} from '@angular/core';
import {MatSidenav} from '@angular/material';
import {SideMenuService} from '../../services/side-menu.service';

@Component({
    selector: 'app-side-nav',
    templateUrl: './side-nav.component.html',
    styleUrls: ['./side-nav.component.scss']
})
export class SideNavComponent {

    @ViewChild(MatSidenav)
    matSidenav: MatSidenav;

    constructor(private sideMenuService: SideMenuService) {
        this.sideMenuService.requestToggleProperty()
            .subscribe(() => {
                this.toggleMenu();
            });
        this.matSidenav.openedStart
    }

    private toggleMenu() {
        this.sideMenuService.notifyStartChanging();
        this.matSidenav.toggle()
            .then(result => {
                this.sideMenuService.notifyFinishChanging();
                if (result === 'open') {
                    this.sideMenuService.notifyOpened();
                } else {
                    this.sideMenuService.notifyClosed();
                }
            });
    }
}
