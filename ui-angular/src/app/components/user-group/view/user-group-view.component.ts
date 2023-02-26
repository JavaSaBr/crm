import {Component} from '@angular/core';
import {TranslateService} from '@ngx-translate/core';
import {ErrorService} from '@app/service/error.service';
import {EntityViewComponent} from '@app/component/entity-view/entity-view.component';
import {EntityViewTab} from '@app/component/entity-view/tab/entity-view-tab';
import {
    EntityFieldDescriptor,
    EntityFieldsViewBlock,
    EntityFieldsViewBlockData
} from '@app/component/entity-view/block/entity/fields/entity-fields-vew-block.component';
import {GlobalLoadingService} from '@app/service/global-loading.service';
import {UserGroup} from '@app/entity/user-group';
import {UserGroupRepository} from '@app/repository/user-group/user-group-repository.service';

@Component({
    selector: 'app-user-group-view',
    templateUrl: '../../entity-view/entity-view.component.html',
    styleUrls: ['../../entity-view/entity-view.component.scss'],
})
export class UserGroupViewComponent extends EntityViewComponent<UserGroup> {

    constructor(
        private readonly userGroupRepository: UserGroupRepository,
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
            'ABOUT GROUP',
            [
                EntityFieldDescriptor.requiredString('Name', 'name'),
            ],
            [
                //EntityFieldDescriptor.password("Password", "password")
            ])
        );
    }

    reloadEntity(userGroup: UserGroup | null): void {
        this.entityProperty.next(UserGroup.copy(userGroup))
    }

    createEntity(): void {
        this.disabledProperty.next(true);
        this.globalLoadingService.increaseLoading();
        this.userGroupRepository.create(this.entity)
            .then(result => this.reloadEntity(result))
            .catch(reason => this.errorService.showError(reason))
            .finally(() => {
                this.globalLoadingService.decreaseLoading();
            });
    }

    saveEntity(): void {
        this.disabledProperty.next(true)
        this.globalLoadingService.increaseLoading();
        this.userGroupRepository.update(this.entity)
            .then(result => this.reloadEntity(result))
            .catch(reason => this.errorService.showError(reason))
            .finally(() => {
                this.globalLoadingService.decreaseLoading();
            });
    }
}
