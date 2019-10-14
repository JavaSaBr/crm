import {Component, OnInit, ViewChild} from '@angular/core';
import {BaseWorkspaceComponent} from '@app/component/workspace/workspace.component';
import {WorkspaceService} from '@app/service/workspace.service';
import {Contact} from '@app/entity/contact';
import {ActivatedRoute} from '@angular/router';
import {Utils} from '@app/util/utils';
import {ContactRepository} from '@app/repository/contact/contact.repository';
import {ContactViewComponent} from '@app/component/contact/view/contact-view.component';

@Component({
    selector: 'app-new-contact',
    templateUrl: './contact-workspace.component.html',
    styleUrls: ['./contact-workspace.component.css'],
})
export class ContactWorkspaceComponent extends BaseWorkspaceComponent implements OnInit {

    public static readonly COMPONENT_NAME = 'contact';
    public static readonly NEW_MODE = 'new';
    public static readonly VIEW_MODE = 'view';

    @ViewChild(ContactViewComponent, {static: true})
    contactView: ContactViewComponent;

    constructor(
        protected readonly workspaceService: WorkspaceService,
        private readonly route: ActivatedRoute,
        private readonly contactService: ContactRepository
    ) {
        super(workspaceService);
    }

    getTitle(): string | null {
        return 'Contact view';
    }

    isFullScreen(): boolean {
        return true;
    }

    isNeedContentPadding(): boolean {
        return true;
    }

    ngOnInit(): void {
        this.route.paramMap.subscribe(value => {

            const id = value.get('id');

            if (id && Utils.isNumber(id)) {
                this.contactService.findById(Number.parseInt(id))
                    .then(loaded => {
                        this.contactView.reload(loaded);
                    });
            } else {
                this.contactView.reload(Contact.create());
            }
        });
    }
}
