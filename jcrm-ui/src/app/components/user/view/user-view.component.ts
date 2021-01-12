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
                EntityFieldDescriptor.requiredString('First name', 'firstName'),
                EntityFieldDescriptor.requiredString('Second name', 'secondName'),
                EntityFieldDescriptor.string('Third name', 'thirdName'),
                EntityFieldDescriptor.requiredUserEmail('Email', 'email'),
                EntityFieldDescriptor.date('Birthday', 'birthday'),
                EntityFieldDescriptor.phoneNumbers('Phone numbers', 'phoneNumbers'),
                EntityFieldDescriptor.messengers('Messengers', 'messengers'),
            ],
            [
                EntityFieldDescriptor.password("Password", "password")
            ])
        );
    }

    reloadEntity(user: User | null): void {
        this.entityProperty.next(User.copy(user))
    }

    createEntity(): void {
        this.disabledProperty.next(true);
        this.globalLoadingService.increaseLoading();
        this.userRepository.create(this.entity)
            .then(result => this.reloadEntity(result))
            .catch(reason => this.errorService.showError(reason))
            .finally(() => {
                this.globalLoadingService.decreaseLoading();
            });
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
