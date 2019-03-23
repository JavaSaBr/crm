import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class NoAuthHomeService {

    public currentSubPage: string;

    constructor() {
        this.currentSubPage = null;
    }

    hasSubPage(): boolean {
        return this.currentSubPage != null;
    }

    activateSubPage(currentSubPage: string) {
        this.currentSubPage = currentSubPage;
    }
}
