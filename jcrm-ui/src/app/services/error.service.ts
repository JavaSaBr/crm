import {Injectable} from '@angular/core';
import {MatSnackBar} from '@angular/material';
import {ErrorResponse} from '@app/error/error-response';

@Injectable({
    providedIn: 'root'
})
export class ErrorService {

    constructor(private readonly snackBar: MatSnackBar) {
    }

    showErrorResponse(response: ErrorResponse) {

        if (!response) {
            return;
        }

        this.snackBar.open(response.errorMessage, 'X', {
            duration: 5000,
        });
    }

    showError(message: string) {

        if (!message) {
            return;
        }

        this.snackBar.open(message, 'X', {
            duration: 5000,
        });
    }
}
