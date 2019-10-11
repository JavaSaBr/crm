import {Injectable, Type} from '@angular/core';
import {BehaviorSubject} from 'rxjs';
import {BaseWorkspaceComponent} from '../components/workspace/workspace.component';

export enum WorkspaceMode {
    DEFAULT = 'default',
    OBJECT_EDITING = 'object_editing'
}

@Injectable({
    providedIn: 'root'
})
export class WorkspaceService {

    private readonly _currentComponent: BehaviorSubject<Type<BaseWorkspaceComponent>>;
    private readonly _currentTitle: BehaviorSubject<string>;
    private readonly _workspaceMode: BehaviorSubject<WorkspaceMode>;

    constructor() {
        this._currentComponent = new BehaviorSubject(null);
        this._currentTitle = new BehaviorSubject(null);
        this._workspaceMode = new BehaviorSubject(WorkspaceMode.DEFAULT);
    }

    activate(type: Type<BaseWorkspaceComponent>, title: string | null = null) {
        this._currentComponent.next(type);
        this._currentTitle.next(title);
    }

    switchWorkspaceMode(mode: WorkspaceMode): void {
        this._workspaceMode.next(mode);
    }

    get currentComponent(): BehaviorSubject<Type<BaseWorkspaceComponent>> {
        return this._currentComponent;
    }

    get currentTitle(): BehaviorSubject<string> {
        return this._currentTitle;
    }

    get workspaceMode(): BehaviorSubject<WorkspaceMode> {
        return this._workspaceMode;
    }
}
