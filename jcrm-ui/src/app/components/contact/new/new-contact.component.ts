import {Component, OnInit, Type} from '@angular/core';
import {BaseWorkspaceComponent, ObjectCreationWorkspaceComponent} from '@app/component/workspace/workspace.component';
import {WorkspaceService} from '@app/service/workspace.service';
import {Contact} from '@app/entity/contact';

@Component({
    selector: 'app-new-contact',
    templateUrl: './new-contact.component.html',
    styleUrls: ['./new-contact.component.css'],
})
export class NewContactComponent extends ObjectCreationWorkspaceComponent implements OnInit {

    contact: Contact | null;

    constructor(protected readonly workspaceService: WorkspaceService) {
        super(workspaceService);
        this.contact = null;
    }

    getComponentType(): Type<BaseWorkspaceComponent> {
        return NewContactComponent;
    }

    protected getTitle(): string | null {
        return 'Contact view';
    }

    ngOnInit(): void {
        this.contact = new Contact();
        this.contact.firstName = 'Test first name';
        this.contact.secondName = 'Test second name';
        this.contact.thirdName = 'Test third name';
    }
}
