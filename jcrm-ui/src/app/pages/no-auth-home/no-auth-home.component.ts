import {Component} from '@angular/core';
import {NoAuthHomeService} from '../../services/no-auth-home.service';

@Component({
    selector: 'app-no-auth-home',
    templateUrl: './no-auth-home.component.html',
    styleUrls: ['./no-auth-home.component.scss']
})
export class NoAuthHomeComponent {

    constructor(public noAuthHomeService: NoAuthHomeService) {
    }
}
