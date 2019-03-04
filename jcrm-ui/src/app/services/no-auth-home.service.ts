import {Injectable} from '@angular/core';

@Injectable({
    providedIn: 'root'
})
export class NoAuthHomeService {

    private currentSubPage: string = null;

    constructor() {
    }

    hasSubPage() {
        return this.currentSubPage != null;
    }

    public activateSubPage(page: string) {
        this.currentSubPage = page;
    }

    getCurrentSubPage() {
        return this.currentSubPage;
    }
}
