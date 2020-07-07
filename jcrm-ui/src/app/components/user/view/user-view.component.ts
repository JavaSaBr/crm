import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {UserRepository} from '@app/repository/user/user.repository';
import {User} from '@app/entity/user';
import {ErrorService} from '@app/service/error.service';
import {EntityViewComponent} from '@app/component/entity-view/entity-view.component';
import {EntityViewTab} from '@app/component/entity-view/tab/entity-view-tab';
import {
    EntityFieldDescriptor,
    EntityFieldsViewBlock,
    EntityFieldsViewBlockData
} from '@app/component/entity-view/block/entity/fields/entity-fields-vew-block.component';
import {GlobalLoadingService} from '@app/service/global-loading.service';

@Component({
    selector: 'app-user-view',
    templateUrl: '../../entity-view/entity-view.component.html',
    styleUrls: ['../../entity-view/entity-view.component.scss'],
})
export class UserViewComponent extends EntityViewComponent<User> {

    //readonly contactNameMaxLength = environment.contactNameMaxLength;
    //readonly contactCompanyMaxLength = environment.contactCompanyMaxLength;

    constructor(
        private readonly userRepository: UserRepository,
        translateService: TranslateService,
        errorService: ErrorService,
        globalLoadingService: GlobalLoadingService
    ) {
        super(translateService, errorService, globalLoadingService);
    }

    protected buildEntityViewTabs(): EntityViewTab[] {
        return [
            new EntityViewTab(
                'Main',
                this.buildEntityFieldsViewBlock()
            )
        ];
    }

    private buildEntityFieldsViewBlock() {
        return new EntityFieldsViewBlock(new EntityFieldsViewBlockData(
            this,
            'ABOUT USER',
            [
                EntityFieldDescriptor.requiredString(
                    'First name',
                    'firstName',
                    newValue => this.entity.firstName = newValue,
                    () => this.entity.firstName
                ),
                EntityFieldDescriptor.requiredString(
                    'Second name',
                    'secondName',
                    newValue => this.entity.secondName = newValue,
                    () => this.entity.secondName
                ),
                EntityFieldDescriptor.string(
                    'Third name',
                    'thirdName',
                    newValue => this.entity.thirdName = newValue,
                    () => this.entity.thirdName
                ),
                EntityFieldDescriptor.requiredEmail(
                    'Email',
                    'userEmail',
                    newValue => this.entity.email = newValue,
                    () => this.entity.email
                ),
                EntityFieldDescriptor.date(
                    'Birthday',
                    'birthday',
                    newValue => this.entity.birthday = newValue,
                    () => this.entity.birthday
                ),
                EntityFieldDescriptor.phoneNumbers(
                    'Phone numbers',
                    'phoneNumbers',
                    phoneNumbers => this.entity.phoneNumbers = phoneNumbers,
                    () => this.entity.phoneNumbers
                ),
                EntityFieldDescriptor.messengers(
                    'Messengers',
                    'messengers',
                    messengers => this.entity.messengers = messengers,
                    () => this.entity.messengers
                ),
            ])
        );
    }

    reloadEntity(user: User | null): void {
        this.entityProperty.next(User.copy(user))
    }

    createEntity(): void {
        this.disabledProperty.next(true)
        this.globalLoadingService.increaseLoading();

        const entity = this.entity;

        console.log(entity);

        /*this.contactRepository.create(this.syncContactWithForm(this.contact))
            .then(result => this.reloadEntity(result))
            .catch(reason => this.errorService.showError(reason))
            .finally(() => this.disabled = false);*/
    }

    saveEntity(): void {
        this.disabledProperty.next(true)
        /*this.contactRepository.update(this.syncContactWithForm(this.contact))
            .then(result => this.reloadEntity(result))
            .catch(reason => this.errorService.showError(reason))
            .finally(() => this.disabled = false);*/
    }

    protected syncEntityWithForm(user: User): User {
        return user;
    }
}
