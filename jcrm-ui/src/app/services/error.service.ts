import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material';
import {ErrorResponse} from '@app/error/error-response';
import {TranslateService} from '@ngx-translate/core';
import {HttpErrorResponse} from '@angular/common/http';

@Injectable({
    providedIn: 'root'
})
export class ErrorService {

    constructor(
        private readonly snackBar: MatSnackBar,
        private readonly translateService: TranslateService
    ) {
    }

    convertError(reason: any): Promise<any | null> {
        if (reason instanceof HttpErrorResponse) {
            return Promise.reject(ErrorResponse.convertToErrorOrNull(reason, this.translateService));
        } else {
            return Promise.reject(reason);
        }
    }

    showError(error: any): void {
        if (error && error instanceof ErrorResponse) {
            this.snackBar.open(error.errorMessage, 'X', {
                duration: 5000,
            });
        }
    }

    showErrorResponse(response: ErrorResponse): void {
        if (response) {
            this.snackBar.open(response.errorMessage, 'X', {
                duration: 5000,
            });
        }
    }

    showMessage(message: string): void {
        if (message) {
            this.snackBar.open(message, 'X', {
                duration: 5000,
            });
        }
    }
}
