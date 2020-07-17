import {BaseWorkspaceComponent} from '@app/component/workspace/base-workspace.component';
import {WorkspaceService} from '@app/service/workspace.service';
import {ActivatedRoute} from '@app/node-modules/@angular/router';
import {GlobalLoadingService} from '@app/service/global-loading.service';
import {ErrorService} from '@app/service/error.service';
import {Directive, OnInit, ViewChild} from '@angular/core';
import {EntityViewComponent} from '@app/component/entity-view/entity-view.component';
import {Utils} from '@app/util/utils';
import {UniqEntity} from '@app/entity/uniq-entity';

@Directive()
export abstract class EntityViewWorkspaceComponent<E extends UniqEntity, T extends EntityViewComponent<E>>
    extends BaseWorkspaceComponent implements OnInit {

    @ViewChild('view', {static: true})
    view: T;

    protected constructor(
        protected readonly route: ActivatedRoute,
        protected readonly globalLoadingService: GlobalLoadingService,
        protected readonly errorService: ErrorService,
        workspaceService: WorkspaceService
    ) {
        super(workspaceService);
    }

    isFullScreen(): boolean {
        return true;
    }

    isNeedLeftRightPadding(): boolean {
        return true;
    }

    isNeedTopBottomPadding(): boolean {
        return false;
    }

    ngOnInit(): void {
        this.globalLoadingService.increaseLoading();
    }

    ngAfterViewInit() {
        super.ngAfterViewInit();

        this.route.paramMap.subscribe(value => {

            const id = value.get('id');

            if (id && Utils.isNumber(id)) {
                this.loadEntityById(Number.parseInt(id))
                    .then(loaded => this.view.reloadEntity(loaded))
                    .catch(reason => this.errorService.showError(reason))
                    .finally(() => this.globalLoadingService.decreaseLoading());
            } else {
                this.view.reloadEntity(this.newEntity());
                this.globalLoadingService.decreaseLoading();
            }
        });
    }

    abstract loadEntityById(id: number): Promise<E | null>;

    abstract newEntity(): E;
}
