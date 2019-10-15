import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class GlobalLoadingService {

    private _loadingCount: BehaviorSubject<number> = new BehaviorSubject<number>(0);

    constructor() {
    }

    get loadingCount(): Observable<number> {
        return this._loadingCount;
    }

    increaseLoading(): void {
        this._loadingCount.next(this._loadingCount.value + 1);
    }

    decreaseLoading(): void {
        this._loadingCount.next(this._loadingCount.value - 1);
    }
}
