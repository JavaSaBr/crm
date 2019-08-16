import {Component, Type} from '@angular/core';
import {BaseWorkspaceComponent, ObjectCreationWorkspaceComponent} from '../workspace/workspace.component';
import {WorkspaceService} from '../../services/workspace.service';

@Component({
    selector: 'app-new-contact',
    templateUrl: './new-contact.component.html',
    styleUrls: ['./new-contact.component.css'],
})
export class NewContactComponent extends ObjectCreationWorkspaceComponent {

    constructor(protected readonly workspaceService: WorkspaceService) {
        super(workspaceService)
    }

    getComponentType(): Type<BaseWorkspaceComponent> {
        return NewContactComponent;
    }
}
