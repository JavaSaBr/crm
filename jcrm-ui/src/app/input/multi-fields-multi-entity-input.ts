import {BaseInput} from '@app/input/base-input';
import {FormControl, NgControl, ValidationErrors, ValidatorFn} from '@angular/forms';
import {ElementRef, Input, OnInit, Optional, Self} from '@angular/core';
import {FocusMonitor} from '@angular/cdk/a11y';
import {Entity} from '@app/entity/entity';
import {Utils} from '@app/util/utils';
import {TranslateService} from '@ngx-translate/core';

export abstract class MultiFieldsMultiEntityInput<T extends Entity> extends BaseInput<T[]> implements OnInit {

    protected _entities: T[];
    protected _entityToControl: Map<T, FormControl[]>;

    protected constructor(
        @Optional() @Self() ngControl: NgControl,
        focusMonitor: FocusMonitor,
        elementRef: ElementRef<HTMLElement>,
        protected readonly translateService: TranslateService
    ) {
        super(ngControl, focusMonitor, elementRef);

        this._entities = [];
        this._entityToControl = new Map();

        const _rawValidators: ValidatorFn[] = ngControl['_rawValidators'];

        //FIXME to find best solution by javasabr
        if (_rawValidators) {
            _rawValidators.push(() => this.validateSubControls());
        } else {
            ngControl['_rawValidators'] = [() => this.validateSubControls()];
        }
    }

    protected removeEntity(entity: T): void {
        if (this.removeEntityInternal(entity)) {
            this.changeFromSubControls();
        }
    }

    protected removeEntityInternal(entity: T): boolean {

        const index = this._entities.indexOf(entity);

        if (index < 0) {
            return false;
        }

        this._entities.splice(index, 1);
        this._entityToControl.delete(entity);

        return true;
    }

    protected addEntity(entity: T) {
        this.addEntityInternal(entity);
        this.changeFromSubControls();
    }

    protected addEntityInternal(entity: T) {

        const controls = this.createFormControls(entity);
        controls.forEach(control => control
            .valueChanges.subscribe(() => this.changeFromSubControls()));

        this._entities.push(entity);
        this._entityToControl.set(entity, controls);
    }


    protected createFormControls(entity: T): FormControl[] {
        return [
            new FormControl()
        ];
    }

    protected findControlsFor(entity: T): FormControl[] {
        return this._entityToControl.get(entity);
    }

    get entities(): T[] {
        return this._entities;
    }

    get empty(): boolean {
        return this._entities.length < 1;
    }

    @Input()
    get value(): T[] {
        return this._entities;
    }

    set value(entities: T[]) {

        Array.from(this.entities)
            .forEach(entity => this.removeEntityInternal(entity));

        entities.forEach(entity => this.addEntityInternal(entity));

        this.changeFromSubControls()
    }

    writeValue(value: any): void {

        const entities: T[] = value;

        if (entities) {
            this.value = entities;
        } else {
            this.value = [];
        }

        super.writeValue(value);
    }

    private changeFromSubControls(): void {
        this.stateChanges.next();
        this.onChange(this.value);
    }

    ngOnInit(): void {}

    private validateSubControls(): ValidationErrors | null {

        let result: ValidationErrors = {};

        this._entityToControl.forEach(controls =>
            controls.forEach(control => Utils.pushTo(control.errors, result)));

        if (Object.keys(result).length < 1) {
            return null;
        }

        return result;
    }
}
