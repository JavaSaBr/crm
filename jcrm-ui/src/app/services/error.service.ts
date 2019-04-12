import {Injectable} from '@angular/core';
import {MatSnackBar} from "@angular/material";

@Injectable({
    providedIn: 'root'
})
export class ErrorService {

    constructor(private readonly snackBar: MatSnackBar) {
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
