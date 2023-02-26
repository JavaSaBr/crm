import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class GlobalLoadingService {

    private readonly loadingCountProperty: BehaviorSubject<number>;

    constructor() {
        this.loadingCountProperty = new BehaviorSubject<number>(0);
    }

    get loadingCount(): Observable<number> {
        return this.loadingCountProperty;
    }

    increaseLoading(): void {
        this.loadingCountProperty.next(this.loadingCountProperty.value + 1);
    }

    decreaseLoading(): void {
        this.loadingCountProperty.next(this.loadingCountProperty.value - 1);
    }
}
