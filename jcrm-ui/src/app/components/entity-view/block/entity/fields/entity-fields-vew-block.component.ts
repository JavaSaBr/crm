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
import {UserEmailValidator} from '@app/util/validator/user-email-validator';
import {ValidatorFn} from '@app/node-modules/@angular/forms';
import {RegistrationService} from '@app/service/registration.service';
import {environment} from '@app/env/environment';
import {Observable} from '@app/node-modules/rxjs';

export class EntityFieldsViewBlockData extends EntityViewBlockData {

    readonly title: string;
    readonly fields: EntityFieldDescriptor[];
    readonly onCreationFields: EntityFieldDescriptor[];

    constructor(
        entityProvider: EntityControl,
        title: string,
        fields: EntityFieldDescriptor[],
        onCreationFields?: EntityFieldDescriptor[]
    ) {
        super(entityProvider);
        this.title = title;
        this.fields = fields;
        this.onCreationFields = onCreationFields ? onCreationFields : [];
    }
}

export class EntityFieldsViewBlock extends EntityViewBlock<EntityFieldsViewBlockData> {

    constructor(data: EntityFieldsViewBlockData) {
        super(EntityViewBlockType.ENTITY_FIELD_FORM, data);
    }
}

export enum EntityFieldType {
    STRING,
    PHONE_NUMBER,
    USER_EMAIL,
    DATE,
    PHONE_NUMBERS,
    MESSENGERS,
    PASSWORD
}

export class EntityFieldDescriptor {

    public static string(
        title: string,
        field: string
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, field, EntityFieldType.STRING,false);
    }

    public static requiredString(
        title: string,
        field: string
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, field, EntityFieldType.STRING,true);
    }

    public static phoneNumber(
        title: string,
        field: string
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, field, EntityFieldType.PHONE_NUMBER,false);
    }

    public static requiredUserEmail(
        title: string,
        field: string
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, field, EntityFieldType.USER_EMAIL,true);
    }

    public static date(
        title: string,
        field: string
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, field, EntityFieldType.DATE,false);
    }

    public static phoneNumbers(
        title: string,
        field: string
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, field, EntityFieldType.PHONE_NUMBERS,false);
    }

    public static messengers(
        title: string,
        field: string
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, field, EntityFieldType.MESSENGERS,false);
    }

    public static password(
        title: string,
        field: string
    ): EntityFieldDescriptor {
        return new EntityFieldDescriptor(title, field, EntityFieldType.PASSWORD,true);
    }

    title: string;
    formControlName: string;
    type: EntityFieldType;
    updateValue: (entity: UniqEntity, newValue: any) => void;
    currentValue: (entity: UniqEntity) => any;
    required: boolean;

    constructor(
        title: string,
        formControlName: string,
        type: EntityFieldType,
        required: boolean = false,
        setter?: (entity: UniqEntity, newValue: any) => void,
        getter?: (entity: UniqEntity) => any
    ) {
        this.title = title;
        this.formControlName = formControlName;
        this.type = type;
        this.required = required;

        if (!getter) {
            getter = entity => entity[formControlName];
        }

        if (!setter) {
            setter = (entity, newValue) => entity[formControlName] = newValue;
        }

        this.updateValue = setter;
        this.currentValue = getter;
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
    entity: UniqEntity;

    entityCreated: boolean;
    editing: boolean;
    disabled: boolean;
    hasChanges: boolean;

    constructor(
        private readonly registrationService: RegistrationService,
        private readonly formBuilder: FormBuilder
    ) {
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

        this.data.fields.forEach(field => this.createControl(field, group));
        this.data.onCreationFields.forEach(field => this.createControl(field, group));

        this.entityFormGroup = this.formBuilder.group(group);
        this.entityFormGroup.valueChanges.subscribe(() => this.hasChanges = true);

        this.data.entityControl.observableEntity()
            .subscribe(value => this.updateEntity(value));
        this.data.entityControl.observableDisabled()
            .subscribe(value => this.disabled = value);
    }

    private createControl(field: EntityFieldDescriptor, group: {}) {

        const validatorOrOpts: ValidatorFn[] = [];

        if (field.required) {
            validatorOrOpts.push(Validators.required);
        }

        if (field.type == EntityFieldType.STRING) {
            group[field.formControlName] = new FormControl(null, validatorOrOpts);
        } else if (field.type == EntityFieldType.PHONE_NUMBER) {
            validatorOrOpts.push(PhoneNumberValidator.fun);
            group[field.formControlName] = new FormControl(null, validatorOrOpts);
        } else if (field.type == EntityFieldType.USER_EMAIL) {

            const userEmailValidator = new UserEmailValidator(this.registrationService);

            validatorOrOpts.push(Validators.pattern(UserEmailValidator.emailPatter));
            validatorOrOpts.push(control => userEmailValidator.validateSync(control));

            group[field.formControlName] = new FormControl(
                null,
                validatorOrOpts,
                control => {

                    if (!this.editing) {
                        return Promise.resolve(null);
                    }

                    return userEmailValidator.validate(control);
                }
            );

        } else if (field.type == EntityFieldType.DATE) {
            group[field.formControlName] = new FormControl(null, validatorOrOpts);
        } else if (field.type == EntityFieldType.PHONE_NUMBERS) {
            group[field.formControlName] = new FormControl(null, validatorOrOpts);
        } else if (field.type == EntityFieldType.MESSENGERS) {
            group[field.formControlName] = new FormControl(null, validatorOrOpts);
        } else if (field.type == EntityFieldType.PASSWORD) {

            validatorOrOpts.push(Validators.minLength(environment.passwordMinLength));
            validatorOrOpts.push(Validators.maxLength(environment.passwordMaxLength));

            group[field.formControlName] = new FormControl(null, validatorOrOpts);
        }
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
                if (control) {
                    control.setValue(null);
                }
            });
        } else {
            this.entityCreated = entity.id != undefined;
            fields.forEach(field => {
                const control = controls[field.formControlName];
                if (control) {
                    control.setValue(field.currentValue(entity));
                }
            });
        }

        this.hasChanges = false;
        this.entity = entity;
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

    cancelEditing() {
        this.editing = false;
        this.updateEntity(this.entity);
    }

    applyForm(): void {

        const controls = this.entityFormGroup.controls;

        this.data.fields.forEach(field => {
            const control = controls[field.formControlName];
            field.updateValue(this.entity, control.value);
        });

        this.data.onCreationFields.forEach(field => {
            const control = controls[field.formControlName];
            field.updateValue(this.entity, control.value);
        });
    }
}
