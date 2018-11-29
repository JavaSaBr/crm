import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, Subject} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class SideMenuService {

    private readonly changing: BehaviorSubject<boolean>;
    private readonly open: BehaviorSubject<boolean>;
    private readonly requestToggle: Subject<void>;

    constructor() {
        this.changing = new BehaviorSubject(false);
        this.open = new BehaviorSubject(false);
        this.requestToggle = new Subject();
    }

    isChanging() {
        return this.changing.getValue();
    }

    changingProperty(): Observable<boolean> {
        return this.changing;
    }

    openProperty(): Observable<boolean> {
        return this.open;
    }

    isOpen(): boolean {
        return this.open.getValue();
    }

    requestToggleProperty(): Observable<void> {
        return this.requestToggle;
    }

    toggleMenu() {
        this.requestToggle.next(null);
    }

    notifyStartChanging() {
        this.changing.next(true);
    }

    notifyFinishChanging() {
        this.changing.next(false);
    }

    notifyOpened() {
        this.open.next(true);
    }

    notifyClosed() {
        this.open.next(false);
    }
}
