import {Injectable, Type} from '@angular/core';
import {Observable, Subject} from 'rxjs';

@Injectable({
    providedIn: 'root'
})
export class WorkspaceService {

    private readonly _currentComponent: Subject<Type<any>>;

    constructor() {
        this._currentComponent = new Subject();
    }

    activate(type: Type<any>) {
        this._currentComponent.next(type);
    }

    get currentComponent(): Observable<Type<any>> {
        return this._currentComponent;
    }
}
