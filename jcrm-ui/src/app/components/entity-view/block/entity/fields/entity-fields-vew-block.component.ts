import {Component, OnInit, Type} from '@angular/core';

import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {
    EditableEntityViewBlock,
    EntityControl,
    EntityViewBlock,
    EntityViewBlockData,
    EntityViewBlockType
} from '@app/component/entity-view/block/entity-view-block';
import {EntityViewBlockComponent} from '@app/component/entity-view/block/entity-view-block.component';
import {PhoneNumberValidator} from '@app/input/phone-number/phone-number-validator';
import {UniqEntity} from '@app/entity/uniq-entity';
import {UserValidator} from '@app/util/validator/user-validator';

export class EntityFieldsViewBlockData extends EntityViewBlockData {

    readonly title: string;
    readonly fields: EntityFieldDescriptor[];

    constructor(entityProvider: EntityControl, title: string, fields: EntityFieldDescriptor[]) {
        super(entityProvider);
        this.title = title;
        this.fields = fields;
    }
}

export class EntityFieldsViewBlock extends EntityViewBlock<EntityFieldsViewBlockData> {

    constructor(data: EntityFieldsViewBlockData) {
        super(EntityViewBlockType.entityFieldForm, data);
    }
}

export enum EntityFieldType {
    STRING,
    PHONE_NUMBER,
    EMAIL
}

export class EntityFieldDescriptor {

    public static string(
        title: string,
        formControlName: string,
        setter: (newValue: any) => void,
        getter: () => any
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, formControlName, EntityFieldType.STRING, setter, getter, false);
    }

    public static requiredString(
        title: string,
        formControlName: string,
        setter: (newValue: any) => void,
        getter: () => any
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, formControlName, EntityFieldType.STRING, setter, getter, true);
    }

    public static phoneNumber(
        title: string,
        formControlName: string,
        setter: (newValue: any) => void,
        getter: () => any
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, formControlName, EntityFieldType.PHONE_NUMBER, setter, getter, false);
    }

    public static requiredEmail(
        title: string,
        formControlName: string,
        setter: (newValue: any) => void,
        getter: () => any
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, formControlName, EntityFieldType.EMAIL, setter, getter, true);
    }

    title: string;
    formControlName: string;
    type: EntityFieldType;
    updateValue: (newValue: any) => void;
    currentValue: () => any;
    required: boolean;

    constructor(title: string, formControlName: string, type: EntityFieldType, setter: (newValue: any) => void, getter: () => any, required: boolean = false) {
        this.title = title;
        this.formControlName = formControlName;
        this.type = type;
        this.updateValue = setter;
        this.currentValue = getter;
        this.required = required;
    }
}

@Component({
    selector: 'app-entity-view-entity-fields',
    templateUrl: 'entity-fields-view-block.component.html',
    styleUrls: ['../../../entity-view.component.scss'],
    host: {'class': 'entity-info-container'}
})
export class EntityFieldsViewBlockComponent extends EntityViewBlockComponent<EntityFieldsViewBlockData>
    implements OnInit, EditableEntityViewBlock {

    fieldType = EntityFieldType;

    entityFormGroup: FormGroup;

    entityCreated: boolean;
    editing: boolean;
    disabled: boolean;
    hasChanges: boolean;

    constructor(private readonly formBuilder: FormBuilder) {
        super();
        this.disabled = false;
        this.editing = false;
        this.hasChanges = false;
        this.entityCreated = false;
    }

    get dataType(): Type<EntityFieldsViewBlockData> {
        return EntityFieldsViewBlockData;
    }

    ngOnInit(): void {

        let group = {};

        const fields = this.data.fields;
        fields.forEach(field => {

            const validatorOrOpts = [];

            if (field.required) {
                validatorOrOpts.push(Validators.required);
            }

            if (field.type == EntityFieldType.STRING) {
                group[field.formControlName] = new FormControl(validatorOrOpts);
            } else if (field.type == EntityFieldType.PHONE_NUMBER) {
                validatorOrOpts.push(PhoneNumberValidator.fun);
                group[field.formControlName] = new FormControl(validatorOrOpts);
            } else if (field.type == EntityFieldType.EMAIL) {
                validatorOrOpts.push(Validators.pattern(UserValidator.emailPatter));
                group[field.formControlName] = new FormControl(validatorOrOpts);
            }
        });

        this.entityFormGroup = this.formBuilder.group(group);
        this.entityFormGroup.valueChanges.subscribe(() => this.hasChanges = true);

        this.data.entityControl.observableEntity()
            .subscribe(value => this.updateEntity(value));
        this.data.entityControl.observableDisabled()
            .subscribe(value => this.disabled = value);
    }

    setEditing(editing: boolean): void {
        this.editing = editing;
    }

    updateEntity(entity: UniqEntity): void {

        const controls = this.entityFormGroup.controls;
        const fields = this.data.fields;

        if (entity == null) {
            this.entityCreated = false;
            fields.forEach(field => {
                const control = controls[field.formControlName];
                control.setValue(null);
            });
        } else {
            this.entityCreated = entity.id != undefined;

            fields.forEach(field => {
                const control = controls[field.formControlName];
                control.setValue(field.currentValue());
            });
        }

        this.hasChanges = false;
    }

    startEditing(): void {
        this.editing = true;
    }

    saveEntity(): void {
        this.applyForm();
        this.data.entityControl.saveEntity();
        this.editing = false;
        this.hasChanges = false;
    }

    createEntity(): void {
        this.applyForm();
        this.data.entityControl.createEntity();
        this.editing = false;
        this.hasChanges = false;
    }

    applyForm(): void {

        const controls = this.entityFormGroup.controls;
        const fields = this.data.fields;

        fields.forEach(field => {
            const control = controls[field.formControlName];
            field.updateValue(control.value);
        });
    }
}
