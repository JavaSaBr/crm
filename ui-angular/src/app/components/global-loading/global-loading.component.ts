import {Component} from '@angular/core';
import {GlobalLoadingService} from '@app/service/global-loading.service';

@Component({
    selector: 'app-global-loading',
    templateUrl: './global-loading.component.html',
    styleUrls: ['./global-loading.component.css'],
})
export class GlobalLoadingComponent {

    loading: boolean;

    constructor(globalLoadingService: GlobalLoadingService) {
        globalLoadingService.loadingCount
            .subscribe(count => this.loading = count > 0);
    }
}
