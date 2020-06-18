import {Component} from '@angular/core';
import {FormBuilder, FormControl, Validators} from '@angular/forms';
import {TranslateService} from '@ngx-translate/core';
import {Utils} from '@app/util/utils';
import {UserRepository} from '@app/repository/user/user.repository';
import {User} from '@app/entity/user';
import {ErrorService} from '@app/service/error.service';
import {EntityViewComponent} from '@app/component/entity-view/entity-view.component';
import {EntityViewTab} from '@app/component/entity-view/tab/entity-view-tab';
import {
    EntityFieldsViewBlock,
    EntityFieldsViewBlockData
} from '@app/component/entity-view/block/entity/fields/entity-fields-vew-block.component';

@Component({
    selector: 'app-user-view',
    templateUrl: '../../entity-view/entity-view.component.html',
    styleUrls: ['../../entity-view/entity-view.component.scss'],
})
export class UserViewComponent extends EntityViewComponent<User> {

    //readonly contactNameMaxLength = environment.contactNameMaxLength;
    //readonly contactCompanyMaxLength = environment.contactCompanyMaxLength;

    readonly entityViewTabs: EntityViewTab[];

    readonly firstName: FormControl;
    readonly secondName: FormControl;
    readonly thirdName: FormControl;

    constructor(
        private readonly userRepository: UserRepository,
        translateService: TranslateService,
        errorService: ErrorService,
        formBuilder: FormBuilder,
    ) {
        super(translateService, errorService, formBuilder);

        const userFormControls = this.entityFormGroup.controls;

        this.firstName = userFormControls['firstName'] as FormControl;
        this.secondName = userFormControls['secondName'] as FormControl;
        this.thirdName = userFormControls['thirdName'] as FormControl;

        this.entityViewTabs = [
            new EntityViewTab(
                'Main',
                [
                    new EntityFieldsViewBlock(
                        new EntityFieldsViewBlockData(
                            'ABOUT USER',
                            this.entityFormGroup,
                            []
                        )
                    )
                ]
            )
        ];
    }

    getEntityViewTabs(): EntityViewTab[] {
        return this.entityViewTabs;
    }

    protected buildEntityFormGroup(formBuilder: FormBuilder) {
        return formBuilder.group({
            firstName: ['', [
                Validators.required
            ]],
            secondName: ['', [
                Validators.required
            ]],
            thirdName: [''],
        });
    }

    reloadEntity(user: User | null): void {

        this.entity = User.copy(user);
        this.firstName.setValue(Utils.emptyIfNull(this.entity.firstName));
        this.secondName.setValue(Utils.emptyIfNull(this.entity.secondName));
        this.thirdName.setValue(Utils.emptyIfNull(this.entity.thirdName));
        this.hasChanges = false;
    }

    createEntity(): void {
        this.disabled = true;
        /*this.contactRepository.create(this.syncContactWithForm(this.contact))
            .then(result => this.reloadEntity(result))
            .catch(reason => this.errorService.showError(reason))
            .finally(() => this.disabled = false);*/
    }

    updateEntity(): void {
        this.disabled = true;
        /*this.contactRepository.update(this.syncContactWithForm(this.contact))
            .then(result => this.reloadEntity(result))
            .catch(reason => this.errorService.showError(reason))
            .finally(() => this.disabled = false);*/
    }

    protected syncEntityWithForm(user: User): User {
        user.firstName = this.firstName.value;
        user.secondName = this.secondName.value;
        user.thirdName = this.thirdName.value;
        return user;
    }
}
