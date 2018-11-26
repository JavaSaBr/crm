import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    constructor() {
    }

    isAuthenticated(): boolean {
        return true;
    }
}
