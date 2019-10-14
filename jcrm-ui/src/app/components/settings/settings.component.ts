import {Component, OnInit, ViewChild} from '@angular/core';
import {BaseWorkspaceComponent} from '@app/component/workspace/workspace.component';
import {WorkspaceService} from '@app/service/workspace.service';
import {ActivatedRoute} from '@angular/router';
import {ContactViewComponent} from '@app/component/contact/view/contact-view.component';

@Component({
    selector: 'app-new-contact',
    templateUrl: './settings.component.html',
    styleUrls: ['./settings.component.css'],
})
export class SettingsComponent extends BaseWorkspaceComponent implements OnInit {

    public static readonly COMPONENT_NAME = 'settings';

    @ViewChild(ContactViewComponent, {static: true})
    contactView: ContactViewComponent;

    constructor(
        protected readonly workspaceService: WorkspaceService,
        private readonly route: ActivatedRoute,
    ) {
        super(workspaceService);
    }

    isNeedGlobalMenu(): boolean {
        return true;
    }

    ngOnInit(): void {

    }
}
