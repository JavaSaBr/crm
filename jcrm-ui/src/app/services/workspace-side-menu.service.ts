import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, Subject} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class WorkspaceSideMenuService {

    private readonly startOpening: Subject<void>;
    private readonly startClosing: Subject<void>;
    private readonly changing: BehaviorSubject<boolean>;
    private readonly open: BehaviorSubject<boolean>;
    private readonly requestMenu: Subject<boolean>;

    constructor() {
        this.startOpening = new Subject();
        this.startClosing = new Subject();
        this.requestMenu = new Subject();
        this.open = new BehaviorSubject(false);
        this.changing = new BehaviorSubject(false);
    }

    isChanging() {
        return this.changing.getValue();
    }

    startOpeningProperty(): Observable<void> {
        return this.startOpening;
    }

    notifyStartOpening() {
        this.startOpening.next();
    }

    startClosingProperty(): Observable<void> {
        return this.startClosing;
    }

    notifyStartClosing() {
        this.startClosing.next();
    }

    openProperty(): Observable<boolean> {
        return this.open;
    }

    isOpen(): boolean {
        return this.open.getValue();
    }

    notifyOpened() {
        this.open.next(true);
    }

    notifyClosed() {
        this.open.next(false);
    }

    requestMenuProperty(): Observable<boolean> {
        return this.requestMenu;
    }

    toggleMenu(open?: boolean) {

        if (this.isChanging()) {
            return;
        }

        if (open === null) {
            this.requestMenu.next(!this.isOpen());
        } else {
            this.requestMenu.next(open);
        }
    }

    changingProperty(): Observable<boolean> {
        return this.changing;
    }

    notifyStartChanging() {
        this.changing.next(true);
    }

    notifyFinishChanging() {
        this.changing.next(false);
    }
}
