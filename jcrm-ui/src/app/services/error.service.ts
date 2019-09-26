import {Injectable} from '@angular/core';
import {MatSnackBar} from "@angular/material";
import {ErrorResponse} from '@app/error/error-response';
import {TranslateService} from '@ngx-translate/core';

@Injectable({
    providedIn: 'root'
})
export class ErrorService {

    constructor(
        private readonly translateService: TranslateService,
        private readonly snackBar: MatSnackBar
    ) {
    }

    showErrorResponse(response: ErrorResponse) {

        if (!response) {
            return
        }

        this.snackBar.open(this.translateService.instant(`ERROR.ERROR.${response.errorCode}`), 'X', {
            duration: 5000,
        });
    }

    showError(message: string) {

        if (!message) {
            return
        }

        this.snackBar.open(message, 'X', {
            duration: 5000,
        });
    }
}
