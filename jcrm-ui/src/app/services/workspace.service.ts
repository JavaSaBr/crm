import {Injectable, Type} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {BaseWorkspaceComponent} from '../components/workspace/workspace.component';

export enum WorkspaceMode {
    DEFAULT = 'default',
    OBJECT_CREATION = 'object_creation'
}

@Injectable({
    providedIn: 'root'
})
export class WorkspaceService {

    private readonly _currentComponent: BehaviorSubject<Type<BaseWorkspaceComponent>>;
    private readonly _workspaceMode: BehaviorSubject<WorkspaceMode>;

    constructor() {
        this._currentComponent = new BehaviorSubject(null);
        this._workspaceMode = new BehaviorSubject(WorkspaceMode.DEFAULT);
    }

    activate(type: Type<BaseWorkspaceComponent>) {
        this._currentComponent.next(type);
    }

    switchWorkspaceMode(mode: WorkspaceMode) {
        this._workspaceMode.next(mode);
    }

    get currentComponent(): BehaviorSubject<Type<BaseWorkspaceComponent>> {
        return this._currentComponent;
    }

    get workspaceMode(): BehaviorSubject<WorkspaceMode> {
        return this._workspaceMode;
    }
}
