import {Component} from '@angular/core';
import {UserService} from './services/user.service';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.scss']
})
export class AppComponent {

    constructor(private userService: UserService) {
    }

    isAuthenticated() {
        return this.userService.isAuthenticated();
    }
}
