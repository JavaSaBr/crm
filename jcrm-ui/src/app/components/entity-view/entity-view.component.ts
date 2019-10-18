import {Entity} from '@app/entity/entity';
import {AfterViewInit, Input} from '@app/node-modules/@angular/core';
import {FormBuilder, FormGroup} from '@app/node-modules/@angular/forms';
import {TranslateService} from '@app/node-modules/@ngx-translate/core';
import {ErrorService} from '@app/service/error.service';

export abstract class EntityViewComponent<T extends Entity> implements AfterViewInit {

    readonly entityFormGroup: FormGroup;

    entity: T | null;

    @Input('editingOnStart')
    editingOnStart: boolean;

    disabled: boolean;
    hasChanges: boolean;
    editing: boolean;

    constructor(
        protected readonly translateService: TranslateService,
        protected readonly errorService: ErrorService,
        formBuilder: FormBuilder
    ) {
        this.entity = null;
        this.disabled = false;
        this.hasChanges = false;
        this.editingOnStart = false;
        this.editing = this.editingOnStart;
        this.entityFormGroup = this.buildEntityFormGroup(formBuilder);
        this.entityFormGroup.valueChanges
            .subscribe(() => this.hasChanges = true);
    }

    ngAfterViewInit(): void {
        setTimeout(() => {

            if (!this.editingOnStart) {
                this.switchEditingState(false);
            }

            this.reloadEntity(this.entity);
        });
    }

    protected abstract buildEntityFormGroup(formBuilder: FormBuilder): FormGroup;

    protected abstract reloadEntity(entity: Entity | null): void;

    createEntity(): void {
    }

    updateEntity(): void {
    }

    protected switchEditingState(editing: boolean): void {
        this.editing = editing;

        let formGroup = this.entityFormGroup;
        let controls = formGroup.controls;

        if (!editing) {
            Object.keys(controls)
                .forEach(key => controls[key].disable());
        } else {
            Object.keys(controls)
                .forEach(key => controls[key].enable());
        }
    }

    protected abstract syncEntityWithForm(entity: T): T;
}
