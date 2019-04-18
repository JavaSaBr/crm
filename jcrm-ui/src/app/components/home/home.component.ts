import {Component, OnInit} from '@angular/core';
import {Router} from '@angular/router';
import {SecurityService} from '../../services/security.service';

@Component({
    selector: 'app-home',
    templateUrl: './home.component.html',
    styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

    constructor(
        private readonly router: Router,
        private readonly securityService: SecurityService
    ) {
    }

    ngOnInit() {

        if (!this.securityService.isAuthenticated()) {
            this.router.navigate(['/no-auth']);
        }

        console.log('');
    }
}
