import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {BaseWorkspaceComponent} from '@app/component/workspace/base-workspace.component';

@Injectable({
    providedIn: 'root'
})
export class WorkspaceService {

    private readonly _component: BehaviorSubject<BaseWorkspaceComponent>;

    constructor() {
        this._component = new BehaviorSubject(null);
    }

    activate(component: BaseWorkspaceComponent) {
        this._component.next(component);
    }

    get component(): Observable<BaseWorkspaceComponent> {
        return this._component;
    }
}
